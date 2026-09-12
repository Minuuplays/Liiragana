package com.langquest;

import com.langquest.model.Gana;
import com.langquest.model.HiraganaData;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class LessonsController {

    @FXML
    private ListView<String> ganaListView;

    @FXML
    public void initialize() {
        List<Gana> allGana = HiraganaData.getAllHiragana();
        for (Gana g : allGana) {
            ganaListView.getItems().add(g.character() + "  ->  " + g.romaji() + "   [" + g.group() + "]");
        }
    }
}