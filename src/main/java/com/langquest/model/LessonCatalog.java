package com.langquest.model;

import java.util.ArrayList;
import java.util.List;

public class LessonCatalog {

    public record LessonDefinition(String title, List<? extends Teachable> items, boolean isHiragana) {
        public int itemCount() {
            return items.size();
        }
    }

    public static List<LessonDefinition> getAllLessons() {
        List<LessonDefinition> lessons = new ArrayList<>();

        for (GanaGroup group : GanaGroup.values()) {
            List<Gana> items = HiraganaData.getAllHiragana().stream()
                    .filter(g -> g.group() == group)
                    .toList();
            if (!items.isEmpty()) {
                lessons.add(new LessonDefinition(formatEnumName(group.name()), items, true));
            }
        }

        for (WordCategory category : WordCategory.values()) {
            List<Word> items = WordData.getAllWords().stream()
                    .filter(w -> w.category() == category)
                    .toList();
            if (!items.isEmpty()) {
                lessons.add(new LessonDefinition(formatEnumName(category.name()), items, false));
            }
        }

        return lessons;
    }

    private static String formatEnumName(String enumName) {
        String name = enumName.replace("_", "-").toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}