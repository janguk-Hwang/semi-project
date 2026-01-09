package com.example.demo.mapper;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.dto.TrackDto;
import com.example.demo.dto.TrackMetaDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArtistMapper {
    int insertAlbums(@Param("name") String name, @Param("album") ArtistAlbumDto artistAlbumDto);
    int insertTrack(@Param("albumMbid") String albumMbid, @Param("track") TrackDto trackDto);

    List<ArtistAlbumDto> selectAlbumsByArtist(String name);
    List<String> selectAllArtists();
    ArtistAlbumDto selectAlbumByMbid(String albumMbid);
    List<TrackDto> selectTracksByAlbum(String albumMBID);

    // ✅ 추가
    String selectYoutubeVideoId(@Param("albumMbid") String albumMbid,
                                @Param("trackNo") int trackNo);

    // ✅ 추가 (TrackMetaDto로)
    TrackMetaDto selectTrackMeta(@Param("albumMbid") String albumMbid,
                                 @Param("trackNo") int trackNo);

    // ✅ 기존 update는 시그니처 통일 추천
    int updateYoutubeVideoId(@Param("albumMbid") String albumMbid,
                             @Param("trackNo") int trackNo,
                             @Param("youtubeVideoId") String youtubeVideoId);
}
