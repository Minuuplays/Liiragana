package com.langquest.model;

import java.util.ArrayList;
import java.util.List;

public class LessonCatalog {

    public record LessonDefinition(String title, int itemCount, boolean isHiragana) {}

    public static List<LessonDefinition> getAllLessons() {
        List<LessonDefinition> lessons = new ArrayList<>();

        for (GanaGroup group : GanaGroup.values()) {
            long count = HiraganaData.getAllHiragana().stream()
                    .filter(g -> g.group() == group)
                    .count();
            if (count > 0) {
                lessons.add(new LessonDefinition(formatEnumName(group.name()), (int) count, true));
            }
        }

        for (WordCategory category : WordCategory.values()) {
            long count = WordData.getAllWords().stream()
                    .filter(w -> w.category() == category)
                    .count();
            if (count > 0) {
                lessons.add(new LessonDefinition(formatEnumName(category.name()), (int) count, false));
            }
        }

        return lessons;
    }

    private static String formatEnumName(String enumName) {
        String name = enumName.replace("_", "-").toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}