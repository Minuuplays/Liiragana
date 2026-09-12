package com.langquest;

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
        showLessons(); // default screen on startup
    }

    @FXML
    private void showLessons() {
        loadView("lessons-view.fxml");
    }

    @FXML
    private void showVocabulary() {
        loadView("vocabulary-view.fxml");
    }

    @FXML
    private void showProfile() {
        loadView("profile-view.fxml");
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