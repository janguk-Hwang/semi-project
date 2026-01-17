package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class YouTubeSearchService {
    @Value("${youtube.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String findTopVideoId(String query) {
        try {
            String q = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url =
                    "https://www.googleapis.com/youtube/v3/search"
                            + "?part=snippet"
                            + "&type=video"
                            + "&maxResults=1"
                            + "&q=" + q
                            + "&key=" + apiKey;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) return null;

            JsonNode root = objectMapper.readTree(res.body());
            JsonNode items = root.path("items");

            if (items.isArray() && items.size() > 0) {
                String videoId = items.get(0).path("id").path("videoId").asText(null);
                return (videoId == null || videoId.isBlank()) ? null : videoId;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
