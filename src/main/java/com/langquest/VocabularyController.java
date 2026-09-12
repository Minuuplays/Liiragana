package com.langquest;

import com.langquest.model.Word;
import com.langquest.model.WordData;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class VocabularyController {

    @FXML
    private ListView<String> wordListView;

    @FXML
    public void initialize() {
        List<Word> allWords = WordData.getAllWords();
        for (Word w : allWords) {
            wordListView.getItems().add(w.kana() + "  ->  " + w.romaji() + "  (" + w.meaning() + ")   [" + w.category() + "]");
        }
    }
}