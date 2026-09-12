package com.langquest.model;

import java.util.List;

public class WordData {
    public static List<Word> getAllWords() {
        return List.of(
                new Word("こんにちは", "konnichiwa", "Hello / Good afternoon", WordCategory.GREETING),
                new Word("おはよう", "ohayou", "Good morning", WordCategory.GREETING),
                new Word("こんばんは", "konbanwa", "Good evening", WordCategory.GREETING),
                new Word("さようなら", "sayounara", "Goodbye", WordCategory.GREETING),
                new Word("ありがとう", "arigatou", "Thank you", WordCategory.GREETING),
                new Word("すみません", "sumimasen", "Excuse me / Sorry", WordCategory.GREETING),
                new Word("はい", "hai", "Yes", WordCategory.GREETING),
                new Word("いいえ", "iie", "No", WordCategory.GREETING),
                new Word("おねがいします", "onegaishimasu", "Please", WordCategory.GREETING),

                new Word("わたし", "watashi", "I / me", WordCategory.NOUN),
                new Word("なまえ", "namae", "name", WordCategory.NOUN),
                new Word("みず", "mizu", "water", WordCategory.NOUN),
                new Word("ねこ", "neko", "cat", WordCategory.NOUN),
                new Word("いぬ", "inu", "dog", WordCategory.NOUN),
                new Word("ひと", "hito", "person", WordCategory.NOUN),
                new Word("がっこう", "gakkou", "school", WordCategory.NOUN),
                new Word("せんせい", "sensei", "teacher", WordCategory.NOUN),
                new Word("ともだち", "tomodachi", "friend", WordCategory.NOUN)
        );
    }
}