package com.langquest;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Runs automatically right after the FXML loads.
        // Setup logic (e.g. loading initial lesson data) goes here later.
    }
}