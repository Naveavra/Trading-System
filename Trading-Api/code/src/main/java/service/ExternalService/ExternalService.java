package service.ExternalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExternalService {
    private HttpURLConnection connection;
    private URL url;

    public ExternalService(String url) throws Exception {
        this.url = new URL(url);
        handshake();
    }

    public void handshake() throws Exception {
//        openConnection();
        // Create the HttpURLConnection object
        Request handshakeRequest = new Request("handshake");
        sendRequest(handshakeRequest);
//        closeConnection();
    }

    public void openConnection() throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
    }

    public void closeConnection()
    {
        // Disconnect the connection
        connection.disconnect();
        connection = null;
    }

    public int sendRequest(Request request) throws Exception {
        // Create the HttpURLConnection object
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(request.toBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response;
        StringBuilder sb = new StringBuilder();
        while ((response = reader.readLine()) != null) {
            sb.append(response);
        }
        response = sb.toString();
        reader.close();
        connection.disconnect();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            if(request.isHandshake())
            {
                return 1;
            }
            return Integer.parseInt(response.toString());
        } else {
            throw new Exception("Error: " + responseCode);
        }

    }
}