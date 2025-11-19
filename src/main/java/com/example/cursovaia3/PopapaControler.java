package com.example.cursovaia3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PopapaControler {
    @FXML
    public TextField addName;


    private final FileHandler<Category> categoryFileHandler = new FileHandler<>("Category.json", Category.class);

    public PopapaControler() throws IOException {
    }


    @FXML
    public void onAddButtonClick(MouseEvent mouseEvent) {
        Category category = new Category(0,addName.getText());
        categoryFileHandler.Write(category);
        Stage stage = (Stage) addName.getScene().getWindow();
        stage.close();
    }

}
