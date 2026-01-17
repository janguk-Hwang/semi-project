package com.example.demo.controller;

import com.example.demo.service.ArtistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
public class YouTubeApiController {
    private final ArtistService artistService;
    public YouTubeApiController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/video-id")
    public Map<String, String> getVideoId(@RequestParam String albumMbid, @RequestParam int trackNo) {
        String videoId = artistService.YoutubeVideoId(albumMbid, trackNo);
        return Map.of("videoId", videoId == null ? "" : videoId);
    }
}
