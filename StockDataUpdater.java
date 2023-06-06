package org.example;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.TimerTask;




public class StockDataUpdater extends TimerTask {


    @Override
    public void run() {
        try {
            // Fetch and display the data from the API
            fetchDataFromAPI();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching data from the API: " + e.getMessage());
        }
    }

    private void fetchDataFromAPI() throws IOException, InterruptedException {
        // Create the API request URL using the stock symbol
        String url = "https://wall-street-warriors-api-um.vercel.app/price?apikey=UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7&symbol=" + SYMBOL;

        // Send the API request and process the response
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Process the JSON response and update the stock data
        processResponse(response.body());
    }

    private void processResponse(String responseJson) {
        // Create a new instance of StockDataConverter
        StockDataConverter stockDataConverter = new StockDataConverter();

        // Process the JSON response and update the stock data in StockDataConverter
        processResponse(stockDataConverter, SYMBOL, responseJson);

        // Print the updated stock data
        stockDataConverter.printStockData();
    }

    private void processResponse(StockDataConverter stockDataConverter, String symbol, String responseJson) {
        // Your existing code to process the JSON response and update the stock data
        // ...

        // Example code to add dummy data to stockDataConverter for testing
        stockDataConverter.addStockPrice(symbol, "High", System.currentTimeMillis(), 123.45);
        stockDataConverter.addStockPrice(symbol, "Low", System.currentTimeMillis(), 67.89);
    }
}

