package market;

import domain.store.storeManagement.AppHistory;
import domain.user.PurchaseHistory;
import domain.user.ShoppingCart;
import org.json.JSONObject;
import service.security.UserAuth;
import service.supplier.ProxySupplier;
import service.UserController;
import service.payment.ProxyPayment;
import utils.*;

import java.time.LocalDateTime;
import domain.store.storeManagement.Store;
import service.MarketController;
import utils.infoRelated.*;
import utils.Response;
import utils.messageRelated.Message;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;
import utils.infoRelated.Receipt;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;


//TODO: find a way to generate tokens, hash passwords, ...

public class Market implements MarketInterface {
    private ConcurrentHashMap<Integer, Admin> admins;
    private final UserController userController;
    private final MarketController marketController;
    //services
    private ProxyPayment proxyPayment;
    private ProxySupplier proxySupplier;
    private UserAuth userAuth;
    private ConcurrentHashMap<Integer, Message> complaints; //complaintId,message
    private final Logger logger;
    private AtomicInteger ids;

    private HashMap<Integer, Action> actionIds;

    private MarketInfo marketInfo;

    public Market(String email, String pass) {
        admins = new ConcurrentHashMap<>();

        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();

        proxyPayment = new ProxyPayment("Google Pay");
        userAuth = new UserAuth();
        proxySupplier = new ProxySupplier("DHL");

        complaints = new ConcurrentHashMap<>();
        marketInfo = new MarketInfo();

        actionIds = new HashMap<>();
        setActionIds();

        ids = new AtomicInteger(1);
        addAdmin(0, null, email, pass);
    }


    @Override
    public Response<Integer> enterGuest() {
        int id = ids.getAndIncrement();
        int guestId = userController.enterGuest(id);
        marketInfo.addUserCount();
        return logAndRes(Logger.logStatus.Success, "guest :" + guestId + " has successfully entered on " + LocalDateTime.now(),
                guestId, null, null);
    }

    @Override
    public Response<String> exitGuest(int guestId) {
        try {
            userController.exitGuest(guestId);
            marketInfo.reduceUserCount();
            return logAndRes(Logger.logStatus.Success, "guest :" + guestId + " has successfully exited on " + LocalDateTime.now(),
                    "existed successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant exit because " + e.getMessage() + " on " + LocalDateTime.now(),
                    null, "exit failed", e.getMessage());
        }
    }

