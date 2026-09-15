package com.langquest;

import com.langquest.model.Teachable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class TeachController {

    @FXML private Label itemLabel;
    @FXML private Label hintLabel;
    @FXML private Label progressLabel;

    private List<? extends Teachable> items;
    private int currentIndex = 0;
    private Runnable onFinished;

    public void startTeaching(List<? extends Teachable> items, Runnable onFinished) {
        this.items = items;
        this.currentIndex = 0;
        this.onFinished = onFinished;
        showItem();
    }

    private void showItem() {
        Teachable item = items.get(currentIndex);
        itemLabel.setText(item.prompt());
        hintLabel.setText(item.hint());
        progressLabel.setText((currentIndex + 1) + " of " + items.size());
    }

    @FXML
    private void handleNext() {
        currentIndex++;
        if (currentIndex >= items.size()) {
            onFinished.run();
        } else {
            showItem();
        }
    }
}