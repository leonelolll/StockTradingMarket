package dashboard;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StockTradingHistory extends JFrame {
    private JTextArea historyTextArea;

    private static final String DB_URL = "jdbc:mysql://localhost/java_login_register";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private Connection connection;

    public StockTradingHistory() {
        setTitle("Buy & Sell History");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    public static void main(String[] args) {
        StockTradingHistory app = new StockTradingHistory();
        app.fetchBuySellHistory();
        app.setSize(969, 628);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
