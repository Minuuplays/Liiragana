package com.langquest;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Animations {

    public static void addHoverScale(Node node) {
        ScaleTransition grow = new ScaleTransition(Duration.millis(120), node);
        grow.setToX(1.06);
        grow.setToY(1.06);
        ScaleTransition shrink = new ScaleTransition(Duration.millis(120), node);
        shrink.setToX(1.0);
        shrink.setToY(1.0);

        node.setOnMouseEntered(e -> { shrink.stop(); grow.playFromStart(); });
        node.setOnMouseExited(e -> { grow.stop(); shrink.playFromStart(); });
    }

    public static void pop(Node node) {
        node.setScaleX(0.85);
        node.setScaleY(0.85);
        node.setOpacity(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(220), node);
        scale.setToX(1);
        scale.setToY(1);
        scale.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.millis(220), node);
        fade.setToValue(1);

        new ParallelTransition(scale, fade).play();
    }

    public static void shake(Node node, Runnable onFinished) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(60), node);
        shake.setFromX(-8);
        shake.setToX(8);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> {
            node.setTranslateX(0);
            onFinished.run();
        });
        shake.play();
    }

    public static void shakeOnFocusLoss(Stage modalStage, Node nodeToShake) {
        modalStage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused && modalStage.isShowing()) {
                shake(nodeToShake, modalStage::requestFocus);
            }
        });
    }
}