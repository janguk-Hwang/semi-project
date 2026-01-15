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
    private static final int SHORT_TEXT_THRESHOLD = 300;     // 300자 미만이면 요약 스킵
    private static final double KOREAN_MAJOR_THRESHOLD = 0.30; // 한글 비율 30% 이상이면 한국어 위주로 판정

    @Transactional
    public ArtistProfileDto getOrFetch(String artistName) throws Exception {
        return getOrFetch(artistName, null);
    }

    /**
     * MBID를 받으면 MBID로 TheAudioDB 조회를 우선 수행
     * - YUDABINBAND(영문) vs 유다빈밴드(한글) 같은 “표기 불일치”를 안정적으로 해결
     * - 캐시 조회/저장은 기존 로직(artistName 키)을 그대로 유지 (기존 코드 호환 목적)
     */
    @Transactional
    public ArtistProfileDto getOrFetch(String artistName, String artistMbid) throws Exception {
        artistName = (artistName == null) ? null : artistName.trim();
        artistMbid = (artistMbid == null) ? null : artistMbid.trim();
        System.out.println("[PROFILE] getOrFetch start = [" + artistName + "], mbid=[" + artistMbid + "]");

        // 1) DB 캐시 조회 (기존 유지: artistName 기준)
        ArtistProfileDto cached = artistMapper.selectArtistProfile(artistName);
        System.out.println("[PROFILE] cached exists = " + (cached != null));

        // 캐시가 있고(이미지 2개 + 소개글) 있으면 그대로 반환 (기존 유지)
        if (cached != null
                && notBlank(cached.getPhotoUrl())
                && notBlank(cached.getFanartUrl())
                && notBlank(cached.getBioSummaryKo())) {
            System.out.println("[PROFILE] cache hit -> return cached");
            return cached;
        }

        // 2) TheAudioDB 수집: MBID 우선 → 실패 시 이름으로 폴백
        ArtistProfileDto fetched = null;
        if (notBlank(artistMbid)) {
            fetched = theAudioDbClient.fetchByMbid(artistMbid);
            System.out.println("[PROFILE] fetched(by mbid) exists = " + (fetched != null));
        }

        if (fetched == null) {
            fetched = theAudioDbClient.fetchByName(artistName);
            System.out.println("[PROFILE] fetched(by name) exists = " + (fetched != null));
        }

        if (fetched == null) return cached;

        fetched.setArtistName(artistName);

        // MBID가 인자로 들어왔는데 응답에 없으면 채워둠
        if (!notBlank(fetched.getArtistMbid()) && notBlank(artistMbid)) {
            fetched.setArtistMbid(artistMbid);
        }

        System.out.println("[PROFILE] fetched.photoUrl = [" + fetched.getPhotoUrl() + "]");
        System.out.println("[PROFILE] fetched.fanartUrl = [" + fetched.getFanartUrl() + "]");
        System.out.println("[PROFILE] fetched.bioRawEn length = " +
                (fetched.getBioRawEn() == null ? "null" : fetched.getBioRawEn().length()));

        // ============================================================
        //   3) 요약/번역 정책 적용
        //    - 짧으면 요약하지 않고 그대로 사용
        //    - 영어가 아니라(한국어 위주면) 번역하지 않음
        //    - 영어 + 길면 요약 → 번역
        // ============================================================
        String raw = fetched.getBioRawEn();
        if (raw == null || raw.isBlank()) {
            fetched.setBioSummaryKo("아티스트 소개 정보가 없습니다.");
            fetched.setBioSummaryEn(null);
            System.out.println("[PROFILE] bio raw empty -> default ko msg");
        } else {
            boolean shortText = raw.length() < SHORT_TEXT_THRESHOLD;

            double krRatio = koreanRatio(raw);
            boolean koreanMajor = krRatio >= KOREAN_MAJOR_THRESHOLD;

            System.out.println("[PROFILE] bio policy -> shortText=" + shortText
                    + ", koreanRatio=" + krRatio
                    + ", koreanMajor=" + koreanMajor);

            // CASE 1) 한국어 위주: 요약/번역 모두 스킵, 그대로 사용
            if (koreanMajor) {
                fetched.setBioSummaryKo(raw);
                fetched.setBioSummaryEn(null);
                System.out.println("[PROFILE] bio is Korean-major -> skip summarize/translate");
            }
            // CASE 2) 영어 위주 + 짧음: 요약 스킵, 원문을 summaryEn으로 사용, 번역만
            else if (shortText) {
                fetched.setBioSummaryEn(raw);

                String summaryKo;
                try {
                    String ko = translator.enToKo(raw);
                    summaryKo = (ko == null || ko.isBlank()) ? raw : ko;
                    System.out.println("[PROFILE] translate(short) ok=" + (ko != null && !ko.isBlank()));
                } catch (Exception e) {
                    summaryKo = raw;
                    System.out.println("[PROFILE] translate(short) exception -> fallback raw");
                }

                fetched.setBioSummaryKo(summaryKo);
                System.out.println("[PROFILE] bio is English-major & short -> skip summarize, translate only");
            }
            // CASE 3) 영어 위주 + 김: 요약 → 번역
            else {
                String summaryEn = BioSummarizer.summarizeEn(raw);
                fetched.setBioSummaryEn(summaryEn);
                System.out.println("[PROFILE] summaryEn = [" + summaryEn + "]");

                String summaryKo;
                if (summaryEn == null || summaryEn.isBlank()) {
                    // 요약 결과가 비면 원문으로라도
                    summaryKo = raw;
                    System.out.println("[PROFILE] summaryEn empty -> fallback raw");
                } else {
                    try {
                        String ko = translator.enToKo(summaryEn);
                        summaryKo = (ko == null || ko.isBlank()) ? summaryEn : ko;
                        System.out.println("[PROFILE] translate(long) ok=" + (ko != null && !ko.isBlank()));
                    } catch (Exception e) {
                        summaryKo = summaryEn;
                        System.out.println("[PROFILE] translate(long) exception -> fallback summaryEn");
                    }
                }

                fetched.setBioSummaryKo(summaryKo);
                System.out.println("[PROFILE] bio is English-major & long -> summarize then translate");
            }
        }

        // 5) 저장(UPsert) (기존 유지)
        System.out.println("[PROFILE] upsert run, artistName(final) = [" + fetched.getArtistName() + "]");
        int n = artistMapper.upsertArtistProfile(fetched);
        System.out.println("[PROFILE] upsert result = " + n);

        // 6) 재조회 반환 (기존 유지)
        ArtistProfileDto saved = artistMapper.selectArtistProfile(artistName);
        System.out.println("[PROFILE] saved exists = " + (saved != null));
        return saved;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    /**
     *   한글 비율 계산 (영문/한글 글자만 분모에 포함)
     * - "Hangul: 블랙핑크" 정도의 한글 섞임은 비율이 낮아서 영어로 유지됨
     * - 한국어 본문은 비율이 커져서 한국어로 판정됨
     */
    private static double koreanRatio(String text) {
        if (text == null || text.isBlank()) return 0.0;
        int ko = 0;
        int letters = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // 알파벳
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                letters++;
            }
            // 한글(가-힣)
            else if (c >= 0xAC00 && c <= 0xD7A3) {
                letters++;
                ko++;
            }
        }
        if (letters == 0) return 0.0;
        return (double) ko / (double) letters;
    }
}
