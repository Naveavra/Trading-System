package server;

import com.google.gson.Gson;
import domain.store.storeManagement.Store;
import org.json.JSONObject;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static spark.Spark.*;

public class Server {
    public static API api = new API();
    static ConnectedThread connectedThread;
    static ConcurrentHashMap<Integer, Boolean> connected = new ConcurrentHashMap<>();
    static int nextUser =1;
    static Gson gson = new Gson();
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
            toSparkRes(res, api.getClientNotifications(userId, token));
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
            toSparkRes(res, api.changeStoreInfo(userId, token, storeId, name, desc, isActive, img));
            return res.body();
//            String ret = "";
//            if(name != null) {
//                toSparkRes(res, api.changeStoreName(userId, token, storeId, name));
//                ret = res + "\n" + res.body();
//                if(res.status() == 400){
//                    res.body(ret);
//                    return res.body();
//                }
//
//            }
//            if(img != null) {
//                toSparkRes(res, api.changeStoreImg(userId, token, storeId, img));
//                ret = res + "\n" + res.body();
//                if(res.status() == 400){
//                    res.body(ret);
//                    return res.body();
//                }
//            }
//            if(desc != null) {
//                toSparkRes(res, api.changeStoreDescription(userId, token, storeId, desc));
//                ret = res + "\n" + res.body();
//                if(res.status() == 400){
//                    res.body(ret);
//                    return res.body();
//                }
//            }
//            if(isActive.equals("false")) {
//                toSparkRes(res, api.closeStore(userId, token, storeId));
//                ret = res + "\n" + res.body();
//                if(res.status() == 400){
//                    res.body(ret);
//                    return res.body();
//                }
//            }
//            if(isActive.equals("true")) {
//                toSparkRes(res, api.reopenStore(userId, token, storeId));
//                ret = res + "\n" + res.body();
//                if(res.status() == 400){
//                    res.body(ret);
//                    return res.body();
//                }
//            }
//            res.body(ret);
//            return res.body();
        });
        patch("api/stores/:storeId/permissions", (req, res) ->{
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int managerId = Integer.parseInt(request.get("managerId").toString());
            List<String> permissions = (List<String>) request.get("permissions");
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
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String newOwner = request.get("newOwner").toString();
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
        delete("api/stores/:id/appointments/managers", (req, res) -> {
            //fire manager
            //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int newManager = Integer.parseInt(request.get("newOwner").toString());
            toSparkRes(res, api.fireManager(userId, token, newManager, storeId));
            return res.body();
        });
        delete("api/stores/:id/appointments/owners", (req, res) -> {
            //fire owner
            //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int newOwner = Integer.parseInt(request.get("newOwner").toString());
            toSparkRes(res, api.fireOwner(userId, token, newOwner, storeId));
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
        delete("api/products", (req, res) ->
                {
                    JSONObject request = new JSONObject(req.body());
                    int userId = Integer.parseInt(request.get("id").toString());
                    String token = req.headers("Authorization");
                    int storeId = Integer.parseInt(request.get("storeId").toString());
                    int productId = Integer.parseInt(request.get("productId").toString());
                    toSparkRes(res, api.deleteProduct(userId, token, storeId, productId));
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
            //when a user change quantity of a product in specific store basket
            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
            // int userId = Integer.parseInt(req.queryParams("userId"));
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            toSparkRes(res, api.getCart(userId));
            return res.body();
        });
//        post("api/cart/:id", (req, res) ->
//        {
//            //when a user creates a basket for store in the first time this function should handle it
//            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
//            JSONObject request = new JSONObject(req.body());
//            int userId = Integer.parseInt(request.get("userId").toString());
//            int storeId = Integer.parseInt(request.get("storeId").toString());
//            int productId = Integer.parseInt(request.get("productId").toString());
//            int quantity = Integer.parseInt(request.get("quantity").toString());
//            toSparkRes(res, api.addProductToCart(userId, storeId, productId, quantity));
//            return res.body();
//        });
//        patch("api/cart/:id", (req, res) -> {
//            //when a user change quantity of a product in specific store basket
//            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
//            JSONObject request = new JSONObject(req.body());
//            int userId = Integer.parseInt(request.get("userId").toString());
//            int storeId = Integer.parseInt(request.get("storeId").toString());
//            int productId = Integer.parseInt(request.get("productId").toString());
//            int quantity = Integer.parseInt(request.get("quantity").toString());
//            toSparkRes(res, api.changeQuantityInCart(userId, storeId, productId, quantity));
//            return res.body();
//        });
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
            //removefromcart
            //when a user change quantity of a product in specific store basket
            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
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
        get("notifications/:userId", (request, response) -> {
            String userId = request.params("userId");
            BlockingQueue<String> userQueue = getUserQueue(userId);

            try {
                // Take the user's notification from the queue (block until available)
                String notification = userQueue.take();
                response.status(200);
                response.body(notification);
                return response.body();
            } catch (InterruptedException e) {
                response.status(500);
                return "Error retrieving notification";
            }
        });

        // Endpoint for the server to push notifications to a user's queue
        post("/notifications", (req, response) -> {
            JSONObject request = new JSONObject(req.body());
            String userId = request.get("userToSend").toString();
            String notification = request.get("message").toString();
            BlockingQueue<String> userQueue = getUserQueue(userId);
            userQueue.offer(notification);

            response.status(200);
            return "Notification received";
        });

    }

    private static BlockingQueue<String> getUserQueue(String userId) {
        synchronized (userQueues) {
            return userQueues.computeIfAbsent(userId, k -> new LinkedBlockingQueue<>());
        }
    }

}