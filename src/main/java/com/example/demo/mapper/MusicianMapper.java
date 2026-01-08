package com.example.demo.mapper;

import com.example.demo.dto.MusicianDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicianMapper {
    List<MusicianDto> selectArtist();
    MusicianDto selectArtistById(int artist_id);
}
