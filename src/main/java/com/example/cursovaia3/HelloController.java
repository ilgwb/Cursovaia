package com.example.cursovaia3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    private FileHandler<Category> categoryFileHandler = new FileHandler<>("Category.json");
    private FileHandler<Product> productFileHandler = new FileHandler<>("Product.json");


    @FXML
    private Label welcomeText;

    public HelloController() throws IOException {
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
