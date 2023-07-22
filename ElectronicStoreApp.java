import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.List;


public class ElectronicStoreApp extends Application {
    public  ElectronicStore model;
    @Override

    public void start(Stage stage) throws Exception {
        Pane aPane = new Pane();
        model = ElectronicStore.createStore();
        ElectronicStoreView view = new ElectronicStoreView();
        aPane.getChildren().add(view);

        view.getAddButton().setDisable(true);
        view.getRemoveButton().setDisable(true);
        view.getSaleButton().setDisable(true);

        // displays in stock products:
        List<String> products = model.displayProducts(model);
        view.getStockList().setItems(FXCollections.observableArrayList(products));

        // display top 3 most popular products:
        List<String> initialPopularProducts = products.subList(0,3);
        view.getMostPopularItemsList().setItems(FXCollections.observableArrayList(initialPopularProducts));


        // listener to check if user presses on stocklistview
        view.getStockList().getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            // check if an item has been selected
            if (newValue.intValue() != -10) {
                // enable the Add to Cart button
                view.getAddButton().setDisable(false);
            }
        });

        // listener to check if user presses on stocklistview
        view.getCartList().getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            // check if an item has been selected
            if (newValue.intValue() != -10) {
                // enable the Add to Cart button
                view.getRemoveButton().setDisable(false);
            }
        });

        // updating the cart
        view.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {

                String selectedItem = view.getStockList().getSelectionModel().getSelectedItem();
                ObservableList<String> cartItems = view.getCartList().getItems();

                //update cart view
                view.addToCartList(selectedItem, cartItems);
                view.getCartLabel().setText(String.format("Current Cart: ($%.2f)", view.updateCartPrice(cartItems, model)));

                // update stock model
                for (Product product : model.getStock()) {
                    if (product != null && selectedItem.contains(product.toString())) {
                        product.setStockQuantity(product.getStockQuantity() - 1);
                    }
                }
                //update stock view
                view.updateStockList(model);
            }
        });

        // check if cart Listview is empty
        view.getCartList().getItems().addListener((ListChangeListener<String>) change -> {
            view.getSaleButton().setDisable(view.getCartList().getItems().isEmpty());
        });

        // updating the cart
        view.getRemoveButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String selectedItem = view.getCartList().getSelectionModel().getSelectedItem();
                ObservableList<String> cartItems = view.getCartList().getItems();

                //update cart view
                view.removeFromCartList(selectedItem, cartItems, model);
                view.getCartLabel().setText(String.format("Current Cart: ($%.2f)", view.updateCartPrice(cartItems, model)));

                // update stock model
                for (Product product : model.getStock()) {
                    System.out.println(selectedItem.substring(3));
                    if (product != null && selectedItem.contains(product.toString())) {
                        System.out.println("Hello");
                        product.setStockQuantity(product.getStockQuantity() + 1);
                        break;
                    }
                }
                //update stock view
                view.updateStockList(model);
            }
        });

        // complete a sale:
        view.getSaleButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                ObservableList<String> cartItems = view.getCartList().getItems();
                model.setNumSales(model.getNumSales() + 1);
                view.completeSale(model , model.getNumSales());
                view.getCartLabel().setText(String.format("Current Cart: ($%.2f)", view.updateCartPrice(cartItems, model)));
                view.updateCartPrice(cartItems, model);
                view.updateStockList(model);
                cartItems.clear();
            }
        });

        view.getResetButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                //reset the model:
                model = ElectronicStore.createStore();
                System.out.println(model);
                ObservableList<String> cartItems = view.getCartList().getItems();
                cartItems.clear();
                view.updateCartPrice(cartItems, model);
                view.updateStockList(model);
                view.resetStore();
                // reset most popular products:
                List<String> initialPopularProducts = products.subList(0,3);
                view.getMostPopularItemsList().setItems(FXCollections.observableArrayList(initialPopularProducts));
            }
        });
        stage.setTitle("Electronic Store Application - " + model.getName());
        stage.setResizable(false);
        stage.setScene(new Scene(aPane, 745, 390));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
