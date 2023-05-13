package server;

import com.google.gson.Gson;
import data.StoreInfo;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import org.json.JSONObject;
import spark.Session;
import utils.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.*;

public class Server {
    public static API api = new API();
    static ConnectedThread connectedThread;
    static ConcurrentHashMap<Integer, Boolean> connected = new ConcurrentHashMap<>();
    static int nextUser =1;
    static Gson gson = new Gson();

    private static void toSparkRes(spark.Response res, Pair<Boolean, JSONObject> apiRes) {
        if (apiRes.getFirst()) {
            res.status(200);
            res.body(apiRes.getSecond().get("value").toString());
        } else {
            res.status(400);
            res.body(apiRes.getSecond().get("errorMsg").toString());
        }
    }

    public static void main(String[] args) {
        api.register("eli@gmail.com", "aA12345", "22/02/2002");
        //Spark.webSocket("/api/login", MainWebSocket.class);
        //Spark.webSocket("/api/member",  MemberWebSocket.class);
        //Pair<Boolean, JSONObject> ans2 = api.register("eli@gmail.com", "123Aaa", "24/02/2002");
        //Pair<Boolean, JSONObject> ans = api.login("eli@gmail.com", "123Aaa");
        //System.out.println(ans.getSecond().get("value"));
        //System.out.println(ans2.getSecond().get("value"));
        //System.out.println(api.getCart(id).getSecond().get("value"));
//        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
//        webSocket("chat", NotificationWebSocket.class);
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
        /*
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
        */
        post("api/auth/ping", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String id = request.get("userId").toString();
            connected.put(Integer.parseInt(id), true);
            System.out.println(id + " becomes true");
            res.status(200);
            res.body("ping success");
            return res.body();
        });
        post("api/auth/guest/enter", (req, res) -> {
            toSparkRes(res, api.enterGuest());
            return res.body();
        });

        post("api/auth/login", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String email = request.get("email").toString();
            String pass = request.get("password").toString();
            toSparkRes(res, api.login(email, pass));
            return res.body();
        });
        post("api/auth/logout", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String userId = request.get("userId").toString();
            String token = req.headers("Authorization");
            System.out.println(token);
            toSparkRes(res, api.logout(Integer.parseInt(userId), token));
            return res.body();
        });
        post("api/auth/register", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String email = request.get("email").toString();
            String pass = request.get("password").toString();
            String bday = request.get("birthday").toString();
            toSparkRes(res, api.register(email, pass, bday));
            return res.body();
        });
        get("api/stores", (req, res) ->
        {
            System.out.println("get store");
            Store store1 = new Store(1, "nike store", 1);
            StoreInfo s1 = new StoreInfo(store1);
            Store store2 = new Store(1, "nike store", 1);
            StoreInfo s2 = new StoreInfo(store2);
            ArrayList<StoreInfo> stores = new ArrayList<>();
            stores.add(s1);
            stores.add(s2);
            //JSONObject json = new JSONObject();
            String response = gson.toJson(stores);
            // json.put("value" , response);
            res.body(response);
            res.status(200);
            return res.body();
        });
        post("api/stores", (req, res) ->
        {
            System.out.println(req.body());
            res.body("success post");
            res.status(200);
            return res.body();
        });
       // delete
        delete("api/stores/:id",(req,res)->{
            System.out.println(req);
            res.body("success delete");
            res.status(200);
            return res.body();
        });
        //--PRODUCTS---
        // post
        post("api/products", (req, res) ->
        {
            //params
            //    id: number;
            //    storeId: number;
            //    category: string[];
            //    name: string;
            //    description: string;
            //    price: number;
            //    quantity: number;
            //    img: string;
            System.out.println(req.body());
            res.body("success post");
            res.status(200);
            return res.body();
        });
        delete("api/products", (req, res) ->
                {
                    //params-
                    //    id: number; //userid
                    //    storeId: number;
                    //    productId: number;
                   return res.body();
                }
                );
        //patch
        patch("api/products", (req, res) ->
        {
            //we will send the function with all the product attributes and chai needs to check every one of them an if not empty to call
            //the appropriate function
            //these are the params
            //    id: number; userid
            //    storeId: number;
            //    productId: number;
            //    category: string[]| null;
            //    name: string | null;
            //    description: string | null;
            //    price: number | null;
            //    quantity: number| null;
            //    img: string | null;
            Store s1 = new Store(0,"test", 2);
            s1.addNewProduct("mazda", "ziv's vehicle", new AtomicInteger(1), 50);
            JSONObject request = new JSONObject(req.body());
            int quantity = Integer.parseInt(request.get("quantity").toString());
            int pid = Integer.parseInt(request.get("id").toString());
            String desc = request.get("description").toString();
            System.out.println(desc);
            s1.setProductQuantity(pid, quantity);
            System.out.println(s1.getInventory().getProduct(pid).quantity);
            System.out.println(req.body());
            res.body("success patch");
            res.status(200);
            return res.body();
        });
        get("api/products", (req, res) ->
        {
            //params-
            //storeId: number
            return res.body();
        }
        );
        post("api/stores/:id/appointments/owners", (req, res) ->
                {
                    //appoint new owner
                    //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
                    System.out.println(req.body());
                    return res.body();
                }
        );
        post("api/stores/:id/appointments/managers", (req, res) ->
                {
                    //appoint new manager
                    //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
                    System.out.println(req.body());
                    return res.body();
                }
        );

        delete("api/stores/:id/appointments/managers", (req, res) ->
                {
                    //fire manager
                    //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
                    System.out.println(req.body());
                    return res.body();
                }
        );
        delete("api/stores/:id/appointments/owners", (req, res) ->
                {
                    //fire owner
                    //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
                    System.out.println(req.body());
                    return res.body();
                }
        );

        //--PRODUCTS---
        //--CART--

    }
}