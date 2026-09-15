package com.langquest.model;

public record Gana(String character, String romaji, GanaGroup group, String audioPath) implements Teachable {

    @Override
    public String prompt() {
        return character;
    }

    @Override
    public String answer() {
        return romaji;
    }

    @Override
    public String hint() {
        return "\"" + character + "\" is pronounced as \"" + romaji + "\"";
    }
}