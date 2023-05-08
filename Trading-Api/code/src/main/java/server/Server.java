package server;

import domain.store.storeManagement.Store;
import market.Admin;
import market.Market;
import org.json.JSONObject;
import spark.Spark;
import utils.Token;
import utils.marketRelated.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class Server {
    public static Market market = new Market(new Admin(-1, "hi@co.il", "123Aaa"));
    static ConnectedThread connectedThread ;
    static ConcurrentHashMap<Integer,Boolean> connected = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        market.register("eli@gmail.com","aA12345","22/02/2002");
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
            Response<Token> r = market.login(email,pass,answers);
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
            Response<Integer> r = market.enterGuest();
            JSONObject json = new JSONObject();
            if(r.getErrorMessage()==null) {
                System.out.println("new user comes in , id: " + r.getValue());
                res.status(200);
                json.put("guestId", r.getValue());
                connected.put(r.getValue(),true);
                res.body(json.toString());
            }
            else{
                res.status(400);
                json.put("errorMsg", r.getValue());
                res.body(json.toString());
            }
            return res.body();
        });
        post("/api/stores",(req,res)->{
            JSONObject request = new JSONObject(req.body());
            String userId = request.get("userId").toString();
            String desc  = request.get("desc").toString();
            Response<Integer> r = market.openStore(Integer.parseInt(userId),desc);
            if(r.getErrorMessage() != null){
                System.out.println("fail open store");
                res.status(400);
                res.body(r.getErrorMessage());
            }
            else{
                System.out.println("success open store");
                res.status(200);
                res.body("success open store");

            }
            return res.body();
        });
        delete("/api/stores/:id",(req,res)->{
            JSONObject request = new JSONObject(req.body());
            String userId = request.get("userId").toString();
            String desc  = request.get("desc").toString();
            Response<Integer> r = market.openStore(Integer.parseInt(userId),desc);
            if(r.getErrorMessage() != null){
                System.out.println("success open store");
                res.status(200);
                res.body("success open store");
            }
            else{
                System.out.println("fail open store");
                res.status(400);
                res.body(r.getErrorMessage());
            }
            return res.body();
        });
        get("api/stores", (req,res)->{
            Response<ConcurrentHashMap<Integer, Store>> r = market.getStores();
            Collection<Store> stores= r.getValue().values();
            JSONObject json = new JSONObject();
            json.put("results",stores.toString());
            System.out.println(json.toString());
            res.body(json.toString());
            return res.body();
        });

    }
}