package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ProfileSelectController {

    @FXML private VBox userButtonsBox;
    @FXML private TextField newUsernameField;

    private Runnable onProfileSelected;

    public void setOnProfileSelected(Runnable callback) {
        this.onProfileSelected = callback;
    }

    @FXML
    public void initialize() {
        Task<List<User>> loadUsers = new Task<>() {
            @Override
            protected List<User> call() {
                return DatabaseManager.getAllUsers();
            }
        };

        loadUsers.setOnSucceeded(e -> {
            for (User user : loadUsers.getValue()) {
                Button button = new Button(user.username());
                button.setPrefWidth(200);
                button.setOnAction(ev -> selectUser(user));
                userButtonsBox.getChildren().add(button);
            }
        });

        AppExecutor.submit(loadUsers);
    }

    @FXML
    private void handleCreateProfile() {
        String username = newUsernameField.getText().trim();
        if (username.isEmpty()) return;

        Task<User> createTask = new Task<>() {
            @Override
            protected User call() {
                return DatabaseManager.createUser(username);
            }
        };

        createTask.setOnSucceeded(e -> selectUser(createTask.getValue()));
        AppExecutor.submit(createTask);
    }

    private void selectUser(User user) {
        CurrentUser.set(user);
        if (onProfileSelected != null) onProfileSelected.run();
    }
}