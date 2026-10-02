package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.AttemptResult;
import com.langquest.model.Exercise;
import com.langquest.model.Lesson;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ExerciseController {

    @FXML private ProgressBar progressBar;
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

        int total = currentLesson.exercises().size();
        animateProgress((double) currentIndex / total);

        if (currentIndex >= total) {
            boolean perfect = score == total;
            promptLabel.setTooltip(null);
            promptLabel.setText(perfect ? "Perfect!  ★" : "Lesson complete!");
            scoreLabel.setText("Score: " + score + " / " + total);
            Animations.pop(promptLabel);

            Task<AttemptResult> saveTask = new Task<>() {
                @Override
                protected AttemptResult call() {
                    return DatabaseManager.recordLessonAttempt(CurrentUser.get().id(), currentLesson.title(), score);
                }
            };
            saveTask.setOnSucceeded(e -> {
                AttemptResult result = saveTask.getValue();
                if (result == AttemptResult.FIRST_ATTEMPT) {
                    ToastManager.show("+" + (score * 10) + " XP!", ToastType.XP);
                } else if (result == AttemptResult.IMPROVED) {
                    ToastManager.show("New personal best!", ToastType.BEST);
                }
            });
            AppExecutor.submit(saveTask);
            return;
        }

        Exercise exercise = currentLesson.exercises().get(currentIndex);
        promptLabel.setText(exercise.prompt());
        Animations.pop(promptLabel);
        Animations.pop(optionsBox);

        if (exercise.clue() != null) {
            Tooltip tooltip = new Tooltip(exercise.clue());
            tooltip.setShowDelay(Duration.millis(150));
            tooltip.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-text-fill: black;");
            promptLabel.setTooltip(tooltip);
        } else {
            promptLabel.setTooltip(null);
        }

        for (String option : exercise.options()) {
            Button button = new Button(option);
            button.getStyleClass().add("option-button");
            button.setOnAction(e -> checkAnswer(button, option, exercise.correctAnswer()));
            Animations.addHoverScale(button);
            optionsBox.getChildren().add(button);
        }

        scoreLabel.setText("Question " + (currentIndex + 1) + " of " + total);
    }

    private void checkAnswer(Button clicked, String selected, String correct) {
        for (var node : optionsBox.getChildren()) {
            node.setDisable(true);
            Button b = (Button) node;
            if (b.getText().equals(correct)) {
                b.getStyleClass().add("option-correct");
            }
        }

        currentIndex++;

        if (selected.equals(correct)) {
            feedbackLabel.setText("Correct!");
            feedbackLabel.getStyleClass().setAll("feedback-correct");
            score++;

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> showExercise());
            pause.play();
        } else {
            clicked.getStyleClass().add("option-wrong");
            Animations.shake(promptLabel.getParent(), () ->
                    Platform.runLater(() -> {
                        showCorrectionDialog(correct);
                        showExercise();
                    }));
        }
    }

    private void animateProgress(double target) {
        new Timeline(new KeyFrame(Duration.millis(350),
                new KeyValue(progressBar.progressProperty(), target))).play();
    }

    private void showCorrectionDialog(String correctAnswer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("correction-dialog.fxml"));
            Parent root = loader.load();

            CorrectionDialogController controller = loader.getController();
            controller.setCorrectAnswer(correctAnswer);

            Stage ownerStage = (Stage) promptLabel.getScene().getWindow();

            Scene dialogScene = new Scene(root);
            dialogScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Correction");
            dialogStage.setScene(dialogScene);
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