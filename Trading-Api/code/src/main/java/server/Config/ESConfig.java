package server.Config;

public class ESConfig {
    private String name;
    private String URL;
    private int responseTime;

    public ESConfig(String name, String URL) {
        this.name = name;
        this.URL = URL;
        this.responseTime = responseTime;
    }

    public ESConfig(String name, String URL, int responseTime) {
        this.name = name;
        this.URL = URL;
        this.responseTime = responseTime;
    }
}