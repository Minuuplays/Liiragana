package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.LeaderboardEntry;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class LeaderboardController {

    @FXML private TableView<LeaderboardEntry> leaderboardTable;
    @FXML private TableColumn<LeaderboardEntry, Integer> rankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> usernameColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> xpColumn;

    @FXML
    public void initialize() {
        rankColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        usernameColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().username()));
        xpColumn.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().totalXp()).asObject());

        Task<List<LeaderboardEntry>> loadTask = new Task<>() {
            @Override
            protected List<LeaderboardEntry> call() {
                return DatabaseManager.getLeaderboard();
            }
        };

        loadTask.setOnSucceeded(e ->
                leaderboardTable.setItems(FXCollections.observableArrayList(loadTask.getValue())));

        AppExecutor.submit(loadTask);
    }
}