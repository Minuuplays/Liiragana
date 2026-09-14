package com.langquest.model;

import java.util.List;

public record Exercise(String prompt, String correctAnswer, List<String> options) {
}