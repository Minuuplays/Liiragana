package com.langquest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonFactory {

    public static Lesson buildLesson(String title, List<? extends Teachable> items) {
        List<Exercise> exercises = new ArrayList<>();

        for (Teachable correct : items) {
            List<String> distractorPool = new ArrayList<>();
            for (Teachable other : items) {
                if (!other.equals(correct)) {
                    distractorPool.add(other.answer());
                }
            }
            Collections.shuffle(distractorPool);

            List<String> options = new ArrayList<>();
            options.add(correct.answer());
            options.addAll(distractorPool.subList(0, Math.min(2, distractorPool.size())));
            Collections.shuffle(options);

            exercises.add(new Exercise(correct.prompt(), correct.answer(), options));
        }

        Collections.shuffle(exercises);
        return new Lesson(title, exercises);
    }
}