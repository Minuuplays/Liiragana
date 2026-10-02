package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.CompletedLesson;
import com.langquest.model.LessonCatalog;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfileController {

    @FXML private Label greetingLabel;
    @FXML private Label xpValueLabel;
    @FXML private Label lessonsValueLabel;
    @FXML private Label wordsValueLabel;
    @FXML private Label hiraganaValueLabel;
    @FXML private ProgressBar hiraganaProgressBar;
    @FXML private Label vocabValueLabel;
    @FXML private ProgressBar vocabProgressBar;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private record ProfileStats(int totalXp, int lessonsCompleted, int totalLessons,
                                int hiraganaCompleted, int hiraganaTotal,
                                int vocabCompleted, int vocabTotal,
                                int wordsLearned) {}

    @FXML
    public void initialize() {
        greetingLabel.setText("こんにちは, " + CurrentUser.get().username() + "! 👋");
        refreshData();
    }

    @FXML
    private void handleViewHistory() {
        mainController.loadLessonHistory();
    }

    @FXML
    private void handleDeleteProfile() {
        Alert firstConfirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete the profile \"" + CurrentUser.get().username() + "\"? This removes all its lesson history and XP.",
                ButtonType.YES, ButtonType.NO
        );
        firstConfirm.setTitle("Delete Profile");
        firstConfirm.setHeaderText(null);

        Optional<ButtonType> first = firstConfirm.showAndWait();
        if (first.isEmpty() || first.get() != ButtonType.YES) return;

        Alert secondConfirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you absolutely sure? This cannot be undone.",
                ButtonType.YES, ButtonType.NO
        );
        secondConfirm.setTitle("Final Confirmation");
        secondConfirm.setHeaderText(null);

        Optional<ButtonType> second = secondConfirm.showAndWait();
        if (second.isEmpty() || second.get() != ButtonType.YES) return;

        Task<Void> deleteTask = new Task<>() {
            @Override
            protected Void call() {
                DatabaseManager.deleteUser(CurrentUser.get().id());
                return null;
            }
        };
        deleteTask.setOnSucceeded(e -> mainController.returnToProfileSelect());
        AppExecutor.submit(deleteTask);
    }

    private void refreshData() {
        Task<ProfileStats> statsTask = new Task<>() {
            private List<CompletedLesson> completed;

            @Override
            protected ProfileStats call() {
                int userId = CurrentUser.get().id();
                completed = DatabaseManager.getCompletedLessons(userId);
                int xp = DatabaseManager.getTotalXp(userId);

                Set<String> completedTitles = completed.stream()
                        .map(CompletedLesson::title)
                        .collect(Collectors.toSet());

                List<LessonCatalog.LessonDefinition> allLessons = LessonCatalog.getAllLessons();

                int hiraganaTotal = 0, hiraganaDone = 0;
                int vocabTotal = 0, vocabDone = 0;
                int wordsLearned = 0;

                for (var lesson : allLessons) {
                    boolean isDone = completedTitles.contains(lesson.title());
                    if (lesson.isHiragana()) {
                        hiraganaTotal++;
                        if (isDone) hiraganaDone++;
                    } else {
                        vocabTotal++;
                        if (isDone) vocabDone++;
                    }
                    if (isDone) wordsLearned += lesson.itemCount();
                }

                return new ProfileStats(xp, completedTitles.size(), allLessons.size(),
                        hiraganaDone, hiraganaTotal, vocabDone, vocabTotal, wordsLearned);
            }

            @Override
            protected void succeeded() {
                ProfileStats stats = getValue();

                xpValueLabel.setText(String.valueOf(stats.totalXp()));
                lessonsValueLabel.setText(stats.lessonsCompleted() + " / " + stats.totalLessons());
                wordsValueLabel.setText(String.valueOf(stats.wordsLearned()));

                hiraganaValueLabel.setText(stats.hiraganaCompleted() + " / " + stats.hiraganaTotal());
                hiraganaProgressBar.setProgress(stats.hiraganaTotal() == 0 ? 0 :
                        (double) stats.hiraganaCompleted() / stats.hiraganaTotal());

                vocabValueLabel.setText(stats.vocabCompleted() + " / " + stats.vocabTotal());
                vocabProgressBar.setProgress(stats.vocabTotal() == 0 ? 0 :
                        (double) stats.vocabCompleted() / stats.vocabTotal());
            }
        };

        AppExecutor.submit(statsTask);
    }
}