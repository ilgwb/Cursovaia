package com.example.cursovaia3;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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


        categoryNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        productNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));

        categoryName.setCellFactory(TextFieldTableCell.forTableColumn());
        productName.setCellFactory(TextFieldTableCell.forTableColumn());
        productCategory.setCellFactory(TextFieldTableCell.forTableColumn());

        categoryName.setOnEditCommit(e -> {
            String oldName = e.getOldValue();
            e.getRowValue().setName(e.getNewValue());
                categoryFileHandler.Update(e.getRowValue());
            Task<List<Product>> getProduct = new Task<List<Product>>() {

                @Override
                protected List<Product> call() throws Exception {
                    return productFileHandler.Read();
                }
            };
            getProduct.setOnSucceeded(o -> {
                List<Product> searchResult = new ArrayList<>();
                for (Product product : getProduct.getValue()) {
                    if (product.getCategory().equals(oldName)) {
                        product.setCategory(e.getNewValue());
                        searchResult.add(product);
                    }
                }
                System.out.println(searchResult);
                productFileHandler.Update(searchResult);
                try {
                    initialize();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(getProduct).start();
        });

        productName.setOnEditCommit(e -> {
            e.getRowValue().setName(e.getNewValue());
                productFileHandler.Update(e.getRowValue());
        });
        productCategory.setOnEditCommit(e -> {
            e.getRowValue().setCategory(e.getNewValue());
            List<Category> categories = categoryFileHandler.Read();
            boolean plag = false;
            for (Category category: categories){
                if(category.getName().equals(e.getRowValue().getCategory())){
                    plag = true;
                    break;
                }
            }
            if (plag == false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ті даун");
                alert.setHeaderText("ряльна даун");
                alert.show();
                try {
                    initialize();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }
                productFileHandler.Update(e.getRowValue());
        });
        Task<List<Product>> getProduct = new Task<List<Product>>() {

            @Override
            protected List<Product> call() throws Exception {
                return productFileHandler.Read();
            }
        };
        getProduct.setOnSucceeded(e -> {

            productTab.getItems().addAll(getProduct.getValue());
        });
        Task<List<Category>> getCategory = new Task<List<Category>>() {
            @Override
            protected List<Category> call() throws Exception {
                return categoryFileHandler.Read();
            }
        };
        getCategory.setOnSucceeded(e -> {
            categoryTab.getItems().addAll(getCategory.getValue());
        });
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
            Category category = categoryTab.getSelectionModel().getSelectedItem();
            if(category != null){
                 categoryFileHandler.Delete(category.getNumber());
                Task<List<Product>> getProduct = new Task<List<Product>>() {

                    @Override
                    protected List<Product> call() throws Exception {
                        return productFileHandler.Read();
                    }
                };
                getProduct.setOnSucceeded(e -> {
                    List<Integer> searchResult = new ArrayList<>();
                    for (Product product: getProduct.getValue()){
                        if (product.getCategory().equals( category.getName())){
                            searchResult.add(product.getNumber());
                        }
                    }
                    System.out.println(searchResult);
                    productFileHandler.Delete(searchResult);
                    try {
                        initialize();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                });
                new Thread(getProduct).start();
            }
        }
    }
    @FXML
    private void onProductAddAction() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popap.fxml"));
        Parent root = loader.load();
        Stage popup = new Stage();
        Scene scene = new Scene(root);
        popup.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class).getResource("popap.css").toExternalForm());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        initialize();
    }
    @FXML
    private void onCategoryAddAction() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popapa.fxml"));
        Parent root = loader.load();
        Stage popup = new Stage();
        Scene scene = new Scene(root);
        popup.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class).getResource("popap.css").toExternalForm());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        initialize();
    }


    public void onSearchButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
        Parent root = loader.load();
        Stage popup = new Stage();
        Scene scene = new Scene(root);
        popup.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class).getResource("popap.css").toExternalForm());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        SearchControler controler = loader.getController();
        String searchText = controler.search.getText();
        productTab.getItems().clear();
        Task<List<Product>> getProduct = new Task<List<Product>>() {

            @Override
            protected List<Product> call() throws Exception {
                return productFileHandler.Read();
            }
        };
        getProduct.setOnSucceeded(e -> {
            List<Product> searchResult = new ArrayList<>();
            for (Product product: getProduct.getValue()){
                if (product.getName().toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))){
                    searchResult.add(product);
                }
                else if (product.getCategory().toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))){
                    searchResult.add(product);
                }
            }
            productTab.getItems().addAll(searchResult);
        });
        new Thread(getProduct).start();
    }

    public void onResetButtonClick(ActionEvent actionEvent) throws IOException {
        initialize();
    }

}
