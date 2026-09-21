package com.langquest;

import com.langquest.model.Lesson;
import com.langquest.model.LessonFactory;
import com.langquest.model.Teachable;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private StackPane contentPane;

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
            setCenterWithSlide(view);
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

    public void loadTeachThenQuiz(String title, List<? extends Teachable> items) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teach-view.fxml"));
            Parent view = loader.load();
            TeachController controller = loader.getController();

            controller.startTeaching(items, () -> {
                Lesson lesson = LessonFactory.buildLesson(title, items);
                loadExerciseView(lesson);
            });

            setCenterWithSlide(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadExerciseView(Lesson lesson) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exercise-view.fxml"));
            Parent view = loader.load();
            ExerciseController controller = loader.getController();
            controller.startLesson(lesson);
            setCenterWithSlide(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            setCenterWithSlide(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCenterWithSlide(Parent newView) {
        double distance = contentPane.getWidth() > 0 ? contentPane.getWidth() : 400;
        newView.setTranslateX(distance);
        contentPane.getChildren().setAll(newView);

        TranslateTransition slide = new TranslateTransition(Duration.millis(250), newView);
        slide.setToX(0);
        slide.play();
    }
}