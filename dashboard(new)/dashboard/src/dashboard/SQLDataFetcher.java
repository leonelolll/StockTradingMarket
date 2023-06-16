package dashboard;

import java.sql.*;

public class SQLDataFetcher {
    public static int calculateTotalQuantity(String symbol) {
        int totalQuantity = 0;

        String url = "jdbc:mysql://localhost:3306/java_login_register";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String sql = "SELECT type, quantity FROM orders WHERE symbol = '" + symbol + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                if (type.equals("BUY")) {
                    totalQuantity += quantity;
                } else if (type.equals("SELL")) {
                    totalQuantity -= quantity;
                }
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalQuantity;
    }

//    public static void main(String[] args) {
//        String symbol = "PDD";
//        int totalQuantity = calculateTotalQuantity(symbol);
//        System.out.println("Total Quantity for symbol " + symbol + ": " + totalQuantity);
//    }
}
