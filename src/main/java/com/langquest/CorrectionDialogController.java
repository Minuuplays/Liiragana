package com.langquest;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CorrectionDialogController {

    @FXML
    private Label correctAnswerLabel;

    private Stage dialogStage;

    public void setCorrectAnswer(String correctAnswer) {
        correctAnswerLabel.setText("The correct answer was: \"" + correctAnswer + "\"");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleUnderstood() {
        dialogStage.close();
    }
}