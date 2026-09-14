package com.langquest;

import com.langquest.model.Lesson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;

    @FXML
    public void initialize() {
        showLessons();
    }

    @FXML
    private void showLessons() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lesson-list-view.fxml"));
            Parent view = loader.load();
            LessonListController controller = loader.getController();
            controller.setMainController(this);
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showVocabulary() {
        loadView("vocabulary-view.fxml");
    }

    @FXML
    private void showProfile() {
        loadView("profile-view.fxml");
    }

    public void loadExerciseView(Lesson lesson) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exercise-view.fxml"));
            Parent view = loader.load();
            ExerciseController controller = loader.getController();
            controller.startLesson(lesson);
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}