package com.example.demo.service;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.dto.TrackDto;
import com.example.demo.dto.TrackMetaDto;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ArtistService {
    private final ArtistMapper artistMapper;
    private final YouTubeSearchService youTubeSearchService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "https://musicbrainz.org/ws/2";
    public List<String> getAllArtists() {
        return artistMapper.selectAllArtists();
    }

    public String YoutubeVideoId(String albumMbid, int trackNo) {
        String cached = artistMapper.selectYoutubeVideoId(albumMbid, trackNo);
        if (cached != null && !cached.isBlank()) return cached;

        TrackMetaDto meta = artistMapper.selectTrackMeta(albumMbid, trackNo);
        if (meta == null) return null;

        String q = meta.getArtistName() + " " + meta.getTrackTitle() + " official audio";
        String videoId = youTubeSearchService.findTopVideoId(q);
        if (videoId == null || videoId.isBlank()) return null;

        artistMapper.updateYoutubeVideoId(albumMbid, trackNo, videoId);
        return videoId;
    }

    @Autowired
    public ArtistService(ArtistMapper artistMapper, YouTubeSearchService youTubeSearchService) {
        this.artistMapper = artistMapper;
        this.youTubeSearchService = youTubeSearchService;
        // SSL 및 연결 안정성을 위한 Apache HttpClient 설정
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * 앨범 상세: DB에서 앨범 + 트랙 조회
     * (선택) youtube_video_id가 비어있으면 YouTube 검색으로 채우고 DB에 저장(캐싱)
     */
    public ArtistAlbumDto getAlbumDetail(String albumMBID) {
        // 1) 앨범 1개 조회 (DB)
        ArtistAlbumDto album = artistMapper.selectAlbumByMbid(albumMBID);
        if (album == null) return null;
        // 2) 트랙 목록 조회 (DB)
        List<TrackDto> tracks = artistMapper.selectTracksByAlbum(albumMBID);
        album.setTracks(tracks);
        // 3) youtubeVideoId 자동 채우기 + DB 저장(캐싱)
        if (tracks != null && !tracks.isEmpty()) {
            String artistName = album.getArtistName();
            if (artistName == null) artistName = "";
            for (TrackDto t : tracks) {
                String current = t.getYoutubeVideoId();
                if (current == null || current.isBlank()) {
                    // 검색 쿼리 구성
                    String title = (t.getTitle() == null) ? "" : t.getTitle();
                    String q = (artistName + " " + title + " official audio").trim();
                    // YouTube에서 videoId 찾기
                    String videoId = youTubeSearchService.findTopVideoId(q);
                    // 찾았으면 DTO에 세팅 + DB에 캐싱
                    if (videoId != null && !videoId.isBlank()) {
                        t.setYoutubeVideoId(videoId);
                        // album_tracks.youtube_video_id 업데이트 (캐싱)
                        artistMapper.updateYoutubeVideoId(albumMBID, Integer.parseInt(t.getNumber()), videoId);
                    }
                }
            }
        }

        return album;
    }

    /**
     * 아티스트 앨범 목록: 캐시(DB) 있으면 DB에서, 없으면 MusicBrainz API 호출 후 저장
     */
    public List<ArtistAlbumDto> getArtistAlbums(String artistName) {
        // [STEP 0] DB에서 이미 데이터가 있으면 앨범 목록에 곡 정보들을 채워서 반환하고 종료한다.
        List<ArtistAlbumDto> dbAlbums = artistMapper.selectAlbumsByArtist(artistName);
        if (dbAlbums != null && !dbAlbums.isEmpty()) {
            System.out.println(">>> [DB Cache] DB에서 데이터를 성공적으로 불러왔습니다: " + artistName);
            for (ArtistAlbumDto album : dbAlbums) {
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
            String artistUrl = UriComponentsBuilder
                    .fromUriString(BASE_URL + "/artist")
                    .queryParam("query", artistName)
                    .queryParam("fmt", "json")
                    .encode()
                    .toUriString();
            Thread.sleep(1100);
            // artistUrl으로 get 요청으로 JSON을 문자열로 받는다. HTTP 상태 코드, 응답 헤더, JSON 문자열이 담겨있다.
            ResponseEntity<String> artistResponse = restTemplate.exchange(artistUrl, HttpMethod.GET, entity, String.class);
            // JSON 원문을 JSON 트리로 파싱하고 artists 필드만 꺼냄. get()은 없으면 null, path()는 없으면 빈 JsonNode 반환
            JsonNode artistsNode = objectMapper.readTree(artistResponse.getBody()).path("artists");
            if (!artistsNode.isArray() || artistsNode.isEmpty()) return albums;
            // 검색어와 정확도가 가장 높은 아티스트의 MBID를 가져옴
            String artistMbid = artistsNode.get(0).path("id").asText();

            // Release Group 조회
            Thread.sleep(1100);
            String groupUrl = UriComponentsBuilder
                    .fromUriString(BASE_URL)
                    .path("/release-group")
                    .queryParam("artist", artistMbid)
                    .queryParam("fmt", "json")
                    .toUriString();
            ResponseEntity<String> groupResponse = restTemplate.exchange(groupUrl, HttpMethod.GET, entity, String.class);
            JsonNode groups = objectMapper.readTree(groupResponse.getBody()).path("release-groups");
            for (JsonNode group : groups) {
                String groupMbid = group.path("id").asText();
                String groupType = group.path("primary-type").asText();
                // 판본 조회
                Thread.sleep(1100);
                String releaseUrl = UriComponentsBuilder
                        .fromUriString(BASE_URL)
                        .path("/release")
                        .queryParam("release-group", groupMbid)
                        .queryParam("fmt", "json")
                        .toUriString();
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
                        String detailUrl = UriComponentsBuilder
                                .fromUriString(BASE_URL)
                                .path("/release/{releaseId}")
                                .queryParam("inc", "recordings")
                                .queryParam("fmt", "json")
                                .buildAndExpand(releaseId)
                                .encode()
                                .toUriString();
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
                                TrackDto dto = new TrackDto();
                                dto.setNumber(track.path("number").asText());
                                dto.setTitle(track.path("title").asText());
                                // youtubeVideoId는 여기서는 비워둠(나중에 DB에서 채우거나 수동 등록)
                                trackList.add(dto);
                            }
                        }
                        albumDto.setTracks(trackList);
                        albums.add(albumDto);

                        try {
                            // 1) 앨범 저장
                            int albumInsertResult = artistMapper.insertAlbums(artistName, albumDto);
                            if (albumInsertResult > 0) {
                                System.out.println(">>> [DB SUCCESS] 앨범 정보 저장 완료: " + albumDto.getTitle());
                                // 2) 트랙 저장
                                int trackSuccessCount = 0;
                                for (TrackDto t : trackList) {
                                    int trackInsertResult = artistMapper.insertTrack(albumDto.getAlbumMBID(), t);
                                    if (trackInsertResult > 0) trackSuccessCount++;
                                }
                                if (trackSuccessCount > 0) {
                                    System.out.println(">>> [DB SUCCESS] " + albumDto.getTitle() + " - 수록곡 " + trackSuccessCount + "곡 저장 완료.");
                                }
                            }
                        } catch (Exception e) {
                            System.err.println(">>> [DB SKIP] 저장 실패 : " + e.getMessage());
                        }
                        break; // 조건 만족하는 release 하나 찾았으면 다음 그룹으로
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return albums;
    }
}
