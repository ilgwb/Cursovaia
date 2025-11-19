package com.example.cursovaia3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PopapControler {
    @FXML
    public TextField addName;
    @FXML
    public TextField addCategory;

    private final FileHandler<Product> productFileHandler = new FileHandler<>("Product.json", Product.class);
    private final FileHandler<Category> categoryFileHandler = new FileHandler<>("Category.json", Category.class);

    public PopapControler() throws IOException {
    }


    @FXML
    public void onAddButtonClick(MouseEvent mouseEvent) {
        Product product = new Product(0,addName.getText(),addCategory.getText());
        List<Category> categories = categoryFileHandler.Read();
        boolean plag = false;
        for (Category category: categories){
            if(category.getName().equals(product.getCategory())){
                plag = true;
                break;
            }
        }
        if (plag == false){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ті даун");
            alert.setHeaderText("ряльна даун");
            alert.show();
            return;
        }
        productFileHandler.Write(product);
        Stage stage = (Stage) addName.getScene().getWindow();
        stage.close();
    }

}
