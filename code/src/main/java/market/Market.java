package market;

import domain.store.order.Order;
import service.UserController;
import service.payment.ProxyPayment;
import utils.Action;
import utils.Logger;
import utils.Message;
import utils.Response;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import domain.store.storeManagement.Store;
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
    private final ConcurrentLinkedDeque<Admin> admins;
    private final UserController userController;
    private final MarketController marketController;
    //serveices
    ProxyPayment proxyPayment;
    Gson gson ;
    private final Logger logger;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Market(Admin admin) {
        admins = new ConcurrentLinkedDeque<>();
        admins.add(admin);
        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();
        gson = new Gson();
        proxyPayment = new ProxyPayment();
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


    public Response<String> getCart(int id) {
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
    public Response<String> makePurchase(int userId,String accountNumber) {
        try {
            HashMap<Integer, HashMap<Integer, Integer>> h = new HashMap<>();
            HashMap<Integer, HashMap<Integer, Integer>> cart = gson.fromJson(userController.getUserCart(userId), h.getClass());
            int amont  = marketController.caclulatePrice(cart);
            String order = marketController.purchaseProducts(cart,userId ,amont);
            proxyPayment.makePurchase(accountNumber,amont);
            logger.log(Logger.logStatus.Success,"user made purchase on "+ LocalDateTime.now());
            return new Response<String>(order,null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"user cant make purchase " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"make purchase failed" , e.getMessage());
        }
    }
    public Response<String> makePurchase(String name,String accountNumber) {
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
        try{
            Message m = userController.writeReviewForStore(orderId,storeId,content,grading,userId);
            marketController.addReviewToStore(storeId, orderId, m);
            logger.log(Logger.logStatus.Success,"user write review on store successfully on "+ LocalDateTime.now());
            return new Response<String>("user write review on store successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant write review on store because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"write review failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> writeReviewToProduct(int orderId, int storeId, int productId, String content, int grading, int userId) {
        try{
            Message m = userController.writeReviewForProduct(orderId,storeId,productId,content,grading,userId);
             marketController.writeReviewForProduct(m);
            logger.log(Logger.logStatus.Success,"user write review on product successfully on "+ LocalDateTime.now());
            return new Response<String>("user write review successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant write review to product because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"write review failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> getProductInformation(int storeId, int productId) {
       try{
           String res = marketController.getProductInformation(storeId,productId);
           logger.log(Logger.logStatus.Success,"user get product information successfully on "+ LocalDateTime.now());
           return new Response<String>(res,null,null);
       }catch (Exception e){
           logger.log(Logger.logStatus.Fail,"cant get product information because: " + e.getMessage()+ "on "+ LocalDateTime.now());
           return new Response<>(null,"get product information failed" , e.getMessage());
       }
    }


    @Override
    public Response<String> getStoreInformation(int storeId) {
        try{
            String res = marketController.getStoreInformation(storeId);
            logger.log(Logger.logStatus.Success,"user get store information successfully on "+ LocalDateTime.now());
            return new Response<String>(res,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get store information because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get product information failed" , e.getMessage());
        }
    }

    public Response<String> getStoreDescription(int storeId)
    {
        try{
            String res = marketController.getStoreDescription(storeId);
            logger.log(Logger.logStatus.Success,"user get store information successfully on "+ LocalDateTime.now());
            return new Response<String>(res,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get store information because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get product information failed" , e.getMessage());
        }
    }
    public Response<String> getStoreProducts(int storeId){
        try{
            String res = marketController.getStoreProducts(storeId);
            logger.log(Logger.logStatus.Success,"user get store products successfully on "+ LocalDateTime.now());
            return new Response<String>(res,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get store products because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get products failed" , e.getMessage());
        }
    }


    //TODO implement store questions
    @Override
    public Response<String> sendQuestion(int userId, int storeId, String msg) {
        try {
            Message m = userController.sendQuestion(userId,storeId,msg);
            marketController.sendQuestion(storeId,m);
            logger.log(Logger.logStatus.Success,"user send question successfully on "+ LocalDateTime.now());
            return new Response<String>("question added successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant send information because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"send information failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> sendComplaint(int userId,int orderId, int storeId, String msg) {
        try {
           Message m = userController.sendComplaint(userId,orderId, storeId, msg);
            for(Admin a :admins){
                a.addComplaint(m);
            }
            logger.log(Logger.logStatus.Success,"user send complaint successfully on "+ LocalDateTime.now());
            return new Response<String>("user send complaint successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant send complaint because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"send Complaint failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> appointManager(int userId, int storeId, int managerIdToAppoint) {
        try{
            userController.appointManager(userId,managerIdToAppoint,storeId);
            logger.log(Logger.logStatus.Success,"user appoint " +managerIdToAppoint + "to Manager in: "+storeId+ " successfully on "+ LocalDateTime.now());
            return new Response<String>("user appointManager successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant appoint Manager because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"appoint Manager failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreDescription(int userId, int storeId, String description) {
        try{
            userController.checkAccess(userId,storeId, Action.changeStoreDescription);
            marketController.setStoreDescription(storeId, description);
            logger.log(Logger.logStatus.Success,"user change store description successfully on "+ LocalDateTime.now());
            return new Response<String>("user change store description successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant change store description because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store description failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changePurchasePolicy(int userId, int storeId, String policy) {
        try{
            userController.checkAccess(userId,storeId, Action.changePurchasePolicy);
            marketController.setStorePurchasePolicy(storeId,policy);
            logger.log(Logger.logStatus.Success,"user change store purchase policy successfully on "+ LocalDateTime.now());
            return new Response<String>("user change store purchase policy successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant change store purchase policy because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store policy purchase failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changeDiscountPolicy(int userId, int storeId, String policy) {
        try{
            userController.checkAccess(userId,storeId, Action.changeDiscountPolicy);
            marketController.setStoreDiscountPolicy(storeId,policy);
            logger.log(Logger.logStatus.Success,"user change store policy discount successfully on "+ LocalDateTime.now());
            return new Response<String>("user change store discount policy successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant change store discount policy because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store discount policy failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> addPurchaseConstraint(int userId, int storeId, String constraint) {
        try{
            userController.checkAccess(userId,storeId, Action.addPurchaseConstraint);
            marketController.addPurchaseConstraint(storeId,constraint);
            logger.log(Logger.logStatus.Success,"user purchase constraint successfully on "+ LocalDateTime.now());
            return new Response<String>("user purchase constraint successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant purchase constraint policy because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"purchase constraint failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> fireManager(int userId, int storeId, int managerToFire) {
        try{
            userController.checkAccess(userId,storeId, Action.fireManager);
            userController.fireManager(userId,managerToFire,storeId);
            logger.log(Logger.logStatus.Success,"user fire manager successfully on "+ LocalDateTime.now());
            return new Response<String>("user fire manager successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant fire manager because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"fire manager failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> checkWorkersStatus(int userId, int storeId, int workerId) {
        try{
            userController.checkAccess(userId,storeId, Action.checkWorkersStatus);
            String res = userController.getWorkerStatus(userId, storeId,workerId);
            logger.log(Logger.logStatus.Success,"user check worker status successfully on "+ LocalDateTime.now());
            return new Response<String>("user check worker status successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant check worker status because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"check worker status failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> viewQuestions(int userId, int storeId) {
        try{
            userController.checkAccess(userId,storeId, Action.viewMessages);
            String res = userController.getQuestions(userId, storeId);
            logger.log(Logger.logStatus.Success,"user check worker status successfully on "+ LocalDateTime.now());
            return new Response<String>(res,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get questions because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get questions failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> answerQuestion(int userId, int storeId, int questionId, String answer) {
        try{
            userController.checkAccess(userId,storeId, Action.answerMessage);
            userController.answerQuestion(userId, storeId,questionId,answer);
            logger.log(Logger.logStatus.Success,"user answer question successfully on "+ LocalDateTime.now());
            return new Response<String>("user answer question successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant answer question because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"answer question failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> seeStoreHistory(int userId, int storeId) {
        try{
            userController.checkAccess(userId,storeId, Action.seeStoreHistory);
            //todo: bring only store history;
            String res = marketController.getStoreInformation(storeId);
            logger.log(Logger.logStatus.Success,"user answer question successfully on "+ LocalDateTime.now());
            return new Response<String>("user answer question successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant answer question because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"answer question failed" , e.getMessage());
        }
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
    public Response<String> appointOwner(int userId, int storeId, int ownerId) {
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
            String users = userController.getUsersInfo();
            logger.log(Logger.logStatus.Success,"admin get users successfully on "+ LocalDateTime.now());
            return new Response<String>(users,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user failed getting all users because :" + e.getMessage());
            return new Response<String>(null,"get users", e.getMessage());
        }
    }
}
