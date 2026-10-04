# Liraganaa 🈴

A JavaFX desktop application for learning a new language, Duolingo-style. This instance teaches **Japanese** — hiragana (all 71 single characters) and vocabulary across 14 categories — through a lesson-based teach-then-quiz flow, with persistent per-user progress tracking.

Built as a course project covering Java/OOP fundamentals, Git, JavaFX GUI design, multithreading, SQLite, and JSON parsing.

---

## Features

- **Hiragana lessons** — all 71 single-character kana (base gojūon + voiced/semi-voiced), grouped into 15 row-based lessons (Vowels, K-row, S-row... through P-row)
- **Vocabulary lessons** — ~112 words across 14 categories (Greetings, Nouns, Family, Numbers, Seasons, Adjectives, Verbs, Colors, Animals, Food, Body Parts, Time, Weather, Place)
- **Teach-then-quiz flow** — each lesson first shows every item once with its reading/meaning, then quizzes with randomized multiple-choice questions and randomized answer order
- **Smart quiz design** — Hiragana lessons test romaji recall directly; Vocabulary lessons test *meaning* recall, with romaji available as a hover tooltip clue rather than a quiz option
- **Lightweight profile system** — pick or create a named profile (no password), with progress tracked separately per user
- **Persistent progress (SQLite)** — best-score-per-lesson is saved and only improves on replay; XP, completion status, and full lesson history persist across sessions
- **Profile dashboard** — total XP, lessons completed, words learned, Hiragana/Vocabulary mastery (with a progress pie chart), a "Continue Learning" shortcut to your next incomplete lesson, and achievement badges
- **Leaderboard** — all profiles ranked by total XP, built on a SQL `JOIN` across users and their completed lessons
- **Responsive, animated UI** — a draggable sidebar, screens that slide/fade in, buttons that grow on hover, a shake effect on wrong answers, and toast notifications for XP gains, personal bests, and login events
- **Background-threaded throughout** — startup data loading, every database read/write, and lesson content loading all run off the UI thread via a shared thread pool, so the interface never freezes

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI | JavaFX 21 (Controls, FXML, Media) |
| Build tool | Maven + `javafx-maven-plugin` |
| Database | SQLite via `sqlite-jdbc` |
| JSON parsing | Jackson (`jackson-databind`) |
| Concurrency | `java.util.concurrent.ExecutorService` + JavaFX `Task` |
| Styling | Plain CSS (`style.css`) |

---

## Project Structure

```
langquest/
├── pom.xml
├── langquest.db                  ← created automatically on first run (gitignored)
├── src/main/java/com/langquest/
│   ├── Main.java                 ← app entry point, scene/screen lifecycle
│   ├── Launcher.java              ← non-Application launch wrapper (for IDE Run button)
│   ├── MainController.java        ← nav shell, screen switching, toast layer
│   ├── BaseController.java        ← abstract base for screens needing shared error handling
│   ├── CurrentUser.java           ← tracks the active session's profile
│   ├── AppExecutor.java           ← shared background thread pool
│   ├── Animations.java            ← reusable hover/pop/shake/slide effects
│   ├── ToastManager.java / ToastType.java
│   ├── ProfileSelectController.java
│   ├── LessonListController.java
│   ├── TeachController.java
│   ├── ExerciseController.java
│   ├── CorrectionDialogController.java
│   ├── VocabularyController.java
│   ├── ProfileController.java
│   ├── LessonHistoryController.java
│   ├── LeaderboardController.java
│   ├── db/
│   │   └── DatabaseManager.java   ← all SQLite CRUD, schema, leaderboard query
│   ├── data/
│   │   └── JsonDataLoader.java    ← Jackson-based JSON → model parsing
│   └── model/
│       ├── Gana.java / GanaGroup.java
│       ├── Word.java / WordCategory.java
│       ├── Teachable.java         ← shared interface for quizzable content
│       ├── Exercise.java / Lesson.java / LessonFactory.java
│       ├── LessonCatalog.java     ← single source of truth for "every lesson that exists"
│       ├── CompletedLesson.java / AttemptResult.java
│       ├── User.java
│       ├── HiraganaData.java / WordData.java   ← populated from JSON at startup
│       └── LeaderboardEntry.java
├── src/main/resources/com/langquest/
│   ├── *.fxml                     ← one per screen
│   ├── style.css
│   └── data/
│       ├── hiragana.json
│       └── words.json
```

---

## Setup & Running

**Requirements:** JDK 21, Maven (or use your IDE's bundled Maven).

```bash
mvn javafx:run
```

Or, from an IDE: open the project, let Maven resolve dependencies, then run `Launcher.main()` (this avoids the "JavaFX runtime components are missing" error you'd get running `Main` directly, since `Main` extends `Application` and needs to be launched via the Maven plugin or a non-`Application` wrapper class).

On first run, `langquest.db` is created automatically in the project root.

---

## Architecture Notes

- **`Teachable` interface** — both `Gana` and `Word` implement it, letting a single generic `LessonFactory`/quiz/teach pipeline work for hiragana rows and vocabulary categories alike, with no duplicated logic.
- **Records + enums throughout** the model layer (`Gana`, `Word`, `Exercise`, `Lesson`, `User`, `CompletedLesson`, `GanaGroup`, `WordCategory`) for immutable, type-safe data.
- **`BaseController`** is an abstract class providing shared error-dialog handling to screens that extend it.
- **Content is JSON, progress is SQLite** — static reference data (hiragana/vocabulary) lives in `resources/.../data/*.json`, parsed via Jackson at startup; per-user dynamic data (XP, completed lessons) lives in SQLite, since it changes at runtime and needs to persist and be queryable (e.g. the leaderboard's `JOIN`).
- **No stored XP counter** — total XP is always computed on demand as `SUM(score) * 10` across a user's completed lessons, rather than incrementally added/subtracted. This avoids drift bugs from double-counting or incomplete rollback on deletion.
- **Best-score-only persistence** — replaying a lesson only updates the stored record if the new score beats the previous one; no reward (and no penalty) for a worse replay.

---

## Course Topics Covered

| Topic | Where |
|-------|-------|
| Java / OOP (incl. interfaces, abstract classes, generics, records, enums) | `model/` package, `BaseController.java` |
| Git / version control | Full commit history |
| JavaFX GUI (SplitPane, BorderPane, GridPane, StackPane, TableView, PieChart, etc.) | `*.fxml`, matching controllers |
| Layout responsiveness | `ExerciseController` property bindings, `SplitPane`/`vgrow` throughout |
| Multithreading / Concurrency Package | `AppExecutor.java`, `Task` usage in nearly every controller |
| SQLite — schema, relationships, CRUD | `db/DatabaseManager.java` |
| JSON parsing | `data/JsonDataLoader.java`, `resources/.../data/*.json` |

---

## Known Limitations / Ideas Not Yet Built

- **Audio pronunciation** — `javafx-media` is included and `Gana` has an `audioPath` field, but no audio files are wired up yet.
- **Live JSON-over-HTTP** — all JSON is currently loaded from local bundled files; fetching JSON from a remote URL at runtime was considered but not implemented.
- **Daily streak tracking** — deferred in favor of the simpler XP/completion model; `completed_at` timestamps already exist and would support adding this later.

---

## Credits

Built by Tahsin, CSE, KUET — with a classmate building a parallel version of this project for a different target language, sharing the same underlying architecture.
