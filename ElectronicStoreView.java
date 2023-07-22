import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import java.util.*;


public class ElectronicStoreView extends Pane {
    private final ListView<String>    MostPopularItemsList;
    private final ListView<String> stockList;
    private final ListView<String> cartList;
    private final Button resetButton;
    private final Button addButton;
    private final Button removeButton;
    private final Button saleButton;
    private Label cartLabel = new Label("Current Cart: ");
    private TextField salesTextField = new TextField();
    private TextField revenueTextField = new TextField();
    private TextField averageSaleTextField = new TextField();


    public Button getAddButton() {
        return addButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getSaleButton() {
        return saleButton;
    }

    public ListView<String> getCartList() {
        return cartList;
    }

    public ListView<String> getMostPopularItemsList() {
        return MostPopularItemsList;
    }

    public ListView<String> getStockList() {
        return stockList;
    }
    public Label getCartLabel() { return cartLabel; }

    public ElectronicStoreView() {
        // Create the labels
        Label storeSummaryLabel = new Label("Store Summary:");
        storeSummaryLabel.relocate(25, 10);

        Label salesLabel = new Label("# Sales:");
        salesLabel.relocate(23, 35);
        salesTextField.relocate(73, 32);
        salesTextField.setPrefSize(90, 10);

        Label revenueLabel = new Label("Revenue:");
        revenueLabel.relocate(20, 65);
        revenueTextField.relocate(73, 61);
        revenueTextField.setPrefSize(90, 10);

        Label averageSaleLabel = new Label("$ / sale:");
        averageSaleLabel.relocate(28, 95);
        averageSaleTextField.relocate(73, 90);
        averageSaleTextField.setPrefSize(90, 10);

        Label MPItemsLabel = new Label("Most Popular Items:");
        MPItemsLabel.relocate(28, 125);
        MostPopularItemsList = new ListView<String>();
        MostPopularItemsList.relocate(10, 150);
        MostPopularItemsList.setPrefSize(150,170);

        resetButton = new Button("Reset Store");
        resetButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(250,250,250); -fx-text-fill: rgb(0,0,0);");
        resetButton.relocate(20, 325);
        resetButton.setPrefSize(125,50);

        Label stockLabel = new Label("Store Stock:");
        stockLabel.relocate(300, 10);
        stockList = new ListView<String>();
        stockList.relocate(173, 32);
        stockList.setPrefSize(275,288);

        addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(250,250,250); -fx-text-fill: rgb(0,0,0);");
        addButton.relocate(247, 325);
        addButton.setPrefSize(125,50);

        cartLabel.relocate(550, 10);
        cartList = new ListView<String>();
        cartList.relocate(460, 32);
        cartList.setPrefSize(275,288);

        removeButton = new Button("Remove From Cart");
        removeButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(250,250,250); -fx-text-fill: rgb(0,0,0);");
        removeButton.relocate(461, 325);
        removeButton.setPrefSize(136,50);

        saleButton = new Button("Complete Sale");
        saleButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(250,250,250); -fx-text-fill: rgb(0,0,0);");
        saleButton.relocate(599, 325);
        saleButton.setPrefSize(136,50);

        salesTextField.setText("0");
        revenueTextField.setText("$0.00");
        averageSaleTextField.setText("N/A");

        getChildren().addAll(addButton, saleButton, removeButton, cartList, cartLabel, stockList, stockLabel, resetButton, MostPopularItemsList, MPItemsLabel, averageSaleTextField, salesLabel, revenueLabel, averageSaleLabel, storeSummaryLabel, salesTextField, revenueTextField);
    }

    public void addToCartList( String selectedItem, ObservableList<String> cartItems) {
        boolean inCart = false;

        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i) != null && cartItems.get(i).contains(selectedItem)) {
                String[] cartItemLine = cartItems.get(i).split(" x ");
                int itemQuantity = Integer.parseInt(cartItemLine[0]);
                itemQuantity++;
                cartItems.set(i, itemQuantity + " x " + selectedItem);
                inCart = true;
                break;
            }
        }
        if (!inCart) {
            cartItems.add("1 x " + selectedItem);
        }
    }

    public void removeFromCartList(String item, ObservableList<String> cartItems, ElectronicStore model) {
        // split the cart item string into product name and quantity
        String[] itemLine = item.split(" x ");
        String productName = itemLine[1];

            // check if product is already in cart
            for (int i = 0; i < cartItems.size(); i++) {
                String currentItem = cartItems.get(i);
                if (currentItem.contains(productName)) {
                    // reduce the quantity of the product in the cart
                    String[] currentItemLine = currentItem.split(" x ");
                    int currentQuantity = Integer.parseInt(currentItemLine[0]);
                    if (currentQuantity == 1) {
                        // if the quantity is 1, remove the item from the cart
                        cartItems.remove(i);
                        updateStockList(model);
                    }
                    if (currentQuantity > 1) {
                        // if the quantity is greater than 1, update the cart item string with the new quantity
                        String newItemLine = (currentQuantity - 1) + " x " + productName;
                        cartItems.set(i, newItemLine);
                        break;
                    }
                }
            }


    }

    public double updateCartPrice(ObservableList<String> cartItems, ElectronicStore model) {
        double totalCartPrice = 0;
        double productPrice = 0;

        for (String item : cartItems) {
            // extract the product name and quantity from the cart item string
            String[] itemLine = item.split(" x ");
            String productName = itemLine[1];
            int quantity = Integer.parseInt(itemLine[0]);

            System.out.println("Product name: " + productName);
            System.out.println("Quantity: " + quantity);

            for (Product product : model.getStock()) {
                if (product != null && product.toString().equals(productName)) {
                    productPrice = product.getPrice();
                }
            }
            totalCartPrice += (quantity * productPrice);
        }
        return totalCartPrice;
    }

    public void updateStockList(ElectronicStore model) {
        ObservableList<String> stockItems = FXCollections.observableArrayList();
        for (Product product : model.getStock()) {
            if (product != null && product.getStockQuantity() > 0) {
                stockItems.add(product.toString());
            }
        }
        stockList.setItems(stockItems);
    }

    public void completeSale(ElectronicStore model, int numSales) {
        double currentCartTotal;
        ObservableList<String> cartItems = getCartList().getItems();
        if (!cartItems.isEmpty()) {
            currentCartTotal = updateCartPrice(cartItems, model);
            model.setRevenue(model.getRevenue() + currentCartTotal);

            revenueTextField.setText(String.format("$%.2f", model.getRevenue()));
            salesTextField.setText((String.format("%d", numSales)));

            double averageSale = model.getRevenue() / numSales;
            averageSaleTextField.setText((String.format("$%.2f", averageSale)));


            for (String item : cartItems) {
                // extract the product name and quantity from the cart item string
                String[] itemLine = item.split(" x ");
                String productName = itemLine[1];
                int quantity = Integer.parseInt(itemLine[0]);

                for (Product product : model.getStock()) {
                    if (product != null && product.toString().equals(productName)) {
                        product.setSoldQuantity(product.getSoldQuantity() + quantity);
                    }
                }
            }

            for (Product product : model.getStock()) {
                if (product != null) {
                    System.out.println(product.getSoldQuantity());
                }
            }

            Arrays.sort(model.getStock(), new Comparator<Product>() {
                public int compare(Product p1, Product p2) {
                    if (p1 == (null) && p2 == null) {
                        return 0;
                    } else if (p1 == null) {
                        return 1;
                    } else if (p2  == null) {
                        return -1;
                    } else {
                        return Integer.compare(p2.getSoldQuantity(), p1.getSoldQuantity());
                    }
                }
            });

            getMostPopularItemsList().getItems().clear();
            int count = 0;
            for (Product product : model.getStock()) {
                count +=1;
                getMostPopularItemsList().getItems().add(product.toString());
                if (count >= 3) {
                    break;
                }
            }
        }
    }

    public void resetStore() {
        // Reset the sales, revenue, and average sale text fields
        salesTextField.setText("0");
        revenueTextField.setText("$0.00");
        averageSaleTextField.setText("N/A");

        // Clear the cart list and set the cart label back to its original text
        cartList.getItems().clear();
        cartLabel.setText("Current Cart:");

        // Clear the most popular items list
        MostPopularItemsList.getItems().clear();
    }


}
