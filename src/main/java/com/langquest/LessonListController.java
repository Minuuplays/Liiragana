package com.langquest;

import com.langquest.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class LessonListController {

    @FXML
    private VBox lessonButtonsBox;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
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
        lessonButtonsBox.getChildren().add(button);
    }

    private String formatEnumName(String enumName) {
        String name = enumName.replace("_", "-").toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}