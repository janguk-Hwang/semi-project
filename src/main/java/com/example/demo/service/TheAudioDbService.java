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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TheAudioDbService {
    @Value("${theaudiodb.apikey}")
    private String apiKey;
    private final CloseableHttpClient http = HttpClients.createDefault();
    private static final String BASE_URL = "https://www.theaudiodb.com/api/v1/json";
    private final ObjectMapper om = new ObjectMapper();
    public ArtistProfileDto fetchByName(String artistName) throws Exception {
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .pathSegment(apiKey, "search.php")
                .queryParam("s", artistName)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
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
            dto.setFanartUrl(a.path("strArtistFanart").asText(null));
            dto.setBioRawEn(a.path("strBiographyEN").asText(null));
            return dto;
        }
    }

}
