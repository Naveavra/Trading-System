package market;

import utils.Order;
import service.UserController;
import service.payment.ProxyPayment;
import utils.*;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import domain.store.storeManagement.Store;
import service.MarketController;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: can remove admins but at least one has to be in the system.
//TODO: need to add those different functions: gettingInformationOnStore(int storeId), searchProduct(String name, ...),
//TODO: getProduct(int storeId, int productId), checkProductAvailability(int storeId, int productId, int quantity),
//TODO: addProduct(int userId, int storeId, int productId, int quantity), changeQuantity(int userId, int storeId, int productId, int quantity),
//TODO: gettingInformationOnProduct(int storeId, int productId), removeFromCart(int userId, int storeId, int productId),
//TODO: getCartContent(int userId), purchaseCart(int userId), openStore(int userId),
public class Market implements MarketInterface {
    private ConcurrentHashMap<Integer,Admin> activeAdmins;
    private ConcurrentHashMap<Integer,Admin> inActiveAdmins;
    private final UserController userController;
    private final MarketController marketController;
    //serveices
    ProxyPayment proxyPayment;
    private ConcurrentHashMap<Integer,Message> complaints; //complaintId,message
    private Gson gson ;
    private final Logger logger;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    AtomicInteger adminId = new AtomicInteger(-2);

    public Market(Admin admin) {
        activeAdmins = new ConcurrentHashMap<>();
        inActiveAdmins = new ConcurrentHashMap<>();
        activeAdmins.put(admin.getAdminId(), admin);
        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();
        gson = new Gson();
        proxyPayment = new ProxyPayment();
        complaints = new ConcurrentHashMap<>();

        admin.addControllers(userController, marketController);
    }


