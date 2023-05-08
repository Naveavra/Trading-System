package server;

import market.Admin;
import market.Market;
import org.json.JSONObject;
import utils.Pair;
import utils.ProductInfo;
import utils.marketRelated.Response;
import utils.messageRelated.Message;
import utils.userInfoRelated.Info;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class API {
    public Market market;
    public API(){
        Admin admin = new Admin(-1, "elibenshimol6@gmail.com", "123Aaa");
        market = new Market(admin);
    }

    public Pair<Boolean, JSONObject> fromResToPair(Response res){
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
            json.put("value", res.getValue());
            return new Pair<>(true, json);
        }
    }

    public Pair<Boolean, JSONObject> enterGuest(){
        Response<Integer> res = market.enterGuest();
        return fromResToPair(res);
    }

    public Pair<Boolean, JSONObject> register(String email, String password, String birthday){
        Response<String> res = market.register(email, password, birthday);
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

    public Pair<Boolean, JSONObject> checkWorkerStatus(int userId, int workerId, int storeId){
        Response<Info> res = market.checkWorkerStatus(userId, workerId, storeId);
        JSONObject json = new JSONObject();
        if(res.errorOccurred())
        {
            json.put("errorMsg", res.getErrorMessage());
            return new Pair<>(false, json);
        }
        else {
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
