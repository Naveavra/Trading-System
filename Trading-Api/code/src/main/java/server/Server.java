package server;

import com.google.gson.Gson;
import data.StoreInfo;
import domain.store.storeManagement.Store;
import org.json.JSONObject;
import spark.Session;
import utils.Pair;
import utils.marketRelated.Response;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

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
            toSparkRes(res, api.logout(Integer.parseInt(userId)));
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

        //Store:
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
            System.out.println(req);
            res.body("success post");
            res.status(200);
            return res.body();
        });
        delete("", (req, res)-> {
            JSONObject request = new JSONObject(req.body());
            int userId = (int) (request.get("userId"));
            int storeId = (int) (request.get("storeId"));
            toSparkRes(res, api.closeStore(userId, storeId));
            return res.body();
        });
        //---Admin-------------------------------:

        //---Cart-------------------------------:
        //---------Guest-------------------------:
        //Could be also for any user....
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

    }
}