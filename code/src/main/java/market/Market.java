package market;

import service.UserController;
import utils.Logger;
import utils.Response;
import com.google.gson.Gson;
import java.time.LocalDateTime;

import service.MarketController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: can remove admins but at least one has to be in the system.
//TODO: need to add those different functions: gettingInformationOnStore(int storeId), searchProduct(String name, ...),
//TODO: getProduct(int storeId, int productId), checkProductAvailability(int storeId, int productId, int quantity),
//TODO: addProduct(int userId, int storeId, int productId, int quantity), changeQuantity(int userId, int storeId, int productId, int quantity),
//TODO: gettingInformationOnProduct(int storeId, int productId), removeFromCart(int userId, int storeId, int productId),
//TODO: getCartContent(int userId), purchaseCart(int userId), openStore(int userId),
public class Market implements MarketInterface {
    private ConcurrentLinkedDeque<Admin> admins;
    private UserController uc = new UserController();
    private MarketController mc = new MarketController();
    private Logger logger = Logger.getInstance();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Market(Admin admin) {
        admins = new ConcurrentLinkedDeque<>();
        admins.add(admin);
    }


    @Override
    public Response<String> register(String email, String pass, String birthday) {
        try {
            uc.register(email, pass, birthday);
            logger.log(Logger.logStatus.Success, "user :" + email + " has successfully register on " + LocalDateTime.now());
            return new Response<String>("user register successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant register because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "register failed", e.getMessage());
        }
    }

    //for each function of guest there will be 2 function , owns get an id and the other gets userName
    @Override
    public Response<String> addProductToCart(int userId, int storeId, int productId, int quantity) {
        try{
            uc.addProductToCart(userId, storeId,productId,quantity);
            String name = uc.getUserName(userId);
            String productName = mc.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + "add " + quantity +" "+ productName +" to shopping cart on " + LocalDateTime.now());
            return new Response<String>("user add to cart successfully",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant add product To Cart because " +e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"add product to cart failed" ,e.getMessage());
        }
    }


    @Override
    public Response<String> addProductToCart(String name, int storeId, int productId, int quantity) {
        try{
            uc.addProductToCart(name, storeId,productId,quantity);
            String productName = mc.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + "add " + quantity +" "+ productName +" to shopping cart on " + LocalDateTime.now());
            return new Response<String>("user add to cart successfully",null,null);
          } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant add product To Cart because " +e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"add product to cart failed" ,e.getMessage());
         }
    }

    @Override
    public Response<String> removeProductFromCart(int userId , int storeId, int productId) {
        try{
            uc.removeProductFromCart(userId, storeId,productId);
            String name = uc.getUserName(userId);
            String productName = mc.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + "remove "+ productName +" from shopping cart on " + LocalDateTime.now());
            return new Response<String>("user remove "+ productName +" from cart successfully",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant remove product from Cart because " +e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"remove product from cart failed" , e.getMessage());
        }
    }
    public Response<String> removeProductFromCart(String name , int storeId, int productId) {
        try{
            uc.removeProductFromCart(name, storeId,productId);
            String productName = mc.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + "remove "+ productName +" from shopping cart on " + LocalDateTime.now());
            return new Response<String>("user remove "+ productName +" from cart successfully",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant remove product from Cart because " +e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"remove product from cart failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changeQuantityInCart(int userId, int storeId, int productId, int change) {
        try{
            uc.changeQuantityInCart(userId, storeId,productId,change);
            String name = uc.getUserName(userId);
            String productName = mc.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + " change quantity to "+ change +" on shopping cart on " + LocalDateTime.now());
            return new Response<String>("user change Quantity of "+productName+" In Cart to "+change +" successfully ",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant remove change quantity in cart because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"remove product from cart failed" , e.getMessage());
        }
    }
    public Response<String> changeQuantityInCart(String name, int storeId, int productId, int change) {
        try{
            uc.changeQuantityInCart(name, storeId,productId,change);
            String productName = mc.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + " change quantity to "+ change +" on shopping cart on " + LocalDateTime.now());
            return new Response<String>("user change Quantity of "+productName+" In Cart to "+change +" successfully ",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant remove change quantity in cart because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"remove product from cart failed" , e.getMessage());
        }
    }


    public Response<String> getCart( int id) {
        try{
            Gson gson = new Gson();
            String cart = gson.toJson(uc.getUserCart(id));
            String name = uc.getUserName(id);
            logger.log(Logger.logStatus.Success,"user" + name + "ask for his cart on "+ LocalDateTime.now());
            return new Response<String>(cart,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant get his cart because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get cart failed" , e.getMessage());
        }
    }
    @Override
    public Response<String> getCart( String userName) {
        try{
            Gson gson = new Gson();
            String cart = gson.toJson(uc.getUserCart(userName));
            logger.log(Logger.logStatus.Success,"user" + userName + "ask for his cart on "+ LocalDateTime.now());
            return new Response<String>(cart,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant get his cart because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get cart failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> buy(int userId) {
//        try {
//            HashMap<Integer, HashMap<Integer, Integer>> cart = uc.getUserCart(userId);
//            String recipt = mc.purchase(userId,cart);
//        } catch (Exception e) {
//
//        }
        return null;
    }
    public Response<String> buy(String name) {
//        try {
//            HashMap<Integer, HashMap<Integer, Integer>> cart = uc.getUserCart(name);
//            mc.purchase(name,cart);
//        } catch (Exception e) {
//
//        }
//        pair<info, cart> = uc.getUserCart(userId);
//        String recipt = mc.purchase(info, cart);
        return null;
    }

    @Override
    public Response<String> login(String email, String pass) {
        try{
            uc.login(email,pass,new ArrayList<String>());
            logger.log(Logger.logStatus.Success,"user" + email + "logged in successfully on "+ LocalDateTime.now());
            return new Response<String>(" u logged successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant get log in because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"log in failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> logout(int userId) {
        try{
            uc.logout(userId);
            logger.log(Logger.logStatus.Success,"user log out successfully on "+ LocalDateTime.now());
            return new Response<String>("goodbye",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant get log out because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"log out failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changeDetails(int userId, String name, String email, String birthday) {
        try{
            uc.changeDetails(userId,name,email,birthday);
            logger.log(Logger.logStatus.Success,"user" + email + "changed details successfully on "+ LocalDateTime.now());
            return new Response<String>(" u changed details successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant change details because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change details failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changePassword(int userId, String oldPass, String newPass) {
        try{
            uc.changePassword(userId,oldPass,newPass);
            String name = uc.getUserName(userId);
            logger.log(Logger.logStatus.Success,"user" + name + "changed password successfully on "+ LocalDateTime.now());
            return new Response<String>(" u changed details successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant change password because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change password failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> openStore(int userId,String des) {
        try{
            mc.openStore(userId ,des);
            String name = uc.getUserName(userId);
            logger.log(Logger.logStatus.Success,"user" + name + "open store successfully on "+ LocalDateTime.now());
            return new Response<String>("u open store store successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant open store  because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"open store failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> getMember(int userId) {
        try{
            String user = uc.getUser(userId);
            logger.log(Logger.logStatus.Success,"user received successfully on "+ LocalDateTime.now());
            return new Response<String>(user,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant received because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get user failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> getUserOrders(int userId) {
        try{
            String orders = mc.getUserOrders(userId);
            logger.log(Logger.logStatus.Success,"user received orders successfully on "+ LocalDateTime.now());
            return new Response<String>(orders,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get user orders because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get user orders failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> writeReviewToStore(int orderId, int storeId, String content, int grading, int userId) {
        return null;
    }

    @Override
    public Response<String> writeReviewToProduct(int orderId, int storeId, int productId, String content, int grading, int userId) {
        return null;
    }

    @Override
    public Response<String> getProductInformation(int userId, int storeId, int producId) {
        return null;
    }

    @Override
    public Response<String> getStoreInformation(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> rateStore(int userId, int storeId, int rating) {
        return null;
    }

    @Override
    public Response<String> rateProduct(int userId, int storeId, int productId, int rating) {
        return null;
    }

    @Override
    public Response<String> sendQuestion(int userId, int storeId, String msg) {
        return null;
    }

    @Override
    public Response<String> sendComplaint(int userId, int storeId, String msg) {
        return null;
    }

    @Override
    public Response<String> sell(int userId, int storeId, int orderId) {
        return null;
    }

    @Override
    public Response<String> appointManager(int userId, int storeId, int managerIdToAppoint) {
        return null;
    }

    @Override
    public Response<String> changeStoreDescription(int userId, int storeId, String description) {
        return null;
    }

    @Override
    public Response<String> changePurchasePolicy(int userId, int storeId, String policy) {
        return null;
    }

    @Override
    public Response<String> changeDiscountPolicy(int userId, int storeId, String policy) {
        return null;
    }

    @Override
    public Response<String> addPurchaseConstraint(int userId, int storeId, String constraint) {
        return null;
    }

    @Override
    public Response<String> fireManager(int userId, int storeId, int managerToFire) {
        return null;
    }

    @Override
    public Response<String> checkWorkersStatus(int userId, int storeId, int workerId) {
        return null;
    }

    @Override
    public Response<String> viewQuestions(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> answerQuestion(int userId, int storeId, int questionId, String answer) {
        return null;
    }

    @Override
    public Response<String> seeStoreHistory(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> addProduct(int useIid, List<String> categories, String name, String description, int price, int quantity) {
        return null;
    }

    @Override
    public Response<String> deleteProduct(int userId, int storeId, int productId) {
        return null;
    }

    @Override
    public Response<String> updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity) {
        return null;
    }

    @Override
    public Response<String> getStoreOrders(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> addOwner(int userId, int storeId, int ownerId) {
        return null;
    }

    @Override
    public Response<String> fireOwner(int userId, int storeId, int ownerId) {
        return null;
    }

    @Override
    public Response<String> changeManagerPermission(int userId, int storeId, List<Integer> permissionsIds) {
        return null;
    }

    @Override
    public Response<String> getAppointments(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> closeStore(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> reopenStore(int userId, int storeId) {
        return null;
    }

    @Override
    public Response<String> getStore(int storeId) {
        return null;
    }

    @Override
    public Response<String> getProducts(int storeId) {
        return null;
    }

    @Override
    public Response<String> getAdmins() {
        return null;
    }

    @Override
    public Response<String> getStores() {
        return null;
    }


    @Override
    public Response<String> addAdmin(int userId, String name, String pass) {
        return null;
    }

    @Override
    public Response<String> removeAdmin(int userId, int adminId) {
        return null;
    }
    public Response<String> getUsers(int userId){
        try{
            String users = uc.getAllUsers(userId);
            logger.log(Logger.logStatus.Success,"admin get users successfully on "+ LocalDateTime.now());
            return new Response<String>(users,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user failed getting all users because :" + e.getMessage());
            return new Response<String>(null,"get users", e.getMessage());
        }
    }
}
