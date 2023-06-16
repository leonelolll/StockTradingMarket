package dashboard;


public class Order {

    private Stock_1 stock;
    private Type type;
    private int shares;
    private double price;
    private User user;

    public Order(Stock_1 stock, Type type, int shares, double price, User user) {
        this.stock = stock;
        this.type = type;
        this.shares = shares;
        this.price = price;
        this.user = user;
    }

    public Stock_1 getStock() {
        return stock;
    }

    public Type getType() {
        return type;
    }

    public void setShares(int shares){
        this.shares = shares;
    }
    
    public int getShares() {
        return shares;
    }

    public double getPrice() {
        return price;
    }
    
    public User getUser(){
        return user;
    }

    public enum Type {
        BUY,
        SELL
    }
}

    
