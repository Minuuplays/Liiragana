package com.langquest;

import com.langquest.db.DatabaseManager;
import com.langquest.model.CompletedLesson;
import com.langquest.model.LessonCatalog;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

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

    @FXML private Label continueLessonTitleLabel;
    @FXML private Button continueLessonButton;
    @FXML private PieChart masteryChart;
    @FXML private Label gettingStartedBadge;
    @FXML private Label hiraganaMasterBadge;
    @FXML private Label vocabMasterBadge;
    @FXML private Label perfectionistBadge;

    private MainController mainController;
    private LessonCatalog.LessonDefinition nextLesson;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private record ProfileStats(int totalXp, int lessonsCompleted, int totalLessons,
                                int hiraganaCompleted, int hiraganaTotal,
                                int vocabCompleted, int vocabTotal,
                                int wordsLearned,
                                LessonCatalog.LessonDefinition nextLesson,
                                boolean gettingStarted, boolean hiraganaMaster,
                                boolean vocabMaster, boolean perfectionist) {}

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
    private void handleContinueLearning() {
        if (nextLesson != null) {
            mainController.loadTeachThenQuiz(nextLesson.title(), nextLesson.items());
        }
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Log out of this profile?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setTitle("Confirm Logout");
        confirm.setHeaderText(null);

        Animations.shakeOnShow((Stage) confirm.getDialogPane().getScene().getWindow(), confirm.getDialogPane());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            mainController.returnToProfileSelect();
        }
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

        Animations.shakeOnShow((Stage) firstConfirm.getDialogPane().getScene().getWindow(), firstConfirm.getDialogPane());
        Optional<ButtonType> first = firstConfirm.showAndWait();
        if (first.isEmpty() || first.get() != ButtonType.YES) return;

        Alert secondConfirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you absolutely sure? This cannot be undone.",
                ButtonType.YES, ButtonType.NO
        );
        secondConfirm.setTitle("Final Confirmation");
        secondConfirm.setHeaderText(null);

        Animations.shakeOnShow((Stage) secondConfirm.getDialogPane().getScene().getWindow(), secondConfirm.getDialogPane());
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

                LessonCatalog.LessonDefinition next = allLessons.stream()
                        .filter(l -> !completedTitles.contains(l.title()))
                        .findFirst()
                        .orElse(null);

                boolean gettingStarted = !completed.isEmpty();
                boolean hiraganaMaster = hiraganaTotal > 0 && hiraganaDone == hiraganaTotal;
                boolean vocabMaster = vocabTotal > 0 && vocabDone == vocabTotal;

                boolean perfectionist = gettingStarted && completed.stream().allMatch(cl ->
                        allLessons.stream()
                                .filter(l -> l.title().equals(cl.title()))
                                .findFirst()
                                .map(l -> cl.score() == l.itemCount())
                                .orElse(false));

                return new ProfileStats(xp, completedTitles.size(), allLessons.size(),
                        hiraganaDone, hiraganaTotal, vocabDone, vocabTotal, wordsLearned,
                        next, gettingStarted, hiraganaMaster, vocabMaster, perfectionist);
            }

            @Override
            protected void succeeded() {
                ProfileStats stats = getValue();
                nextLesson = stats.nextLesson();

                xpValueLabel.setText(String.valueOf(stats.totalXp()));
                lessonsValueLabel.setText(stats.lessonsCompleted() + " / " + stats.totalLessons());
                wordsValueLabel.setText(String.valueOf(stats.wordsLearned()));

                hiraganaValueLabel.setText(stats.hiraganaCompleted() + " / " + stats.hiraganaTotal());
                hiraganaProgressBar.setProgress(stats.hiraganaTotal() == 0 ? 0 :
                        (double) stats.hiraganaCompleted() / stats.hiraganaTotal());

                vocabValueLabel.setText(stats.vocabCompleted() + " / " + stats.vocabTotal());
                vocabProgressBar.setProgress(stats.vocabTotal() == 0 ? 0 :
                        (double) stats.vocabCompleted() / stats.vocabTotal());

                if (stats.nextLesson() != null) {
                    continueLessonTitleLabel.setText(stats.nextLesson().title());
                    continueLessonButton.setVisible(true);
                    continueLessonButton.setManaged(true);
                } else {
                    continueLessonTitleLabel.setText("You've completed every lesson! 🎉");
                    continueLessonButton.setVisible(false);
                    continueLessonButton.setManaged(false);
                }

                masteryChart.getData().setAll(
                        new PieChart.Data("Completed", stats.lessonsCompleted()),
                        new PieChart.Data("Remaining", stats.totalLessons() - stats.lessonsCompleted())
                );

                gettingStartedBadge.getStyleClass().setAll(stats.gettingStarted() ? "badge-earned" : "badge-locked");
                hiraganaMasterBadge.getStyleClass().setAll(stats.hiraganaMaster() ? "badge-earned" : "badge-locked");
                vocabMasterBadge.getStyleClass().setAll(stats.vocabMaster() ? "badge-earned" : "badge-locked");
                perfectionistBadge.getStyleClass().setAll(stats.perfectionist() ? "badge-earned" : "badge-locked");
            }
        };

        AppExecutor.submit(statsTask);
    }
}