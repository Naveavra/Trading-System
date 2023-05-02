package server;

import market.Admin;
import market.Market;
import org.json.JSONObject;
import spark.Spark;
import utils.marketRelated.Response;

import java.util.LinkedList;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        Spark.webSocket("/api/login", MainWebSocket.class);
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