package dashboard;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StockTradingHistory extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_login_register";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private Connection connection;
    private JTextArea historyTextArea;

    public StockTradingHistory() {
        setTitle("Buy & Sell History");
        setSize(969, 628);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }

        fetchBuySellHistory();
    }

    private void fetchBuySellHistory() {
        try {
            Statement statement = connection.createStatement();

            String sql = "SELECT username, symbol, price, type, quantity, buyingPrice, sellingPrice, profitLoss, timestamp, balance FROM orders";
            ResultSet resultSet = statement.executeQuery(sql);

            StringBuilder stringBuilder = new StringBuilder();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String symbol = resultSet.getString("symbol");
                String type = resultSet.getString("type");
                double price = resultSet.getDouble("price");
                double quantity = resultSet.getDouble("quantity");
                double buyingPrice = resultSet.getDouble("buyingPrice");
                double sellingPrice = resultSet.getDouble("sellingPrice");
                double profitLoss = resultSet.getDouble("profitLoss");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");

                stringBuilder.append("Username: ").append(username).append("\n");
                stringBuilder.append("Symbol: ").append(symbol).append("\n");
                stringBuilder.append("Type: ").append(type).append("\n");
                stringBuilder.append("Price: ").append(price).append("\n");
                stringBuilder.append("Quantity: ").append(quantity).append("\n");
                stringBuilder.append("Buying Price: ").append(buyingPrice).append("\n");
                stringBuilder.append("Selling Price: ").append(sellingPrice).append("\n");
                stringBuilder.append("Profit/Loss: ").append(profitLoss).append("\n");
                stringBuilder.append("Timestamp: ").append(timestamp.toString()).append("\n\n");
            }

            historyTextArea.setText(stringBuilder.toString());
        } catch (SQLException e) {
            System.out.println("Failed to fetch trading history: " + e.getMessage());
        }
    }


    private void addTradePanel(String id, String username, String symbol, double price, String type, int quantity, double buyingPrice, double sellingPrice, double profitLoss, String timestamp, double balance) {
        JPanel tradePanel = new JPanel();
        tradePanel.setLayout(new GridLayout(0, 1));

        tradePanel.add(new JLabel("ID: " + id));
        tradePanel.add(new JLabel("Username: " + username));
        tradePanel.add(new JLabel("Symbol: " + symbol));
        tradePanel.add(new JLabel("Price: " + price));
        tradePanel.add(new JLabel("Type: " + type));
        tradePanel.add(new JLabel("Quantity: " + quantity));
        tradePanel.add(new JLabel("Buying Price: " + buyingPrice));
        tradePanel.add(new JLabel("Selling Price: " + sellingPrice));
        tradePanel.add(new JLabel("Profit/Loss: " + profitLoss));
        tradePanel.add(new JLabel("Timestamp: " + timestamp));
        tradePanel.add(new JLabel("Balance: " + balance));

        this.getContentPane().add(tradePanel);
    }

    public static void main(String[] args) {
        StockTradingHistory app = new StockTradingHistory();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
