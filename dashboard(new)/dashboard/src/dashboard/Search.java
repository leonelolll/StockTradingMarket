package dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class Search extends JFrame implements ActionListener {
    private JTextField symbolField;
    private JTextArea resultArea;

    public Search() {
        setTitle("Stock Price Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        symbolField = new JTextField();
        mainPanel.add(symbolField, BorderLayout.NORTH);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        mainPanel.add(searchButton, BorderLayout.SOUTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String symbol = symbolField.getText();
        if (symbol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a symbol.");
            return;
        }

        try {
            // Make API request
            String apiKey = "UM-7c700383fa9d2afff0cb740d8aa832d75b3fe72ff9000cbeb691c38b2b6fced7";
            String apiUrl = "https://wall-street-warriors-api-um.vercel.app/price?apikey=" + apiKey + "&symbol=" + symbol;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                String result = parseJSON(response.toString(), symbol);

                // Display result in the GUI
                resultArea.setText(result);
            } else {
                JOptionPane.showMessageDialog(this, "API request failed. Response code: " + responseCode);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while making the API request: " + ex.getMessage());
        }
    }

    private String parseJSON(String json, String symbolName) {
        StringBuilder result = new StringBuilder();

        // Parse the JSON response and retrieve desired data
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject symbolData = jsonObject.getJSONObject(symbolName);
            JSONObject openData = symbolData.getJSONObject("Open");
            String latestTimestamp = openData.keys().next();

            result.append("Symbol: ").append(symbolField.getText()).append("\n");
            result.append("Latest Open Price: $").append(openData.getDouble(latestTimestamp)).append("\n");

            // Get latest close price
            JSONObject closeData = symbolData.getJSONObject("Close");
            latestTimestamp = openData.keys().next();
            result.append("Latest Close Price: $").append(closeData.getDouble(latestTimestamp)).append("\n");

            // Get latest highest price
            JSONObject highData = symbolData.getJSONObject("High");
            latestTimestamp = openData.keys().next();
            result.append("Latest Highest Price: $").append(highData.getDouble(latestTimestamp)).append("\n");

            // Get latest lowest price
            JSONObject lowData = symbolData.getJSONObject("Low");
            latestTimestamp = openData.keys().next();
            result.append("Latest Lowest Price: $").append(lowData.getDouble(latestTimestamp)).append("\n");

            // Extract other desired data from the JSON object and append it to the result StringBuilder

        } catch (Exception e) {
            result.append("An error occurred while parsing the JSON response.");
        }

        return result.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Search search = new Search();
            search.setVisible(true);
        });
    }
}
