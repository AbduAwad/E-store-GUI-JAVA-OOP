//Base class for all products the store will sell
public abstract class Product{
    private final double price;
    private int stockQuantity;
    private int soldQuantity = 0;
    private String name;


    public Product(double initPrice, int initQuantity) {
        price = initPrice;
        stockQuantity = initQuantity;
    }

    public String getName() {
        return name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public double getPrice() {
        return price;
    }
    public void setStockQuantity(int x) {
        stockQuantity = x;
    }
    public void setSoldQuantity(int x) {
        soldQuantity = x;
    }

    //Returns the total revenue (price * amount) if there are at least amount items in stock
    //Return 0 otherwise (i.e., there is no sale completed)
    public double sellUnits(int amount) {
        if (amount > 0 && stockQuantity >= amount) {
            stockQuantity -= amount;
            soldQuantity += amount;
            return price * amount;
        }
        return 0.0;
    }
}