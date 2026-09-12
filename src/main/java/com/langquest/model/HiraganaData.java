package com.langquest.model;

import java.util.List;

public class HiraganaData {

    public static List<Gana> getAllHiragana() {
        return List.of(
                // Vowels
                new Gana("あ", "a", GanaGroup.VOWELS, null),
                new Gana("い", "i", GanaGroup.VOWELS, null),
                new Gana("う", "u", GanaGroup.VOWELS, null),
                new Gana("え", "e", GanaGroup.VOWELS, null),
                new Gana("お", "o", GanaGroup.VOWELS, null),

                // k-row
                new Gana("か", "ka", GanaGroup.K_ROW, null),
                new Gana("き", "ki", GanaGroup.K_ROW, null),
                new Gana("く", "ku", GanaGroup.K_ROW, null),
                new Gana("け", "ke", GanaGroup.K_ROW, null),
                new Gana("こ", "ko", GanaGroup.K_ROW, null),

                // s-row
                new Gana("さ", "sa", GanaGroup.S_ROW, null),
                new Gana("し", "shi", GanaGroup.S_ROW, null),
                new Gana("す", "su", GanaGroup.S_ROW, null),
                new Gana("せ", "se", GanaGroup.S_ROW, null),
                new Gana("そ", "so", GanaGroup.S_ROW, null),

                // t-row
                new Gana("た", "ta", GanaGroup.T_ROW, null),
                new Gana("ち", "chi", GanaGroup.T_ROW, null),
                new Gana("つ", "tsu", GanaGroup.T_ROW, null),
                new Gana("て", "te", GanaGroup.T_ROW, null),
                new Gana("と", "to", GanaGroup.T_ROW, null),

                // n-row
                new Gana("な", "na", GanaGroup.N_ROW, null),
                new Gana("に", "ni", GanaGroup.N_ROW, null),
                new Gana("ぬ", "nu", GanaGroup.N_ROW, null),
                new Gana("ね", "ne", GanaGroup.N_ROW, null),
                new Gana("の", "no", GanaGroup.N_ROW, null),

                // h-row
                new Gana("は", "ha", GanaGroup.H_ROW, null),
                new Gana("ひ", "hi", GanaGroup.H_ROW, null),
                new Gana("ふ", "fu", GanaGroup.H_ROW, null),
                new Gana("へ", "he", GanaGroup.H_ROW, null),
                new Gana("ほ", "ho", GanaGroup.H_ROW, null),

                // m-row
                new Gana("ま", "ma", GanaGroup.M_ROW, null),
                new Gana("み", "mi", GanaGroup.M_ROW, null),
                new Gana("む", "mu", GanaGroup.M_ROW, null),
                new Gana("め", "me", GanaGroup.M_ROW, null),
                new Gana("も", "mo", GanaGroup.M_ROW, null),

                // y-row
                new Gana("や", "ya", GanaGroup.Y_ROW, null),
                new Gana("ゆ", "yu", GanaGroup.Y_ROW, null),
                new Gana("よ", "yo", GanaGroup.Y_ROW, null),

                // r-row
                new Gana("ら", "ra", GanaGroup.R_ROW, null),
                new Gana("り", "ri", GanaGroup.R_ROW, null),
                new Gana("る", "ru", GanaGroup.R_ROW, null),
                new Gana("れ", "re", GanaGroup.R_ROW, null),
                new Gana("ろ", "ro", GanaGroup.R_ROW, null),

                // w-row + standalone n
                new Gana("わ", "wa", GanaGroup.W_ROW, null),
                new Gana("を", "wo", GanaGroup.W_ROW, null),
                new Gana("ん", "n", GanaGroup.W_ROW, null),

                // g-row (voiced k)
                new Gana("が", "ga", GanaGroup.G_ROW, null),
                new Gana("ぎ", "gi", GanaGroup.G_ROW, null),
                new Gana("ぐ", "gu", GanaGroup.G_ROW, null),
                new Gana("げ", "ge", GanaGroup.G_ROW, null),
                new Gana("ご", "go", GanaGroup.G_ROW, null),

                // z-row (voiced s)
                new Gana("ざ", "za", GanaGroup.Z_ROW, null),
                new Gana("じ", "ji", GanaGroup.Z_ROW, null),
                new Gana("ず", "zu", GanaGroup.Z_ROW, null),
                new Gana("ぜ", "ze", GanaGroup.Z_ROW, null),
                new Gana("ぞ", "zo", GanaGroup.Z_ROW, null),

                // d-row (voiced t)
                new Gana("だ", "da", GanaGroup.D_ROW, null),
                new Gana("ぢ", "ji", GanaGroup.D_ROW, null),
                new Gana("づ", "zu", GanaGroup.D_ROW, null),
                new Gana("で", "de", GanaGroup.D_ROW, null),
                new Gana("ど", "do", GanaGroup.D_ROW, null),

                // b-row (voiced h)
                new Gana("ば", "ba", GanaGroup.B_ROW, null),
                new Gana("び", "bi", GanaGroup.B_ROW, null),
                new Gana("ぶ", "bu", GanaGroup.B_ROW, null),
                new Gana("べ", "be", GanaGroup.B_ROW, null),
                new Gana("ぼ", "bo", GanaGroup.B_ROW, null),

                // p-row (semi-voiced h)
                new Gana("ぱ", "pa", GanaGroup.P_ROW, null),
                new Gana("ぴ", "pi", GanaGroup.P_ROW, null),
                new Gana("ぷ", "pu", GanaGroup.P_ROW, null),
                new Gana("ぺ", "pe", GanaGroup.P_ROW, null),
                new Gana("ぽ", "po", GanaGroup.P_ROW, null)
        );
    }
}