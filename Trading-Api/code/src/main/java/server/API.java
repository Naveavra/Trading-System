package server;

import com.google.gson.Gson;
import domain.store.storeManagement.Store;
import market.Market;
import org.json.JSONObject;
import utils.*;

import java.util.*;

import utils.infoRelated.*;
import utils.Response;
import utils.infoRelated.Receipt;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;
import utils.stateRelated.Action;

public class API {
    public Market market;
    private HashMap<String, Integer> actionStrings;
    private Gson gson;
    public API(){
        market = new Market("elibenshimol6@gmail.com", "123Aaa");
        gson = new Gson();
        actionStrings = new HashMap<>();
        getActionStrings();

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

    public Pair<Boolean, JSONObject> getCart(int id){
        Response<List<? extends Information>> res = market.getCart(id);
        return fromResToPairList(res);
    }

    public Pair<Boolean, JSONObject> makePurchase(int userId , String accountNumber){
        Response<Receipt> res = market.makePurchase(userId, accountNumber);
        return fromResToPairInfo(res);
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
    public Pair<Boolean, JSONObject> changeMemberAttributes(int userId, String token, String newEmail, String oldPass, String newPass) {
        Response<String> res = market.changeMemberAttributes(userId, token, newEmail, oldPass, newPass);
        return fromResToPair(res);
    }
    public Pair<Boolean, JSONObject> openStore(int userId, String token, String name, String storeDescription, String img){
        Response<Integer> res = market.openStore(userId, token, name, storeDescription, img);
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
        Response<HashMap<Integer, ? extends Information>> res = market.checkReviews(userId, token, storeId);
        return fromResToPairHashMap(res, "reviewId", "review");
    }

    public Pair<Boolean, JSONObject> getStoreProducts(int storeId){
        Response<List<? extends  Information>> res = market.getStoreProducts(storeId);
        return fromResToPairList(res);
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
                                                     String img, String isActive){
        Response<String> res = market.changeStoreInfo(userId, token, storeId, name, desc, img, isActive);
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


    public Pair<Boolean, JSONObject> addManagerPermissions(int ownerId, String token, int userId,int storeId, List<String> permissionsIds)
    {
        List<String> actionStr = Information.fromStringToActionString(permissionsIds);
        List<Integer> permissions = new ArrayList<>();
        for(String str : actionStr)
            permissions.add(actionStrings.get(str));
        Response<String> res = market.addManagerPermissions(ownerId, token, userId, storeId, permissions);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeManagerPermissions(int ownerId, String token, int userId,int storeId, List<String> permissionsIds)
    {
        List<String> actionStr = Information.fromStringToActionString(permissionsIds);
        List<Integer> permissions = new ArrayList<>();
        for(String str : actionStr)
            permissions.add(actionStrings.get(str));
        Response<String> res = market.removeManagerPermissions(ownerId, token, userId, storeId, permissions);
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


    public Pair<Boolean, JSONObject> adminLogout(int adminId)
    {
        Response<String> res = market.adminLogout(adminId);
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

    //TODO: fix
    public Pair<Boolean, JSONObject> watchEventLog(int adminId, String token)
    {
        Response<List<? extends Information>> res = market.watchEventLog(adminId, token);
        return fromResToPairList(res);
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

    public Pair<Boolean, JSONObject> getNotification(int userId, String token) {
        Response<Notification> res =  market.getNotification(userId, token);
        return fromResToPairInfo(res);
    }

    public Pair<Boolean, JSONObject> watchMarketStatus(int adminId, String token)
    {
        Response<MarketInfo> res = market.watchMarketStatus(adminId, token);
        // TODO: cast this to json
        return fromResToPairInfo(res);
    }

    public Pair<Boolean, JSONObject> sendNotification(int userId, String token, String username, String notification) {
        Response<String> res = market.sendNotification(userId, token, NotificationOpcode.CHAT_MESSAGE, username, notification);
        return fromResToPair(res);
    }

    //for actions to actionString
    private void getActionStrings(){
        actionStrings.put(Action.addProduct.toString(), 0);
        actionStrings.put(Action.removeProduct.toString(), 1);
        actionStrings.put(Action.updateProduct.toString(), 2);
        actionStrings.put(Action.changeStoreDetails.toString(), 3);
        actionStrings.put(Action.changePurchasePolicy.toString(),4);
        actionStrings.put(Action.changeDiscountPolicy.toString(),5);
        actionStrings.put(Action.addPurchaseConstraint.toString(), 6);
        actionStrings.put(Action.addDiscountConstraint.toString(),7);
        actionStrings.put(Action.viewMessages.toString(),8);
        actionStrings.put(Action.answerMessage.toString(),9);
        actionStrings.put(Action.seeStoreHistory.toString(), 10);
        actionStrings.put(Action.seeStoreOrders.toString(), 11);
        actionStrings.put(Action.checkWorkersStatus.toString(), 12);
        actionStrings.put(Action.appointManager.toString(), 13);
        actionStrings.put(Action.fireManager.toString(), 14);
        actionStrings.put(Action.appointOwner.toString(),15);
        actionStrings.put(Action.fireOwner.toString(),16);
        actionStrings.put(Action.changeManagerPermission.toString(),17);
        actionStrings.put(Action.closeStore.toString(), 18);
        actionStrings.put(Action.reopenStore.toString(), 19);
    }

    public void mockData() {
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
        res2 = market.addProduct(id2, token2, sid2, categories, "air2", "more comfy", 300, 10, "https://images.pexels.com/photos/4215840/pexels-photo-4215840.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        int pid2 = res2.getValue();
        market.addProductToCart(id1, sid1, pid1, 3);
        market.addProductToCart(id1, sid2, pid2, 5);
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
