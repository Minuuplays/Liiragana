package com.langquest.model;

public interface Teachable {
    String prompt();   // what's shown, e.g. a kana character or a word
    String answer();   // correct romaji, used for the quiz
    String hint();      // teaching text shown before quizzing
}