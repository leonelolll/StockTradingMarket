package org.example;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;

import static org.example.testAPI.fetchDataFromAPI;


public class RefreshStock{
    public static void main(String[]args) {


        Timer timer = new Timer();
        timer.schedule(new RefreshStockPrice(), 0, 5 * 60 * 1000);
    }
private static class RefreshStockPrice extends TimerTask {


    @Override
    public void run() {

            // Fetch and display the data from the API
            try {
                fetchDataFromAPI();
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }


}

