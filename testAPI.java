package org.example;
import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

        public class testAPI {
                        public static void main(String[] args) {
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

                    System.out.println(response.body());
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }


