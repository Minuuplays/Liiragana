package com.langquest;

import com.langquest.model.Exercise;
import com.langquest.model.Lesson;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.langquest.db.DatabaseManager;

import java.io.IOException;

public class ExerciseController {

    @FXML private Label promptLabel;
    @FXML private HBox optionsBox;
    @FXML private Label feedbackLabel;
    @FXML private Label scoreLabel;

    private Lesson currentLesson;
    private int currentIndex = 0;
    private int score = 0;

    public void startLesson(Lesson lesson) {
        this.currentLesson = lesson;
        this.currentIndex = 0;
        this.score = 0;
        showExercise();
    }

    private void showExercise() {
        feedbackLabel.setText("");
        optionsBox.getChildren().clear();

        if (currentIndex >= currentLesson.exercises().size()) {
            promptLabel.setText("Lesson complete!");
            scoreLabel.setText("Score: " + score + " / " + currentLesson.exercises().size());

            Task<Void> saveTask = new Task<>() {
                @Override
                protected Void call() {
                    DatabaseManager.recordLessonAttempt(CurrentUser.get().id(), currentLesson.title(), score);                    return null;
                }
            };
            AppExecutor.submit(saveTask);
            return;
        }

        Exercise exercise = currentLesson.exercises().get(currentIndex);
        promptLabel.setText(exercise.prompt());

        for (String option : exercise.options()) {
            Button button = new Button(option);
            button.setOnAction(e -> checkAnswer(option, exercise.correctAnswer()));
            optionsBox.getChildren().add(button);
        }

        scoreLabel.setText("Question " + (currentIndex + 1) + " of " + currentLesson.exercises().size());
    }

    private void checkAnswer(String selected, String correct) {
        for (var node : optionsBox.getChildren()) {
            node.setDisable(true);
        }

        currentIndex++;

        if (selected.equals(correct)) {
            feedbackLabel.setText("Correct!");
            score++;

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> showExercise());
            pause.play();
        } else {
            showCorrectionDialog(correct);
            showExercise(); // only runs once the dialog is closed — see explanation below
        }
    }

    private void showCorrectionDialog(String correctAnswer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("correction-dialog.fxml"));
            Parent root = loader.load();

            CorrectionDialogController controller = loader.getController();
            controller.setCorrectAnswer(correctAnswer);

            Stage ownerStage = (Stage) promptLabel.getScene().getWindow();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Correction");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(ownerStage);
            dialogStage.setResizable(false);

            dialogStage.setOnShown(event -> {
                dialogStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - dialogStage.getWidth() / 2);
                dialogStage.setY(ownerStage.getY() + ownerStage.getHeight() / 2 - dialogStage.getHeight() / 2);
            });

            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}