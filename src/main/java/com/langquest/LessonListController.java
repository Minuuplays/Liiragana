package com.langquest;

import com.langquest.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        for (GanaGroup group : GanaGroup.values()) {
            List<Gana> ganaInGroup = HiraganaData.getAllHiragana().stream()
                    .filter(g -> g.group() == group)
                    .toList();

            if (ganaInGroup.isEmpty()) continue;

            String label = formatGroupName(group) + "  (" + ganaInGroup.size() + ")";
            Button button = new Button(label);
            button.setPrefWidth(220);

            button.setOnAction(e -> {
                Lesson lesson = LessonFactory.buildLesson(formatGroupName(group), ganaInGroup);
                mainController.loadExerciseView(lesson);
            });

            lessonButtonsBox.getChildren().add(button);
        }
    }

    private String formatGroupName(GanaGroup group) {
        String name = group.name().replace("_", "-").toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}