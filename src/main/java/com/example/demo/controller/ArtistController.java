package com.example.demo.controller;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.dto.ArtistProfileDto;
import com.example.demo.service.ArtistProfileService;
import com.example.demo.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final ArtistProfileService artistProfileService; // ✅ 추가

    @GetMapping("/artist/new")
    public String artistRegisterForm() {
        return "Artist/insertArtist";
    }

    @GetMapping("/artist")
    public String artistRoot() {
        List<String> artistList = artistService.getAllArtists();

        // 등록된 가수가 하나도 없으면 등록 폼으로 보내기
        if (artistList == null || artistList.isEmpty()) {
            return "redirect:/artist/new";
        }

        String first = artistList.get(0);
        String encoded = java.net.URLEncoder.encode(first, java.nio.charset.StandardCharsets.UTF_8);
        return "redirect:/artist/" + encoded;
    }

    @GetMapping("/artist/{name}")
    public String artistPage(@PathVariable("name") String name, Model model) {

        // 1) 기존: 앨범 목록
        List<ArtistAlbumDto> albums = artistService.getArtistAlbums(name);
        model.addAttribute("albumList", albums);
        model.addAttribute("artistName", name);

        // 2) 좌측/목록: 아티스트 리스트
        List<String> artistList = artistService.getAllArtists();
        model.addAttribute("artistList", artistList);

        // 3) 프로필(thumb+fanart+한글요약) 가져와서 모델에 담기
        // DB에 없으면 TheAudioDB 호출 → 요약/번역 → upsert → 반환
        try {
            ArtistProfileDto profile = artistProfileService.getOrFetch(name);
            model.addAttribute("profile", profile);

            // fanart는 상단 배경으로 자주 쓰니 별도 키도 추가(템플릿 편의)
            if (profile != null) {
                model.addAttribute("artistFanart", profile.getFanartUrl());
            }
        } catch (Exception e) {
            // 번역/외부 API 문제로 페이지가 아예 깨지지 않게 방어
            model.addAttribute("profile", null);
            model.addAttribute("artistFanart", null);
        }
        return "Artist/ArtistPage";
    }

    @GetMapping("/artist/album/{albumId}")
    public String albumDetail(@PathVariable String albumId, Model model) {
        ArtistAlbumDto albumDetailDto = artistService.getAlbumDetail(albumId);
        model.addAttribute("album", albumDetailDto);
        return "Artist/albumDetail";
    }
}
