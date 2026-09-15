package com.langquest.model;

public record Word(String kana, String romaji, String meaning, WordCategory category) implements Teachable {

    @Override
    public String prompt() {
        return kana;
    }

    @Override
    public String answer() {
        return romaji;
    }

    @Override
    public String hint() {
        return "\"" + kana + "\" means \"" + meaning + "\" (" + romaji + ")";
    }
}