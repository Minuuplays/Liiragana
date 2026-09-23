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

    @Override
    public void start(Stage stage) throws IOException {
        Parent loadingRoot = FXMLLoader.load(Main.class.getResource("loading-view.fxml"));
        stage.setTitle("LangQuest");
        stage.setScene(new Scene(loadingRoot, 300, 150));
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

        startupTask.setOnSucceeded(e -> {
            try {
                Parent root = new FXMLLoader(Main.class.getResource("main-view.fxml")).load();
                stage.setScene(new Scene(root));
                stage.setMinWidth(500);
                stage.setMinHeight(400);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        startupTask.setOnFailed(e -> startupTask.getException().printStackTrace());

        AppExecutor.submit(startupTask);
    }

    public static void main(String[] args) {
        launch(args);
    }
}