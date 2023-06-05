package service.ExternalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestsES {
    public static void main(String[] args) {
        // Specify the action type and additional parameters based on your requirements
        String actionType = "handshake";

        // Create the request content
        String requestBody = "action_type=" + actionType;

        try {
            // Create the URL object
            URL url = new URL("https://php-server-try.000webhostapp.com/");

            // Create the HttpURLConnection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            // Send the request
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            // Get the response
            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            StringBuilder sb = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                sb.append(response);
            }
            reader.close();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Process the response
                String result = sb.toString();
                System.out.println("Response: " + result);
            } else {
                // Handle error
                System.out.println("Error: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
