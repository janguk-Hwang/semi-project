package com.example.demo.mapper;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.dto.TrackDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArtistMapper {
    // @Param을 통해 XML에서 #{name}, #{album}으로 쓸 수 있게 매핑합니다.
    int insertAlbums(@Param("name") String name, @Param("album") ArtistAlbumDto artistAlbumDto);

    // 트랙 저장 시에도 어떤 앨범의 곡인지 알려주기 위해 albumMbid를 추가합니다.
    int insertTrack(@Param("albumMbid") String albumMbid, @Param("track") TrackDto trackDto);

    List<ArtistAlbumDto> selectAlbumsByArtist(String name);
    List<TrackDto> selectTracksByAlbum(String album_mbid);
    List<String> selectAllArtists();
}