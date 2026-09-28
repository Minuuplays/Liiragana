package com.langquest;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public abstract class BaseController {

    protected abstract String screenName();   // every screen must declare its name

    protected void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(screenName() + " - Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}