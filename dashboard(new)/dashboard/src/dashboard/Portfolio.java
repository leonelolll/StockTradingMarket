package dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {
    private Map<Stock_1, Integer> holdings;
    private double accountBalance = 50000;
    private double initialFunds = 50000;
    private List<StockTransaction> stockTransaction;
    private double profitLoss = 0;

    public Portfolio() {
        holdings = new HashMap<>();
        this.stockTransaction = new ArrayList<>();
    }

    public double getAccountBalance(){
        return accountBalance;
    }
    
    public void updateAccountBalance(double price){
        if(price < 0)
            initialFunds += price;
        accountBalance += price;
    }
    
    public void addStock(Stock_1 stock, int shares, double buyPrice) {
        int currentShares = holdings.getOrDefault(stock, 0);
        holdings.put(stock, currentShares + shares);
        
        StockTransaction transaction = new StockTransaction(stock, shares, buyPrice, 0.0);
        stockTransaction.add(transaction);
    }

    public void removeStock(Stock_1 stock, int shares, double sellPrice) {
        int currentShares = holdings.getOrDefault(stock, 0);
        if (currentShares >= shares) {
            holdings.put(stock, currentShares - shares);
            
            for (int i = 0; i < stockTransaction.size(); i++) {
                StockTransaction transaction = stockTransaction.get(i);
                if(transaction.getStock() == stock) {
                    if (transaction.getSellPrice() == 0){
                        if(transaction.getShares() == shares){
                            transaction.setSellPrice(sellPrice);
                            return;
                        }
                        else if(transaction.getShares() > shares){
                            transaction.setShares(transaction.getShares() - shares);
                            StockTransaction newTransaction = new StockTransaction(stock,shares,transaction.getCostPrice(),sellPrice);
                            this.stockTransaction.add(newTransaction);
                            return;
                        }else
                            return;
                    }
                }
            }
             
        }
        
    }
    
    public double getTotalProfitLoss(){
        for (StockTransaction transaction : stockTransaction) {
            if (transaction.getSellPrice() != 0)
                profitLoss += transaction.getProfitLoss() * transaction.getShares();
        }
        return profitLoss;
    }

    public Map<Stock_1, Integer> getHoldings() {
        return holdings;
    }

    public double getValue() {
        double value = 0.0;
        for (Map.Entry<Stock_1, Integer> entry : holdings.entrySet()) {
            Stock_1 stock = entry.getKey();
            int shares = entry.getValue();
            value += stock.getPrice() * shares;
        }
        return value;
    }
    
    
    public void display() {
        System.out.println("Portfolio Holdings:");
        for (Map.Entry<Stock_1, Integer> entry : holdings.entrySet()) {
            Stock_1 stock = entry.getKey();
            int shares = entry.getValue();
            double value = stock.getPrice() * shares;
            System.out.println("Stock: " + stock.getName() + ", Shares: " + shares + ", Value: $" + value);
        }
        
        System.out.println("Initial Funds Balance: $" + initialFunds);
        System.out.println("Account Balance: $" + accountBalance);
        System.out.println("Total Portfolio Value: $" + (getValue() + accountBalance));
        System.out.println("Total Profit/Loss: $" + getTotalProfitLoss());
        
        System.out.println("\nStock Transactions:");
        for (StockTransaction transaction : stockTransaction) {
            double sellPrice = transaction.getSellPrice();
            if(sellPrice!=0.0){
                Stock_1 stock = transaction.getStock();
                int shares = transaction.getShares();
                double costPrice = transaction.getCostPrice();
                double profitLoss = transaction.getProfitLoss();
                

                System.out.println("Stock: " + stock.getName() + ", Shares: " + shares + ", Cost Price per Unit: $" + costPrice +
                        ", Sell Price per Unit: $" + sellPrice + ", Profit/Loss per Unit: $" + profitLoss );
            }
        }
    }
}