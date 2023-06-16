package dashboard;




public class Stock_1 {
    private String symbol;
    private String name;
    private double price;

    public Stock_1(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    
    @Override
    public String toString(){
        return "\n|\t" + getSymbol() + "\t|\t" + getName() + "\t|\t" + getPrice() + "\t|\n";
    }
}