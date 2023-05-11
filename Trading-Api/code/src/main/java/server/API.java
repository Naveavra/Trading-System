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
    public Pair<Boolean,JSONObject> logout(int userId, String token){
        Response<String> res = market.logout(userId, token);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> checkSecurityQuestions(int userId, String token, List<String> answers){
        Response<String> res = market.checkSecurityQuestions(userId, token, answers);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addSecurityQuestion(int userId, String token, String question, String answer){
        Response<String> res = market.addSecurityQuestion(userId, token, question, answer);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeAnswerForLoginQuestion(int userId, String token, String question, String answer){
        Response<String> res = market.changeAnswerForLoginQuestion(userId, token, question, answer);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> removeSecurityQuestion(int userId, String token, String question){
        Response<String> res = market.removeSecurityQuestion(userId, token, question);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> displayNotifications(int userId, String token){
        Response<List<String>> res = market.displayNotifications(userId, token);
        return fromResToPair(res);
    }

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

    public Pair<Boolean, JSONObject> openStore(int userId, String token, String storeDescription){
        Response<Integer> res = market.openStore(userId, token, storeDescription);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getMemberInformation(int userId, String token){
        Response<Info> res = market.getMemberInformation(userId, token);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> getUserPurchaseHistory(int userId, String token, int buyerId){
        //               orderId,         storeId,       productId, quantity
        Response<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> res = market.getUserPurchaseHistory(userId, token, buyerId);
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


//TODO: add from manager methods

    public Pair<Boolean, JSONObject> appointManager(int userId, String token, int managerIdToAppoint, int storeId){
        Response<String> res = market.appointManager(userId, token, managerIdToAppoint, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> changeStoreDescription(int userId, String token, int storeId, String description){
        Response<String> res = market.changeStoreDescription(userId, token, storeId, description);
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

    public Pair<Boolean, JSONObject> checkWorkerStatus(int userId, String token, int workerId, int storeId) {
        Response<Info> res = market.checkWorkerStatus(userId, token, workerId, storeId);
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


    public Pair<Boolean, JSONObject> checkWorkersStatus(int userId, String token, int workerId){
        Response<List<Info>> res = market.checkWorkersStatus(userId, token, workerId);
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

    public Pair<Boolean, JSONObject> closeStore(int userId, String token, int storeId){
        Response<String> res = market.closeStore(userId, token, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> reopenStore(int userId, String token, int storeId){
        Response<String> res = market.reopenStore(userId, token, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addProduct(int userId, String token, int storeId, List<String> categories, String name , String description , int price , int quantity){
        Response<Integer> res = market.addProduct(userId, token, storeId, categories, name, description, price, quantity);
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

    public Pair<Boolean, JSONObject> closeStorePermanently(int adminId, String token, int storeId){
        Response<String> res = market.closeStorePermanently(adminId, token, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> appointOwner(int userId, String token, int ownerId, int storeId)
    {
        Response<String> res = market.appointOwner(userId, token, ownerId, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> fireOwner(int userId, String token, int ownerId, int storeId)
    {
        Response<String> res = market.fireOwner(userId, token, ownerId, storeId);
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> addManagerPermission(int ownerId, String token, int userId,int storeId, int permissionsId)
    {
        Response<String> res = market.addManagerPermission(ownerId, token, userId, storeId, permissionsId);
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

    public Pair<Boolean, JSONObject> removeManagerPermission(int ownerId, String token, int userId,int storeId, int permissionsId)
    {
        Response<String> res = market.removeManagerPermission(ownerId, token, userId, storeId, permissionsId);
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
