package com.langquest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonFactory {

    /**
     * Builds a multiple-choice lesson from a group of Gana. Each exercise
     * shows one kana; wrong-answer options are randomly drawn from the
     * rest of the same group.
     */
    public static Lesson buildLesson(String title, List<Gana> ganaGroup) {
        List<Exercise> exercises = new ArrayList<>();

        for (Gana correct : ganaGroup) {
            List<String> distractorPool = new ArrayList<>();
            for (Gana other : ganaGroup) {
                if (!other.equals(correct)) {
                    distractorPool.add(other.romaji());
                }
            }
            Collections.shuffle(distractorPool);

            List<String> options = new ArrayList<>();
            options.add(correct.romaji());
            options.addAll(distractorPool.subList(0, Math.min(2, distractorPool.size())));
            Collections.shuffle(options);

            exercises.add(new Exercise(correct.character(), correct.romaji(), options));
        }

        return new Lesson(title, exercises);
    }
}