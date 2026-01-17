package com.example.demo.mapper;

import com.example.demo.dto.ArtistAlbumDto;
import com.example.demo.dto.ArtistProfileDto;
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
    String selectYoutubeVideoId(@Param("albumMbid") String albumMbid,
                                @Param("trackNo") int trackNo);

    TrackMetaDto selectTrackMeta(@Param("albumMbid") String albumMbid,
                                 @Param("trackNo") int trackNo);

    int updateYoutubeVideoId(@Param("albumMbid") String albumMbid,
                             @Param("trackNo") int trackNo,
                             @Param("youtubeVideoId") String youtubeVideoId);
    ArtistProfileDto selectArtistProfile(String artistName);
    int upsertArtistProfile(ArtistProfileDto dto);
}
