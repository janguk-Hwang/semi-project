package com.example.demo.service;

import com.example.demo.dto.ArtistProfileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TheAudioDbService {

    @Value("${theaudiodb.apikey}")
    private String apiKey;

    private final CloseableHttpClient http = HttpClients.createDefault();
    private final ObjectMapper om = new ObjectMapper();

    /** MBID로 조회 (artist-mb.php) */
    public ArtistProfileDto fetchByMbid(String artistMbid) throws Exception {
        if (artistMbid == null || artistMbid.isBlank()) return null;

        String encoded = URLEncoder.encode(artistMbid.trim(), StandardCharsets.UTF_8);
        String url = "https://www.theaudiodb.com/api/v1/json/" + apiKey + "/artist-mb.php?i=" + encoded;

        HttpGet get = new HttpGet(url);
        try (CloseableHttpResponse res = http.execute(get)) {
            String json = EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8);
            JsonNode root = om.readTree(json);

            JsonNode artists = root.path("artists");
            if (!artists.isArray() || artists.isEmpty()) return null;

            JsonNode a = artists.get(0);

            ArtistProfileDto dto = new ArtistProfileDto();
            dto.setArtistMbid(firstNonBlank(a.path("strMusicBrainzID").asText(null), artistMbid));
            dto.setArtistName(a.path("strArtist").asText(null));
            dto.setPhotoUrl(a.path("strArtistThumb").asText(null));

            // fanart는 여러 필드가 존재 가능
            dto.setFanartUrl(firstNonBlank(
                    a.path("strArtistFanart").asText(null),
                    a.path("strArtistFanart2").asText(null),
                    a.path("strArtistFanart3").asText(null),
                    a.path("strArtistFanart4").asText(null)
            ));

            dto.setBioRawEn(a.path("strBiographyEN").asText(null));
            return dto;
        }
    }

    /** ✅ 기존 유지: 이름으로 조회 (search.php) */
    public ArtistProfileDto fetchByName(String artistName) throws Exception {
        if (artistName == null || artistName.isBlank()) return null;

        String encoded = URLEncoder.encode(artistName.trim(), StandardCharsets.UTF_8);
        String url = "https://www.theaudiodb.com/api/v1/json/" + apiKey + "/search.php?s=" + encoded;

        HttpGet get = new HttpGet(url);
        try (CloseableHttpResponse res = http.execute(get)) {
            String json = EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8);
            JsonNode root = om.readTree(json);

            JsonNode artists = root.path("artists");
            if (!artists.isArray() || artists.isEmpty()) return null;

            JsonNode a = artists.get(0);

            ArtistProfileDto dto = new ArtistProfileDto();
            dto.setArtistName(artistName);
            dto.setArtistMbid(a.path("strMusicBrainzID").asText(null));
            dto.setPhotoUrl(a.path("strArtistThumb").asText(null));
            dto.setFanartUrl(firstNonBlank(
                    a.path("strArtistFanart").asText(null),
                    a.path("strArtistFanart2").asText(null),
                    a.path("strArtistFanart3").asText(null),
                    a.path("strArtistFanart4").asText(null)
            ));
            dto.setBioRawEn(a.path("strBiographyEN").asText(null));
            return dto;
        }
    }

    private String firstNonBlank(String... arr) {
        if (arr == null) return null;
        for (String s : arr) {
            if (s != null && !s.isBlank()) return s;
        }
        return null;
    }
}
