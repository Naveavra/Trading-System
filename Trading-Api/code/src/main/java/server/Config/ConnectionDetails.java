package server.Config;
import java.net.InetAddress;

public class ConnectionDetails {
    private String address;
    private int port;

    public ConnectionDetails(String address, int port) {
        // Validate the IP address
        if (!isValidIPAddress(address)) {
            throw new IllegalArgumentException("Invalid IP address: " + address);
        }
        this.address = address;
        // Validate the port number
        if (!isValidPort(port)) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    /**
     * Checks if the given IP address is valid.
     *
     * @param ipAddress The IP address to validate.
     * @return `true` if the IP address is valid, `false` otherwise.
     */
    private boolean isValidIPAddress(String ipAddress) {
        try {
            InetAddress.getByName(ipAddress);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the given port number is valid (within the valid range).
     *
     * @param port The port number to validate.
     * @return `true` if the port number is valid, `false` otherwise.
     */
    private boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }
}
