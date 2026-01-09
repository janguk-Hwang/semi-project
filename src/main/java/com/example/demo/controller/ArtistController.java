package com.example.demo.controller;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    @GetMapping("/artist/new")
    public String artistRegisterForm() {
        return "Artist/insertArtist";
    }
    @GetMapping("/artist")
    public String artistRoot() {
        List<String> artistList = artistService.getAllArtists();
        // 등록된 가수가 하나도 없으면 등록 폼으로 보내기 (혹은 빈 페이지)
        if (artistList == null || artistList.isEmpty()) {
            return "redirect:/artist/new";
        }
        String first = artistList.get(0);
        String encoded = java.net.URLEncoder.encode(first, java.nio.charset.StandardCharsets.UTF_8);
        return "redirect:/artist/" + encoded;
    }

    @GetMapping("/artist/{name}")
    public String Artist(@PathVariable("name") String name, Model model) {
        List<ArtistAlbumDto> albums = artistService.getArtistAlbums(name);
        model.addAttribute("albumList", albums);
        model.addAttribute("artistName", name);
        List<String> artistList = artistService.getAllArtists();
        model.addAttribute("artistList", artistList);
        return "Artist/ArtistPage";
    }

    @GetMapping("/artist/album/{albumId}")
    public String albumDetail(@PathVariable String albumId, Model model){
        ArtistAlbumDto albumDetailDto = artistService.getAlbumDetail(albumId);
        model.addAttribute("album", albumDetailDto);
        return "Artist/albumDetail";
    }
}