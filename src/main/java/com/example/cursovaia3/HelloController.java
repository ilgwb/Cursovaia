package com.example.cursovaia3;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.SplitPaneSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;

public class HelloController {
    @FXML
    public TableColumn<Product, Integer> productNum;
    @FXML
    public TableColumn<Product, String> productName;
    @FXML
    public TableColumn<Product, String> productCategory;
    @FXML
    public TableColumn<Category, Integer> categoryNum;
    @FXML
    public TableColumn<Category, String> categoryName;
    @FXML
    public TableView<Product> productTab;
    @FXML
    public TableView<Category> categoryTab;


    private final FileHandler<Category> categoryFileHandler = new FileHandler<>("Category.json", Category.class);
    private final FileHandler<Product> productFileHandler = new FileHandler<>("Product.json", Product.class);

    @FXML
    private void initialize() throws IOException {
        productTab.getItems().clear();
        categoryTab.getItems().clear();


        // Отвечают за распределение полей класса по колонкам

        categoryNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        productNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Отвечает за то какой тип данных будет при изменении и как с ним обращаться
        categoryName.setCellFactory(TextFieldTableCell.forTableColumn());
        productName.setCellFactory(TextFieldTableCell.forTableColumn());
        productCategory.setCellFactory(TextFieldTableCell.forTableColumn());


        // Отвечает за то что будет происходить когда пользователь изменит табличку
        categoryName.setOnEditCommit(e -> {
            e.getRowValue().setName(e.getNewValue());
                categoryFileHandler.Update(e.getRowValue());
        });

        productName.setOnEditCommit(e -> {
            e.getRowValue().setName(e.getNewValue());
                productFileHandler.Update(e.getRowValue());
        });
        productCategory.setOnEditCommit(e -> {
            e.getRowValue().setCategory(e.getNewValue());
                productFileHandler.Update(e.getRowValue());
        });





        // Создает задачу загрузки данных из файла
        Task<List<Product>> getProduct = new Task<List<Product>>() {
            // Что будет выполняться в другом потоке и вернет список машин
            @Override
            protected List<Product> call() throws Exception {
                return productFileHandler.Read();
            }
        };

        // Говорю что делать после того как задача успешно завершится
        getProduct.setOnSucceeded(e -> {
            // Обновляю таблицу
            // getProduct.getValue() это возвращенный результат из задачи getProduct
            // тоесть список машин
            productTab.getItems().addAll(getProduct.getValue());
        });
        Task<List<Category>> getCategory = new Task<List<Category>>() {
            // Что будет выполняться в другом потоке и вернет список машин
            @Override
            protected List<Category> call() throws Exception {
                return categoryFileHandler.Read();
            }
        };

        // Говорю что делать после того как задача успешно завершится
        getCategory.setOnSucceeded(e -> {
            // Обновляю таблицу
            // getProduct.getValue() это возвращенный результат из задачи getProduct
            // тоесть список машин
            categoryTab.getItems().addAll(getCategory.getValue());
            System.out.println(getCategory.getValue());
        });

        // Запускаю задачу
        new Thread(getProduct).start();
        new Thread(getCategory).start();
    }

    @FXML
    private Label welcomeText;

    public HelloController() throws IOException {
    }

    @FXML
    private void onProductTableKeyReleased(KeyEvent eventRemove) throws IOException {
        if(eventRemove.getCode() == KeyCode.DELETE){
            int number = productTab.getSelectionModel().getSelectedItem().getNumber();
            if(number != 0){
                 productFileHandler.Delete(number);
                 initialize();
            }
        }
    }
    @FXML
    private void onCategoryTableKeyReleased(KeyEvent eventRemove) throws IOException {
        if(eventRemove.getCode() == KeyCode.DELETE){
            int number = categoryTab.getSelectionModel().getSelectedItem().getNumber();
            if(number != 0){
                 categoryFileHandler.Delete(number);
                 initialize();
            }
        }
    }

    @FXML
    private void onAddProductButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popup-product.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        popup.setScene(new Scene(root));
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();

    }
}
