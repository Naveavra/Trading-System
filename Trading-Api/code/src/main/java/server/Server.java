package server;

import com.google.gson.Gson;
import org.json.JSONObject;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static spark.Spark.*;

public class Server {
    public static API api = new API();
    static ConnectedThread connectedThread;
    static ConcurrentHashMap<Integer, Boolean> connected = new ConcurrentHashMap<>();
    private static HashMap< Integer, ArrayBlockingQueue<String>> messageQueue = new HashMap<>();
    private static HashMap<String, BlockingQueue<String>> userQueues = new HashMap<>();

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
//        messageQueue.put(0,new ArrayBlockingQueue<>(20));
//        messageQueue.put(1,new ArrayBlockingQueue<>(20));
//        api.register("eli@gmail.com", "aA12345", "22/02/2002");
        init();
        api.mockData();
        connectedThread = new ConnectedThread(connected);
       // connectedThread.start();
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

        // auth/api
        post("api/auth/login", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String email = request.get("email").toString();
            String pass = request.get("password").toString();
            toSparkRes(res, api.login(email, pass));
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
        post("api/auth/logout", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            toSparkRes(res, api.logout(userId));
            return res.body();
        });
        post("api/auth/guest/enter", (req, res) -> {
            toSparkRes(res, api.enterGuest());
            return res.body();
        });
        post("api/auth/ping", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String id = request.get("userId").toString();
            connected.put(Integer.parseInt(id), true);
            res.status(200);
            res.body("ping success");
            return res.body();
        });
        post("api/auth/getNotifications", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = request.get("token").toString();
            toSparkRes(res, api.getMemberNotifications(userId, token));
            return res.body();
        });
        post("api/auth/getClient", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            toSparkRes(res, api.getClient(userId, token));
            return res.body();
        });

        //stores
        get("api/stores/info" , (req,res)->{
            toSparkRes(res, api.getStores());
            return res.body();
        });
        post("api/stores/:storeId/getStore", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            toSparkRes(res, api.getStore(userId, token, storeId));
            return res.body();
        });
        get("api/stores/:id/products", (req, res) ->{
            JSONObject request = new JSONObject(req.body());
            int storeId = Integer.parseInt(request.get("storeId").toString());
            toSparkRes(res, api.getStoreProducts(storeId));
            return res.body();
        });
        post("api/stores", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String name = request.getString("name");
            String description = request.getString("desc");
            String img = request.getString("img");
            String token = req.headers("Authorization");
            toSparkRes(res, api.openStore(userId, token, name, description, img));
            return res.body();
        });
        patch("api/stores/:id", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String token = req.headers("Authorization");
            String name = request.get("name").toString();
            String desc = request.get("desc").toString();
            String isActive = request.get("isActive").toString();
            String img = request.get("img").toString();
            toSparkRes(res, api.changeStoreInfo(userId, token, storeId, name, desc, img, isActive));
            return res.body();

        });
        patch("api/stores/:storeId/permissions", (req, res) ->{
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int managerId = Integer.parseInt(request.get("managerId").toString());
            String perStr = request.get("permissions").toString();
            String[] arr = perStr.substring(1, perStr.length() - 1).split(",");
            List<String> permissions = new ArrayList<>();
            for (String s : arr) permissions.add(s.substring(1, s.length() - 1));
            String mode = request.get("mode").toString();
            if(mode.equals("add"))
                toSparkRes(res, api.addManagerPermissions(userId, token, managerId, storeId, permissions));
            else{
                toSparkRes(res, api.removeManagerPermissions(userId, token, managerId, storeId, permissions));
            }
            return res.body();
        });
        delete("api/stores/:id", (req, res)-> {
            JSONObject request = new JSONObject(req.body());
            int adminId = Integer.parseInt(request.get("adminId").toString());
            int storeId = Integer.parseInt(request.get("id").toString());
            String token = req.headers("Authorization");
            toSparkRes(res, api.closeStorePermanently(adminId, token, storeId));
            return res.body();
        });
        post("api/stores/:id/appointments/owners", (req, res) -> {
            //appoint new owner
            //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String newOwner = request.get("emailOfUser").toString();
            toSparkRes(res, api.appointOwner(userId, token, newOwner, storeId));
            return res.body();
        });
        post("api/stores/:id/appointments/managers", (req, res) -> {
            //appoint new manager
            //this function will receive {"storeId":0,"userIncharge":1,"newManager":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String newManager = request.get("emailOfUser").toString();
            toSparkRes(res, api.appointManager(userId, token, newManager, storeId));
            return res.body();
        });
        patch("api/stores/:id/appointments/managers", (req, res) -> {
            //fire manager
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int managerToFire = Integer.parseInt(request.get("userToFire").toString());
            toSparkRes(res, api.fireManager(userId, token, managerToFire, storeId));
            return res.body();
        });
        patch("api/stores/:id/appointments/owners", (req, res) -> {
            //fire owner
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int ownerToFire = Integer.parseInt(request.get("userToFire").toString());
            toSparkRes(res, api.fireOwner(userId, token, ownerToFire, storeId));
            return res.body();
        });

        //products
        get("api/products", (req, res) -> {
            toSparkRes(res, api.getProducts());
            return res.body();
        });
        post("api/products", (req, res) ->
        {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("id").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String catStr = request.get("category").toString();
            String[] arr = catStr.substring(1, catStr.length() - 1).split(",");
            List<String> categories = new ArrayList<>(Arrays.asList(arr));
            String name = request.get("name").toString();
            String description = request.get("description").toString();
            int price = Integer.parseInt(request.get("price").toString());
            int quantity = Integer.parseInt(request.get("quantity").toString());
            String img = request.get("img").toString();
            toSparkRes(res, api.addProduct(userId, token, storeId, categories, name, description, price, quantity, img));
            return res.body();
        });
        delete("api/products", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("id").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int productId = Integer.parseInt(request.get("productId").toString());
            toSparkRes(res, api.deleteProduct(userId, token, storeId, productId));
            return res.body();
        });
        //patch
        patch("api/products", (req, res) ->
                {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("id").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int productId = Integer.parseInt(request.get("productId").toString());
            String catStr = request.get("category").toString();
            List<String> categories =null;
            if (!catStr.equals("null")) {
                String[] arr = catStr.substring(1, catStr.length() - 1).split(",");
                categories =new ArrayList<>(Arrays.asList(arr));
            }
            String name = request.getString("name");
            String description = request.get("description").toString();
            int price =0;
            String strPrice = request.get("price").toString();
            if(!strPrice.equals("null")){
                price  = Integer.parseInt(strPrice);
            }
            else{
                price =-1;
            }
            int quantity = 0 ;
            String strQuantity = request.get("quantity").toString();
            if(!strQuantity.equals(("null"))){
                quantity =  Integer.parseInt(strQuantity);
            }
            else{
                quantity =-1;
            }
            String img = request.get("img").toString();
            toSparkRes(res, api.updateProduct(userId, token, storeId, productId, categories, name, description, price, quantity, img));
            return res.body();
        });

        //cart
        post("api/cart/:id", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            toSparkRes(res, api.getCart(userId));
            return res.body();
        });
        patch("api/cart/add/:id", (req, res) -> {
            //addtoart
            //when a user change quantity of a product in specific store basket
            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int productId = Integer.parseInt(request.get("productId").toString());
            int quantity = Integer.parseInt(request.get("quantity").toString());
            toSparkRes(res, api.changeQuantityInCart(userId, storeId, productId, quantity));
            return res.body();
        });
        patch("api/cart/remove/:id", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int productId = Integer.parseInt(request.get("productId").toString());
            int quantity = Integer.parseInt(request.get("quantity").toString());
            toSparkRes(res, api.changeQuantityInCart(userId, storeId, productId, -1 * quantity));
            return res.body();
        });
        post("api/cart/buy/:id", (req, res) ->{
            //buycart
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String accountNumber = request.get("accountNumber").toString();
            toSparkRes(res, api.makePurchase(userId, accountNumber));
            return res.body();
        });

        delete("api/cart/:id", (req, res) ->
        {
            //delete cart
            //params userId
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            toSparkRes(res, api.removeCart(userId));
            return res.body();
        });

        //----------------------------- notifications------------------------

        // Endpoint for client to subscribe for notifications
        post("api/auth/notifications", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            toSparkRes(res, api.getNotification(userId, token));
            return res.body();
        });

        // Endpoint for the server to push notifications to a user's queue
        post("api/auth/notifications/msg", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            String userToName = request.get("userName").toString();
            String notification = request.get("message").toString();
            //TODO:
            toSparkRes(res, api.sendNotification(userId, token, userToName, notification));
            return res.body();
        });

    }
}