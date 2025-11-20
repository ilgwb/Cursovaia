package com.example.cursovaia3;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SearchControler {
    @FXML
    public TextField search;

    public void onSearchButtonClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) search.getScene().getWindow();
        stage.close();
    }
}
