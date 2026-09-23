package com.langquest.model;

import java.util.List;

public class HiraganaData {
    private static List<Gana> allHiragana = List.of();

    public static void setData(List<Gana> data) {
        allHiragana = data;
    }

    public static List<Gana> getAllHiragana() {
        return allHiragana;
    }
}