    @Override
    public Response<String> register(String email, String pass, String birthday) {
        try {
            userController.register(email, pass, birthday);
            logger.log(Logger.logStatus.Success, "user :" + email + " has successfully register on " + LocalDateTime.now());
            return new Response<String>("registaered successfully", null, null);
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
            return new Response<>("user add to cart successfully", null, null);
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
            int amount  = marketController.calculatePrice(cart);
            Receipt receipt = marketController.purchaseProducts(cart,userId ,amount);
            proxyPayment.makePurchase(accountNumber,amount);
            userController.purchaseMade(userId, receipt.getOrderId(), amount);
            logger.log(Logger.logStatus.Success,"user made purchase on "+ LocalDateTime.now());
            return new Response<String>(gson.toJson(receipt),null,null);
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
    public Response<Integer> login(String email, String pass, List<String> answers) {
        try{
            int memberId = userController.login(email,pass,answers);
            logger.log(Logger.logStatus.Success,"user" + email + "logged in successfully on "+ LocalDateTime.now());
            return new Response<Integer>(memberId,null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user cant get log in because " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"log in failed" , e.getMessage());
        }
    }

    //TODO need to add display notifications after login

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
            Message m = userController.sendQuestionToStore(userId, msg, storeId);
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
           Message m = userController.writeComplaintToMarket(orderId, storeId, msg, userId);
            complaints.put(m.getMessageId(),m);
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
            if(userController.checkPermission(userId, Action.changeStoreDescription, storeId)){
                marketController.setStoreDescription(storeId, description);
                logger.log(Logger.logStatus.Success,"user change store description successfully on "+ LocalDateTime.now());
                return new Response<String>("user change store description successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant change store description because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store description failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant change store description because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store description failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changePurchasePolicy(int userId, int storeId, String policy) {
        try{
            if(userController.checkPermission(userId, Action.changePurchasePolicy, storeId)){
                marketController.setStorePurchasePolicy(storeId,policy);
                logger.log(Logger.logStatus.Success,"user change store purchase policy successfully on "+ LocalDateTime.now());
                return new Response<String>("user change store purchase policy successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant change store purchase policy because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store purchase policy failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant change store purchase policy because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store policy purchase failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changeDiscountPolicy(int userId, int storeId, String policy) {
        try{
            if(userController.checkPermission(userId, Action.changeDiscountPolicy, storeId)){
                marketController.setStoreDiscountPolicy(storeId,policy);
                logger.log(Logger.logStatus.Success,"user change store policy discount successfully on "+ LocalDateTime.now());
                return new Response<String>("user change store discount policy successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant change store policy discount because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store policy discount failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant change store discount policy because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change store discount policy failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> addPurchaseConstraint(int userId, int storeId, String constraint) {
        try{
            if(userController.checkPermission(userId, Action.addPurchaseConstraint, storeId)){
                marketController.addPurchaseConstraint(storeId,constraint);
                logger.log(Logger.logStatus.Success,"user add purchase constraint successfully on "+ LocalDateTime.now());
                return new Response<String>("user add purchase constraint successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant add purchase constraint because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change add purchase constraint failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant add purchase constraint policy because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"add purchase constraint failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> fireManager(int userId, int storeId, int managerToFire) {
        try{
            if(userController.checkPermission(userId, Action.fireManager, storeId)){
                userController.fireManager(userId,managerToFire,storeId);
                logger.log(Logger.logStatus.Success,"user fire manager successfully on "+ LocalDateTime.now());
                return new Response<String>("user fire manager successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant fire manager because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change fire manager failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant fire manager because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"fire manager failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> checkWorkersStatus(int userId, int storeId) {
        try{
            if(userController.checkPermission(userId, Action.checkWorkersStatus, storeId)){
                String res = userController.getWorkersInformation(userId, storeId);
                logger.log(Logger.logStatus.Success,"user check worker status successfully on "+ LocalDateTime.now());
                return new Response<String>(res,null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant check worker status because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change check worker status failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant check worker status because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"check worker status failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> viewQuestions(int userId, int storeId) {
        try{
            if(userController.checkPermission(userId, Action.viewMessages, storeId)) {
                String res = marketController.getQuestions(storeId);
                logger.log(Logger.logStatus.Success, "user get questions successfully on " + LocalDateTime.now());
                return new Response<String>(res, null, null);
            }
            logger.log(Logger.logStatus.Fail,"cant get questions because: user dont have access on "+ LocalDateTime.now());
            return new Response<>(null,"get questions status failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant get questions because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get questions failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> answerQuestion(int userId, int storeId, int questionId, String answer) {
        try{
            if(userController.checkPermission(userId, Action.answerMessage, storeId)){
                marketController.answerQuestion(storeId,questionId,answer);
                logger.log(Logger.logStatus.Success,"user answer question successfully on "+ LocalDateTime.now());
                return new Response<String>("user answer question successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant answer question because: user dont have access on "+ LocalDateTime.now());
            return new Response<>(null,"get answer question failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant answer question because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"answer question failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> seeStoreHistory(int userId, int storeId) {
        try{
            if(userController.checkPermission(userId, Action.seeStoreHistory, storeId)) {
                String res = marketController.getStoreInformation(storeId);
                logger.log(Logger.logStatus.Success,"user get store history successfully on "+ LocalDateTime.now());
                return new Response<String>(res,null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant get store history  because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"answer question failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant  get store history  because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null," get store history failed" , e.getMessage());
        }
    }
    @Override
    public Response<String> addProduct(int userId,int storeId, List<String> categories, String name, String description, int price, int quantity) {
        try{
            if(userController.checkPermission(userId,Action.addProduct,storeId)){
                marketController.addProduct(storeId,name,description,price,quantity,categories);
            }
            else{
                logger.log(Logger.logStatus.Fail,"Product Addition Failed, Because: User doesn't have necessary permissions. on: " + LocalDateTime.now());
                return new Response<>(null,"Product Addition Failed","User doesn't have necessary permissions");
            }
            logger.log(Logger.logStatus.Success,"Add product successfull on: "+ LocalDateTime.now());
            return new Response<String>(null,null,null);
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail,"Cant add product because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"Product Addition Failed" , e.getMessage());
        }

    }
    //TODO miki
    @Override
    public Response<String> deleteProduct(int userId, int storeId, int productId) {
        try {
            if(userController.checkPermission(userId,Action.removeProduct,storeId)){
                marketController.deleteProduct(storeId,productId);
            }
        } catch (Exception e) {

        }
    }
    //TODO miki
    @Override
    public Response<String> updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity) {
        return null;
    }

    @Override
    public Response<HashMap<Integer,Order>> getStoreOrders(int userId, int storeId) {
       //get store orderHistory
        try {
            if(userController.checkPermission(userId, Action.seeStoreOrders, storeId)){
                String res = marketController.getStoreOrderHistory(storeId);
                HashMap<Integer,Order> orders  = new HashMap<>();
                orders = gson.fromJson(res ,orders.getClass());
                logger.log(Logger.logStatus.Success,"user get orders successfully on "+ LocalDateTime.now());
                return new Response<HashMap<Integer,Order>>(orders,null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant get store orders  because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get store orders failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant  get store orders   because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get store orders  failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> appointOwner(int userId, int storeId, int ownerId) {
        try 
        {
            userController.appointOwner(ownerId, userId, storeId);
            logger.log(Logger.logStatus.Success,"appointed user successfully on "+ LocalDateTime.now());
            return new Response<>("user appointManager successfully",null,null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant appoint Manager because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"appoint Manager failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> fireOwner(int userId, int storeId, int ownerId) {
        try{
            if(userController.checkPermission(userId, Action.fireOwner, storeId)){
                userController.fireManager(userId,ownerId,storeId);
                logger.log(Logger.logStatus.Success,"user fire owner successfully on "+ LocalDateTime.now());
                return new Response<>("user fire owner successfully",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant fire owner because: user dont have access"+ "on "+ LocalDateTime.now());
            return new Response<>(null,"change fire owner failed" , "user dont have access");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"cant fire owner because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"fire owner failed" , e.getMessage());
        }
    }

    @Override
    public Response<String> changeManagerPermission(int userId, int storeId, List<Integer> permissionsIds) {
        return null;
    }

    @Override
    public Response<String> getAppointments(int userId, int storeId) {
        try {
            if(userController.checkPermission(userId, Action.seeStoreHistory, storeId)){
                String res = marketController.getAppointments(storeId);

            }
        } catch (Exception e) {

        }
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
    public Response<String> closeStorePermanently(int adminId, int storeId) {
        if(adminId < 0){
            Admin admin = activeAdmins.get(adminId);
            if(admin != null){
                admin.closeStorePermanently(storeId);
            }
            else
                return new Response<>(null, "close permanent not successfully", "the id given is not of an admin");
        }
        else
            return new Response<>(null, "close permanent not successfully", "the id given is not of an admin");
    }

    @Override
    public Response<String> getStore(int storeId) {
        try {
            String store = marketController.getStoreInformation(storeId);
            logger.log(Logger.logStatus.Success,"user got  the store successfully on "+ LocalDateTime.now());
            return new Response<>(store,null,null);

        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"cant get store because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get store failed" , e.getMessage());
        }
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
        try {
            String stores = marketController.getStoresInformation();
            logger.log(Logger.logStatus.Success,"user got the stores successfully on "+ LocalDateTime.now());
            return new Response<>(stores,null,null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail,"cant get stores because: " + e.getMessage()+ "on "+ LocalDateTime.now());
            return new Response<>(null,"get store failed" , e.getMessage());
        }
    }


    @Override
    public Response<String> addAdmin(int userId, String email, String pass) {
       Admin memane = activeAdmins.get(userId);
       if(memane != null){
           for(Admin a :activeAdmins.values()){
                if(a.ge)
           }
           Admin a  = new Admin(adminId,name,pass);
       }
    }

    @Override
    public Response<String> removeAdmin(int userId, int adminId) {
        return null;
    }

    /**
     * return json of all the relevant information about the users: email, id, name
     */
    @Override
    public Response<String> getUsers(int adminId){
        try{
            Admin a = activeAdmins.get(adminId);
            if(a != null) {
                String users = userController.getUsersInformation();
                logger.log(Logger.logStatus.Success, "admin get users successfully on " + LocalDateTime.now());
                return new Response<String>(users, null, null);
            }
            return new Response<>(null, "can't get the users information", "the id given is not of admin");
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user failed getting all users because :" + e.getMessage());
            return new Response<String>(null,"get users", e.getMessage());
        }
    }
    public Response<String> answerComplaint(int adminId,int complaintId,String ans){
        try{
            Admin a = activeAdmins.get(adminId);
            if (a != null) {
                Message m = complaints.get(complaintId);
                if (m != null) {
                    m.sendFeedback(ans);
                    logger.log(Logger.logStatus.Success,"admin answer complaint successfully on "+ LocalDateTime.now());
                    return new Response<String>("admin answer complaint",null,null);
                }
                logger.log(Logger.logStatus.Fail,"cant get message to answer on"+ LocalDateTime.now());
                return new Response<String>(null,"answer complaint failed", "message does not found");
            }
        logger.log(Logger.logStatus.Fail,"cant get admin on"+ LocalDateTime.now());
        return new Response<String>(null,"answer complaint failed", "admin wasn't found");
    }   catch (Exception e){
            logger.log(Logger.logStatus.Fail,"user failed answer complaints because:"+e.getMessage()+" on"+ LocalDateTime.now());
            return new Response<String>(null,"answer complaint failed", e.getMessage());
        }
    }
    public Response cancelMembership(int adminId,int userToRemove){
        try{
            Admin a = activeAdmins.get(adminId);
            if(a!=null){
                userController.cancelMembership(userToRemove);
                logger.log(Logger.logStatus.Success,"admin cancel Membership successfully on "+ LocalDateTime.now());
                return new Response<String>("admin cancel Membership complaint",null,null);
            }
            logger.log(Logger.logStatus.Fail,"cant cancel Membership on"+ LocalDateTime.now());
            return new Response<String>(null,"cancel Membership failed", "admin wasnt found");
        }catch(Exception e){
            logger.log(Logger.logStatus.Fail,"user failed cancel Membership because:"+e.getMessage()+" on"+ LocalDateTime.now());
            return new Response<String>(null,"cancel Membership failed", e.getMessage());
        }
    }
    public Response sendNotification(int adminId,int userToSendTo,String msg){
        try{
            Admin a = activeAdmins.get(adminId);
            if(a!=null){
                Notification<String> n = new Notification<String>(msg);
                userController.addNotification(userToSendTo,n);
            }
        }catch (Exception e){

        }
    }
.}
