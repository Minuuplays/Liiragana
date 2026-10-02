package com.langquest;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ToastManager {

    private static VBox layer;

    public static void register(VBox toastLayer) {
        layer = toastLayer;
    }

    public static void show(String message, ToastType type) {
        if (layer == null) return;

        Platform.runLater(() -> {
            Label toast = new Label(type.emoji() + "  " + message);
            toast.getStyleClass().addAll("toast", type.styleClass());
            toast.setOpacity(0);
            layer.getChildren().add(toast);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), toast);
            fadeIn.setToValue(1);

            PauseTransition stay = new PauseTransition(Duration.seconds(2));

            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), toast);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> layer.getChildren().remove(toast));

            new SequentialTransition(fadeIn, stay, fadeOut).play();
        });
    }
}