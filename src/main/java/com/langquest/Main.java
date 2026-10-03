package com.langquest;

import com.langquest.data.JsonDataLoader;
import com.langquest.db.DatabaseManager;
import com.langquest.model.HiraganaData;
import com.langquest.model.WordData;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        Parent loadingRoot = FXMLLoader.load(Main.class.getResource("loading-view.fxml"));
        stage.setTitle("LangQuest");
        Scene loadingScene = new Scene(loadingRoot, 500, 300);
        loadingScene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        stage.setScene(loadingScene);
        stage.show();

        Task<Void> startupTask = new Task<>() {
            @Override
            protected Void call() {
                HiraganaData.setData(JsonDataLoader.loadHiragana());
                WordData.setData(JsonDataLoader.loadWords());
                DatabaseManager.initialize();
                return null;
            }
        };

        startupTask.setOnSucceeded(e -> showProfileSelect());
        startupTask.setOnFailed(e -> startupTask.getException().printStackTrace());

        AppExecutor.submit(startupTask);
    }

    private void showProfileSelect() {
        try {
            FXMLLoader selectLoader = new FXMLLoader(Main.class.getResource("profile-select-view.fxml"));
            Parent selectRoot = selectLoader.load();
            ProfileSelectController selectController = selectLoader.getController();

            selectController.setOnProfileSelected(this::showMainApp);

            Scene selectScene = new Scene(selectRoot, 600, 450);
            selectScene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            primaryStage.setScene(selectScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showMainApp() {
        try {
            FXMLLoader mainLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
            Parent mainRoot = mainLoader.load();
            MainController mainController = mainLoader.getController();
            mainController.setOnProfileSelect(this::showProfileSelect);

            Scene mainScene = new Scene(mainRoot, 980, 620);
            mainScene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            primaryStage.setScene(mainScene);
            primaryStage.setMinWidth(500);
            primaryStage.setMinHeight(400);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}