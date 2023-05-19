package server;

import com.google.gson.Gson;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Store;
import domain.user.ShoppingCart;
import market.Admin;
import market.Market;
import org.json.JSONObject;
import utils.*;

import java.util.*;
import java.util.stream.Collectors;

import utils.infoRelated.*;
import utils.Response;
import utils.messageRelated.Message;
import utils.infoRelated.Receipt;




public class API {
    public Market market;
    Gson gson;
    public API(){
        market = new Market("elibenshimol6@gmail.com", "123Aaa");
        gson = new Gson();
    }

    public Pair<Boolean, JSONObject> fromResToPair(Response res){
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            if(res.getValue().getClass() != String.class && res.getValue().getClass() != Integer.class)
                json.put("value", gson.toJson(res.getValue()));
            else
                json.put("value", res.getValue());
            return new Pair<>(true, json);
        }
    }

    public Pair<Boolean, JSONObject> fromResToPairInfo(Response<? extends Information> res){
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", res.getValue().toJson());
            return new Pair<>(true, json);
        }
    }

    public static Pair<Boolean, JSONObject> fromResToPairList(Response<List<? extends Information>> res){
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", Information.infosToJson(res.getValue()));
            return new Pair<>(true, json);
        }
    }

    private Pair<Boolean, JSONObject> fromResToPairHashMap(Response<HashMap<Integer, ? extends Information>> res, String key,
                                                           String value){
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", Information.hashMapToJson(res.getValue(), key, value));
            return new Pair<>(true, json);
        }
    }

    //guest functions
    public Pair<Boolean, JSONObject> enterGuest(){
        Response<Integer> res = market.enterGuest();
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> exitGuest(int guestId){
        Response<String> res = market.exitGuest(guestId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> register(String email, String password, String birthday){
        Response<String> res = market.register(email, password, birthday);
        return fromResToPair(res);

    }

    public Pair<Boolean, JSONObject> addProductToCart(int userId,int storeId ,int productId, int quantity){
        Response<String> res = market.addProductToCart(userId, storeId, productId, quantity);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeProductFromCart(int userId,  int storeId, int productId){
        Response<String> res = market.removeProductFromCart(userId, storeId, productId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeQuantityInCart(int userId, int storeId, int productId, int change){
        Response<String> res = market.changeQuantityInCart(userId, storeId, productId, change);
        return fromResToPair(res);
    }
    public Pair<Boolean, JSONObject> removeCart(int userId) {
        Response<String> res = market.removeCart(userId);
        return fromResToPair(res);
    }

//    public List<JSONObject> getBaskets(HashMap<Integer, HashMap<Integer, Integer>> basketsMaps)
//    {
//        List<JSONObject> baskets = new ArrayList();
//        for (Map.Entry<Integer, HashMap<Integer, Integer>> basketEntry : basketsMaps.entrySet()) {
//            JSONObject basketJson = getBasket(basketEntry);
//            baskets.add(basketJson);
//        }
//        return baskets;
//    }

    public Pair<Boolean, JSONObject> getCart(int id){
        Response<List<? extends Information>> res = market.getCart(id);
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> makePurchase(int userId , String accountNumber){
        Response<Receipt> res = market.makePurchase(userId, accountNumber);
        return fromResToPairInfo(res);
    }
    public Pair<Boolean, JSONObject> getStoreDescription(int storeId){
        Response<String> res = market.getStoreDescription(storeId);
        return fromResToPair(res);
    }


    //member functions
    public Pair<Boolean, JSONObject> login(String email , String pass){
        Response<LoginInformation> res = market.login(email, pass);
        return fromResToPairInfo(res);
    }
    public Pair<Boolean, JSONObject> getMemberNotifications(int userId, String token) {
        Response<List<String>> res = market.getMemberNotifications(userId, token);
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", NotificationsToJson(res.getValue()));
            return new Pair<>(true, json);
        }
    }

    public JSONObject NotificationsToJson(List<String> list)
    {
        JSONObject json = new JSONObject();
        json.put("notifications", list);
        return json;
    }

    public Pair<Boolean, JSONObject> getClient(int userId, String token) {
        Response<LoginInformation> res = market.getMember(userId, token);
        return fromResToPairInfo(res);
    }
    public Pair<Boolean,JSONObject> logout(int userId){
        Response<String> res = market.logout(userId);
        return fromResToPair(res);
    }

    //TODO: check that works in getClient and login
    public Pair<Boolean, JSONObject> displayNotifications(int userId, String token){
        Response<List<String>> res = market.displayNotifications(userId, token);
        return fromResToPair(res);
    }


    //TODO: add to front a way to change user info
    public Pair<Boolean, JSONObject> changePassword(int userId, String token, String oldPass, String newPass){
        Response<String> res = market.changePassword(userId, token, oldPass, newPass);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeName(int userId, String token, String newUserName){
        Response<String> res = market.changeName(userId, token, newUserName);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeEmail(int userId, String token, String newEmail){
        Response<String> res = market.changeEmail(userId, token, newEmail);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> openStore(int userId, String token, String name, String storeDescription, String img){
        Response<Integer> res = market.openStore(userId, token, name, storeDescription, img);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getMemberInformation(int userId, String token){
        Response<Info> res = market.getMemberInformation(userId, token);
        return fromResToPairInfo(res);
    }

    public Pair<Boolean, JSONObject> getUserPurchaseHistory(int userId, String token, int buyerId){
        //               orderId,         storeId,       productId, quantity
        Response<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> res = market.getUserPurchaseHistoryHash(userId, token, buyerId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> writeReviewToStore(int userId, String token, int orderId, int storeId, String content, int grading){
        Response<String> res = market.writeReviewToStore(userId, token, orderId, storeId, content, grading);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> writeReviewToProduct(int userId, String token, int orderId, int storeId,int productId, String content, int grading){
        Response<String> res = market.writeReviewToProduct(userId, token, orderId, storeId, productId, content, grading);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> checkReviews(int userId, String token, int storeId){
        Response<HashMap<Integer, Message>> res = market.checkReviews(userId, token, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getProductInformation(int storeId, int productId){
        Response<ProductInfo> res = market.getProductInformation(storeId, productId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getStoreInformation(int storeId){
        Response<StoreInfo> res = market.getStoreInformation(storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getStoreProducts(int storeId){
        Response<List<ProductInfo>> res = market.getStoreProducts(storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> sendQuestion(int userId, String token, int storeId,String msg){
        Response<String> res = market.sendQuestion(userId, token, storeId, msg);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> sendComplaint(int userId, String token, int orderId, int storeId, String msg){
        Response<String> res = market.sendComplaint(userId, token, orderId, storeId, msg);
        return fromResToPair(res);
    }


    public Pair<Boolean, JSONObject> appointManager(int userId, String token, String managerToAppoint, int storeId){
        Response<String> res = market.appointManager(userId, token, managerToAppoint, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeStoreInfo(int userId, String token, int storeId, String name, String desc,
                                                     String isActive, String img){
        String ret = "";
        Response<String> ans;
        Pair<Boolean, JSONObject> check;
        if(name != null) {
            check = changeStoreName(userId, token, storeId, name);
            ret = ret + "\n" + check.getSecond();
            if(!check.getFirst()){
                ans = new Response<>(null, "change name failed", ret);
                return fromResToPair(ans);
            }

        }
        if(img != null) {
            check = changeStoreImg(userId, token, storeId, img);
            ret = ret + "\n" + check.getSecond();
            if(!check.getFirst()){
                ans = new Response<>(null, "change img failed", ret);
                return fromResToPair(ans);
            }
        }
        if(desc != null) {
            check = changeStoreDescription(userId, token, storeId, desc);
            ret = ret + "\n" + check.getSecond();
            if(!check.getFirst()){
                ans = new Response<>(null, "change desc failed", ret);
                return fromResToPair(ans);
            }
        }
        if(isActive.equals("false")) {
            check = closeStore(userId, token, storeId);
            ret = ret + "\n" + check.getSecond();
            if(!check.getFirst()){
                ans = new Response<>(null, "close store failed", ret);
                return fromResToPair(ans);
            }
        }
        if(isActive.equals("true")) {
            check = reopenStore(userId, token, storeId);
            ret = ret + "\n" + check.getSecond();
            if(!check.getFirst()){
                ans = new Response<>(null, "reopen store failed", ret);
                return fromResToPair(ans);
            }
        }
        ans = new Response<>(ret, null, null);
        return fromResToPair(ans);

    }
    public Pair<Boolean, JSONObject> changeStoreDescription(int userId, String token, int storeId, String description){
        Response<String> res = market.changeStoreDescription(userId, token, storeId, description);
        return fromResToPair(res);
    }
    public Pair<Boolean, JSONObject> changeStoreImg(int userId, String token, int storeId, String img) {
        Response<String> res = market.changeStoreImg(userId, token, storeId, img);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeStoreName(int userId, String token, int storeId, String name) {
        Response<String> res = market.changeStoreName(userId, token, storeId, name);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changePurchasePolicy(int userId, String token, int storeId, String policy){
        Response<String> res = market.changePurchasePolicy(userId, token, storeId, policy);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeDiscountPolicy(int userId, String token, int storeId, String policy){
        Response<String> res = market.changePurchasePolicy(userId, token, storeId, policy);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addPurchaseConstraint(int userId, String token, int storeId, String policy){
        Response<String> res = market.addPurchaseConstraint(userId, token, storeId, policy);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> fireManager(int userId, String token, int managerToFire, int storeId){
        Response<String> res = market.fireManager(userId, token, managerToFire, storeId);
        return fromResToPair(res);
    }


    public Pair<Boolean, JSONObject> checkWorkerStatus(int userId, String token, int workerId, int storeId) {
        Response<Info> res = market.checkWorkerStatus(userId, token, workerId, storeId);
        return fromResToPairInfo(res);
    }


    public Pair<Boolean, JSONObject> checkWorkersStatus(int userId, String token, int workerId){
        Response<List<? extends Information>> res = market.checkWorkersStatus(userId, token, workerId);
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> closeStore(int userId, String token, int storeId){
        Response<String> res = market.closeStore(userId, token, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> reopenStore(int userId, String token, int storeId){
        Response<String> res = market.reopenStore(userId, token, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addProduct(int userId, String token, int storeId, List<String> categories, String name , String description,
                                                int price , int quantity, String img){
        Response<Integer> res = market.addProduct(userId, token, storeId, categories, name, description, price, quantity, img);
        return fromResToPair(res);
    }


    public Pair<Boolean, JSONObject> getProducts(){
        Response<List<? extends Information>> res = market.getProducts();
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> closeStorePermanently(int adminId, String token, int storeId){
        Response<String> res = market.closeStorePermanently(adminId, token, storeId);
        return fromResToPair(res);
    }


    public Pair<Boolean, JSONObject> appointOwner(int userId, String token, String ownerMail, int storeId)
    {
        Response<String> res = market.appointOwner(userId, token, ownerMail, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> fireOwner(int userId, String token, int ownerId, int storeId)
    {
        Response<String> res = market.fireOwner(userId, token, ownerId, storeId);
        return fromResToPair(res);
    }


    public Pair<Boolean, JSONObject> addManagerPermissions(int ownerId, String token, int userId,int storeId, List<Integer> permissionsIds)
    {
        Response<String> res = market.addManagerPermissions(ownerId, token, userId, storeId, permissionsIds);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeManagerPermissions(int ownerId, String token, int userId,int storeId, List<Integer> permissionsIds)
    {
        Response<String> res = market.removeManagerPermissions(ownerId, token, userId, storeId, permissionsIds);
        return fromResToPair(res);
    }


    public Pair<Boolean, JSONObject> answerQuestion(int userId, String token, int storeId ,int questionId, String answer)
    {
        Response<String> res = market.answerQuestion(userId, token, storeId, questionId, answer);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> seeStoreHistory(int userId, String token, int storeId)
    {
        Response<List<? extends Information>> res = market.seeStoreHistory(userId, token, storeId);
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> deleteProduct(int userId, String token, int storeId, int productId)
    {
        Response<String> res = market.deleteProduct(userId, token, storeId, productId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> updateProduct(int userId, String token, int storeId, int productId, List<String> categories, String name , String description ,
                                                   int price , int quantity, String img)
    {
        Response<String> res = market.updateProduct(userId, token, storeId, productId, categories, name, description, price, quantity, img);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> adminLogin(String email, String password)
    {
        Response<LoginInformation> res = market.adminLogin(email, password);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> adminLogout(int adminId)
    {
        Response<String> res = market.adminLogout(adminId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getAdmins(int adminId, String token)
    {
        Response<HashMap<Integer, Admin>> res = market.getAdmins(adminId, token);
        //TODO: fix that
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addAdmin(int adminId, String token, String email , String pass)
    {
        Response<String> res = market.addAdmin(adminId, token, email, pass);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeAdmin(int adminId, String token)
    {
        Response<String> res = market.removeAdmin(adminId, token);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> answerComplaint(int adminId, String token, int complaintId, String ans)
    {
        Response<String> res = market.answerComplaint(adminId, token, complaintId, ans);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> cancelMembership(int adminId, String token, int userToRemove)
    {
        Response<String> res = market.cancelMembership(adminId, token, userToRemove);
        return fromResToPair(res);
    }

    private String logsToString(HashMap<Logger.logStatus, List<String>> logs){
        List<String> logsList = new ArrayList();
        for (Map.Entry<Logger.logStatus, List<String>> logEntry : logs.entrySet()) {
            JSONObject productJson = new JSONObject();
            productJson.put("status", logEntry.getKey());
            productJson.put("messages", logEntry.getValue());
            logsList.add(productJson.toString());
        }
        String logsMessages = logsList.stream()
                .collect(Collectors.joining(",", "[", "]"));
        return logsMessages;
    }


    //TODO: fix
    public Pair<Boolean, JSONObject> watchEventLog(int adminId, String token)
    {
        Response<List<String>> res1 = market.watchEventLog(adminId, token);
        Response<List<String>> res2 = market.watchFailLog(adminId, token);
        //[{"status": ["log1...", "log2...", ......]}, ...]
        JSONObject json = new JSONObject();
        if(res1.errorOccurred())
        {
            json.put("errorMsg", res1.getErrorMessage());
            return new Pair<>(false, json);
        }
        else if(res2.errorOccurred())
        {
            json.put("errorMsg", res2.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            List<String> event = res1.getValue();
            List<String> fail = res2.getValue();
            HashMap<Logger.logStatus, List<String>> ans = new HashMap<>();
            ans.put(Logger.logStatus.Fail, fail);
            ans.put(Logger.logStatus.Success, event);
            json.put("value", logsToString(ans));
            return new Pair<>(true, json);
        }
    }

    public Pair<Boolean, JSONObject> viewQuestions(int userId, String token, int storeId)
    {
        Response<HashMap<Integer, ? extends Information>> res = market.viewQuestions(userId, token, storeId);
        return fromResToPairHashMap(res, "messageId", "question");
    }

    public Pair<Boolean, JSONObject> getUsersPurchaseHistory(int buyerId, String token)
    {
        Response<List<? extends Information>> res = market.getUsersPurchaseHistory(buyerId, token);
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> getStores()
    {
        Response<List<? extends Information>> res = market.getStoresInformation();
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> getStore(int userId, String token, int storeId) {
        Response<Store> res =  market.getStore(userId, token, storeId);
        return fromResToPairInfo(res);
    }

    public Pair<Boolean, JSONObject> getAppointments(int userId, String token, int storeId)
    {
        Response<AppHistory> res = market.getAppointments(userId, token, storeId);
        // TODO: cast this to json
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> watchMarketStatus(int adminId, String token)
    {
        Response<MarketInfo> res = market.watchMarketStatus(adminId, token);
        // TODO: cast this to json
        return fromResToPairInfo(res);
    }

    public void mockData(){
        market.register("eli@gmail.com", "123Aaa", "24/02/2002");
        market.register("ziv@gmail.com", "456Bbb", "01/01/2002");
        market.register("nave@gmail.com", "789Ccc", "01/01/1996");
        Response<LoginInformation> res = market.login("eli@gmail.com", "123Aaa");
        int id1 = res.getValue().getUserId();
        String token1 = res.getValue().getToken();
        res = market.login("ziv@gmail.com", "456Bbb");
        int id2 = res.getValue().getUserId();
        String token2 = res.getValue().getToken();
        Response<Integer> res2 = market.openStore(id1, token1, "nike", "shoe store", "https://images.pexels.com/photos/786003/pexels-photo-786003.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        int sid1 = res2.getValue();
        res2 = market.openStore(id2, token2, "rollups", "candy store", "https://images.pexels.com/photos/65547/pexels-photo-65547.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        int sid2 = res2.getValue();
        List<String> categories = new LinkedList<>();
        categories.add("shoes");
        categories.add("new");
        categories.add("fresh");
        res2 = market.addProduct(id1, token1, sid1, categories, "air1", "comfy", 100, 20, "https://images.pexels.com/photos/13691727/pexels-photo-13691727.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        int pid1 = res2.getValue();
        res2 = market.addProduct(id1, token1, sid1, categories, "air2", "more comfy", 300, 10,"https://images.pexels.com/photos/4215840/pexels-photo-4215840.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        int pid2 = res2.getValue();
        market.addProductToCart(id1, sid2, pid1, 3);
        market.addProductToCart(id2, sid1, pid1, 5);
        Response<Receipt> res3 = market.makePurchase(id1, "9999999");
        market.writeReviewToStore(id1, token1, res3.getValue().getOrderId(), sid2, "bad store", 2);
        res3 = market.makePurchase(id2, "111111");
        market.writeReviewToStore(id2, token2, res3.getValue().getOrderId(), sid1, "good store", 4);
        market.sendQuestion(id1, token1, sid1, "why bad?");
        market.appointManager(id1, token1, "ziv@gmail.com", sid1);
        market.appointManager(id2, token2, "eli@gmail.com", sid2);
        market.logout(id1);
        market.logout(id2);
    }

}