    @Override
    public Response<String> register(String email, String pass, String birthday) {
        try {
            String hashedPassword = userAuth.hashPassword(email, pass, true);
            userController.checkPassword(pass);
            int id = ids.getAndIncrement();
            userController.register(id, email, hashedPassword, birthday);
            marketInfo.addRegisteredCount();
            return logAndRes(Logger.logStatus.Success, "user :" + email + " has successfully register on " + LocalDateTime.now(),
                    "registered successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant register because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "register failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> login(String email, String pass) {
        try {
            userAuth.checkPassword(email, pass);
            if (!checkIsAdmin(email)){
                String hashedPass = userAuth.hashPassword(email, pass, false);
                int memberId = userController.login(email, hashedPass);
                userController.checkPassword(pass);
                marketInfo.addUserCount();
                String token = userAuth.generateToken(memberId);
                LoginInformation loginInformation = getLoginInformation(token, memberId, email);
                return logAndRes(Logger.logStatus.Success, "user " + email + " logged in successfully on " + LocalDateTime.now(),
                        loginInformation, null, null);
            }
            else{
                return adminLogin(email, pass);
            }
        } catch(Exception e){
            return logAndRes(Logger.logStatus.Fail, "user cant get log in because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "log in failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> getMember(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            LoginInformation loginInformation;
            if (!checkIsAdmin(userId)) {
                loginInformation = getLoginInformation(token, userId, userController.getUserEmail(userId));
                return new Response<LoginInformation>(loginInformation, null, null);
            }
            Admin a = getAdmin(userId);
            loginInformation = getAdminLoginInformation(token, userId, a.getEmailAdmin());
            return new Response<LoginInformation>(loginInformation, null, null);
        }catch (Exception e){
            return new Response<>(null, "get member failed", e.getMessage());
        }
    }

    @Override
    public Response<String> checkSecurityQuestions(int userId, String token, List<String> answers) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkSecurityQuestions(userId, answers);
            return logAndRes(Logger.logStatus.Success, "user: " + userId + " entered security answers successfully on " + LocalDateTime.now(),
                    "security questions were added successfully", null, null);
        }
        catch (Exception e){
            return logAndRes(Logger.logStatus.Fail, "user cant add security question because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "add security question failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addSecurityQuestion(int userId, String token, String question, String answer) {
        try {
            userAuth.checkUser(userId, token);
            userController.addSecurityQuestion(userId, question, answer);
            return logAndRes(Logger.logStatus.Success, "user: " + userId + " added security question successfully on " + LocalDateTime.now(),
                    "security question added successfully", null, null);
        }
        catch (Exception e){
            return logAndRes(Logger.logStatus.Fail, "user cant add security question because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "add security question failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeAnswerForLoginQuestion(int userId, String token, String question, String answer) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeAnswerForLoginQuestion(userId, question, answer);
            return logAndRes(Logger.logStatus.Success, "user: " + userId + " changed security answer successfully on " + LocalDateTime.now(),
                    "security answer changed successfully", null, null);
        }
        catch (Exception e){
            return logAndRes(Logger.logStatus.Fail, "user cant change security answer because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change security answer failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeSecurityQuestion(int userId, String token, String question) {
        try {
            userAuth.checkUser(userId, token);
            userController.removeSecurityQuestion(userId, question);
            return logAndRes(Logger.logStatus.Success, "user: " + userId + " removed security question successfully on " + LocalDateTime.now(),
                    "security question removed successfully", null, null);
        }
        catch (Exception e){
            return logAndRes(Logger.logStatus.Fail, "user cant remove security question because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "remove security question failed", e.getMessage());
        }
    }



    @Override
    public Response<String> sendNotification(int userId, String token, String receiverEmail, String notify){
        try {
            userAuth.checkUser(userId, token);
            Notification<String> notification = new Notification<>(notify);
            userController.addNotification(receiverEmail, notification);
            return logAndRes(Logger.logStatus.Success, "notification was sent to " + receiverEmail + " on " + LocalDateTime.now(),
                    "notification sent", null, null);
        }catch (Exception e){
            return logAndRes(Logger.logStatus.Success, "notification was not sent to " + receiverEmail + " on " + LocalDateTime.now(),
                    null, "notification not sent", e.getMessage());
        }
    }

    private void addNotification(int userId, String notify) throws Exception{
        Notification<String> notification = new Notification<>(notify);
        userController.addNotification(userId, notification);
    }
    @Override
    public Response<List<String>> displayNotifications(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            List<String> notifications = userController.displayNotifications(userId);
            return logAndRes(Logger.logStatus.Success, "user: " + userId + "got notifications successfully on " + LocalDateTime.now(),
                    notifications, null, null);
        }
        catch (Exception e){
            return logAndRes(Logger.logStatus.Fail, "user cant get his notifications because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "display notifications failed", e.getMessage());
        }
    }


    @Override
    public Response<String> logout(int userId) {
        try {
            userController.logout(userId);
            marketInfo.reduceUserCount();
            return logAndRes(Logger.logStatus.Success, "user log out successfully on " + LocalDateTime.now(),
                    "user log out successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant get log out because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "log out failed", e.getMessage());
        }
    }


    //for each function of guest there will be 2 function , owns get an id and the other gets userName
    @Override
    public Response<String> addProductToCart(int userId, int storeId, int productId, int quantity) {
        try {
            marketController.checkProductInStore(storeId, productId);
            userController.addProductToCart(userId, storeId, productId, quantity);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId, productId);
            return logAndRes(Logger.logStatus.Success, "user " + name + "add " + quantity + " " + productName + " to shopping cart on " + LocalDateTime.now(),
                    "user add to cart successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant add product To Cart because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "add product to cart failed", e.getMessage());
        }
    }



    @Override
    public Response<String> removeProductFromCart(int userId, int storeId, int productId) {
        try {
            userController.removeProductFromCart(userId, storeId, productId);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId, productId);
            return logAndRes(Logger.logStatus.Success, "user " + name + "remove " + productName + " from shopping cart on " + LocalDateTime.now(),
                    "user remove " + productName + " from cart successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant remove product from Cart because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "remove product from cart failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeQuantityInCart(int userId, int storeId, int productId, int change) {
        try {
            userController.changeQuantityInCart(userId, storeId, productId, change);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId, productId);
            return logAndRes(Logger.logStatus.Success, "user " + name + " change quantity to " + change + " on shopping cart on " + LocalDateTime.now(),
                    "user change Quantity of " + productName + " In Cart to " + change + " successfully ", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant remove change quantity in cart because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "remove product from cart failed", e.getMessage());
        }
    }

    public Response<List<JSONObject>> getCartJson(int id) {
        try {
            List<JSONObject> cart = userController.getUserCartJson(id);
            String name = userController.getUserName(id);
            return logAndRes(Logger.logStatus.Success, "user" + name + "ask for his cart on " + LocalDateTime.now(),
                    cart, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant get his cart because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get cart failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeCart(int userId) {
        try{
            userController.removeCart(userId);
            String name = userController.getUserName(userId);
            return logAndRes(Logger.logStatus.Success, "user" + name + "ask to remove his cart on " + LocalDateTime.now(),
                    "remove cart succeeded", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant remove his cart because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "remove cart failed", e.getMessage());
        }
    }


    //TODO: add proxy supplier to the function(somewhere)
    @Override
    public synchronized Response<Receipt> makePurchase(int userId, String accountNumber) {
        try {
            ShoppingCart cart = new ShoppingCart(userController.getUserCart(userId));
            Pair<Receipt, Set<Integer>> ans = marketController.purchaseProducts(cart, userId);
            Receipt receipt = ans.getFirst();
            Set<Integer> creatorIds = ans.getSecond();
            proxyPayment.makePurchase(accountNumber, receipt.getTotalPrice());
            userController.purchaseMade(userId, receipt.getOrderId(), receipt.getTotalPrice());
            //TODO: make an external service handle notifications
            for(int creatorId : creatorIds)
                addNotification(creatorId, "a new purchase was made in your store");
            marketInfo.addPurchaseCount();
            return logAndRes(Logger.logStatus.Success, "user made purchase on " + LocalDateTime.now(),
                    receipt, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant make purchase " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "make purchase failed", e.getMessage());
        }
    }


    @Override
    public Response<String> changeName(int userId, String token, String newUserName) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeUserName(userId, newUserName);
            String name = userController.getUserName(userId);
            return logAndRes(Logger.logStatus.Success, "user" + name + "changed name successfully on " + LocalDateTime.now(),
                    " you changed details successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant change name because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change name failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeEmail(int userId, String token, String newEmail) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeUserEmail(userId, newEmail);
            String name = userController.getUserName(userId);
            return logAndRes(Logger.logStatus.Success, "user" + name + "changed email successfully on " + LocalDateTime.now(),
                    " you changed details successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant change email because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change email failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changePassword(int userId, String token, String oldPass, String newPass) {
        try {
            userAuth.checkUser(userId, token);
            String email = userController.getUserEmail(userId);
            userAuth.checkPassword(email, oldPass);
            String oldHashedPass = userAuth.hashPassword(email, oldPass, false);
            String newHashedPass = userAuth.hashPassword(email, newPass, true);
            userController.checkPassword(newPass);
            userController.changeUserPassword(userId, oldHashedPass, newHashedPass);
            String name = userController.getUserName(userId);
            return logAndRes(Logger.logStatus.Success, "user" + name + "changed password successfully on " + LocalDateTime.now(),
                    " you changed details successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant change password because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change password failed", e.getMessage());
        }
    }

    @Override
    public Response<Integer> openStore(int userId, String token, String storeName, String des, String img) {
        try {
            userAuth.checkUser(userId, token);
            userController.canOpenStore(userId);
            Store store = marketController.openStore(userId, storeName, des, img);
            userController.openStore(userId, store);
            String name = userController.getUserName(userId);
            return logAndRes(Logger.logStatus.Success, "user" + name + "open store successfully on " + LocalDateTime.now(),
                    store.getStoreId(), null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant open store  because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "open store failed", e.getMessage());
        }
    }

    @Override
    public Response<Info> getMemberInformation(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            Info user = userController.getUserPrivateInformation(userId);
            return logAndRes(Logger.logStatus.Success, "user received successfully on " + LocalDateTime.now(),
                    user, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant received because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get user failed", e.getMessage());
        }
    }

    private void checkAdminOrSame(int userId, int buyerId) throws Exception{
        if (!(userId < 0 || userId == buyerId)) //if it's the admin or the user himself
        {
            throw new Exception("user failed to get purchase history because he doesnt have permission ");
        }
        else if(userId == buyerId && (!userController.isActiveUser(userId)))
            throw new Exception("the id given belong to an inactive member");
    }

    @Override
    public Response<PurchaseHistory> getUserPurchaseHistory(int userId, String token, int buyerId) {
        try {
            userAuth.checkUser(userId, token);
            checkAdminOrSame(userId, buyerId);
            PurchaseHistory orders = userController.getUserPurchaseHistory(buyerId);
            return logAndRes(Logger.logStatus.Success, "user received orders successfully on " + LocalDateTime.now(),
                    orders, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get user orders because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get user orders failed", e.getMessage());
        }
    }

    //TODO: add external service for messages and notifications
    @Override
    public Response<String> writeReviewToStore(int userId, String token, int orderId, int storeId, String content, int grading) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.writeReviewForStore(orderId, storeId, content, grading, userId);
            int creatorId = marketController.addReviewToStore(m);
            m.addOwnerEmail(userController.getUserEmail(creatorId));
            addNotification(creatorId,"a review of has been added for store: " + storeId);
            return logAndRes(Logger.logStatus.Success, "user write review on store successfully on " + LocalDateTime.now(),
                    "user write review on store successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant write review on store because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "write review failed", e.getMessage());
        }
    }


    @Override
    public Response<String> writeReviewToProduct(int userId, String token, int orderId, int storeId, int productId, String content, int grading) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.writeReviewForProduct(orderId, storeId, productId, content, grading, userId);
            int creatorId = marketController.writeReviewForProduct(m);
            m.addOwnerEmail(userController.getUserEmail(creatorId));
            addNotification(creatorId,"a review of has been added for product: "+productId +" in store: " + storeId);
            return logAndRes(Logger.logStatus.Success, "user write review on product successfully on " + LocalDateTime.now(),
                    "user write review successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant write review to product because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "write review failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, Message>> checkReviews(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.viewMessages, storeId);
            HashMap<Integer, Message> reviews = marketController.viewReviews(storeId);
            return logAndRes(Logger.logStatus.Success, "user checked reviews on store successfully on " + LocalDateTime.now(),
                    reviews, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant check reviews to store because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "check review failed", e.getMessage());
        }
    }

    @Override
    public Response<ProductInfo> getProductInformation(int storeId, int productId) {
        try {
            ProductInfo res = marketController.getProductInformation(storeId, productId);
            return logAndRes(Logger.logStatus.Success, "user get product information successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get product information because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get product information failed", e.getMessage());
        }
    }


    public Response<List<? extends Information>> getStoresInformation() {
        try {
            List<StoreInfo> res = marketController.getStoresInformation();
            return logAndRes(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get store information because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get product information failed", e.getMessage());
        }
    }
    @Override
    public Response<StoreInfo> getStoreInformation(int storeId) {
        try {
            StoreInfo res = marketController.getStoreInformation(storeId);
            return logAndRes(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get store information because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get product information failed", e.getMessage());
        }
    }

    @Override
    public Response<Store> getStore(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.seeStoreHistory, storeId);
            return logAndRes(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now(),
                    marketController.getStore(storeId), null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get store because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get store failed", e.getMessage());
        }
    }


    @Override
    public Response<String> getStoreDescription(int storeId) {
        try {
            String res = marketController.getStoreDescription(storeId);
            return logAndRes(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get store information because: " + e.getMessage() + "on " + LocalDateTime.now(),
            null, "get product information failed", e.getMessage());
        }
    }


    //TODO: need someone to edit filter(both functions) to fit the template of the rest of the functions
    @Override
    public Response showFilterOptions() {
        return new Response<>(marketController.showFilterOptions(),null,null);
    }

    @Override
    public Response filterBy(HashMap<String,String> filterOptions) {
        ArrayList<ProductInfo> result = marketController.filterBy(filterOptions);
        if(result.isEmpty()){
            logger.log(Logger.logStatus.Fail, "No products found by those filter options, on " + LocalDateTime.now());
            return new Response<>(null, "No products found by those filter options", "result array is empty, no products found");
        }
        logger.log(Logger.logStatus.Success,"Filtered products successfully on" + LocalDateTime.now());
        return new Response<>(result,null,null);
    }

    @Override
    public Response<List<ProductInfo>> getStoreProducts(int storeId) {
        try {
            List<ProductInfo> res = marketController.getStoreProducts(storeId);
            return logAndRes(Logger.logStatus.Success, "user get store products successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get store products because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get products failed", e.getMessage());
        }
    }


    @Override
    public Response<String> sendQuestion(int userId, String token, int storeId, String msg) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.sendQuestionToStore(userId, storeId, msg);
            int creatorId = marketController.addQuestion(m);
            m.addOwnerEmail(userController.getUserEmail(creatorId));
            addNotification(userId, "a question of has been added for store: " + storeId);
            return logAndRes(Logger.logStatus.Success, "user send question successfully on " + LocalDateTime.now(),
                    "question added successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant send information because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "send information failed", e.getMessage());
        }
    }

    @Override
    public Response<String> sendComplaint(int userId, String token, int orderId, int storeId, String msg) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.writeComplaintToMarket(orderId, storeId, msg, userId);
            complaints.put(m.getMessageId(), m);
            return logAndRes(Logger.logStatus.Success, "user send complaint successfully on " + LocalDateTime.now(),
                    "user send complaint successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant send complaint because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "send Complaint failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointManager(int userId, String token, String managerToAppoint, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointManager(userController.getUserEmail(userId), managerToAppoint, storeId);
            return logAndRes(Logger.logStatus.Success, "user appoint " + managerToAppoint + "to Manager in: " + storeId + " successfully on " + LocalDateTime.now(),
                    "user appointManager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointManager(int userId, String token, int managerIdToAppoint, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointManager(userId, managerIdToAppoint, storeId);
            return logAndRes(Logger.logStatus.Success, "user appoint " + managerIdToAppoint + "to Manager in: " + storeId + " successfully on " + LocalDateTime.now(),
                    "user appointManager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreDescription(int userId, String token, int storeId, String description) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.changeStoreDetails, storeId);
            marketController.setStoreDescription(storeId, description);
            return logAndRes(Logger.logStatus.Success, "user change store description successfully on " + LocalDateTime.now(),
                    "user change store description successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant change store description because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change store description failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreImg(int userId, String token, int storeId, String img) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.changeStoreDetails, storeId);
            marketController.setStoreImg(storeId, img);
            return logAndRes(Logger.logStatus.Success, "user change store img successfully on " + LocalDateTime.now(),
                    "user change store img successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant change store img because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change store img failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreName(int userId, String token, int storeId, String name) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.changeStoreDetails, storeId);
            marketController.setStoreName(storeId, name);
            return logAndRes(Logger.logStatus.Success, "user change store name successfully on " + LocalDateTime.now(),
                    "user change store name successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant change store img because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change store name failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changePurchasePolicy(int userId, String token, int storeId, String policy) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.changePurchasePolicy, storeId);
            marketController.setStorePurchasePolicy(storeId, policy);
            return logAndRes(Logger.logStatus.Success, "user change store purchase policy successfully on " + LocalDateTime.now(),
                    "user change store purchase policy successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant change store purchase policy because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "change store policy purchase failed", e.getMessage());
        }
    }

    //TODO: check what to do with discount policy
//    @Override
//    public Response<String> addDiscountPolicy(int userId, String token, int storeId, String policy) {
//        try {
//            userAuth.checkUser(userId, token);
//            if (userController.checkPermission(userId, Action.changeDiscountPolicy, storeId)) {
////                marketController.setStoreDiscountPolicy(storeId, policy);
//                logger.log(Logger.logStatus.Success, "user change store policy discount successfully on " + LocalDateTime.now());
//                return new Response<>("user change store discount policy successfully", null, null);
//            }
//            logger.log(Logger.logStatus.Fail, "cant change store policy discount because: user dont have access" + "on " + LocalDateTime.now());
//            return new Response<>(null, "change store policy discount failed", "user dont have access");
//        } catch (Exception e) {
//            logger.log(Logger.logStatus.Fail, "cant change store discount policy because: " + e.getMessage() + "on " + LocalDateTime.now());
//            return new Response<>(null, "change store discount policy failed", e.getMessage());
//        }
//    }

    @Override
    public Response<String> addPurchaseConstraint(int userId, String token, int storeId, String constraint) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.addPurchaseConstraint, storeId);
            marketController.addPurchaseConstraint(storeId, constraint);
            return logAndRes(Logger.logStatus.Success, "user add purchase constraint successfully on " + LocalDateTime.now(),
                    "user add purchase constraint successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant add purchase constraint policy because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "add purchase constraint failed", e.getMessage());
        }
    }

    @Override
    public Response<String> fireManager(int userId, String token, int managerToFire, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.fireManager, storeId);
            userController.fireManager(userId, managerToFire, storeId);
            return logAndRes(Logger.logStatus.Success, "user fire manager successfully on " + LocalDateTime.now(),
                    "user fire manager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant fire manager because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "fire manager failed", e.getMessage());
        }
    }

    @Override
    public Response<Info> checkWorkerStatus(int userId, String token, int workerId, int storeId) {
        try{
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.checkWorkersStatus, storeId);
            Info res = userController.getWorkerInformation(userId, workerId, storeId);
            return logAndRes(Logger.logStatus.Success, "user check worker status successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant check worker status because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "check worker status failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> checkWorkersStatus(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.checkWorkersStatus, storeId);
            List<Info> res = userController.getWorkersInformation(userId, storeId);
            return logAndRes(Logger.logStatus.Success, "user check worker status successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant check worker status because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "check worker status failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, ? extends Information>> viewQuestions(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.viewMessages, storeId);
            HashMap<Integer, Message> res = marketController.getQuestions(storeId);
            return logAndRes(Logger.logStatus.Success, "user get questions successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get questions because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get questions failed", e.getMessage());
        }
    }

    @Override
    public Response<String> answerQuestion(int userId, String token, int storeId, int questionId, String answer) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.answerMessage, storeId);
            marketController.answerQuestion(storeId, questionId, answer);
            return logAndRes(Logger.logStatus.Success, "user answer question successfully on " + LocalDateTime.now(),
                    "user answer question successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant answer question because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "answer question failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> seeStoreHistory(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.seeStoreHistory, storeId);
            List<OrderInfo> res = marketController.getStoreOrderHistory(storeId);
            return logAndRes(Logger.logStatus.Success, "user get store history successfully on " + LocalDateTime.now(),
                    res, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant  get store history  because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, " get store history failed", e.getMessage());
        }
    }

    @Override
    public Response<Integer> addProduct(int userId, String token, int storeId, List<String> categories, String name, String description,
                                        int price, int quantity, String img) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.addProduct, storeId);
            int productId = marketController.addProduct(storeId, name, description, price, quantity, categories, img);
            return logAndRes(Logger.logStatus.Success, "Add product successfull on: " + LocalDateTime.now(),
                    productId, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "Cant add product because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "Product Addition Failed", e.getMessage());
        }

    }

    @Override
    public Response<String> deleteProduct(int userId, String token, int storeId, int productId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.removeProduct, storeId);
            marketController.deleteProduct(storeId, productId);
            return logAndRes(Logger.logStatus.Success, "Delete product successful on: " + LocalDateTime.now(),
                    "product was successfully deleted", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "Cant delete product because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "Product deletion Failed", e.getMessage());
        }
    }


    //TODO: need to use supplierProxy for changing the quantity
    @Override
    public Response<String> updateProduct(int userId, String token, int storeId, int productId, List<String> categories, String name, String description,
                                          int price, int quantity, String img) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.updateProduct, storeId);
            proxySupplier.orderSupplies(storeId, productId, quantity);
            marketController.updateProduct(storeId, productId, categories, name, description, price, quantity, img);
            return logAndRes(Logger.logStatus.Success, "Update product successful on: " + LocalDateTime.now(),
                    "updated product was successful", null, null);
        }catch (Exception e){
            return logAndRes(Logger.logStatus.Fail, "Cant update product because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "Product Update Failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointOwner(int userId, String token, String owner, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointOwner(userController.getUserEmail(userId), owner, storeId);
            return logAndRes(Logger.logStatus.Success, "appointed user successfully on " + LocalDateTime.now(),
                    "user appointManager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointOwner(int userId, String token, int ownerId, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointOwner(userId, ownerId, storeId);
            return logAndRes(Logger.logStatus.Success, "appointed user successfully on " + LocalDateTime.now(),
                    "user appointManager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> fireOwner(int userId, String token, int ownerId, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.fireOwner, storeId);
            userController.fireOwner(userId, ownerId, storeId);
            return logAndRes(Logger.logStatus.Success, "user fire owner successfully on " + LocalDateTime.now(),
                    "user fire owner successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant fire owner because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "fire owner failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addManagerPermission(int ownerId, String token, int userId, int storeId, int permissionsId) {
        try {
            userAuth.checkUser(ownerId, token);
            Action a = actionIds.get(permissionsId);
            userController.addManagerAction(ownerId, userId, a, storeId);
            return logAndRes(Logger.logStatus.Success, "the action " + permissionsId + " has been added to user: " + userId +
                    "on time: " + LocalDateTime.now(), "add manager permission was successful", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant add permission because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "add permission failed", e.getMessage());
        }
    }


    //TODO: check if need to add logger or not
    @Override
    public Response<String> addManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds) {
        try {
            userAuth.checkUser(ownerId, token);
            for(int permissionsId : permissionsIds)
                addManagerPermission(ownerId, token, userId, storeId, permissionsId);
            return new Response<>("add manager permissions was successful", null, null);
        } catch (Exception e) {
            return new Response<>(null, "add permissions failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeManagerPermission(int ownerId, String token, int userId, int storeId, int permissionsId) {
        try {
            userAuth.checkUser(ownerId, token);
            Action a = actionIds.get(permissionsId);
            userController.removeManagerAction(ownerId, userId, a, storeId);
            return logAndRes(Logger.logStatus.Success, "the action " + permissionsId + " has been removed from user: " + userId +
                    "on time: " + LocalDateTime.now(), "manager permission was removed successful", null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant remove permission because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "remove permission fail", e.getMessage());
        }
    }
    @Override
    public Response<String> removeManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds) {
        try {
            userAuth.checkUser(ownerId, token);
            for(int permissionId : permissionsIds) {
                Action a = actionIds.get(permissionId);
                if(a != null) {
                    userController.removeManagerAction(ownerId, userId, a, storeId);
                    logger.log(Logger.logStatus.Success, "the action " + permissionId + " has been removed from user: " + userId +
                            "on time: " + LocalDateTime.now());
                }
                else{
                    logger.log(Logger.logStatus.Fail, "cant add permission because the action id does not match an action on " + LocalDateTime.now());
                    return new Response<>(null, "add permission failed", "no id match");
                }
            }
            return new Response<>("manager permission was removed successful", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant remove permission because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "remove permission fail", e.getMessage());
        }
    }


    @Override
    public Response<AppHistory> getAppointments(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.seeStoreHistory, storeId)) {
                AppHistory res = marketController.getAppointments(storeId);
                logger.log(Logger.logStatus.Success, "user get appointments" + LocalDateTime.now());
                return new Response<>(res, null, null);
            } else {
                logger.log(Logger.logStatus.Fail, "cant get store appointments: user dont have access" + "on " + LocalDateTime.now());
                return new Response<>(null, "get appointments failed", "user dont have access");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get appointments: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get appointments failed", e.getMessage());
        }
    }

    @Override
    public Response<String> closeStore(int userId, String token, int storeId) {
        try
        {
            userAuth.checkUser(userId, token);
            userController.closeStore(userId, storeId);
            logger.log(Logger.logStatus.Success, "user closed store" + LocalDateTime.now());
            return new Response<>("close store was successful", null, null);

        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant close store: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "close store failed", e.getMessage());
        }
    }

    @Override
    public Response<String> reopenStore(int userId, String token, int storeId) {
        try
        {
            userAuth.checkUser(userId, token);
            userController.reOpenStore(userId, storeId);
            logger.log(Logger.logStatus.Success, "user reopened store" + LocalDateTime.now());
            return new Response<>("reopen store was successful", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant reopen store: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "reopen store failed", e.getMessage());
        }

    }

    @Override
    public Response<String> closeStorePermanently(int adminId, String token, int storeId){
        if (adminId < 0) {
            Admin admin = activeAdmins.get(adminId);
            if (admin != null) {
                try {
                    userAuth.checkUser(adminId, token);
                    admin.closeStorePermanently(storeId, -1);
                    logger.log(Logger.logStatus.Success, "the store: " + storeId + " has been permanently closed by admin: " + adminId + " at time " + LocalDateTime.now());
                    return new Response<String>("close was permanently closed", null, null);
                }
                catch (Exception e){
                    logger.log(Logger.logStatus.Fail, "the user: " + adminId + "  cannot close store because: " + e.getMessage() + " at time " + LocalDateTime.now());
                    return new Response<>(null, "close permanent not successfully", e.getMessage());
                }
            } else {
                logger.log(Logger.logStatus.Fail, "the user: " + adminId + " cannot permanently close stores"  +" at time " + LocalDateTime.now());
                return new Response<>(null, "close permanent not successfully", "the id given is not of an admin");
            }
        } else {
            logger.log(Logger.logStatus.Fail, "the user: " + adminId + " cannot permanently close stores"  +" at time " + LocalDateTime.now());
            return new Response<>(null, "close permanent not successfully", "the id given is not of an admin");
        }
    }

    @Override
public Response<List<ProductInfo>> getProducts(int storeId){
    try{
        List<ProductInfo> products = marketController.getStoreProducts(storeId);
        logger.log(Logger.logStatus.Success, "store get products successfully on " + LocalDateTime.now());
        return new Response<>(products, null, null);
    }catch(Exception e){
        logger.log(Logger.logStatus.Fail, "cant get store products because: " + e.getMessage() + "on " + LocalDateTime.now());
        return new Response<>(null, "get store products failed", e.getMessage());
    }
}
    @Override
    public Response<List<? extends Information>> getProducts() {
        try{
            List<ProductInfo> products = marketController.getAllProducts();
            logger.log(Logger.logStatus.Success, "store get products successfully on " + LocalDateTime.now());
            return new Response<>(products, null, null);
        }catch(Exception e){
            logger.log(Logger.logStatus.Fail, "cant get store products because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get store products failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> adminLogin(String email, String pass) {
        try {
            for (Admin a : activeAdmins.values()) {
                if (a.checkEmail(email)) {
                    logger.log(Logger.logStatus.Fail, "cant login admin , admin already connected " + LocalDateTime.now());
                    return new Response<>(null, "log admin failed", "u are already connected");
                }
            }
            for (Admin a : inActiveAdmins.values()) {
                String hashedPass = userAuth.hashPassword(email, pass, false);
                if (a.checkEmail(email) && a.checkPassword(hashedPass)) {
                    int id = a.getAdminId();
                    activeAdmins.put(a.getAdminId(), inActiveAdmins.remove(a.getAdminId()));
                    logger.log(Logger.logStatus.Success, "admin logged in successfully on " + LocalDateTime.now());
                    LoginInformation loginInformation = new LoginInformation(userAuth.generateToken(id), id, email, true, null,
                            false, null, null, null, null);
                    return new Response<>(loginInformation, null, null);
                }
            }
            logger.log(Logger.logStatus.Fail, "cant login admin , wrong details" + LocalDateTime.now());
            return new Response<>(null, "log admin failed", "wrong details");
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant login admin because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "login admin failed", e.getMessage());
        }
    }

    @Override
    public Response<String> adminLogout(int adminId) {
        Admin a = activeAdmins.get(adminId);
        if (a != null) {
            inActiveAdmins.put(adminId, activeAdmins.remove(adminId));
            logger.log(Logger.logStatus.Success, "admin logged out successfully on " + LocalDateTime.now());
            return new Response<>("u logged out", null, null);
        }
        a = inActiveAdmins.get(adminId);
        if (a != null) {
            logger.log(Logger.logStatus.Fail, "cant log out admin , admin not connected " + LocalDateTime.now());
            return new Response<>(null, "log out admin failed", "u are not connected");
        }
        logger.log(Logger.logStatus.Fail, "cant log out admin , admin not exist " + LocalDateTime.now());
        return new Response<>(null, "log out admin failed", "u dont exist");
    }

    @Override
    public Response<HashMap<Integer,Admin>> getAdmins(int adminId, String token) {
        try {
            userAuth.checkUser(adminId, token);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant get admins because: " + e.getMessage() +" on time: " + LocalDateTime.now());
            return new Response<>(null, "get admins failed", e.getMessage());
        }
        Admin a = activeAdmins.get(adminId);
        HashMap<Integer,Admin> list= new HashMap<>();
        if(a != null){
            for(Integer key : activeAdmins.keySet()){
                list.put(key,activeAdmins.get(key));
            }
            for(Integer key : inActiveAdmins.keySet()){
                list.put(key,inActiveAdmins.get(key));
            }
            logger.log(Logger.logStatus.Success, "admin get all admins successfully on " + LocalDateTime.now());
            return new Response<>(list, null, null);
        }
        a= inActiveAdmins.get(adminId);
        if(a !=null){
            logger.log(Logger.logStatus.Fail, "cant get admins , admin not connected " + LocalDateTime.now());
            return new Response<>(null, "get admins failed", "u are not connected");
        }
        logger.log(Logger.logStatus.Fail, "cant get admins , admin not connected " + LocalDateTime.now());
        return new Response<>(null, "get admins failed", "u are not connected");
    }



    @Override
    public Response<String> addAdmin(int userId, String token, String email, String pass) {
        if (userId != 0) {
            try {
                userAuth.checkUser(userId, token);
            } catch (Exception e) {
                logger.log(Logger.logStatus.Fail, "cant add admin because: " + e.getMessage() + " on time: " + LocalDateTime.now());
                return new Response<>(null, "add admin failed", e.getMessage());
            }
            Admin appoint = activeAdmins.get(userId);
            if (appoint != null) {
                for (Admin a : activeAdmins.values()) {
                    if (a.checkEmail(email)) {
                        logger.log(Logger.logStatus.Fail, "cant add new admin , another admin already exist with this name on " + LocalDateTime.now());
                        return new Response<>(null, "add admin failed", "another admin already exist with this name");
                    }
                }
                for (Admin a : inActiveAdmins.values()) {
                    if (a.checkEmail(email)) {
                        logger.log(Logger.logStatus.Fail, "cant add new admin , another admin already exist with this name on " + LocalDateTime.now());
                        return new Response<>(null, "add admin failed", "another admin already exist with this name");
                    }
                }
            }
            else{
                logger.log(Logger.logStatus.Fail, "cant add new admin , u dont have access" + LocalDateTime.now());
                return new Response<>(null, "add admin failed", "u dont have access");
            }
        }
        String hashedPass = userAuth.hashPassword(email, pass, true);
        Admin a = new Admin(adminId.getAndDecrement(), email, hashedPass);
        a.addControllers(userController, marketController);
        inActiveAdmins.put(a.getAdminId(), a);
        logger.log(Logger.logStatus.Success, "admin added new admin successfully on " + LocalDateTime.now());
        return new Response<>("admin added new admin successfully", null, null);
    }

    @Override
    public Response<String> removeAdmin(int adminId, String token) {
        try{
            userAuth.checkUser(adminId, token);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant remove admin because: " + e.getMessage() +" on time: " + LocalDateTime.now());
            return new Response<>(null, "remove admin failed", e.getMessage());
        }
        Admin me = activeAdmins.get(adminId);
        if(activeAdmins.size() + inActiveAdmins.size() == 1){
            logger.log(Logger.logStatus.Fail, "cant remove admin , need at least 1 admin in the system" + LocalDateTime.now());
            return new Response<>(null, "remove admin failed", "need at least 1 admin in the system");
        }
        if (me != null) {
            activeAdmins.remove(adminId);
            logger.log(Logger.logStatus.Success, "admin removed himself successfully on " + LocalDateTime.now());
            return new Response<>("u removed u self successfully", null, null);
        }
        logger.log(Logger.logStatus.Fail, "cant remove admin , u dont have access" + LocalDateTime.now());
        return new Response<>(null, "remove admin failed", "u dont have access");
    }

    /**
     * return json of all the relevant information about the users: email, id, name
     */
    @Override
    public Response<List<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>>> getUsersPurchaseHistory(int adminId, String token) {
        try {
            userAuth.checkUser(adminId, token);
            Admin a = activeAdmins.get(adminId);
            if (a != null) {
                List<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> users = userController.getUsersInformation();
                logger.log(Logger.logStatus.Success, "admin get users successfully on " + LocalDateTime.now());
                return new Response<>(users, null, null);
            }
            return new Response<>(null, "can't get the users information", "the id given is not of admin");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user failed getting all users because :" + e.getMessage());
            return new Response<>(null, "get users", e.getMessage());
        }
    }

    @Override
    public Response<String> answerComplaint(int adminId, String token, int complaintId, String ans) {
        try {
            userAuth.checkUser(adminId, token);
            Admin a = activeAdmins.get(adminId);
            if (a != null) {
                Message m = complaints.get(complaintId);
                if (m != null) {
                    m.sendFeedback(ans);
                    logger.log(Logger.logStatus.Success, "admin answer complaint successfully on " + LocalDateTime.now());
                    return new Response<>("admin answer complaint", null, null);
                }
                logger.log(Logger.logStatus.Fail, "cant get message to answer on" + LocalDateTime.now());
                return new Response<>(null, "answer complaint failed", "message does not found");
            }
            logger.log(Logger.logStatus.Fail, "cant get admin on" + LocalDateTime.now());
            return new Response<>(null, "answer complaint failed", "admin wasn't found");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user failed answer complaints because:" + e.getMessage() + " on" + LocalDateTime.now());
            return new Response<>(null, "answer complaint failed", e.getMessage());
        }
    }

    @Override
    public Response<String> cancelMembership(int adminId, String token, int userToRemove) {
        try {
            userAuth.checkUser(adminId, token);
            Admin admin = activeAdmins.get(adminId);
            if (admin != null) {
                admin.cancelMembership(userToRemove);
                logger.log(Logger.logStatus.Success, "admin cancel Membership successfully on " + LocalDateTime.now());
                return new Response<>("admin cancel Membership complaint", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant cancel Membership on" + LocalDateTime.now());
            return new Response<>(null, "cancel Membership failed", "admin wasnt found");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user failed cancel Membership because:" + e.getMessage() + " on" + LocalDateTime.now());
            return new Response<>(null, "cancel Membership failed", e.getMessage());
        }
    }

    @Override
    public Response<List<String>> watchEventLog(int adminId, String token){
        try{
            userAuth.checkUser(adminId, token);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant watch log because: " + e.getMessage() +" on time: " + LocalDateTime.now());
            return new Response<>(null, "watch log failed", e.getMessage());
        }
        Admin admin = activeAdmins.get(adminId);
        if(admin !=null){
            logger.log(Logger.logStatus.Success, "admin get log successfully on " + LocalDateTime.now());
            return new Response<>(logger.getEventMap(), null, null);
        }
        logger.log(Logger.logStatus.Fail, "cant get admin on" + LocalDateTime.now());
        return new Response<>(null, "watch event log failed", "admin wasn't found");
    }

    @Override
    public Response<List<String>> watchFailLog(int adminId, String token){
        try{
            userAuth.checkUser(adminId, token);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant watch log because: " + e.getMessage() +" on time: " + LocalDateTime.now());
            return new Response<>(null, "watch log failed", e.getMessage());
        }
        Admin admin = activeAdmins.get(adminId);
        if(admin !=null){
            logger.log(Logger.logStatus.Success, "admin get log successfully on " + LocalDateTime.now());
            return new Response<>(logger.getFailMap(), null, null);
        }
        logger.log(Logger.logStatus.Fail, "cant get admin on" + LocalDateTime.now());
        return new Response<>(null, "watch event log failed", "admin wasn't found");
    }

    @Override
    public Response watchMarketStatus(int adminId, String token) {
        try{
            userAuth.checkUser(adminId, token);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant watch market status because: " + e.getMessage() +" on time: " + LocalDateTime.now());
            return new Response<>(null, "watch market status failed", e.getMessage());
        }
        Admin admin = activeAdmins.get(adminId);
        if(admin != null){
            marketInfo.calculateAverages();
            logger.log(Logger.logStatus.Success, "admin get market status successfully on  " + LocalDateTime.now());
            return new Response<>(marketInfo, null, null);
        }
        else{
            logger.log(Logger.logStatus.Fail, "cant get admin on" + LocalDateTime.now());
            return new Response<>(null, "watch market status", "admin wasn't found");
        }
    }


    @Override
    public Response setPaymentService(int adminId, String token, String paymentService) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            proxyPayment.setRealPayment(paymentService);
            return new Response("Set payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Set Payment Service", "Set payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response getPaymentServicePossible(int adminId, String token) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            return new Response(proxyPayment.getPaymentServicesPossibleOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Possible Payment Service", "Get possible payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response getPaymentServiceAvailable(int userId) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            //TODO: userAuth.checkUser(userId);
            //TODO: admin = getAdmin(adminId);
            return new Response(proxyPayment.getPaymentServicesAvailableOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Available Payment Service", "Get available payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response addPaymentService(int adminId, String token, String paymentService) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            proxyPayment.addPaymentService(paymentService);
            return new Response("Add payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Payment Service", "Add payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response removePaymentService(int adminId, String token, String paymentService) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            proxyPayment.removePaymentService(paymentService);
            return new Response("Add payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Remove Payment Service", "Remove payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response setSupplierService(int adminId, String token, String supplierService) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            proxySupplier.setRealSupplier(supplierService);
            return new Response("Set supplier service to: " + supplierService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Set Supplier Service", "Set supplier service fail: " + e.getMessage());
        }
    }

    @Override
    public Response getSupplierServicePossible(int adminId, String token) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            return new Response(proxySupplier.getSupplierServicesPossibleOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Possible Supplier Service", "Get possible supplier service fail: " + e.getMessage());
        }
    }

    @Override
    public Response getSupplierServiceAvailable(int userId) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            //TODO: userAuth.checkUser(userId);
            //TODO: admin = activeAdmins.get(adminId);
            return new Response(proxySupplier.getSupplierServicesAvailableOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Available Supplier Service", "Get available supplier service fail: " + e.getMessage());
        }
    }

    @Override
    public Response addSupplierService(int adminId, String token, String supplierService) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            proxySupplier.addSupplierService(supplierService);
            return new Response("Add supplier service to: " + supplierService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Supplier Service", "Add supplier service fail: " + e.getMessage());
        }
    }


    @Override
    public Response removeSupplierService(int adminId, String token, String supplierService) {
        //TODO: ADD logger
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            admin = getAdmin(adminId);
            proxySupplier.removeSupplierService(supplierService);
            return new Response("Remove supplier service to: " + supplierService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Remove Supplier Service", "Remove supplier service fail: " + e.getMessage());
        }
    }


    //functions for tests
    public String addTokenForTests() {
        return userAuth.generateToken(0);
    }
    public Response<HashMap<Integer, HashMap<java.lang.Integer,java.lang.Integer>>> getCart(int id) {
        try {
            HashMap<Integer, HashMap<java.lang.Integer,java.lang.Integer>> cart = userController.getUserCart(id).getContent();
            String name = userController.getUserName(id);
            return logAndRes(Logger.logStatus.Success, "user" + name + "ask for his cart on " + LocalDateTime.now(),
                    cart, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "user cant get his cart because " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get cart failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> getUserPurchaseHistoryHash(int userId, String token, int buyerId) {
        try {
            userAuth.checkUser(userId, token);
            checkAdminOrSame(userId, buyerId);
            HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> orders = userController.getUserPurchaseHistoryHash(userId, buyerId);
            return logAndRes(Logger.logStatus.Success, "user received orders successfully on " + LocalDateTime.now(),
                    orders, null, null);
        } catch (Exception e) {
            return logAndRes(Logger.logStatus.Fail, "cant get user orders because: " + e.getMessage() + "on " + LocalDateTime.now(),
                    null, "get user orders failed", e.getMessage());
        }
    }

    public int getAdminsize(){
        return activeAdmins.size() + inActiveAdmins.size();
    }

    public void setActionIds(){
        actionIds.put(0, Action.addProduct);
        actionIds.put(1, Action.removeProduct);
        actionIds.put(2, Action.updateProduct);
        actionIds.put(3, Action.changeStoreDetails);
        actionIds.put(4, Action.changePurchasePolicy);
        actionIds.put(5, Action.changeDiscountPolicy);
        actionIds.put(6, Action.addPurchaseConstraint);
        actionIds.put(7, Action.addDiscountConstraint);
        actionIds.put(8, Action.viewMessages);
        actionIds.put(9, Action.answerMessage);
        actionIds.put(10, Action.seeStoreHistory);
        actionIds.put(11, Action.seeStoreOrders);
        actionIds.put(12, Action.checkWorkersStatus);
        actionIds.put(13, Action.appointManager);
        actionIds.put(14, Action.fireManager);
        actionIds.put(15, Action.appointOwner);
        actionIds.put(16, Action.fireOwner);
        actionIds.put(17, Action.changeManagerPermission);
        actionIds.put(18, Action.closeStore);
        actionIds.put(19, Action.reopenStore);
    }

    public Response logAndRes(Logger.logStatus state, String logString, Object value, String errorTi , String errorMsg) {
        logger.log(state, logString);
        return new Response<>(value, errorTi, errorMsg);
    }

    //check if admin
    public boolean checkIsAdmin(int adminId){
        return admins.containsKey(adminId);
    }
    public boolean checkIsAdmin(String email){
        boolean isAdmin = false;
        for (Admin a : admins.values()) {
            if (a.checkEmail(email))
                isAdmin = true;
        }
        return isAdmin;
    }

    //get login information
    public LoginInformation getLoginInformation(String token, int memberId, String email) throws Exception{
        return new LoginInformation(token, memberId, email, true, displayNotifications(memberId, token).getValue(),
                userController.hasSecQuestions(memberId), userController.getUserRoles(memberId),
                userController.getStoreNames(memberId), userController.getStoreImgs(memberId),
                userController.getPermissions(memberId));
    }

    public LoginInformation getAdminLoginInformation(String token, int userId, String email){
        new LoginInformation(token, userId, email, true, null,
                false, null, null, null, null);
    }


    //to get admin
    private Admin getAdmin(int adminId) throws Exception {
        Admin admin = admins.get(adminId);
        if(admin == null || (!admin.getIsActive()))
        {
            throw new Exception("id given does not belong to an active admin");
        }
        return admin;
    }

}
