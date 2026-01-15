package com.example.demo.service;

import com.example.demo.Summarizer.BioSummarizer;
import com.example.demo.dto.ArtistProfileDto;
import com.example.demo.mapper.ArtistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistProfileService {

    private final ArtistMapper artistMapper;
    private final TheAudioDbService theAudioDbClient;
    private final Translator translator;

    @Transactional
    public ArtistProfileDto getOrFetch(String artistName) throws Exception {

        // ✅ 저장/조회 키 통일
        artistName = (artistName == null) ? null : artistName.trim();

        System.out.println("[PROFILE] getOrFetch start = [" + artistName + "]");

        // 1) DB 캐시 조회
        ArtistProfileDto cached = artistMapper.selectArtistProfile(artistName);
        System.out.println("[PROFILE] cached exists = " + (cached != null));

        // 캐시가 있고(이미지 2개 + 소개글) 있으면 그대로 반환
        if (cached != null
                && notBlank(cached.getPhotoUrl())
                && notBlank(cached.getFanartUrl())
                && notBlank(cached.getBioSummaryKo())) {
            System.out.println("[PROFILE] cache hit -> return cached");
            return cached;
        }

        // 2) TheAudioDB 수집
        ArtistProfileDto fetched = theAudioDbClient.fetchByName(artistName);
        System.out.println("[PROFILE] fetched exists = " + (fetched != null));
        if (fetched == null) return cached;
        fetched.setArtistName(artistName);

        System.out.println("[PROFILE] fetched.photoUrl = [" + fetched.getPhotoUrl() + "]");
        System.out.println("[PROFILE] fetched.fanartUrl = [" + fetched.getFanartUrl() + "]");
        System.out.println("[PROFILE] fetched.bioRawEn length = " +
                (fetched.getBioRawEn() == null ? "null" : fetched.getBioRawEn().length()));

        // 3) 요약(EN)
        String summaryEn = BioSummarizer.summarizeEn(fetched.getBioRawEn());
        fetched.setBioSummaryEn(summaryEn);
        System.out.println("[PROFILE] summaryEn = [" + summaryEn + "]");

        // 4) 번역(KO)
        String summaryKo;

        if (summaryEn == null || summaryEn.isBlank()) {
            summaryKo = "아티스트 소개 정보가 없습니다.";
            System.out.println("[PROFILE] summaryEn empty -> default ko msg");
        } else {
            try {
                String ko = translator.enToKo(summaryEn); // 키 없으면 null 반환될 수 있음
                if (ko == null || ko.isBlank()) {
                    summaryKo = summaryEn; // 번역 불가면 EN 요약으로라도 표시
                    System.out.println("[PROFILE] translate skip/fail -> fallback EN summary");
                } else {
                    summaryKo = ko;
                    System.out.println("[PROFILE] translate ok");
                }
            } catch (Exception e) {
                summaryKo = summaryEn; // 번역 실패 fallback
                System.out.println("[PROFILE] translate exception -> fallback EN summary");
            }
        }

        fetched.setBioSummaryKo(summaryKo);

        // ✅ 5) 저장(UPsert)
        System.out.println("[PROFILE] upsert run, artistName(final) = [" + fetched.getArtistName() + "]");
        int n = artistMapper.upsertArtistProfile(fetched);
        System.out.println("[PROFILE] upsert result = " + n);

        // 6) 재조회 반환
        ArtistProfileDto saved = artistMapper.selectArtistProfile(artistName);
        System.out.println("[PROFILE] saved exists = " + (saved != null));
        return saved;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
