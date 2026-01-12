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
    public ArtistProfileDto fetchByName(String artistName) throws Exception {
        String encoded = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
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
            dto.setPhotoUrl(a.path("strArtistThumb").asText(null));   // 대표
            dto.setFanartUrl(a.path("strArtistFanart").asText(null)); // 상단 헤더
            dto.setBioRawEn(a.path("strBiographyEN").asText(null));    // 원문(영문)
            return dto;
        }
    }
}
