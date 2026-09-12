package com.langquest.model;

public class Gana {
    private final String character;   // e.g. "あ"
    private final String romaji;      // e.g. "a"
    private final String group;       // e.g. "vowels", "k-row"
    private final String audioPath;   // path to pronunciation clip, filled in later

    public Gana(String character, String romaji, String group, String audioPath) {
        this.character = character;
        this.romaji = romaji;
        this.group = group;
        this.audioPath = audioPath;
    }

    public String getCharacter() { return character; }
    public String getRomaji() { return romaji; }
    public String getGroup() { return group; }
    public String getAudioPath() { return audioPath; }
}