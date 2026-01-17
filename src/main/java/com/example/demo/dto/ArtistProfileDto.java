package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArtistProfileDto {
    private String artistName;
    private String artistMbid;
    private String photoUrl;     // strArtistThumb
    private String fanartUrl;    // strArtistFanart
    private String bioRawEn;     // strBiographyEN
    private String bioSummaryEn; // 요약(EN)
    private String bioSummaryKo; // 요약(KO)
}
