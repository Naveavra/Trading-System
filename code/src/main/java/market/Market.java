package market;

import service.UserController;
import utils.Logger;
import utils.Response;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import domain.store.storeManagement.Store;
import service.MarketController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: can remove admins but at least one has to be in the system.
//TODO: need to add those different functions: gettingInformationOnStore(int storeId), searchProduct(String name, ...),
//TODO: getProduct(int storeId, int productId), checkProductAvailability(int storeId, int productId, int quantity),
//TODO: addProduct(int userId, int storeId, int productId, int quantity), changeQuantity(int userId, int storeId, int productId, int quantity),
//TODO: gettingInformationOnProduct(int storeId, int productId), removeFromCart(int userId, int storeId, int productId),
//TODO: getCartContent(int userId), purchaseCart(int userId), openStore(int userId),
public class Market implements MarketInterface {
    private final ConcurrentLinkedDeque<Admin> admins;
    private final UserController userController;
    private final MarketController marketController;
    private final Logger logger;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Market(Admin admin) {
        admins = new ConcurrentLinkedDeque<>();
        admins.add(admin);
        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();
    }


    @Override
    public Response<String> register(String email, String pass, String birthday) {
        try {
            userController.register(email, pass, birthday);
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
            userController.addProductToCart(userId, storeId,productId,quantity);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId , productId);
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
            userController.addProductToCart(name, storeId,productId,quantity);
            String productName = marketController.getProductName(storeId , productId);
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
            userController.removeProductFromCart(userId, storeId,productId);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + "remove "+ productName +" from shopping cart on " + LocalDateTime.now());
            return new Response<String>("user remove "+ productName +" from cart successfully",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant remove product from Cart because " +e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"remove product from cart failed" , e.getMessage());
        }
    }
    public Response<String> removeProductFromCart(String name , int storeId, int productId) {
        try{
            userController.removeProductFromCart(name, storeId,productId);
            String productName = marketController.getProductName(storeId , productId);
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
            userController.changeQuantityInCart(userId, storeId,productId,change);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId , productId);
            logger.log(Logger.logStatus.Success,"user " + name + " change quantity to "+ change +" on shopping cart on " + LocalDateTime.now());
            return new Response<String>("user change Quantity of "+productName+" In Cart to "+change +" successfully ",null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant remove change quantity in cart because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"remove product from cart failed" , e.getMessage());
        }
    }
    public Response<String> changeQuantityInCart(String name, int storeId, int productId, int change) {
        try{
            userController.changeQuantityInCart(name, storeId,productId,change);
            String productName = marketController.getProductName(storeId , productId);
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
            String cart = gson.toJson(userController.getUserCart(id));
            String name = userController.getUserName(id);
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
            String cart = gson.toJson(userController.getUserCart(userName));
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
            userController.login(email,pass,new ArrayList<String>());
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
            userController.logout(userId);
            logger.log(Logger.logStatus.Success,"user log out successfully on "+ LocalDateTime.now());
            return new Response<String>("goodbye",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant get log out because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"log out failed" , e.getMessage());
        }
    }
    @Override
    public Response<String> changeName(int userId, String newUserName){
        try{
            userController.changeUserName(userId, newUserName);
            String name = userController.getUserName(userId);
            logger.log(Logger.logStatus.Success,"user" + name + "changed name successfully on "+ LocalDateTime.now());
            return new Response<String>(" u changed details successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant change name because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change name failed" , e.getMessage());
        }
    }
    @Override
    public Response<String> changeEmail(int userId, String newEmail){
        try{
            userController.changeUserEmail(userId, newEmail);
            String name = userController.getUserName(userId);
            logger.log(Logger.logStatus.Success,"user" + name + "changed email successfully on "+ LocalDateTime.now());
            return new Response<String>(" u changed details successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant change email because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change email failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changePassword(int userId, String oldPass, String newPass) {
        try{
            userController.changeUserPassword(userId,oldPass,newPass);
            String name = userController.getUserName(userId);
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
            if(userController.canOpenStore(userId)) {
                Store store = marketController.openStore(userId, des);
                userController.openStore(userId, store);
                String name = userController.getUserName(userId);
                logger.log(Logger.logStatus.Success, "user" + name + "open store successfully on " + LocalDateTime.now());
                return new Response<>("u open store store successfully", null, null);
            }
            else {
                return new Response<>(null, "open store failed","user is not allowed to open store");
            }
            }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant open store  because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"open store failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> getMemberInformation(int userId) {
        try{
            String user = userController.getUserInformation(userId);
            logger.log(Logger.logStatus.Success,"user received successfully on "+ LocalDateTime.now());
            return new Response<String>(user,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant received because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get user failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> getUserPurchaseHistory(int userId) {
        try{
            String orders = userController.getUserPurchaseHistory(userId);
            logger.log(Logger.logStatus.Success,"user received orders successfully on "+ LocalDateTime.now());
            return new Response<String>(orders,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get user orders because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get user orders failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> writeReviewToStore(int orderId, int storeId, String content, int grading, int userId) {
        return null;}

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
    public Response<String> closeStorePermanetly(int adminId, int storeId) {
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

    /**
     * return json of all the relevant information about the users: email, id, name
     */
    public Response<String> getUsers(){
        try{
            String users = userController.getAllUserNames();
            logger.log(Logger.logStatus.Success,"admin get users successfully on "+ LocalDateTime.now());
            return new Response<String>(users,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user failed getting all users because :" + e.getMessage());
            return new Response<String>(null,"get users", e.getMessage());
        }
    }
}
