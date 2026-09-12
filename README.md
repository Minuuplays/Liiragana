# LangQuest

A JavaFX desktop app for learning a new language, Duolingo-style — this instance teaches Japanese (hiragana, basic words and phrases).

## Requirements
- JDK 21
- Maven (bundled with most IDEs, or install separately)

## Running it
```
mvn javafx:run
```

If that works, you should see a small window that says "LangQuest is alive!" — that confirms JavaFX, Maven, and the plugin are all wired up correctly.

## Project layout
```
src/main/java/com/langquest/
    Main.java          - application entry point
    model/              - domain classes (Kana, Word, Lesson, Exercise, UserProfile)
```
