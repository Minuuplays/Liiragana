package com.langquest.model;

import java.util.List;

public record Lesson(String title, List<Exercise> exercises) {
}