package com.langquest.model;

public interface Teachable {
    String prompt();          // what's shown, e.g. a kana character or a word
    String quizOptionText();  // correct answer + option-pool text used in the quiz
    String hint();            // teaching text shown before quizzing

    default String clue() {   // optional hover-clue during the quiz; null = no clue shown
        return null;
    }
}