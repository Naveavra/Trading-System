package service.ExternalService;

import utils.Exceptions.ExternalServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ExternalService {
    private String name;
    private int timeoutSeconds;
    private HttpURLConnection connection;
    private URL url;
    private static final String UNEXPECTED_OUTPUT = "unexpected-output";
    private static final int DEFAULT_TIMEOUT = 15;
    private boolean available;

    public ExternalService(String name, String url) throws IOException, ExternalServiceException {
        this.name = name;
        this.url = new URL(url);
        timeoutSeconds = DEFAULT_TIMEOUT;
        available = false;
    }

    public ExternalService(String name, String url, int timeoutSeconds) throws IOException, ExternalServiceException {
        this.name = name;
        this.url = new URL(url);
        this.timeoutSeconds = timeoutSeconds;
    }

    protected void handshake() throws IOException, ExternalServiceException {
        Request handshakeRequest = new Request("handshake");
        sendRequest(handshakeRequest);
    }

    private void openConnection() throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        connection.setConnectTimeout(timeoutSeconds * 1000); // Set the connection timeout
        connection.setReadTimeout(timeoutSeconds * 1000); // Set the read timeout
    }

    private void closeConnection() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    public int sendRequest(Request request) throws IOException, ExternalServiceException {
        openConnection();

        try {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.toBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String response;
                while ((response = reader.readLine()) != null) {
                    sb.append(response);
                }
                reader.close();

                String responseString = sb.toString();
                if (responseString.equals(UNEXPECTED_OUTPUT)) {
                    throw new ExternalServiceException("External service returned an unexpected output!");
                } else if (request.isHandshake()) {
                    return 1;
                } else {
                    try {
                        return Integer.parseInt(responseString);
                    } catch (NumberFormatException e) {
                        throw new ExternalServiceException("Failed to parse response as an integer: " + responseString, e);
                    }
                }
            } else {
                throw new ExternalServiceException("Error: " + responseCode);
            }
        } catch (SocketTimeoutException e) {
            throw new ExternalServiceException("Response timeout occurred.", e);
        } finally {
            closeConnection();
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }
}
