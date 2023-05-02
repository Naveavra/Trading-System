package server;


import org.json.JSONObject;
import spark.Filter;

import java.util.Map;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });
        get("/hello", (req, res) -> "Hello World");
        post("/api/login", (req, res) -> {


            String x = req.body();
            JSONObject j = new JSONObject(x);
            String email = j.get("email").toString();
            System.out.println(email);
            JSONObject json= new JSONObject();
            json.put("rememberMe",true);
            json.put("token", "aaaa");
            json.put("userId","1");
            json.put("userName",email);
            String ans = json.toString();
            return ans;
        });
    }
}