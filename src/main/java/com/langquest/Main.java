package com.langquest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Entry point for LangQuest.
 *
 * Right now this just proves the toolchain works end to end:
 * JDK -> Maven -> javafx-maven-plugin -> an actual window on screen.
 * Once this opens cleanly, we move on to the real domain model and UI.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("LangQuest is alive!");
        label.setStyle("-fx-font-size: 18px;");

        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("LangQuest - Setup Check");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
