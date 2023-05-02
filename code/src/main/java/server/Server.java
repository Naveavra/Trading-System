package server;

import org.json.simple.JSONObject;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        get("/api/login", (req, res) -> {
            JSONObject json= new JSONObject();
            json.put("token", "aaaa");
            json.put("userId","1");
            json.put("userName","eli");
            String ans = json.toString();
            return ans;
        });
    }
}