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

import static spark.Spark.*;

public class Server {
    public static API api = new API();
    static ConnectedThread connectedThread;
    static ConcurrentHashMap<Integer, Boolean> connected = new ConcurrentHashMap<>();
    static int nextUser =1;
    static Gson gson = new Gson();
    private static HashMap< Integer, ArrayBlockingQueue<String>> messageQueue = new HashMap<>();
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
        post("api/auth/ping", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String id = request.get("userId").toString();
            connected.put(Integer.parseInt(id), true);
            System.out.println(id + " becomes true");
            res.status(200);
            res.body("ping success");
            return res.body();
        });

        //--AUTHENTICATION---
        //-----GUEST-----
        post("api/auth/guest/enter", (req, res) -> {
            toSparkRes(res, api.enterGuest());
            return res.body();
        });
        //-----MEMBER-----
        post("api/auth/login", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            String email = request.get("email").toString();
            String pass = request.get("password").toString();
            toSparkRes(res, api.login(email, pass));
            return res.body();
        });

        //getClient
        post("api/auth/getClient", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            toSparkRes(res, api.getClient(userId, token));
            return res.body();
        });
        post("api/auth/logout", (req, res) -> {
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            toSparkRes(res, api.logout(userId, token));
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
        get("api/auth/getClient",(req,res)->{
            JSONObject request = new JSONObject(req.body());
            int id = Integer.parseInt(request.get("userId").toString());
            String token = request.get("token").toString();
            toSparkRes(res, api.getClient(id, token));
            return res.body();
        });
        // delete
        delete("api/stores/:id", (req, res)-> {
            JSONObject request = new JSONObject(req.body());
            int adminId = Integer.parseInt(request.get("adminId").toString());
            int storeId = Integer.parseInt(request.get("id").toString());
            String token = req.headers("Authorization");
            toSparkRes(res, api.closeStorePermanently(adminId, token, storeId));
            return res.body();
        });

        //---Cart-------------------------------:
        //---------Guest-------------------------:
        get("api/guest/:id/cart",(req,res)->{
            JSONObject request = new JSONObject(req.body());
            int userId = (int) (request.get("userId"));
            toSparkRes(res, api.getCart(userId));
            return res.body();
        });

        post("api/guest/:id/cart/:storeId/:productId", (req, res)->{
            JSONObject request = new JSONObject(req.body());
            int userId = (int) (request.get("userId"));
            int storeId = (int) (request.get("storeId"));
            int productId = (int) (request.get("productId"));
            int quantity = (int) (request.get("newQuantity"));
            toSparkRes(res, api.addProductToCart(userId, storeId, productId, quantity));
            return res.body();
        });

        patch("api/guest/:id/cart/:storeId/:productId", (req, res)->{
            JSONObject request = new JSONObject(req.body());
            int userId = (int) (request.get("userId"));
            int storeId = (int) (request.get("storeId"));
            int productId = (int) (request.get("productId"));
            int quantity = (int) (request.get("newQuantity"));
            toSparkRes(res, api.changeQuantityInCart(userId, storeId, productId, quantity));
            return res.body();
        });

        delete("api/guest/:id/cart/:storeId/:productId", (req, res)->{
            JSONObject request = new JSONObject(req.body());
            int userId = (int) (request.get("userId"));
            int storeId = (int) (request.get("storeId"));
            int productId = (int) (request.get("productId"));
            toSparkRes(res, api.removeProductFromCart(userId, storeId, productId));
            return res.body();
        });
        //TODO: ---------Member-------------------------:



        //--PRODUCTS---
        // post
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
                    int quantity = (int) (request.get("newQuantity"));
                    String img = request.get("newQuantity").toString();//TODO: Add img feature
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
            String[] arr = catStr.substring(1, catStr.length() - 1).split(",");
            List<String> categories = new ArrayList<>(Arrays.asList(arr));
            String name = request.get("name").toString();
            String description = request.get("description").toString();
            int price = Integer.parseInt(request.get("price").toString());
            int quantity = (int) (request.get("newQuantity"));
            String img = request.get("newQuantity").toString();//TODO: Add img feature
            toSparkRes(res, api.updateProduct(userId, token, storeId, productId, categories, name, description, price, quantity));
            return res.body();
        });
        get("api/products", (req, res) ->
        {
            JSONObject request = new JSONObject(req.body());
            int storeId = Integer.parseInt(request.get("storeId").toString());
            toSparkRes(res, api.getProducts(storeId));
            return res.body();
        }
        );
        //--APPOINTMENTS---
        post("api/stores/:id/appointments/owners", (req, res) ->
        {
            //appoint new owner
            //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String newOwner = request.get("newOwner").toString();
            toSparkRes(res, api.appointOwner(userId, token, newOwner, storeId));
            return res.body();
        }
        );
        post("api/stores/:id/appointments/managers", (req, res) ->
        {
            //appoint new manager
            //this function will receive {"storeId":0,"userIncharge":1,"newManager":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            String newManager = request.get("newManager").toString();
            toSparkRes(res, api.appointManager(userId, token, newManager, storeId));
            return res.body();
        }
        );

        delete("api/stores/:id/appointments/managers", (req, res) ->
        {
            //fire manager
            //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int newManager = Integer.parseInt(request.get("newOwner").toString());
            toSparkRes(res, api.fireManager(userId, token, newManager, storeId));
            return res.body();
        }
        );
        delete("api/stores/:id/appointments/owners", (req, res) ->
        {
            //fire owner
            //this function will receive {"storeId":0,"userIncharge":1,"newOwner":2}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userIncharge").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int newOwner = Integer.parseInt(request.get("newOwner").toString());
            toSparkRes(res, api.fireOwner(userId, token, newOwner, storeId));
            return res.body();
        }
        );

        //--APPOINTMENTS
        //--CART
        post("api/cart/:id", (req, res) ->
        {
            //when a user creates a basket for store in the first time this function should handle it
            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int productId = Integer.parseInt(request.get("productId").toString());
            int quantity = Integer.parseInt(request.get("quantity").toString());
            toSparkRes(res, api.addProductToCart(userId, storeId, productId, quantity));
            return res.body();
        }
        );
        patch("api/cart/:id", (req, res) ->
        {
            //when a user change quantity of a product in specific store basket
            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            String token = req.headers("Authorization");
            int storeId = Integer.parseInt(request.get("storeId").toString());
            int productId = Integer.parseInt(request.get("productId").toString());
            int quantity = Integer.parseInt(request.get("quantity").toString());
            toSparkRes(res, api.changeQuantityInCart(userId, storeId, productId, quantity));
            return res.body();
        }
        );
        get("api/cart/:id", (req, res) ->
        {
            //when a user change quantity of a product in specific store basket
            //params {"userId":0,"storeId":0,"prouctId":1,"quantity":5}
           // int userId = Integer.parseInt(req.queryParams("userId"));
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            toSparkRes(res, api.getCart(userId));
            return res.body();
        }
        );
        delete("api/cart/:id", (req, res) ->
        {
            //delete cart
            //params userId
            JSONObject request = new JSONObject(req.body());
            int userId = Integer.parseInt(request.get("userId").toString());
            //TODO: toSparkRes(res, api.removeCart(userId));
            return res.body();
        });

    }

}