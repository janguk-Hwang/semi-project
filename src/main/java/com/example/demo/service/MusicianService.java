package com.example.demo.service;

import com.example.demo.dto.MusicianDto;
import com.example.demo.mapper.MusicianMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicianService {
    private final MusicianMapper musicianMapper;

    public List<MusicianDto> selectArtist(){
        return musicianMapper.selectArtist();
    }

    public MusicianDto selectArtistById(int artist_id){
        return musicianMapper.selectArtistById(artist_id);
    }
}
