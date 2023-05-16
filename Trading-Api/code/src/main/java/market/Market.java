package market;

import domain.store.storeManagement.AppHistory;
import service.security.UserAuth;
import service.supplier.ProxySupplier;
import service.UserController;
import service.payment.ProxyPayment;
import utils.*;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import domain.store.storeManagement.Store;
import service.MarketController;
import utils.marketRelated.MarketInfo;
import utils.marketRelated.Response;
import utils.messageRelated.Message;
import utils.messageRelated.Notification;
import utils.orderRelated.OrderInfo;
import utils.stateRelated.Action;
import utils.userInfoRelated.Info;
import utils.userInfoRelated.Receipt;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


//TODO: find a way to generate tokens, hash passwords, ...

public class Market implements MarketInterface {
    private ConcurrentHashMap<Integer, Admin> activeAdmins;
    private ConcurrentHashMap<Integer, Admin> inActiveAdmins;
    private final UserController userController;
    private final MarketController marketController;
    //services
    private ProxyPayment proxyPayment;
    private ProxySupplier proxySupplier;
    private UserAuth userAuth;
    private ConcurrentHashMap<Integer, Message> complaints; //complaintId,message
    private Gson gson;
    private final Logger logger;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private AtomicInteger adminId = new AtomicInteger(-2);

    private HashMap<Integer, Action> actionIds;

    private MarketInfo marketInfo;

