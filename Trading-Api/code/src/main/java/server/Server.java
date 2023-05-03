package server;

import market.Admin;
import market.Market;
import org.json.JSONObject;
import utils.Token;
import utils.marketRelated.Response;

import java.util.LinkedList;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        Market market = new Market(new Admin(1, "hi@co.il", "123Aaa"));
        market.register("eli@gmail.com","aA12345","22/02/2002");
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

        post("/api/auth/login", (req, res) -> {
            System.out.println(req);
            System.out.println(req.body());
            System.out.println(req.headers());
            System.out.println(req.userAgent());
            JSONObject request = new JSONObject(req.body());
            String email = request.get("email").toString();
            String pass  = request.get("password").toString();
            LinkedList<String> answers = new LinkedList<>();
            Response<Token> r = market.login(email,pass,answers);
           // System.out.println(r.getValue().getUserName());
            JSONObject json = new JSONObject();
            if(r.getErrorMessage()==null) {
                json.put("token", r.getValue().getToken());
                json.put("userId", r.getValue().getUserId());
                json.put("userName", r.getValue().getUserName());
                System.out.println(json);
                res.status(200);
                res.body(json.toString());
            }
            else {
                 json.put("errorMsg", r.getErrorMessage());
                 res.status(400);
                res.body(json.toString());
            }
                return res.body();
        });
    }
}