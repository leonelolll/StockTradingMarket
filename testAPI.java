package org.example;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;



public class testAPI {
    public static void fetchDataFromAPI() throws IOException{
        Scanner input = new Scanner(System.in);
        String symbol;
        System.out.print("Enter the stock symbol : ");
        symbol = input.next();
        String url = "https://wall-street-warriors-api-um.vercel.app/price?apikey=UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7&symbol=" + symbol;
        String rapidApiKey = "UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Process the JSON response and store data in StockDataConverter
            StockDataConverter stockDataConverter = new StockDataConverter();
            // Assuming the response body is stored in 'responseJson' variable
            processResponse(stockDataConverter, symbol, response.body());

            // Print the stock data
            stockDataConverter.printStockData();

            // Create an instance of StockDataUpdater
          //  StockDataUpdater stockDataUpdater = new StockDataUpdater(stockDataConverter, symbol);

            // Start updating the stock data at regular intervals
          // stockDataUpdater.startUpdating();

            // System.out.println(response.body());





        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    static void processResponse(StockDataConverter stockDataConverter, String symbol, String responseJson) {
        JSONObject json = new JSONObject(responseJson);


        JSONObject stockData = json.getJSONObject(symbol);

        for (String field : stockData.keySet()) {
            JSONObject stageData = stockData.getJSONObject(field);
            for (String timestamp : stageData.keySet()) {
                double price = stageData.getDouble(timestamp);
                long parsedTimestamp = Long.parseLong(timestamp);

                stockDataConverter.addStockPrice(symbol,field, parsedTimestamp, price);
            }


        }
    }
}




