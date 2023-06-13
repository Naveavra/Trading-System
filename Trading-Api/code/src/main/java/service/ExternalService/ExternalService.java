package service.ExternalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ExternalService {
    private int second2Timeout;
    private HttpURLConnection connection;
    private URL url;
    private String UNEXPECTED_OUTPUT = "unexpected-output";
    private int DEFAULT_TIMEOUT = 15;

    public ExternalService(String url) throws Exception {
        this.url = new URL(url);
        second2Timeout = DEFAULT_TIMEOUT;
        handshake();
    }

    public ExternalService(String url, int timeout) throws Exception {
        this.url = new URL(url);
        second2Timeout = timeout;
        handshake();
    }

    public void handshake() throws Exception {
        Request handshakeRequest = new Request("handshake");
        sendRequest(handshakeRequest);
    }

    public void openConnection() throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        connection.setConnectTimeout(second2Timeout * 1000); // Set the connection timeout
        connection.setReadTimeout(second2Timeout * 1000); // Set the read timeout
    }

    public void closeConnection() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    public int sendRequest(Request request) throws Exception {
        openConnection();

        try {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.toBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String response;
            while ((response = reader.readLine()) != null) {
                sb.append(response);
            }
            reader.close();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseString = sb.toString();
                if (responseString.equals(UNEXPECTED_OUTPUT)) {
                    throw new Exception("External service returned an unexpected output!");
                } else if (request.isHandshake()) {
                    return 1;
                }
                return Integer.parseInt(responseString);
            } else {
                throw new Exception("Error: " + responseCode);
            }
        } catch (SocketTimeoutException e) {
            throw new Exception("Response timeout occurred.", e);
        } catch (Exception e){
            throw e;
        }finally {
            closeConnection();
        }
    }
}