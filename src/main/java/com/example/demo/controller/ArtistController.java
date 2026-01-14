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
import org.springframework.web.util.UriComponentsBuilder;

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
        // 등록된 가수가 하나도 없으면 등록 폼으로 보내기
        if (artistList == null || artistList.isEmpty()) {
            return "redirect:/artist/new";
        }
        // 등록된 가수들의 이름을 사전순으로 조회, 첫 번째 가수의 페이지가 보임
        String first = artistList.get(0);
        // 가수 이름 그대로 URL에 그대로 쓰기에는 위험한 값이 들어있을 수 있다.('/', 공백 문자, 괄호, 한글)
        String url = UriComponentsBuilder
                .fromPath("/artist/{name}")     // URL의 형태를 선언, {name}은 치환될 자리, artistPage()의 URL 패턴과 동일하도록
                //.queryParam("area", "kor")    // 추가로 area 값을 @RequestParam 또는 params 조건으로 처리한다면
                                                // .queryParam("area", "kor")를 사용해 쿼리 파라미터를 확장할 수 있다.
                .buildAndExpand(first)      // {name}자리에 first를 치환
                .encode()                   // 치환한 값이 URL에서 안전한 형태가 되도록 Percent-Encoding 적용(공백, /, ?, &, # 등을 %xx(16진수) 형태로 변환)
                .toUriString();             // UriComponents(객체)를 최종 URL 문자열로 변환 .encode()의 결과는 UriComponents이므로 URL 문자열로 변환해야 한다.
        return "redirect:" + url;
    }

    @GetMapping("/artist/{name}")
    public String artistPage(@PathVariable("name") String name, Model model) {
        // 앨범 목록
        List<ArtistAlbumDto> albums = artistService.getArtistAlbums(name);
        model.addAttribute("albumList", albums);
        model.addAttribute("artistName", name);
        // 아티스트 리스트
        List<String> artistList = artistService.getAllArtists();
        model.addAttribute("artistList", artistList);
        // 3) 프로필(대표 이미지 + fanart + 한글요약) 가져와서 모델에 담기
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
