//package dashboard;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.sql.*;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONObject;
//
//import dashboard.SQLDataFetcher;
//public class TradingFunctionality extends JFrame {
//    private static final String API_BASE_URL = "https://wall-street-warriors-api-um.vercel.app/price?apikey=UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7&symbol=";
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private static final long COMPETITION_DURATION_WEEKS = 6;
//    private static final long TRADING_PERIOD_DAYS = 3;
//    private static final double INITIAL_FUNDS = 50000;
//    private static final int MAX_STOCKS_PER_ORDER = 500;
//
//    private JLabel balanceLabel;
//    private JLabel profitLabel;
//    private JLabel remainingDaysLabel;
//    private JTextField symbolField;
//    private JTextField quantityField;
//    private JButton buyButton;
//    private JButton sellButton;
//
//    private double balance;
//    private double profit;
//    private LocalDateTime competitionEndDate;
//
//    private Map<String, Double> stockPrices = new HashMap<String,Double>(); // Stores the latest stock prices
//
//    private static final String DB_URL = "jdbc:mysql://localhost/java_login_register";
//    private static final String DB_USERNAME = "root";
//    private static final String DB_PASSWORD = "";
//    private Connection connection;
//    private JTextArea historyTextArea;
//    public TradingFunctionality() {
//        setTitle("Buy & Sell History");
//        setSize(400, 300);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        historyTextArea = new JTextArea();
//        historyTextArea.setEditable(false);
//
//        JScrollPane scrollPane = new JScrollPane(historyTextArea);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//        getContentPane().add(scrollPane, BorderLayout.CENTER);
//
//        balanceLabel = new JLabel("Balance: RM" + INITIAL_FUNDS);
//        profitLabel = new JLabel("Profit: RM0.00");
//        remainingDaysLabel = new JLabel("Remaining Days: N/A");
//        symbolField = new JTextField(10);
//        quantityField = new JTextField(10);
//        buyButton = new JButton("Buy");
//        sellButton = new JButton("Sell");
//
//        competitionEndDate = LocalDateTime.now().plusWeeks(COMPETITION_DURATION_WEEKS);
//        balance = INITIAL_FUNDS;
//
//        setLayout(new GridLayout(5, 1));
//
//        add(balanceLabel);
//        add(profitLabel);
//        add(remainingDaysLabel);
//
//        JPanel tradePanel = new JPanel();
//        tradePanel.add(new JLabel("Symbol:"));
//        tradePanel.add(symbolField);
//        tradePanel.add(new JLabel("Quantity:"));
//        tradePanel.add(quantityField);
//        tradePanel.add(buyButton);
//        tradePanel.add(sellButton);
//        add(tradePanel);
//
//        buyButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String symbol = symbolField.getText();
//                double quantity = Double.parseDouble(quantityField.getText());
//                buyStock(symbol, quantity);
//            }
//        });
//
//        sellButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String symbol = symbolField.getText();
//                double quantity = Double.parseDouble(quantityField.getText());
//                sellStock(symbol, quantity);
//            }
//        });
//
//        try {
//            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//            System.out.println("Connected to the database.");
//        } catch (SQLException e) {
//            System.out.println("Failed to connect to the database: " + e.getMessage());
//        }
//    }
//
//    private void buyStock(String symbol, double quantity) {
//        double price = retrieveStockPrice(symbol);
//
//        double totalCost = price * quantity;
//
//        int currentQuantity = SQLDataFetcher.calculateTotalQuantity(symbol);
//        if (totalCost <= balance) {
//            if (quantity <= MAX_STOCKS_PER_ORDER ) {
//                balance -= totalCost;
//                stockPrices.put(symbol, price);
//                updateLabels();
//
//                insertOrder(symbol, quantity, price, "BUY");
//
//                JOptionPane.showMessageDialog(this, "Successfully bought " + quantity + " shares of " + symbol);
//
//                // TODO: Implement the buy order functionality
//                // You can write the code here to handle the buy order logic and update the database accordingly
//            } else {
//                JOptionPane.showMessageDialog(this, "Exceeded maximum stocks per order");
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Insufficient funds!");
//        }
//    }
//
//    private void sellStock(String symbol, double quantity) {
//        double price = retrieveStockPrice(symbol);
//
//        if (stockPrices.containsKey(symbol)) {
//            double currentPrice = stockPrices.get(symbol);
//            double totalValue = currentPrice * quantity;
//            int currentQuantity = SQLDataFetcher.calculateTotalQuantity(symbol);
//            if (quantity <= MAX_STOCKS_PER_ORDER || quantity <= currentQuantity) {
//                if (totalValue <= balance) {
//                    balance += totalValue;
//                    stockPrices.put(symbol, price);
//                    updateLabels();
//
//                    insertOrder(symbol, quantity, price, "SELL");
//
//                    JOptionPane.showMessageDialog(this, "Successfully sold " + quantity + " shares of " + symbol);
//
//                    // TODO: Implement the sell order functionality
//                    // You can write the code here to handle the sell order logic and update the database accordingly
//                } else {
//                    JOptionPane.showMessageDialog(this, "You don't have enough shares to sell!");
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Exceeded maximum stocks per order! ");
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "You don't own any shares of this stock or your remaining stock quantity is not enough!!");
//        }
//    }
//
//    private double retrieveStockPrice(String symbol) {
//        String apiUrl = API_BASE_URL + symbol;
//        double price = 0.0;
//
//        try {
//            URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String response = reader.readLine();
//
//                price = parseStockPriceFromResponse(response,symbol);
//            } else {
//                System.out.println("API request failed with response code: " + responseCode);
//            }
//
//            connection.disconnect();
//        } catch (IOException e) {
//            System.out.println("Failed to retrieve stock price: " + e.getMessage());
//        }
//
//        return price;
//    }
//    private void fetchBuySellHistory() {
//        String url = "jdbc:mysql://localhost:3306/java_login_register";
//        String username = "root";
//        String password = "";
//
//        try {
//            Connection connection = DriverManager.getConnection(url, username, password);
//            Statement statement = connection.createStatement();
//
//            String sql = "SELECT * FROM orders WHERE type IN ('BUY', 'SELL') ";//ORDER BY timestamp DESC";
//            ResultSet resultSet = statement.executeQuery(sql);
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            while (resultSet.next()) {
//                String symbol = resultSet.getString("symbol");
//                String type = resultSet.getString("type");
//                int quantity = resultSet.getInt("quantity");
//                double price = resultSet.getDouble("price");
//
//                stringBuilder.append("Symbol: ").append(symbol).append("\n");
//                stringBuilder.append("Type: ").append(type).append("\n");
//                stringBuilder.append("Quantity: ").append(quantity).append("\n");
//                stringBuilder.append("Price: ").append(price).append("\n");
//                stringBuilder.append("-----------------------").append("\n");
//            }
//
//            resultSet.close();
//            statement.close();
//            connection.close();
//
//            historyTextArea.setText(stringBuilder.toString());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private double parseStockPriceFromResponse(String response, String symbol) {
//        double stockPrice = 0.0;
//
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            JSONObject symbolData = jsonObject.getJSONObject(symbol);
//
//            JSONObject open = symbolData.getJSONObject("Open");
//            String latestTimestamp = open.keys().next();
//            double latestPrice = open.getDouble(latestTimestamp);
//
//            stockPrice = latestPrice;
//        } catch (Exception e) {
//            System.out.println("Failed to parse stock price from response: " + e.getMessage());
//        }
//
//        return stockPrice;
//    }
//
//    private void insertOrder(String symbol, double quantity, double price, String type) {
//        try {
//            String sql = "INSERT INTO orders (symbol, quantity, price, type) VALUES (?, ?, ?, ?)";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, symbol);
//            statement.setDouble(2, quantity);
//            statement.setDouble(3, price);
//            statement.setString(4, type);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Failed to insert order: " + e.getMessage());
//        }
//    }
//
//    private void updateLabels() {
//        balanceLabel.setText("Balance: RM" + String.format("%.2f", balance));
//        profitLabel.setText("Profit: RM" + String.format("%.2f", profit));
//        remainingDaysLabel.setText("Remaining Days: " + getRemainingDays());
//    }
//
//    private long getRemainingDays() {
//        Duration duration = Duration.between(LocalDateTime.now(), competitionEndDate);
//        return Math.max(duration.toDays(), 0);
//    }
//
//    public static void main(String[] args) {
//
//        TradingFunctionality app = new TradingFunctionality();
//        app.fetchBuySellHistory();
//        app.setSize(400, 300);
//        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        app.setVisible(true);
//
//
//    }
//}
//
//
package dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import dashboard.SQLDataFetcher;
import java.io.IOException;

import org.json.JSONObject;

import dashboard.SQLDataFetcher;
public class TradingFunctionality extends JFrame {
    private static final String API_BASE_URL = "https://wall-street-warriors-api-um.vercel.app/price?apikey=UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7&symbol=";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final long COMPETITION_DURATION_WEEKS = 6;
    private static final long TRADING_PERIOD_DAYS = 3;
    private static final double INITIAL_FUNDS = 50000;
    private static final int MAX_STOCKS_PER_ORDER = 500;

    private JLabel balanceLabel;
    private JLabel profitLabel;
    private JLabel remainingDaysLabel;
    private JTextField symbolField;
    private JTextField quantityField;
    private JButton buyButton;
    private JButton sellButton;
     private JButton backButton;
     private JPanel tradePanel;

    private double balance;
    private double profit;
    private LocalDateTime competitionEndDate;

    private Map<String, Double> stockPrices = new HashMap<String,Double>(); // Stores the latest stock prices

    private static final String DB_URL = "jdbc:mysql://localhost/java_login_register";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private Connection connection;
    private JTextArea historyTextArea;
    public TradingFunctionality() {
        setTitle("Buy & Sell History");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        
      

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        balanceLabel = new JLabel("Balance: RM" + INITIAL_FUNDS);
        profitLabel = new JLabel("Profit: RM0.00");
        remainingDaysLabel = new JLabel("Remaining Days: N/A");
        symbolField = new JTextField(10);
        quantityField = new JTextField(10);
        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");
        backButton = new JButton("Back");

        competitionEndDate = LocalDateTime.now().plusWeeks(COMPETITION_DURATION_WEEKS);
        balance = INITIAL_FUNDS;

        setLayout(new GridLayout(5, 1));

        add(balanceLabel);
        add(profitLabel);
        add(remainingDaysLabel);
        add(backButton);

        JPanel tradePanel = new JPanel();
        tradePanel.add(new JLabel("Symbol:"));
        tradePanel.add(symbolField);
        tradePanel.add(new JLabel("Quantity:"));
        tradePanel.add(quantityField);
        tradePanel.add(buyButton);
        tradePanel.add(sellButton);
        tradePanel.add(backButton);
        
        add(tradePanel);
        
         backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewJFrame newFrame = new NewJFrame();
                newFrame.setVisible(true);
                dispose(); // Close the current frame
            }
        });
        

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String symbol = symbolField.getText();
                double quantity = Double.parseDouble(quantityField.getText());
                buyStock(symbol, quantity);
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String symbol = symbolField.getText();
                double quantity = Double.parseDouble(quantityField.getText());
                sellStock(symbol, quantity);
            }
        });

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    //buying Stock
    private void buyStock(String symbol, double quantity) {
        double price = retrieveStockPrice(symbol);

        double totalCost = price * quantity;

        int currentQuantity = SQLDataFetcher.calculateTotalQuantity(symbol);
        if (totalCost <= balance) {
            if (quantity <= MAX_STOCKS_PER_ORDER ) {
                balance -= totalCost;
                stockPrices.put(symbol, price);
                updateLabels();

                insertOrder(symbol, quantity, price, "BUY");

                JOptionPane.showMessageDialog(this, "Successfully bought " + quantity + " shares of " + symbol);

                // TODO: Implement the buy order functionality
                // You can write the code here to handle the buy order logic and update the database accordingly
            } else {
                JOptionPane.showMessageDialog(this, "Exceeded maximum stocks per order");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Insufficient funds!");
        }
    }

    private void sellStock(String symbol, double quantity) {
        double price = retrieveStockPrice(symbol);

        if (stockPrices.containsKey(symbol)) {
            double currentPrice = stockPrices.get(symbol);
            double totalValue = currentPrice * quantity;
            int currentQuantity = SQLDataFetcher.calculateTotalQuantity(symbol);
            if (quantity <= MAX_STOCKS_PER_ORDER || quantity <= currentQuantity) {
                if (totalValue <= balance) {
                    balance += totalValue;
                    stockPrices.put(symbol, price);
                    updateLabels();

                    insertOrder(symbol, quantity, price, "SELL");

                    JOptionPane.showMessageDialog(this, "Successfully sold " + quantity + " shares of " + symbol);

                    // TODO: Implement the sell order functionality
                    // You can write the code here to handle the sell order logic and update the database accordingly
                } else {
                    JOptionPane.showMessageDialog(this, "You don't have enough shares to sell!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Exceeded maximum stocks per order! ");
            }
        } else {
            JOptionPane.showMessageDialog(this, "You don't own any shares of this stock or your remaining stock quantity is not enough!!");
        }
    }

    private double retrieveStockPrice(String symbol) {
        String apiUrl = API_BASE_URL + symbol;
        double price = 0.0;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();

                price = parseStockPriceFromResponse(response,symbol);
            } else {
                System.out.println("API request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Failed to retrieve stock price: " + e.getMessage());
        }

        return price;
    }
    private void fetchBuySellHistory() {
        String url = "jdbc:mysql://localhost:3306/java_login_register";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM orders WHERE type IN ('BUY', 'SELL') ";//ORDER BY timestamp DESC";
            ResultSet resultSet = statement.executeQuery(sql);

            StringBuilder stringBuilder = new StringBuilder();

            while (resultSet.next()) {
                String symbol = resultSet.getString("symbol");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                stringBuilder.append("Symbol: ").append(symbol).append("\n");
                stringBuilder.append("Type: ").append(type).append("\n");
                stringBuilder.append("Quantity: ").append(quantity).append("\n");
                stringBuilder.append("Price: ").append(price).append("\n");
                stringBuilder.append("-----------------------").append("\n");
            }

            resultSet.close();
            statement.close();
            connection.close();

            historyTextArea.setText(stringBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private double parseStockPriceFromResponse(String response, String symbol) {
        double stockPrice = 0.0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject symbolData = jsonObject.getJSONObject(symbol);

            JSONObject open = symbolData.getJSONObject("Open");
            String latestTimestamp = open.keys().next();
            double latestPrice = open.getDouble(latestTimestamp);

            stockPrice = latestPrice;
        } catch (Exception e) {
            System.out.println("Failed to parse stock price from response: " + e.getMessage());
        }

        return stockPrice;
    }

    private void insertOrder(String symbol, double quantity, double price, String type) {
        try {
            String sql = "INSERT INTO orders (symbol, quantity, price, type) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, symbol);
            statement.setDouble(2, quantity);
            statement.setDouble(3, price);
            statement.setString(4, type);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to insert order: " + e.getMessage());
        }
    }

    private void updateLabels() {
        balanceLabel.setText("Balance: RM" + String.format("%.2f", balance));
        profitLabel.setText("Profit: RM" + String.format("%.2f", profit));
        remainingDaysLabel.setText("Remaining Days: " + getRemainingDays());
    }

    private long getRemainingDays() {
        Duration duration = Duration.between(LocalDateTime.now(), competitionEndDate);
        return Math.max(duration.toDays(), 0);
    }

    public static void main(String[] args) {

        TradingFunctionality app = new TradingFunctionality();
        app.setSize(969, 628);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);


    }
}


