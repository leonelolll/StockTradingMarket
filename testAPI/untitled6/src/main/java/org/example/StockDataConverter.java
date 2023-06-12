package org.example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class StockDataConverter {

    private Map<String, PriorityQueue<StockPrice>> stockData;

    public StockDataConverter() {
        stockData = new HashMap<>();
    }

    public void addStockPrice(String symbol,String field, long timestamp, double price) {
        stockData.putIfAbsent(symbol, new PriorityQueue<>());
        PriorityQueue<StockPrice> prices = stockData.get(symbol);
        prices.offer(new StockPrice(symbol ,field,timestamp,price));
    }

    public void printStockData() {
        for (String symbol : stockData.keySet()) {
            System.out.println("Symbol: " + symbol);

            PriorityQueue<StockPrice> prices = stockData.get(symbol);

            String currentTimestamp = null;

            // Print table header
            System.out.println("---------------------------------------------");
            System.out.printf("| %-12s | %-19s | %-8s |\n", "Field", "Timestamp", "Price");
            System.out.println("---------------------------------------------");


            while (!prices.isEmpty()) {
                StockPrice price = prices.poll();

                // Format timestamp
                String formattedTimestamp = formatTimestamp(price.getTimestamp());

                if (currentTimestamp == null || !currentTimestamp.equals(formattedTimestamp)) {
                    // Print line separator
                    System.out.println("---------------------------------------------");
                    currentTimestamp = formattedTimestamp;
                }

                // Print table row

                System.out.printf("| %-12s | %-19s | %-8s |\n", price.getField(), formattedTimestamp, price.getPrice());
            }

            // Print line separator after all rows
            System.out.println("---------------------------------------------");
            System.out.println();
        }
    }

    private String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateTime);
    }

    private static class StockPrice implements Comparable<StockPrice> {
        private String symbol;
        private String field;
        private long timestamp;
        private double price;

        public StockPrice(String symbol,String field , long timestamp, double price) {
            this.symbol = symbol;
            this.field = field;
            this.timestamp = timestamp;
            this.price = price;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getField(){
            return field;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public int compareTo(StockPrice other) {
            return Long.compare(this.timestamp, other.timestamp);
        }
    }
}




