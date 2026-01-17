package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArtistAlbumDto {
    private String albumMBID;
    private String artistName;
    private String title;
    private String type;
    private String releaseDate;
    private String coverImageUrl;
    private List<TrackDto> tracks;
}
