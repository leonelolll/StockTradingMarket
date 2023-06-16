package dashboard;

public class StockTransaction {
    private Stock_1 stock;
    private int shares;
    private double costPrice;
    private double sellPrice;
    private double ProfitLoss;

    public StockTransaction(Stock_1 stock, int shares, double costPrice, double sellPrice) {
        this.stock = stock;
        this.shares = shares;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
    }

    public Stock_1 getStock() {
        return stock;
    }

    public void setStock(Stock_1 stock) {
        this.stock = stock;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getProfitLoss() {
        ProfitLoss = sellPrice - costPrice;
        return ProfitLoss;
    }

    public void setProfitLoss(double ProfitLoss) {
        this.ProfitLoss = ProfitLoss;
    }
    
    
}
