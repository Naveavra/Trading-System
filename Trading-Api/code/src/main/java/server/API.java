package server;

import com.google.gson.Gson;
import market.Admin;
import market.Market;
import org.json.JSONObject;
import utils.Pair;
import utils.ProductInfo;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import utils.LoginInformation;
import utils.StoreInfo;
import utils.marketRelated.Response;
import utils.messageRelated.Message;
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

//    public Response answerQuestion(int userId,int storeId ,int questionId,String answer);
//    public Response seeStoreHistory(int userId,int storeId);
//    public Response addProduct(int useIid, int storeId,List<String> categories, String name , String description , int price , int quantity);
//    public Response deleteProduct(int userId,int storeId,int productId);
//    public Response updateProduct(int userId, int storeId,int productId, List<String> categories, String name , String description , int price , int quantity);
//    //public Response getStoreOrders(int userId , int storeId);
//
//    public Response getAppointments(int userId, int storeId);
//
//    //store methods
//    //todo: decide if getStore will bring every thing togheter , products , orders , ..statistics
//    //public Response getStore(int storeId);
//
//    // admin methods
//    public Response adminLogin(String email ,String pass);
//    public Response adminLogout(int adminId);
//    public Response getAdmins(int adminId);
//    public Response getStores();
//    public Response addAdmin(int userId, String email , String pass);
//    public Response removeAdmin(int adminId);
//    public Response getUsersPurchaseHistory(int buyerId);
//    public Response answerComplaint(int adminId,int complaintId,String ans);
//    public Response cancelMembership(int adminId,int userToRemove);
//    public Response watchLog(int adminId);
//
//    public Response watchMarketStatus(int adminId);

//    public Pair<Boolean, JSONObject> viewQuestions(int userId, int storeId){
//        Response<HashMap<Integer, Message>> res = market.viewQuestions(userId, storeId);
//        JSONObject json = new JSONObject();
//        if(res.errorOccurred())
//        {
//            json.put("errorMsg", res.getErrorMessage());
//            return new Pair<>(false, json);
//        }
//        else {
//            json.put("value", questionsToJson(res.getValue()));
//            return new Pair<>(true, json);
//        }
//    }
//    public Response viewQuestions(int userId,int storeId);



}