    public Market(Admin admin) {
        activeAdmins = new ConcurrentHashMap<>();
        inActiveAdmins = new ConcurrentHashMap<>();
        activeAdmins.put(admin.getAdminId(), admin);
        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();
        gson = new Gson();
        proxyPayment = new ProxyPayment();
        userAuth = new UserAuth();
        proxySupplier = new ProxySupplier();
        complaints = new ConcurrentHashMap<>();
        actionIds = new HashMap<>();
        marketInfo = new MarketInfo();

        admin.addControllers(userController, marketController);

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


    @Override
    public Response<Integer> enterGuest() {
        int guestId = userController.enterGuest();
        logger.log(Logger.logStatus.Success, "guest :" + guestId + " has successfully entered on " + LocalDateTime.now());
        marketInfo.addUserCount();
        return new Response<>(guestId, null, null);
    }

    @Override
    public Response<String> exitGuest(int guestId) {
        try {
            userController.exitGuest(guestId);
            logger.log(Logger.logStatus.Success, "guest :" + guestId + " has successfully exited on " + LocalDateTime.now());
            marketInfo.reduceUserCount();
            return new Response<>("existed successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant exit because " + e.getMessage() + " on " + LocalDateTime.now());
            return new Response<>(null, "exit failed", e.getMessage());
        }
    }

    @Override
    public Response<String> register(String email, String pass, String birthday) {
        try {
            String hashedPassword = userAuth.hashPassword(email, pass, true);
            userController.checkPassword(pass);
            userController.register(email, hashedPassword, birthday);
            logger.log(Logger.logStatus.Success, "user :" + email + " has successfully register on " + LocalDateTime.now());
            marketInfo.addRegisteredCount();
            return new Response<>("registered successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant register because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "register failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> login(String email, String pass) {
        try {
            userAuth.checkPassword(email, pass);
            boolean isAdmin = false;
            for (Admin a : activeAdmins.values()) {
                if (a.checkEmail(email))
                    isAdmin = true;
            }
            for (Admin a : inActiveAdmins.values()) {
                if (a.checkEmail(email))
                    isAdmin = true;
            }
            if (!isAdmin) {
                String hashedPass = userAuth.hashPassword(email, pass, false);
                int memberId = userController.login(email, hashedPass);
                userController.checkPassword(pass);
                marketInfo.addUserCount();
                logger.log(Logger.logStatus.Success, "user " + email + " logged in successfully on " + LocalDateTime.now());
                String token = userAuth.generateToken(memberId);
                LoginInformation loginInformation = new LoginInformation(token, memberId, email, true, displayNotifications(memberId, token).getValue(),
                        userController.hasSecQuestions(memberId), userController.getUserRoles(memberId),
                        userController.getStoreNames(memberId), userController.getStoreImgs(memberId));
                return new Response<LoginInformation>(loginInformation, null, null);
            }
            else{
                return adminLogin(email, pass);
            }
        } catch(Exception e){
            logger.log(Logger.logStatus.Fail, "user cant get log in because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "log in failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> getMember(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            LoginInformation loginInformation;
            if (userId % 2 == 1 && userController.isActiveUser(userId)) {
                loginInformation = new LoginInformation(token, userId, userController.getUserEmail(userId), true, displayNotifications(userId, token).getValue(),
                        userController.hasSecQuestions(userId), userController.getUserRoles(userId),
                        userController.getStoreNames(userId), userController.getStoreImgs(userId));
                return new Response<LoginInformation>(loginInformation, null, null);
            }
            else if(activeAdmins.containsKey(userId)){
                loginInformation = new LoginInformation(token, userId, activeAdmins.get(userId).getEmailAdmin(), true, null,
                        false, null, null, null);
                return new Response<LoginInformation>(loginInformation, null, null);
            }
            else
                return new Response<>(null, "get member failed", "the userId given does not belong to any user");
        }catch (Exception e){
            return new Response<>(null, "get member failed", e.getMessage());
        }
    }

    @Override
    public Response<String> checkSecurityQuestions(int userId, String token, List<String> answers) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkSecurityQuestions(userId, answers);
            logger.log(Logger.logStatus.Success, "user: " + userId + " entered security answers successfully on " + LocalDateTime.now());
            return new Response<String>("security questions were added successfully", null, null);
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail, "user cant add security question because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "add security question failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addSecurityQuestion(int userId, String token, String question, String answer) {
        try {
            userAuth.checkUser(userId, token);
            userController.addSecurityQuestion(userId, question, answer);
            logger.log(Logger.logStatus.Success, "user: " + userId + " added security question successfully on " + LocalDateTime.now());
            return new Response<String>("security question added successfully", null, null);
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail, "user cant add security question because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "add security question failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeAnswerForLoginQuestion(int userId, String token, String question, String answer) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeAnswerForLoginQuestion(userId, question, answer);
            logger.log(Logger.logStatus.Success, "user: " + userId + " changed security answer successfully on " + LocalDateTime.now());
            return new Response<String>("security answer changed successfully", null, null);
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail, "user cant change security answer because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change security answer failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeSecurityQuestion(int userId, String token, String question) {
        try {
            userAuth.checkUser(userId, token);
            userController.removeSecurityQuestion(userId, question);
            logger.log(Logger.logStatus.Success, "user: " + userId + " removed security question successfully on " + LocalDateTime.now());
            return new Response<String>("security question removed successfully", null, null);
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail, "user cant remove security question because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "remove security question failed", e.getMessage());
        }
    }



    @Override
    public Response<List<String>> displayNotifications(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            List<String> notifications = userController.displayNotifications(userId);
            logger.log(Logger.logStatus.Success, "user: " + userId + "got notifications successfully on " + LocalDateTime.now());
            return new Response<>(notifications, null, null);
        }
        catch (Exception e){
            logger.log(Logger.logStatus.Fail, "user cant get his notifications because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "display notifications failed", e.getMessage());
        }
    }


    //when logging out the system returns an id of a guest for the now logged-out user to use
    @Override
    public Response<String> logout(int userId) {
        try {
            userController.logout(userId);
            marketInfo.reduceUserCount();
            logger.log(Logger.logStatus.Success, "user log out successfully on " + LocalDateTime.now());
            return new Response<>("user log out successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant get log out because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "log out failed", e.getMessage());
        }
    }


    //for each function of guest there will be 2 function , owns get an id and the other gets userName
    @Override
    public Response<String> addProductToCart(int userId, int storeId, int productId, int quantity) {
        try {
            List<ProductInfo> productInfos = marketController.getStoreProducts(storeId);
            boolean check = false;
            for(ProductInfo product : productInfos)
                if(product.id == productId)
                    check = true;
            if(check) {
                userController.addProductToCart(userId, storeId, productId, quantity);
                String name = userController.getUserName(userId);
                String productName = marketController.getProductName(storeId, productId);
                logger.log(Logger.logStatus.Success, "user " + name + "add " + quantity + " " + productName + " to shopping cart on " + LocalDateTime.now());
                return new Response<>("user add to cart successfully", null, null);
            }
            else {
                logger.log(Logger.logStatus.Fail, "the user  can't add the product because the productId does not exist in the store on " + LocalDateTime.now());
                return new Response<>(null, "add product to cart failed", "productId does not exist in store");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant add product To Cart because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "add product to cart failed", e.getMessage());
        }
    }



    @Override
    public Response<String> removeProductFromCart(int userId, int storeId, int productId) {
        try {
            userController.removeProductFromCart(userId, storeId, productId);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId, productId);
            logger.log(Logger.logStatus.Success, "user " + name + "remove " + productName + " from shopping cart on " + LocalDateTime.now());
            return new Response<>("user remove " + productName + " from cart successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant remove product from Cart because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "remove product from cart failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeQuantityInCart(int userId, int storeId, int productId, int change) {
        try {
            userController.changeQuantityInCart(userId, storeId, productId, change);
            String name = userController.getUserName(userId);
            String productName = marketController.getProductName(storeId, productId);
            logger.log(Logger.logStatus.Success, "user " + name + " change quantity to " + change + " on shopping cart on " + LocalDateTime.now());
            return new Response<>("user change Quantity of " + productName + " In Cart to " + change + " successfully ", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant remove change quantity in cart because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "remove product from cart failed", e.getMessage());
        }
    }


    public Response<HashMap<Integer, HashMap<Integer, Integer>>> getCart(int id) {
        try {
            HashMap<Integer, HashMap<Integer, Integer>> cart = userController.getUserCart(id);
            String name = userController.getUserName(id);
            logger.log(Logger.logStatus.Success, "user" + name + "ask for his cart on " + LocalDateTime.now());
            return new Response<>(cart, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant get his cart because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get cart failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeCart(int userId) {
        try{
            userController.removeCart(userId);
            String name = userController.getUserName(userId);
            logger.log(Logger.logStatus.Success, "user" + name + "ask to remove his cart on " + LocalDateTime.now());
            return new Response<>("remove cart succeeded", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant remove his cart because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "remove cart failed", e.getMessage());
        }
    }

    @Override
    public synchronized Response<Receipt> makePurchase(int userId, String accountNumber) {
        try {
            HashMap<Integer, HashMap<Integer, Integer>> cart = userController.getUserCart(userId);
            Pair<Receipt, Set<Integer>> ans = marketController.purchaseProducts(cart, userId);
            Receipt receipt = ans.getFirst();
            Set<Integer> creatorIds = ans.getSecond();
            proxyPayment.makePurchase(accountNumber, receipt.getTotalPrice());
            userController.purchaseMade(userId, receipt.getOrderId(), receipt.getTotalPrice());
            for(int creatorId : creatorIds) {
                String notify = "a new purchase was made in your store";
                Notification<String> notification = new Notification<>(notify);
                userController.addNotification(creatorId, notification);
            }
            marketInfo.addPurchaseCount();
            logger.log(Logger.logStatus.Success, "user made purchase on " + LocalDateTime.now());
            return new Response<>(receipt, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant make purchase " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "make purchase failed", e.getMessage());
        }
    }


    @Override
    public Response<String> changeName(int userId, String token, String newUserName) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeUserName(userId, newUserName);
            String name = userController.getUserName(userId);
            logger.log(Logger.logStatus.Success, "user" + name + "changed name successfully on " + LocalDateTime.now());
            return new Response<>(" u changed details successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant change name because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change name failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeEmail(int userId, String token, String newEmail) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeUserEmail(userId, newEmail);
            String name = userController.getUserName(userId);
            logger.log(Logger.logStatus.Success, "user" + name + "changed email successfully on " + LocalDateTime.now());
            return new Response<>(" u changed details successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant change email because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change email failed", e.getMessage());
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
            logger.log(Logger.logStatus.Success, "user" + name + "changed password successfully on " + LocalDateTime.now());
            return new Response<>(" u changed details successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant change password because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change password failed", e.getMessage());
        }
    }

    @Override
    public Response<Integer> openStore(int userId, String token, String des) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.canOpenStore(userId)) {
                Store store = marketController.openStore(userId, des);
                userController.openStore(userId, store);
                String name = userController.getUserName(userId);
                logger.log(Logger.logStatus.Success, "user" + name + "open store successfully on " + LocalDateTime.now());
                return new Response<>(store.getStoreId(), null, null);
            } else {
                return new Response<>(null, "open store failed", "user is not allowed to open store");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant open store  because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "open store failed", e.getMessage());
        }
    }

    @Override
    public Response<Integer> openStore(int userId, String token, String storeName, String des, String img) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.canOpenStore(userId)) {
                Store store = marketController.openStore(userId, storeName, des, img);
                userController.openStore(userId, store);
                String name = userController.getUserName(userId);
                logger.log(Logger.logStatus.Success, "user" + name + "open store successfully on " + LocalDateTime.now());
                return new Response<>(store.getStoreId(), null, null);
            } else {
                return new Response<>(null, "open store failed", "user is not allowed to open store");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant open store  because " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "open store failed", e.getMessage());
        }
    }

    @Override
    public Response<Info> getMemberInformation(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            Info user = userController.getUserPrivateInformation(userId);
            logger.log(Logger.logStatus.Success, "user received successfully on " + LocalDateTime.now());
            return new Response<>(user, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "user cant received because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get user failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> getUserPurchaseHistory(int userId, String token, int buyerId) {
        try {
            userAuth.checkUser(userId, token);
            if ((userId < 0 || userId == buyerId)) //if it's the admin or the user himself
            {
                HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> orders = userController.getUserPurchaseHistory(userId, buyerId);
                logger.log(Logger.logStatus.Success, "user received orders successfully on " + LocalDateTime.now());
                return new Response<>(orders, null, null);
            }
            logger.log(Logger.logStatus.Fail, "user failed to get purchase history because he doesnt have permission " + LocalDateTime.now());
            return new Response<>(null, "getPurchaseHistory failed", "no permission to see");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get user orders because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get user orders failed", e.getMessage());
        }
    }

    @Override
    public Response<String> writeReviewToStore(int userId, String token, int orderId, int storeId, String content, int grading) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.writeReviewForStore(orderId, storeId, content, grading, userId);
            int creatorId = marketController.addReviewToStore(m);
            m.addOwnerEmail(userController.getUserEmail(creatorId));
            String notify = "a review of has been added for store: " + storeId;
            Notification<String> notification = new Notification<>(notify);
            userController.addNotification(creatorId, notification);
            logger.log(Logger.logStatus.Success, "user write review on store successfully on " + LocalDateTime.now());
            return new Response<>("user write review on store successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant write review on store because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "write review failed", e.getMessage());
        }
    }

    @Override
    public Response<String> writeReviewToProduct(int userId, String token, int orderId, int storeId, int productId, String content, int grading) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.writeReviewForProduct(orderId, storeId, productId, content, grading, userId);
            int creatorId = marketController.writeReviewForProduct(m);
            m.addOwnerEmail(userController.getUserEmail(creatorId));
            logger.log(Logger.logStatus.Success, "user write review on product successfully on " + LocalDateTime.now());
            return new Response<>("user write review successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant write review to product because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "write review failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, Message>> checkReviews(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if(userController.checkPermission(userId, Action.viewMessages, storeId)) {
                HashMap<Integer, Message> reviews = marketController.viewReviews(storeId);
                logger.log(Logger.logStatus.Success, "user checked reviews on store successfully on " + LocalDateTime.now());
                return new Response<>(reviews, null, null);
            }
            else {
                logger.log(Logger.logStatus.Fail, "cant check review to store because the user: " + userId +
                        " does not have permission "+ "on " + LocalDateTime.now());
                return new Response<>(null, "check reviews failed", "user does not have permission");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant check reviews to store because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "check review failed", e.getMessage());
        }
    }

    @Override
    public Response<ProductInfo> getProductInformation(int storeId, int productId) {
        try {
            ProductInfo res = marketController.getProductInformation(storeId, productId);
            logger.log(Logger.logStatus.Success, "user get product information successfully on " + LocalDateTime.now());
            return new Response<>(res, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get product information because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }


    public Response<List<StoreInfo>> getStoresInformation() {
        try {
            List<StoreInfo> res = marketController.getStoresInformation();
            logger.log(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now());
            return new Response<>(res, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get store information because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }
    @Override
    public Response<StoreInfo> getStoreInformation(int storeId) {
        try {
            StoreInfo res = marketController.getStoreInformation(storeId);
            logger.log(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now());
            return new Response<>(res, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get store information because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }

    @Override
    public Response<Store> getStore(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if(userController.checkPermission(userId, Action.seeStoreHistory, storeId)) {
                logger.log(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now());
                return new Response<>(marketController.getStore(storeId), null, null);
            }
            logger.log(Logger.logStatus.Fail, "user can't get the store because he does not have permission");
            return new Response<>(null, "get store failed", "dont have permission");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get store because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get store failed", e.getMessage());
        }
    }


    public Response<String> getStoreDescription(int storeId) {
        try {
            String res = marketController.getStoreDescription(storeId);
            logger.log(Logger.logStatus.Success, "user get store information successfully on " + LocalDateTime.now());
            return new Response<>(res, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get store information because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }

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
            logger.log(Logger.logStatus.Success, "user get store products successfully on " + LocalDateTime.now());
            return new Response<>(res, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get store products because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get products failed", e.getMessage());
        }
    }


    @Override
    public Response<String> sendQuestion(int userId, String token, int storeId, String msg) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.sendQuestionToStore(userId, msg, storeId);
            int creatorId = marketController.addQuestion(m);
            m.addOwnerEmail(userController.getUserEmail(creatorId));
            String notify = "a question of has been added for store: " + storeId;
            Notification<String> notification = new Notification<>(notify);
            userController.addNotification(creatorId, notification);
            logger.log(Logger.logStatus.Success, "user send question successfully on " + LocalDateTime.now());
            return new Response<>("question added successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant send information because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "send information failed", e.getMessage());
        }
    }

    @Override
    public Response<String> sendComplaint(int userId, String token, int orderId, int storeId, String msg) {
        try {
            userAuth.checkUser(userId, token);
            Message m = userController.writeComplaintToMarket(orderId, storeId, msg, userId);
            complaints.put(m.getMessageId(), m);
            logger.log(Logger.logStatus.Success, "user send complaint successfully on " + LocalDateTime.now());
            return new Response<>("user send complaint successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant send complaint because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "send Complaint failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointManager(int userId, String token, String managerToAppoint, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointManager(userController.getUserEmail(userId), managerToAppoint, storeId);
            logger.log(Logger.logStatus.Success, "user appoint " + managerToAppoint + "to Manager in: " + storeId + " successfully on " + LocalDateTime.now());
            return new Response<>("user appointManager successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointManager(int userId, String token, int managerIdToAppoint, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointManager(userId, managerIdToAppoint, storeId);
            logger.log(Logger.logStatus.Success, "user appoint " + managerIdToAppoint + "to Manager in: " + storeId + " successfully on " + LocalDateTime.now());
            return new Response<>("user appointManager successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreDescription(int userId, String token, int storeId, String description) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.changeStoreDetails, storeId)) {
                marketController.setStoreDescription(storeId, description);
                logger.log(Logger.logStatus.Success, "user change store description successfully on " + LocalDateTime.now());
                return new Response<>("user change store description successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant change store description because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change store description failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant change store description because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change store description failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreImg(int userId, String token, int storeId, String img) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.changeStoreDetails, storeId)) {
                marketController.setStoreImg(storeId, img);
                logger.log(Logger.logStatus.Success, "user change store img successfully on " + LocalDateTime.now());
                return new Response<>("user change store img successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant change store img because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change store img failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant change store img because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change store img failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreName(int userId, String token, int storeId, String name) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.changeStoreDetails, storeId)) {
                marketController.setStoreName(storeId, name);
                logger.log(Logger.logStatus.Success, "user change store name successfully on " + LocalDateTime.now());
                return new Response<>("user change store name successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant change store name because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change store name failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant change store img because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change store name failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changePurchasePolicy(int userId, String token, int storeId, String policy) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.changePurchasePolicy, storeId)) {
                marketController.setStorePurchasePolicy(storeId, policy);
                logger.log(Logger.logStatus.Success, "user change store purchase policy successfully on " + LocalDateTime.now());
                return new Response<>("user change store purchase policy successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant change store purchase policy because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change store purchase policy failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant change store purchase policy because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change store policy purchase failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addDiscountPolicy(int userId, String token, int storeId, String policy) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.changeDiscountPolicy, storeId)) {
//                marketController.setStoreDiscountPolicy(storeId, policy);
                logger.log(Logger.logStatus.Success, "user change store policy discount successfully on " + LocalDateTime.now());
                return new Response<>("user change store discount policy successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant change store policy discount because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change store policy discount failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant change store discount policy because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "change store discount policy failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addPurchaseConstraint(int userId, String token, int storeId, String constraint) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.addPurchaseConstraint, storeId)) {
                marketController.addPurchaseConstraint(storeId, constraint);
                logger.log(Logger.logStatus.Success, "user add purchase constraint successfully on " + LocalDateTime.now());
                return new Response<>("user add purchase constraint successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant add purchase constraint because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change add purchase constraint failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant add purchase constraint policy because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "add purchase constraint failed", e.getMessage());
        }
    }

    @Override
    public Response<String> fireManager(int userId, String token, int managerToFire, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.fireManager, storeId)) {
                userController.fireManager(userId, managerToFire, storeId);
                logger.log(Logger.logStatus.Success, "user fire manager successfully on " + LocalDateTime.now());
                return new Response<>("user fire manager successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant fire manager because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change fire manager failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant fire manager because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "fire manager failed", e.getMessage());
        }
    }

    @Override
    public Response<Info> checkWorkerStatus(int userId, String token, int workerId, int storeId) {
        try{
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.checkWorkersStatus, storeId)) {
                Info res = userController.getWorkerInformation(userId, workerId, storeId);
                logger.log(Logger.logStatus.Success, "user check worker status successfully on " + LocalDateTime.now());
                return new Response<>(res, null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant check worker status because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change check worker status failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant check worker status because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "check worker status failed", e.getMessage());
        }
    }

    @Override
    public Response<List<Info>> checkWorkersStatus(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.checkWorkersStatus, storeId)) {
                List<Info> res = userController.getWorkersInformation(userId, storeId);
                logger.log(Logger.logStatus.Success, "user check worker status successfully on " + LocalDateTime.now());
                return new Response<>(res, null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant check worker status because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change check worker status failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant check worker status because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "check worker status failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, Message>> viewQuestions(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.viewMessages, storeId)) {
                HashMap<Integer, Message> res = marketController.getQuestions(storeId);
                logger.log(Logger.logStatus.Success, "user get questions successfully on " + LocalDateTime.now());
                return new Response<>(res, null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant get questions because: user dont have access on " + LocalDateTime.now());
            return new Response<>(null, "get questions status failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant get questions because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "get questions failed", e.getMessage());
        }
    }

    @Override
    public Response<String> answerQuestion(int userId, String token, int storeId, int questionId, String answer) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.answerMessage, storeId)) {
                marketController.answerQuestion(storeId, questionId, answer);
                logger.log(Logger.logStatus.Success, "user answer question successfully on " + LocalDateTime.now());
                return new Response<>("user answer question successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant answer question because: user dont have access on " + LocalDateTime.now());
            return new Response<>(null, "get answer question failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant answer question because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "answer question failed", e.getMessage());
        }
    }

    @Override
    public Response<List<OrderInfo>> seeStoreHistory(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.seeStoreHistory, storeId)) {
                List<OrderInfo> res = marketController.getStoreOrderHistory(storeId);
                logger.log(Logger.logStatus.Success, "user get store history successfully on " + LocalDateTime.now());
                return new Response<>(res, null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant get store history  because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "answer question failed", "user dont have access");
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
            int productId = -1;
            if (userController.checkPermission(userId, Action.addProduct, storeId)) {
                productId = marketController.addProduct(storeId, name, description, price, quantity, categories, img);
            } else {
                logger.log(Logger.logStatus.Fail, "Product Addition Failed, Because: User doesn't have necessary permissions. on: " + LocalDateTime.now());
                return new Response<>(null, "Product Addition Failed", "User doesn't have necessary permissions");
            }
            logger.log(Logger.logStatus.Success, "Add product successfull on: " + LocalDateTime.now());
            return new Response<>(productId, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "Cant add product because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "Product Addition Failed", e.getMessage());
        }

    }

    @Override
    public Response<Integer> addProduct(int userId, String token, int storeId, List<String> categories, String name, String description, int price, int quantity) {
        try {
            userAuth.checkUser(userId, token);
            int productId = -1;
            if (userController.checkPermission(userId, Action.addProduct, storeId)) {
                productId = marketController.addProduct(storeId, name, description, price, quantity, categories);
            } else {
                logger.log(Logger.logStatus.Fail, "Product Addition Failed, Because: User doesn't have necessary permissions. on: " + LocalDateTime.now());
                return new Response<>(null, "Product Addition Failed", "User doesn't have necessary permissions");
            }
            logger.log(Logger.logStatus.Success, "Add product successfull on: " + LocalDateTime.now());
            return new Response<>(productId, null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "Cant add product because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "Product Addition Failed", e.getMessage());
        }

    }

    @Override
    public Response<String> deleteProduct(int userId, String token, int storeId, int productId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.removeProduct, storeId)) {
                marketController.deleteProduct(storeId, productId);
            } else {
                logger.log(Logger.logStatus.Fail, "Can't remove product, user doesnt have permission, On:" + LocalDateTime.now());
                return new Response<>(null, "Cant Remove Product", "User doesn't have permission");
            }
            logger.log(Logger.logStatus.Success, "Delete product successful on: " + LocalDateTime.now());
            return new Response<>("product was successfully deleted", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "Cant delete product because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "Product deletion Failed", e.getMessage());
        }
    }


    //TODO: need to use supplierProxy for changing the quantity
    @Override
    public Response<String> updateProduct(int userId, String token, int storeId, int productId, List<String> categories, String name, String description,
                                          int price, int quantity, String img) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.updateProduct, storeId)) {
                proxySupplier.orderSupplies(storeId, productId, quantity);
                marketController.updateProduct(storeId, productId, categories, name, description, price, quantity, img);
            }
            else {
                logger.log(Logger.logStatus.Fail, "Can't Update product, user doesnt have permission, On:" + LocalDateTime.now());
                return new Response<>(null, "Can't Update Product", "User doesn't have permission");
            }
            logger.log(Logger.logStatus.Success, "Update product successful on: " + LocalDateTime.now());
            return new Response<>("updated product was successful", null, null);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "Cant update product because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "Product Update Failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointOwner(int userId, String token, String owner, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointOwner(userController.getUserEmail(userId), owner, storeId);
            logger.log(Logger.logStatus.Success, "appointed user successfully on " + LocalDateTime.now());
            return new Response<>("user appointManager successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointOwner(int userId, String token, int ownerId, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointOwner(userId, ownerId, storeId);
            logger.log(Logger.logStatus.Success, "appointed user successfully on " + LocalDateTime.now());
            return new Response<>("user appointManager successfully", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant appoint Manager because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> fireOwner(int userId, String token, int ownerId, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            if (userController.checkPermission(userId, Action.fireOwner, storeId)) {
                userController.fireOwner(userId, ownerId, storeId);
                logger.log(Logger.logStatus.Success, "user fire owner successfully on " + LocalDateTime.now());
                return new Response<>("user fire owner successfully", null, null);
            }
            logger.log(Logger.logStatus.Fail, "cant fire owner because: user dont have access" + "on " + LocalDateTime.now());
            return new Response<>(null, "change fire owner failed", "user dont have access");
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant fire owner because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "fire owner failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addManagerPermission(int ownerId, String token, int userId, int storeId, int permissionsId) {
        try {
            userAuth.checkUser(ownerId, token);
            Action a = actionIds.get(permissionsId);
            if(a != null) {
                userController.addManagerAction(ownerId, userId, a, storeId);
                logger.log(Logger.logStatus.Success, "the action " + permissionsId + " has been added to user: " + userId +
                        "on time: " + LocalDateTime.now());
                return new Response<>("add manager permission was successful", null, null);
            }
            else{
                logger.log(Logger.logStatus.Fail, "cant add permission because the action id does not match an action on " + LocalDateTime.now());
                return new Response<>(null, "add permission failed", "no id match");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant add permission because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "add permission failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds) {
        try {
            userAuth.checkUser(ownerId, token);
            for(int permissionsId : permissionsIds) {
                Action a = actionIds.get(permissionsId);
                if(a != null) {
                    userController.addManagerAction(ownerId, userId, a, storeId);
                    logger.log(Logger.logStatus.Success, "the action " + permissionsId + " has been added to user: " + userId +
                            "on time: " + LocalDateTime.now());
                }
                else{
                    logger.log(Logger.logStatus.Fail, "cant add permission because the action id does not match an action on " + LocalDateTime.now());
                    return new Response<>(null, "add permission failed", "no id match");
                }
            }
            return new Response<>("add manager permissions was successful", null, null);
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant add permission because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "add permission failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeManagerPermission(int ownerId, String token, int userId, int storeId, int permissionsId) {
        try {
            userAuth.checkUser(ownerId, token);
            Action a = actionIds.get(permissionsId);
            if(a != null) {
                userController.removeManagerAction(ownerId, userId, a, storeId);
                logger.log(Logger.logStatus.Success, "the action " + permissionsId + " has been removed from user: " + userId +
                        "on time: " + LocalDateTime.now());
                return new Response<>("manager permission was removed successful", null, null);
            }
            else{
                logger.log(Logger.logStatus.Fail, "cant add permission because the action id does not match an action on " + LocalDateTime.now());
                return new Response<>(null, "add permission failed", "no id match");
            }
        } catch (Exception e) {
            logger.log(Logger.logStatus.Fail, "cant remove permission because: " + e.getMessage() + "on " + LocalDateTime.now());
            return new Response<>(null, "remove permission fail", e.getMessage());
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
                    admin.closeStorePermanently(storeId);
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
    public Response<List<ProductInfo>> getProducts() {
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
                            false, null, null, null);
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
        try{
            userAuth.checkUser(userId, token);
        }catch (Exception e){
            logger.log(Logger.logStatus.Fail, "cant add admin because: " + e.getMessage() +" on time: " + LocalDateTime.now());
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
            String hashedPass = userAuth.hashPassword(email, pass, true);
            Admin a = new Admin(adminId.getAndDecrement(), email, hashedPass);
            inActiveAdmins.put(a.getAdminId(), a);
            logger.log(Logger.logStatus.Success, "admin added new admin successfully on " + LocalDateTime.now());
            return new Response<>("admin added new admin successfully", null, null);
        }
        logger.log(Logger.logStatus.Fail, "cant add new admin , u dont have access" + LocalDateTime.now());
        return new Response<>(null, "add admin failed", "u dont have access");
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

    public String addTokenForTests() {
        return userAuth.generateToken(0);
    }
}
