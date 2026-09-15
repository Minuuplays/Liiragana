package com.langquest.model;

import java.util.List;

public class WordData {
    public static List<Word> getAllWords() {
        return List.of(
                // Greetings
                new Word("こんにちは", "konnichiwa", "Hello / Good afternoon", WordCategory.GREETING),
                new Word("おはよう", "ohayou", "Good morning", WordCategory.GREETING),
                new Word("こんばんは", "konbanwa", "Good evening", WordCategory.GREETING),
                new Word("さようなら", "sayounara", "Goodbye", WordCategory.GREETING),
                new Word("ありがとう", "arigatou", "Thank you", WordCategory.GREETING),
                new Word("すみません", "sumimasen", "Excuse me / Sorry", WordCategory.GREETING),
                new Word("はい", "hai", "Yes", WordCategory.GREETING),
                new Word("いいえ", "iie", "No", WordCategory.GREETING),
                new Word("おねがいします", "onegaishimasu", "Please", WordCategory.GREETING),

                // Nouns
                new Word("わたし", "watashi", "I / me", WordCategory.NOUN),
                new Word("なまえ", "namae", "name", WordCategory.NOUN),
                new Word("みず", "mizu", "water", WordCategory.NOUN),
                new Word("ねこ", "neko", "cat", WordCategory.NOUN),
                new Word("いぬ", "inu", "dog", WordCategory.NOUN),
                new Word("ひと", "hito", "person", WordCategory.NOUN),
                new Word("がっこう", "gakkou", "school", WordCategory.NOUN),
                new Word("せんせい", "sensei", "teacher", WordCategory.NOUN),
                new Word("ともだち", "tomodachi", "friend", WordCategory.NOUN),

                // Family
                new Word("かぞく", "kazoku", "family", WordCategory.FAMILY),
                new Word("ちち", "chichi", "father (own)", WordCategory.FAMILY),
                new Word("はは", "haha", "mother (own)", WordCategory.FAMILY),
                new Word("あに", "ani", "older brother", WordCategory.FAMILY),
                new Word("あね", "ane", "older sister", WordCategory.FAMILY),
                new Word("おとうと", "otouto", "younger brother", WordCategory.FAMILY),
                new Word("いもうと", "imouto", "younger sister", WordCategory.FAMILY),
                new Word("こども", "kodomo", "child", WordCategory.FAMILY),

                // Numbers
                new Word("いち", "ichi", "one", WordCategory.NUMBER),
                new Word("に", "ni", "two", WordCategory.NUMBER),
                new Word("さん", "san", "three", WordCategory.NUMBER),
                new Word("よん", "yon", "four", WordCategory.NUMBER),
                new Word("ご", "go", "five", WordCategory.NUMBER),
                new Word("ろく", "roku", "six", WordCategory.NUMBER),
                new Word("なな", "nana", "seven", WordCategory.NUMBER),
                new Word("はち", "hachi", "eight", WordCategory.NUMBER),
                new Word("きゅう", "kyuu", "nine", WordCategory.NUMBER),
                new Word("じゅう", "juu", "ten", WordCategory.NUMBER),

                // Seasons
                new Word("きせつ", "kisetsu", "season", WordCategory.SEASON),
                new Word("はる", "haru", "spring", WordCategory.SEASON),
                new Word("なつ", "natsu", "summer", WordCategory.SEASON),
                new Word("あき", "aki", "autumn / fall", WordCategory.SEASON),
                new Word("ふゆ", "fuyu", "winter", WordCategory.SEASON),

                // Adjectives
                new Word("おおきい", "ookii", "big", WordCategory.ADJECTIVE),
                new Word("ちいさい", "chiisai", "small", WordCategory.ADJECTIVE),
                new Word("あたらしい", "atarashii", "new", WordCategory.ADJECTIVE),
                new Word("ふるい", "furui", "old (objects)", WordCategory.ADJECTIVE),
                new Word("いい", "ii", "good", WordCategory.ADJECTIVE),
                new Word("わるい", "warui", "bad", WordCategory.ADJECTIVE),
                new Word("たのしい", "tanoshii", "fun / enjoyable", WordCategory.ADJECTIVE),
                new Word("むずかしい", "muzukashii", "difficult", WordCategory.ADJECTIVE)
        );
    }
}