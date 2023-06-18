package server.Config;

public class ESConfig {
    private String name;
    private String URL;
    private boolean isDefault;
    private int responseTime;
    private int DEFAULT_TIMEOUT = 15;

    public ESConfig() {
        isDefault = true;
    }

    public ESConfig(String name, String URL, int responseTime) {
        isDefault = false;
        this.name = name;
        this.URL = URL;
        this.responseTime = responseTime;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public boolean isDefault() {
        return isDefault;
    }
}