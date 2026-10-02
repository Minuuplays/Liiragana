package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.CompletedLesson;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Optional;

public class LessonHistoryController {

    @FXML private TableView<CompletedLesson> completedTable;
    @FXML private TableColumn<CompletedLesson, String> lessonColumn;
    @FXML private TableColumn<CompletedLesson, Integer> scoreColumn;
    @FXML private TableColumn<CompletedLesson, String> dateColumn;

    @FXML
    public void initialize() {
        lessonColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().title()));
        scoreColumn.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().score()).asObject());
        dateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().completedAt()));

        refreshData();
    }

    @FXML
    private void handleRefresh() {
        refreshData();
    }

    @FXML
    private void handleDelete() {
        CompletedLesson selected = completedTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("No Selection");
            warning.setHeaderText(null);
            warning.setContentText("Select a completed lesson first.");
            warning.showAndWait();
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Remove \"" + selected.title() + "\" from your completed lessons?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Task<Void> deleteTask = new Task<>() {
                @Override
                protected Void call() {
                    DatabaseManager.deleteCompletedLesson(CurrentUser.get().id(), selected.title());
                    return null;
                }
            };
            deleteTask.setOnSucceeded(e -> refreshData());
            AppExecutor.submit(deleteTask);
        }
    }

    private void refreshData() {
        Task<List<CompletedLesson>> loadTask = new Task<>() {
            @Override
            protected List<CompletedLesson> call() {
                return DatabaseManager.getCompletedLessons(CurrentUser.get().id());
            }
        };
        loadTask.setOnSucceeded(e ->
                completedTable.setItems(FXCollections.observableArrayList(loadTask.getValue())));
        AppExecutor.submit(loadTask);
    }
}