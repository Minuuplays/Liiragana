package com.langquest.model;

import java.util.List;

public class WordData {
    private static List<Word> allWords = List.of();

    public static void setData(List<Word> data) {
        allWords = data;
    }

    public static List<Word> getAllWords() {
        return allWords;
    }
}