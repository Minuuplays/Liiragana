package com.langquest.model;

public record Word(String kana, String romaji, String meaning, WordCategory category) implements Teachable {

    @Override
    public String prompt() {
        return kana;
    }

    @Override
    public String quizOptionText() {
        return meaning;
    }

    @Override
    public String hint() {
        return "\"" + kana + "\" means \"" + meaning + "\" (" + romaji + ")";
    }

    @Override
    public String clue() {
        return romaji;
    }
}