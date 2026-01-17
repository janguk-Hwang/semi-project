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
    private final ArtistProfileService artistProfileService;

    @GetMapping("/artist/new")
    public String artistRegisterForm() {
        return "Artist/insertArtist";
    }

    @GetMapping("/artist")
    public String artistRoot() {
        List<String> artistList = artistService.getAllArtists();

        if (artistList == null || artistList.isEmpty()) {
            return "redirect:/artist/new";
        }

        String first = artistList.get(0);
        String encoded = java.net.URLEncoder.encode(first, java.nio.charset.StandardCharsets.UTF_8);
        return "redirect:/artist/" + encoded;
    }

    @GetMapping("/artist/{name}")
    public String artistPage(@PathVariable("name") String name, Model model) {

        // 1) 앨범 목록
        List<ArtistAlbumDto> albums = artistService.getArtistAlbums(name);
        model.addAttribute("albumList", albums);
        model.addAttribute("artistName", name);

        // 2) 좌측/목록: 아티스트 리스트
        List<String> artistList = artistService.getAllArtists();
        model.addAttribute("artistList", artistList);

        // 3) MBID 확보
        String artistMbid = null;
        try {
            artistMbid = artistService.getArtistMbidByName(name);
        } catch (Exception ignore) {
        }

        // 4) 프로필: MBID 전달해서 TheAudioDB를 MBID 우선 조회
        try {
            ArtistProfileDto profile = artistProfileService.getOrFetch(name, artistMbid);
            model.addAttribute("profile", profile);
            model.addAttribute("artistFanart", profile != null ? profile.getFanartUrl() : null);
        } catch (Exception e) {
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
