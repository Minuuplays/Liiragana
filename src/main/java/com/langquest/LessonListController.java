package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LessonListController {

    @FXML
    private VBox lessonButtonsBox;

    private MainController mainController;
    private Map<String, Integer> bestScores = Map.of();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        Task<Map<String, Integer>> loadScores = new Task<>() {
            @Override
            protected Map<String, Integer> call() {
                return DatabaseManager.getCompletedLessons(CurrentUser.get().id()).stream()
                        .collect(Collectors.toMap(CompletedLesson::title, CompletedLesson::score));
            }
        };

        loadScores.setOnSucceeded(e -> {
            bestScores = loadScores.getValue();
            buildLessonList();
        });

        AppExecutor.submit(loadScores);
    }

    private void buildLessonList() {
        lessonButtonsBox.getChildren().clear();

        Label ganaHeader = new Label("Hiragana");
        ganaHeader.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        lessonButtonsBox.getChildren().add(ganaHeader);

        for (GanaGroup group : GanaGroup.values()) {
            List<Gana> ganaInGroup = HiraganaData.getAllHiragana().stream()
                    .filter(g -> g.group() == group)
                    .toList();
            if (ganaInGroup.isEmpty()) continue;
            addLessonButton(formatEnumName(group.name()), ganaInGroup);
        }

        Label wordHeader = new Label("Vocabulary");
        wordHeader.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        lessonButtonsBox.getChildren().add(wordHeader);

        for (WordCategory category : WordCategory.values()) {
            List<Word> wordsInCategory = WordData.getAllWords().stream()
                    .filter(w -> w.category() == category)
                    .toList();
            if (wordsInCategory.isEmpty()) continue;
            addLessonButton(formatEnumName(category.name()), wordsInCategory);
        }
    }

    private void addLessonButton(String title, List<? extends Teachable> items) {
        Button button = new Button(title + "  (" + items.size() + ")");
        button.setPrefWidth(220);
        button.setOnAction(e -> mainController.loadTeachThenQuiz(title, items));

        Integer bestScore = bestScores.get(title);
        if (bestScore != null) {
            button.getStyleClass().add(bestScore == items.size() ? "lesson-perfect" : "lesson-completed");
        }

        lessonButtonsBox.getChildren().add(button);
    }

    private String formatEnumName(String enumName) {
        String name = enumName.replace("_", "-").toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}