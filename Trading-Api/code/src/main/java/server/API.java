package server;

import com.google.gson.Gson;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Store;
import market.Admin;
import market.Market;
import org.json.JSONObject;
import utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import utils.marketRelated.MarketInfo;
import utils.marketRelated.Response;
import utils.messageRelated.Message;
import utils.orderRelated.OrderInfo;
import utils.userInfoRelated.Info;
import utils.userInfoRelated.Receipt;




public class API {
    public Market market;
    Gson gson;
    public API(){
        Admin admin = new Admin(-1, "elibenshimol6@gmail.com", "123Aaa");
        market = new Market(admin);
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

    public Pair<Boolean, JSONObject> getCart(int id){
        Response<HashMap<Integer, HashMap<Integer, Integer>>> res = market.getCart(id);
        return fromResToPair(res);
    }


    //TODO: make a test to check functionality for purchase
    public Pair<Boolean, JSONObject> makePurchase(int userId , String accountNumber){
        Response<Receipt> res = market.makePurchase(userId, accountNumber);
        return fromResToPair(res);
    }
    public Pair<Boolean, JSONObject> getStoreDescription(int storeId){
        Response<String> res = market.getStoreDescription(storeId);
        return fromResToPair(res);
    }


    //member functions
    public Pair<Boolean, JSONObject> login(String email , String pass){
        Response<LoginInformation> res = market.login(email, pass);
        return fromResToPair(res);
    }
    public Pair<Boolean,JSONObject> logout(int userId){
        Response<String> res = market.logout(userId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> checkSecurityQuestions(int userId, List<String> answers){
        Response<String> res = market.checkSecurityQuestions(userId, answers);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addSecurityQuestion(int userId, String question, String answer){
        Response<String> res = market.addSecurityQuestion(userId, question, answer);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeAnswerForLoginQuestion(int userId, String question, String answer){
        Response<String> res = market.changeAnswerForLoginQuestion(userId, question, answer);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeSecurityQuestion(int userId, String question){
        Response<String> res = market.removeSecurityQuestion(userId, question);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> displayNotifications(int userId){
        Response<List<String>> res = market.displayNotifications(userId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changePassword(int userId,String oldPass ,String newPass){
        Response<String> res = market.changePassword(userId, oldPass, newPass);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeName(int userId, String newUserName){
        Response<String> res = market.changeName(userId, newUserName);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeEmail(int userId, String newEmail){
        Response<String> res = market.changeEmail(userId, newEmail);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> openStore(int userId,String storeDescription){
        Response<Integer> res = market.openStore(userId, storeDescription);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getMemberInformation(int userId){
        Response<Info> res = market.getMemberInformation(userId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getUserPurchaseHistory(int userId, int buyerId){
        //               orderId,         storeId,       productId, quantity
        Response<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> res = market.getUserPurchaseHistory(userId, buyerId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> writeReviewToStore(int orderId, int storeId, String content, int grading, int userId){
        Response<String> res = market.writeReviewToStore(orderId, storeId, content, grading, userId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> writeReviewToProduct(int orderId, int storeId,int productId, String content, int grading, int userId){
        Response<String> res = market.writeReviewToProduct(orderId, storeId, productId, content, grading, userId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> checkReviews(int userId, int storeId){
        Response<HashMap<Integer, Message>> res = market.checkReviews(userId, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getProductInformation(int storeId , int productId){
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

    public Pair<Boolean, JSONObject> sendQuestion(int userId,int storeId,String msg){
        Response<String> res = market.sendQuestion(userId, storeId, msg);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> sendComplaint(int userId,int orderId,int storeId,String msg){
        Response<String> res = market.sendComplaint(userId, orderId, storeId, msg);
        return fromResToPair(res);
    }


//TODO: add from manager methods

    public Pair<Boolean, JSONObject> appointManager(int userId, int storeId, int managerIdToAppoint){
        Response<String> res = market.appointManager(userId, storeId, managerIdToAppoint);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeStoreDescription(int userId, int storeId, String description){
        Response<String> res = market.changeStoreDescription(userId, storeId, description);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changePurchasePolicy(int userId, int storeId, String policy){
        Response<String> res = market.changePurchasePolicy(userId, storeId, policy);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeDiscountPolicy(int userId, int storeId, String policy){
        Response<String> res = market.changePurchasePolicy(userId, storeId, policy);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addPurchaseConstraint(int userId, int storeId, String policy){
        Response<String> res = market.addPurchaseConstraint(userId, storeId, policy);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> fireManager(int userId, int storeId, int managerToFire){
        Response<String> res = market.fireManager(userId, storeId, managerToFire);
        return fromResToPair(res);
    }

    private JSONObject infoToJson(Info info)
    {
        JSONObject json = new JSONObject();
        json.put("userId", info.getId());
        json.put("userName", info.getName());
        json.put("email", info.getEmail());
        json.put("birthday", info.getBirthday());
        json.put("age", info.getAge());
        String permissions = info.getManagerActions().stream()
                .map(act -> String.valueOf(act))
                .collect(Collectors.joining(",", "[", "]"));
        json.put("managerPermissions", permissions);
        return json;
    }

    public Pair<Boolean, JSONObject> checkWorkerStatus(int userId, int workerId, int storeId) {
        Response<Info> res = market.checkWorkerStatus(userId, workerId, storeId);
        JSONObject json = new JSONObject();
        if (res.errorOccurred()) {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        } else {
            json.put("value", infoToJson(res.getValue()));
            return new Pair<>(true, json);
        }
    }

    private String infosToJson(List<Info> infos)
    {
        return infos.stream()
                .map(info -> infoToJson(info).toString())
                .collect(Collectors.joining(",", "[", "]"));
    }


    public Pair<Boolean, JSONObject> checkWorkersStatus(int userId, int workerId){
        Response<List<Info>> res = market.checkWorkersStatus(userId, workerId);
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", infosToJson(res.getValue()));
            return new Pair<>(true, json);
        }
    }

    public Pair<Boolean, JSONObject> closeStore(int userId, int storeId){
        Response<String> res = market.closeStore(userId, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> reopenStore(int userId, int storeId){
        Response<String> res = market.reopenStore(userId, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addProduct(int userId, int storeId, List<String> categories, String name , String description , int price , int quantity){
        Response<Integer> res = market.addProduct(userId, storeId, categories, name, description, price, quantity);
        return fromResToPair(res);
    }

    private JSONObject productToJson(ProductInfo product)
    {
        JSONObject json = new JSONObject();
        json.put("productId", product.getId());
        json.put("name", product.getName());
        json.put("description", product.getDescription());
        json.put("price", product.getPrice());
        json.put("quantity", product.getQuantity());
        String categories = product.getCategories().stream()
                .collect(Collectors.joining(",", "[", "]"));
        json.put("categories", categories);
        return json;
    }

    private String productsToJson(List<ProductInfo> products)
    {
        return products.stream()
                .map(info -> productToJson(info).toString())
                .collect(Collectors.joining(",", "[", "]"));
    }

    public Pair<Boolean, JSONObject> getProducts(int storeId){
        Response<List<ProductInfo>> res = market.getProducts(storeId);
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", productsToJson(res.getValue()));
            return new Pair<>(true, json);
        }

    }

    public Pair<Boolean, JSONObject> closeStorePermanently(int adminId, int storeId){
        Response<String> res = market.closeStorePermanently(adminId, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> appointOwner(int userId , int storeId,int ownerId)
    {
        Response<String> res = market.appointOwner(userId, storeId, ownerId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> fireOwner(int userId , int storeId, int ownerId)
    {
        Response<String> res = market.fireOwner(userId, storeId, ownerId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addManagerPermission(int ownerId, int userId,int storeId, int permissionsId)
    {
        Response<String> res = market.addManagerPermission(ownerId, userId, storeId, permissionsId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addManagerPermissions(int ownerId, int userId,int storeId, List<Integer> permissionsIds)
    {
        Response<String> res = market.addManagerPermissions(ownerId, userId, storeId, permissionsIds);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeManagerPermissions(int ownerId, int userId,int storeId, List<Integer> permissionsIds)
    {
        Response<String> res = market.removeManagerPermissions(ownerId, userId, storeId, permissionsIds);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeManagerPermission(int ownerId, int userId,int storeId, int permissionsId)
    {
        Response<String> res = market.removeManagerPermission(ownerId, userId, storeId, permissionsId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> answerQuestion(int userId,int storeId ,int questionId,String answer)
    {
        Response<String> res = market.answerQuestion(userId, storeId, questionId, answer);
        return fromResToPair(res);
    }

    private JSONObject getBasket(Map.Entry<Integer, HashMap<Integer, Integer>> basketEntry){
        JSONObject basketJson = new JSONObject();
        basketJson.put("storeId", basketEntry.getKey());
        List<String> bucketList = new ArrayList();
        for (Map.Entry<Integer, Integer> productEntry : basketEntry.getValue().entrySet()) {
            JSONObject productJson = new JSONObject();
            productJson.put("productId", productEntry.getKey());
            productJson.put("quantity", productEntry.getValue());
            bucketList.add(productJson.toString());
        }
        String bucket = bucketList.stream()
                .collect(Collectors.joining(",", "[", "]"));
        basketJson.put("products", bucket);
        return basketJson;
    }

    private JSONObject orderToJson(OrderInfo order) {
        JSONObject json = new JSONObject();
        json.put("orderId", order.getOrderId());
        json.put("userId", order.getUserId());
        json.put("price", order.getTotalPrice());
        List<String> baskets = new ArrayList();
        for (Map.Entry<Integer, HashMap<Integer, Integer>> basketEntry : order.getProductsInStores().entrySet()) {
            JSONObject basketJson = getBasket(basketEntry);
            baskets.add(basketJson.toString());
        }
        String products = baskets.stream()
                .collect(Collectors.joining(",", "[", "]"));
        json.put("productsInStores", products);
        return json;
    }

    private String ordersToJson(List<OrderInfo> orders) {
        return orders.stream()
                .map(order -> orderToJson(order).toString())
                .collect(Collectors.joining(",", "[", "]"));
    }

    public Pair<Boolean, JSONObject> seeStoreHistory(int userId,int storeId)
    {
        Response<List<OrderInfo>> res = market.seeStoreHistory(userId, storeId);
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", ordersToJson(res.getValue()));
            return new Pair<>(true, json);
        }
    }

    public Pair<Boolean, JSONObject> deleteProduct(int userId,int storeId,int productId)
    {
        Response<String> res = market.deleteProduct(userId, storeId, productId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> updateProduct(int userId, int storeId, int productId, List<String> categories, String name , String description , int price , int quantity)
    {
        Response<String> res = market.updateProduct(userId, storeId, productId, categories, name, description, price, quantity);
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

    public Pair<Boolean, JSONObject> getAdmins(int adminId)
    {
        Response<HashMap<Integer, Admin>> res = market.getAdmins(adminId);
        //TODO: fix that
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addAdmin(int adminId, String email , String pass)
    {
        Response<String> res = market.addAdmin(adminId, email, pass);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeAdmin(int adminId)
    {
        Response<String> res = market.removeAdmin(adminId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> answerComplaint(int adminId, int complaintId, String ans)
    {
        Response<String> res = market.answerComplaint(adminId, complaintId, ans);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> cancelMembership(int adminId, int userToRemove)
    {
        Response<String> res = market.cancelMembership(adminId, userToRemove);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> watchLog(int adminId)
    {
        Response<HashMap<Logger.logStatus, List<String>>> res = market.watchLog(adminId);
        //TODO: fix that return value
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> viewQuestions(int userId, int storeId)
    {
        Response<HashMap<Integer, Message>> res = market.viewQuestions(userId, storeId);
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", questionsToJson(res.getValue()));
            return new Pair<>(true, json);
        }
    }

    private String questionsToJson(HashMap<Integer, Message> questions) {
        List<String> questionsList = new ArrayList<>();
        for (Map.Entry<Integer, Message> question: questions.entrySet())
        {
            JSONObject questionJson = new JSONObject();
            questionJson.put("questionId", question.getKey());
            questionJson.put("message", question.getValue());
            questionsList.add(questionJson.toString());
        }
        return questionsList.stream()
                .collect(Collectors.joining(",", "[", "]"));
    }

    public Pair<Boolean, JSONObject> getUsersPurchaseHistory(int buyerId)
    {
        Response<List<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>>> res = market.getUsersPurchaseHistory(buyerId);
        // TODO: cast this to json
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getStores()
    {
        Response<ConcurrentHashMap<Integer, Store>> res = market.getStores();
        // TODO: cast this to json
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getAppointments(int userId, int storeId)
    {
        Response<AppHistory> res = market.getAppointments(userId, storeId);
        // TODO: cast this to json
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> watchMarketStatus(int adminId)
    {
        Response<MarketInfo> res = market.watchMarketStatus(adminId);
        // TODO: cast this to json
        return fromResToPair(res);
    }




}
