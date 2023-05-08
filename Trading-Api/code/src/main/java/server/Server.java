package server;

import market.Admin;
import market.Market;
import org.json.JSONObject;
import spark.Spark;
import utils.Token;
import utils.marketRelated.Response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class Server {
    public static API api = new API();
    static ConnectedThread connectedThread ;
    static ConcurrentHashMap<Integer,Boolean> connected = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        api.register("eli@gmail.com","aA12345","22/02/2002");
        //Spark.webSocket("/api/login", MainWebSocket.class);
        //Spark.webSocket("/api/member",  MemberWebSocket.class);
        init();
        connectedThread = new ConnectedThread(connected);
        connectedThread.start();
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
            JSONObject request = new JSONObject(req.body());
            String email = request.get("email").toString();
            String pass  = request.get("password").toString();
            LinkedList<String> answers = new LinkedList<>();
            Response<Token> r = api.login(email,pass,answers);
            // System.out.println(r.getValue().getUserName());
            JSONObject json = new JSONObject();
            if(r.getErrorMessage()==null) {
                System.out.println("new user comes in , id: " + r.getValue().getUserId());
                json.put("token", r.getValue().getToken());
                json.put("userId", r.getValue().getUserId());
                json.put("userName", r.getValue().getUserName());
                json.put("isAdmin",false);
                connected.put( r.getValue().getUserId() ,true);
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
        post("/api/auth/ping" ,(req,res)->{
            JSONObject request = new JSONObject(req.body());
            String id = request.get("userId").toString();
            connected.put(Integer.parseInt(id), true);
            System.out.println(id + " becomes true");
            res.status(200);
            res.body("ping success");
            return res.body();
        });
        post("/api/auth/guest/enter", (req, res) -> {
            api.enterGuest(res);
            if(res.status() == 200){
                connected.put(.getValue(),true);
            }
        });

    }
}