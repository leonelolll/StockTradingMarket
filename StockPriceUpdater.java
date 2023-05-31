
package wiatuto5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockPriceUpdater {
    private static final String API_ENDPOINT = "https://wall-street-warriors-api-um.vercel.app/price?apikey=UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7&symbol=";
    private static final String STOCK_SYMBOL = "AAPL";

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(StockPriceUpdater::fetchStockData, 0, 5, TimeUnit.MINUTES);
    }

    private static void fetchStockData() {
        try {
            URL url = new URL(API_ENDPOINT + STOCK_SYMBOL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Process the fetched data
                String stockData = response.toString();
                System.out.println("Stock Data for " + STOCK_SYMBOL + ":");
                System.out.println(stockData);
            } else {
                System.out.println("API request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}