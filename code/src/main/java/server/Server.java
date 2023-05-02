package server;

import market.Admin;
import market.Market;
import org.json.JSONObject;
import spark.Spark;
import utils.marketRelated.Response;

import java.util.LinkedList;

import org.json.JSONObject;
import spark.Filter;

import java.util.Map;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        Spark.webSocket("/api/login", MainWebSocket.class);
        init();
        /*Market market = new Market(new Admin(1, "hi@co.il", "123Aaa"));
        get("/hello", (req, res) -> "Hello World");
        get("/api/login", (req, res) -> {
            String x = req.body();
            System.out.println(req.body());
            JSONObject j = new JSONObject(x);
            String email = j.get("email").toString();
            JSONObject ans = new JSONObject();
            ans.put("errorOccurred", "false");
            ans.put("msg", "null");

            internalServerError("");
            JSONObject token= new JSONObject();
            token.put("token", "aaaa");
            token.put("userId","1");
            token.put("userName",email);
            ans.put("token", token.toString());
            return ans.toString();
=======
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

        Response<String> response2 = market.register("eli123@gmai.com", "122Aaa", "24/02/2002");
        JSONObject jsonObject2 = new JSONObject(response2);
        Response<Integer> response = market.login("eli123@gmai.com", "122Aaa", new LinkedList<>());
        JSONObject jsonObject = new JSONObject(response);
        System.out.println(jsonObject);
        System.out.println(jsonObject2);
         */
    }
}