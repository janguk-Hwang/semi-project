package com.example.demo.Summarizer;

public class BioSummarizer {
    public static String summarizeEn(String raw) {
        if (raw == null) return null;
        String text = raw.trim().replace("\r", "");
        if (text.isBlank()) return null;
        // 첫 문단
        String firstParagraph = text.split("\\n\\s*\\n")[0].trim();
        // 첫 2~3문장
        String[] sentences = firstParagraph.split("(?<=[.!?])\\s+");
        int take = Math.min(sentences.length, 3);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < take; i++) sb.append(sentences[i].trim()).append(" ");
        String out = sb.toString().trim();
        int max = 650;
        if (out.length() > max) out = out.substring(0, max).trim() + "…";
        return out;
    }
}