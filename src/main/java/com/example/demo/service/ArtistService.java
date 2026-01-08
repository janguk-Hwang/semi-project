package com.example.demo.service;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.dto.TrackDto;
import com.example.demo.mapper.ArtistMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ArtistService {
    private final ArtistMapper artistMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "https://musicbrainz.org/ws/2";
    public List<String> getAllArtists() {
        return artistMapper.selectAllArtists();
    }
    @Autowired
    public ArtistService(ArtistMapper artistMapper) {
        this.artistMapper = artistMapper;
        // SSL 및 연결 안정성을 위한 Apache HttpClient 설정
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(factory);
    }

    public List<ArtistAlbumDto> getArtistAlbums(String artistName) {
        // [STEP 0] DB에서 캐시된 데이터가 있는지 확인
        List<ArtistAlbumDto> dbAlbums = artistMapper.selectAlbumsByArtist(artistName);
        if (dbAlbums != null && !dbAlbums.isEmpty()) {
            System.out.println(">>> [DB Cache] DB에서 데이터를 성공적으로 불러왔습니다: " + artistName);
            for (ArtistAlbumDto album : dbAlbums) {
                // 각 앨범에 맞는 트랙 정보를 DB에서 가져와 합침
                album.setTracks(artistMapper.selectTracksByAlbum(album.getAlbumMBID()));
            }
            return dbAlbums;
        }

        // [STEP 1] DB에 데이터가 없으므로 API 호출 시작
        System.out.println("========== [API Request] 외부 API 호출 시작: " + artistName + " ==========");
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        List<ArtistAlbumDto> albums = new ArrayList<>();

        try {
            // 아티스트 검색
            String encodedName = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
            String artistUrl = BASE_URL + "/artist?query=" + encodedName + "&fmt=json";

            Thread.sleep(1100); // Rate Limit 준수
            ResponseEntity<String> artistResponse = restTemplate.exchange(artistUrl, HttpMethod.GET, entity, String.class);
            JsonNode artistsNode = objectMapper.readTree(artistResponse.getBody()).path("artists");

            if (!artistsNode.isArray() || artistsNode.isEmpty()) return albums;
            String artistMbid = artistsNode.get(0).path("id").asText();

            // Release Group 조회
            Thread.sleep(1100);
            String groupUrl = BASE_URL + "/release-group?artist=" + artistMbid + "&fmt=json";
            ResponseEntity<String> groupResponse = restTemplate.exchange(groupUrl, HttpMethod.GET, entity, String.class);
            JsonNode groups = objectMapper.readTree(groupResponse.getBody()).path("release-groups");

            for (JsonNode group : groups) {
                String groupMbid = group.path("id").asText();
                String groupType = group.path("primary-type").asText();

                // 판본 조회
                Thread.sleep(1100);
                String releaseUrl = BASE_URL + "/release?release-group=" + groupMbid + "&fmt=json";
                ResponseEntity<String> releaseResponse = restTemplate.exchange(releaseUrl, HttpMethod.GET, entity, String.class);
                JsonNode releases = objectMapper.readTree(releaseResponse.getBody()).path("releases");

                for (JsonNode release : releases) {
                    String status = release.path("status").asText();
                    String country = release.path("country").asText();
                    String language = release.path("text-representation").path("language").asText();

                    // 공식 한국반 필터링
                    if ("Official".equalsIgnoreCase(status) && "KR".equalsIgnoreCase(country) && "kor".equalsIgnoreCase(language)) {

                        Thread.sleep(1100);
                        String releaseId = release.path("id").asText();
                        String detailUrl = BASE_URL + "/release/" + releaseId + "?inc=recordings&fmt=json";
                        ResponseEntity<String> detailResponse = restTemplate.exchange(detailUrl, HttpMethod.GET, entity, String.class);
                        JsonNode detail = objectMapper.readTree(detailResponse.getBody());

                        ArtistAlbumDto albumDto = new ArtistAlbumDto();
                        albumDto.setAlbumMBID(releaseId);
                        albumDto.setTitle(detail.path("title").asText());
                        albumDto.setType(groupType);
                        albumDto.setReleaseDate(detail.path("date").asText());
                        albumDto.setCoverImageUrl("https://coverartarchive.org/release/" + releaseId + "/front");

                        List<TrackDto> trackList = new ArrayList<>();
                        JsonNode mediaArray = detail.path("media");
                        if (mediaArray.isArray() && mediaArray.size() > 0) {
                            JsonNode tracks = mediaArray.get(0).path("tracks");
                            for (JsonNode track : tracks) {
                                trackList.add(new TrackDto(track.path("number").asText(), track.path("title").asText()));
                            }
                        }
                        albumDto.setTracks(trackList);
                        albums.add(albumDto);

                        // [INSERT 로직 수정]
                        try {
                            // 1. 앨범 정보 저장 (중복 호출 제거 및 반환값 확인)
                            int albumInsertResult = artistMapper.insertAlbums(artistName, albumDto);

                            if (albumInsertResult > 0) {
                                System.out.println(">>> [DB SUCCESS] 앨범 정보 저장 완료: " + albumDto.getTitle());

                                // 2. 앨범 저장 성공 시 해당 앨범의 트랙들 저장
                                int trackSuccessCount = 0;
                                for (TrackDto t : trackList) {
                                    // 앨범 MBID를 함께 넘겨서 DB의 FK 관계를 맺어줍니다.
                                    int trackInsertResult = artistMapper.insertTrack(albumDto.getAlbumMBID(), t);
                                    if (trackInsertResult > 0) {
                                        trackSuccessCount++;
                                    }
                                }
                                if (trackSuccessCount > 0) {
                                    System.out.println(">>> [DB SUCCESS] " + albumDto.getTitle() + " - 수록곡 " + trackSuccessCount + "곡 저장 완료.");
                                }
                            }
                        } catch (Exception e) {
                            System.err.println(">>> [DB SKIP] 저장 실패 (중복 데이터 등): " + e.getMessage());
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("!!! 서비스 처리 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return albums;
    }
}