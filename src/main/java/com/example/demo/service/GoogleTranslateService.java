package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleTranslateService implements Translator {

    @Value("${gcp.projectId}")
    private String projectId;

    @Value("${gcp.location:global}")
    private String location;

    // 예: classpath:keys/gcp-service-account.json
    @Value("${gcp.serviceAccountKeyPath}")
    private String serviceAccountKeyPath;

    private final ResourceLoader resourceLoader;

    private final CloseableHttpClient http = HttpClients.createDefault();
    private final ObjectMapper om = new ObjectMapper();

    private String getAccessToken() throws Exception {
        Resource resource = resourceLoader.getResource(serviceAccountKeyPath);

        try (InputStream is = resource.getInputStream()) {
            GoogleCredentials cred = GoogleCredentials.fromStream(is)
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-translation"));
            cred.refreshIfExpired();

            if (cred.getAccessToken() == null) return null;
            return cred.getAccessToken().getTokenValue();
        }
    }

    @Override
    public String enToKo(String text) throws Exception {
        if (text == null || text.isBlank()) return null;

        // v3 REST: projects/{project}/locations/{location}:translateText
        String parent = "projects/" + projectId + "/locations/" + location;
        String url = "https://translate.googleapis.com/v3/" + parent + ":translateText";

        String token = getAccessToken();
        if (token == null || token.isBlank()) {
            System.out.println("[TRANSLATE] token is null/blank");
            return null;
        }

        // 요청 바디 구성
        ObjectNode body = om.createObjectNode();
        body.putArray("contents").add(text);
        body.put("sourceLanguageCode", "en");
        body.put("targetLanguageCode", "ko");

        HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        post.setEntity(new StringEntity(body.toString(), StandardCharsets.UTF_8));

        String json;
        int status;

        try (var res = http.execute(post)) {
            status = res.getCode();
            json = EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8);
        }

        // 실패 응답이면 서버가 준 JSON을 그대로 로그로 남기고 null 반환
        if (status < 200 || status >= 300) {
            System.out.println("[TRANSLATE] HTTP " + status + " error json = " + json);
            return null;
        }

        JsonNode root = om.readTree(json);
        JsonNode translations = root.path("translations");
        if (!translations.isArray() || translations.isEmpty()) return null;

        String translated = translations.get(0)
                .path("translatedText")
                .asText(null);

        if (translated == null) return null;

        // HTML 엔티티 디코딩 (&#39; -> ' , &quot; -> " 등)
        return StringEscapeUtils.unescapeHtml4(translated);
    }
}
