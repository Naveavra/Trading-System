package market;

import database.daos.DaoTemplate;
import database.daos.LoggerDao;
import database.dtos.LoggerDto;
import domain.states.Permissions;
import domain.store.storeManagement.AppHistory;
import domain.user.Member;
import domain.user.StringChecks;
import domain.user.PurchaseHistory;
import domain.user.ShoppingCart;
import org.json.JSONObject;
import service.security.UserAuth;
import service.ExternalService.Supplier.ProxySupplier;
import service.UserController;
import service.ExternalService.Payment.ProxyPayment;
import utils.*;

import domain.store.storeManagement.Store;
import service.MarketController;
import utils.infoRelated.*;
import utils.Response;
import utils.messageRelated.*;
import utils.stateRelated.Action;
import utils.infoRelated.Receipt;


import java.util.*;
import java.util.stream.Collectors;


public class Market implements MarketInterface {
    private final UserController userController;
    private final MarketController marketController;
    //services
    private ProxyPayment proxyPayment;
    private ProxySupplier proxySupplier;
    private UserAuth userAuth;
    private final Logger logger;

    private HashMap<Integer, Action> actionIds;

    private MarketInfo marketInfo;


    private LoggerDao loggerDao;

    public Market(Admin a) {

        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();

        userAuth = new UserAuth();
//        try {
//            proxyPayment = new ProxyPayment();
//            proxySupplier = new ProxySupplier();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        marketInfo = new MarketInfo();

        actionIds = Permissions.getActionIds();

        addAdmin(a);

        loggerDao = new LoggerDao();
    }


    @Override
    public Response<Integer> enterGuest() {
        int guestId = userController.enterGuest();
        marketInfo.addUserCount();
        return logAndRes(Event.LogStatus.Success, "guest has successfully entered",
                StringChecks.curDayString(), userController.getUserName(guestId),
                guestId, null, null);
    }

    @Override
    public Response<String> exitGuest(int guestId) {
        try {
            userController.exitGuest(guestId);
            marketInfo.reduceUserCount();
            return logAndRes(Event.LogStatus.Success, "guest has successfully exited",
                    StringChecks.curDayString(), userController.getUserName(guestId),
                    "existed successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant exit because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(guestId),
                    null, "exit failed", e.getMessage());
        }
    }

    @Override
    public Response<String> register(String email, String pass, String birthday) {
        try {
            String hashedPassword = userAuth.hashPassword(email, pass);
            userController.register(email, pass, hashedPassword, birthday);
            marketInfo.addRegisteredCount();
            return logAndRes(Event.LogStatus.Success, "successfully registered",
                    StringChecks.curDayString(), email,
                    "registered successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant register because " + e.getMessage(),
                    StringChecks.curDayString(), email,
                    null, "register failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> login(String email, String pass) {
        try {
            String hashedPass = userAuth.hashPassword(email, pass);
            int memberId = userController.login(email, hashedPass);
            marketInfo.addUserCount();
            String token = userAuth.generateToken(memberId);
            LoginInformation loginInformation = userController.getLoginInformation(memberId, token);
            return logAndRes(Event.LogStatus.Success, "logged in successfully",
                    StringChecks.curDayString(), email,
                    loginInformation, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant get log in because " + e.getMessage(),
                    StringChecks.curDayString(), email,
                    null, "log in failed", e.getMessage());
        }
    }

    @Override
    public Response<LoginInformation> getMember(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            LoginInformation loginInformation = userController.getLoginInformation(userId, token);
            return new Response<LoginInformation>(loginInformation, null, null);
        } catch (Exception e) {
            return new Response<>(null, "get member failed", e.getMessage());
        }
    }


    @Override
    public Response<String> sendNotification(int userId, String token, NotificationOpcode opcode, String receiverEmail, String notify) {
        try {
            userAuth.checkUser(userId, token);
            String senderEmail = userController.getUserEmail(userId);
            Notification notification = new Notification(opcode, notify + ". from " + senderEmail);
            userController.addNotification(receiverEmail, notification);
            return logAndRes(Event.LogStatus.Success, "notification was sent to " + receiverEmail,
                    StringChecks.curDayString(), senderEmail,
                    "notification sent", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "notification was not sent to " + receiverEmail,
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "notification not sent", e.getMessage());
        }
    }


    public void addNotification(int userId,NotificationOpcode opcode, String notify) throws Exception{
        Notification notification = new Notification(opcode, notify);
        userController.addNotification(userId, notification);
    }

    private List<String> toStringList(List<Notification> notifications) {
        return notifications
                .stream()
                .map(notification -> notification.toString())
                .collect(Collectors.toList());
    }

    @Override
    public Response<List<String>> displayNotifications(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            List<Notification> notifications = userController.displayNotifications(userId);
            return logAndRes(Event.LogStatus.Success, "user got notifications successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    toStringList(notifications), null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant get his notifications because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "display notifications failed", e.getMessage());
        }
    }


    @Override
    public Response<String> logout(int userId) {
        try {
            userController.logout(userId);
            marketInfo.reduceUserCount();
            return logAndRes(Event.LogStatus.Success, "user logged out successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user logged out successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant get log out because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "log out failed", e.getMessage());
        }
    }

    @Override
    public Response<String> addProductToCart(int userId, int storeId, int productId, int quantity) {
        try {
            marketController.checkProductInStore(storeId, productId);
            userController.addProductToCart(userId, storeId, marketController.getProductInformation(storeId, productId), quantity);
            String productName = marketController.getProductName(storeId, productId);
            return logAndRes(Event.LogStatus.Success, "user added " + productName + " " + quantity + " to shopping cart",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user add to cart successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant add product To Cart because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "add product to cart failed", e.getMessage());
        }
    }


    @Override
    public Response<String> removeProductFromCart(int userId, int storeId, int productId) {
        try {
            userController.removeProductFromCart(userId, storeId, productId);
            String productName = marketController.getProductName(storeId, productId);
            return logAndRes(Event.LogStatus.Success, "user removed " + productName + " from shopping cart",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user removed " + productName + " from cart successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant remove product from Cart because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "remove product from cart failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeQuantityInCart(int userId, int storeId, int productId, int change) {
        try {
            userController.changeQuantityInCart(userId, storeId, marketController.getProductInformation(storeId, productId), change);
            String productName = marketController.getProductName(storeId, productId);
            return logAndRes(Event.LogStatus.Success, "user changed quantity for " + productName + " to " + change,
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user change Quantity of " + productName + " In Cart to " + change + " successfully ", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant remove change quantity in cart because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "remove product from cart failed", e.getMessage());
        }
    }

    public Response<List<? extends Information>> getCart(int id) {
        try {
            List<? extends Information> baskets = userController.getUserCart(id).getContent();
            return logAndRes(Event.LogStatus.Success, "user asked for his cart",
                    StringChecks.curDayString(), userController.getUserName(id),
                    baskets, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant get his cart because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(id),
                    null, "get cart failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeCart(int userId) {
        try {
            userController.removeCart(userId);
            return logAndRes(Event.LogStatus.Success, "user cleaned his cart",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "remove cart succeeded", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant remove his cart because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "remove cart failed", e.getMessage());
        }
    }

    @Override
    public synchronized Response<Receipt> makePurchase(int userId, JSONObject payment, JSONObject supplier) {
        try {
            ShoppingCart cart = new ShoppingCart(userController.getUserCart(userId));
            int totalPrice = marketController.calculatePrice(cart);
            Pair<Receipt, Set<Integer>> ans = marketController.purchaseProducts(cart, userController.getUser(userId), totalPrice);
//            proxyPayment.makePurchase(payment, totalPrice);
//            proxySupplier.orderSupplies(supplier, cart);
            Receipt receipt = ans.getFirst();
            Set<Integer> creatorIds = ans.getSecond();
            userController.purchaseMade(userId, receipt);
            for (int creatorId : creatorIds)
                addNotification(creatorId, NotificationOpcode.PURCHASE_IN_STORE, "a new purchase was made in your store");
            marketInfo.addPurchaseCount();
            return logAndRes(Event.LogStatus.Success, "user made purchase",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    receipt, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant make purchase " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "make purchase failed", e.getMessage());
        }
    }


    @Override
    public Response<String> changeMemberAttributes(int userId, String token, String newEmail, String newBirthday) {
        try {
            userAuth.checkUser(userId, token);
            userController.changeMemberAttributes(userId, newEmail, newBirthday);
            return logAndRes(Event.LogStatus.Success, "user changed attributes successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    " you changed details successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant change attributes because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "change attributes failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeMemberPassword(int userId, String token, String oldPass, String newPass) {
        try {
            userAuth.checkUser(userId, token);
            String oldHashedPass = userAuth.hashPassword(userController.getUserName(userId), oldPass);
            String newHashedPass = userAuth.hashPassword(userController.getUserName(userId), newPass);
            userController.changeMemberPassword(userId, oldHashedPass, newPass, newHashedPass);
            return logAndRes(Event.LogStatus.Success, "user changed attributes successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    " you changed details successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant change attributes because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "change attributes failed", e.getMessage());
        }
    }

    @Override
    public Response<Integer> openStore(int userId, String token, String storeName, String des, String img) {
        try {
            userAuth.checkUser(userId, token);
            Store store = marketController.openStore(userController.getActiveMember(userId), storeName, des, img);
            userController.openStore(userId, store);
            return logAndRes(Event.LogStatus.Success, "user opened store successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    store.getStoreId(), null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant open store  because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "open store failed", e.getMessage());
        }
    }

    @Override
    public Response<PurchaseHistory> getUserPurchaseHistory(int userId, String token, int buyerId) {
        try {
            userAuth.checkUser(userId, token);
            PurchaseHistory orders;
            orders = userController.getUserPurchaseHistory(userId, buyerId);
            return logAndRes(Event.LogStatus.Success, "user received orders successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    orders, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant get user orders because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "get user orders failed", e.getMessage());
        }
    }

    //TODO: add external service for messages and notifications
    @Override
    public Response<String> writeReviewToStore(int userId, String token, int orderId, String storeName, String content, int grading) {
        try {
            userAuth.checkUser(userId, token);
            int storeId = marketController.getStoreId(storeName);
            StoreReview m = userController.writeReviewForStore(orderId, storeId, content, grading, userId);
            int creatorId = marketController.addReviewToStore(m);
            addNotification(creatorId, NotificationOpcode.STORE_REVIEW, "a review of has been added for store: " + storeId);
            return logAndRes(Event.LogStatus.Success, "user wrote review on store successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user write review on store successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant write review on store because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "write review failed", e.getMessage());
        }
    }


    @Override
    public Response<String> writeReviewToProduct(int userId, String token, int orderId, int storeId, int productId, String content, int grading) {
        try {
            userAuth.checkUser(userId, token);
            ProductReview p = userController.writeReviewForProduct(orderId, storeId, productId, content, grading, userId);
            int creatorId = marketController.writeReviewForProduct(p);
            addNotification(creatorId, NotificationOpcode.PRODUCT_REVIEW, "a review of has been added for product: " + productId + " in store: " + storeId);
            return logAndRes(Event.LogStatus.Success, "user wrote review on product successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user write review successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant write review to product because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "write review failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> checkReviews(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            List<StoreReview> reviews = marketController.viewReviews(storeId);
            return logAndRes(Event.LogStatus.Success, "user checked reviews of store " + storeId + " successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    reviews, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant check reviews of store " + storeId + " because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "check review failed", e.getMessage());
        }
    }

    @Override
    public Response<ProductInfo> getProductInformation(int storeId, int productId) {
        try {
            ProductInfo res = marketController.getProductInformation(storeId, productId);
            return new Response<>(res, null, null);
        } catch (Exception e) {
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }


    public Response<List<? extends Information>> getStoresInformation() {
        try {
            List<StoreInfo> res = marketController.getStoresInformation();
            return new Response<>(res, null, null);
        } catch (Exception e) {
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }

    @Override
    public Response<StoreInfo> getStoreInformation(int storeId) {
        try {
            StoreInfo res = marketController.getStoreInformation(storeId);
            return new Response<>(res, null, null);
        } catch (Exception e) {
            return new Response<>(null, "get product information failed", e.getMessage());
        }
    }

    @Override
    public Response<Store> getStore(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            return logAndRes(Event.LogStatus.Success, "user got store information successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    marketController.getStore(storeId), null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant get store because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "get store failed", e.getMessage());
        }
    }

    //TODO: need someone to edit filter(both functions) to fit the template of the rest of the functions
    @Override
    public Response showFilterOptions() {
        return new Response<>(marketController.showFilterOptions(), null, null);
    }

    @Override
    public Response<List<? extends Information>> filterBy(HashMap<String, String> filterOptions) {
        ArrayList<ProductInfo> result = marketController.filterBy(filterOptions);
        if (result.isEmpty()) {
            return new Response<>(null, "No products found by those filter options", "result array is empty, no products found");
        }
        return new Response<>(result, null, null);
    }

    @Override
    public Response<List<? extends Information>> getStoreProducts(int storeId) {
        try {
            List<ProductInfo> res = marketController.getStoreProducts(storeId);
            return new Response<>(res, null, null);
        } catch (Exception e) {
            return new Response<>(null, "get products failed", e.getMessage());
        }
    }


    @Override
    public Response<String> sendQuestion(int userId, String token, int storeId, String msg) {
        try {
            userAuth.checkUser(userId, token);
            Question q = userController.sendQuestionToStore(userId, storeId, msg);
            int creatorId = marketController.addQuestion(q);
            addNotification(creatorId, NotificationOpcode.QUESTION, "a question of has been added for store: " + storeId);
            return logAndRes(Event.LogStatus.Success, "user sent question to store " + storeId + " successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "question added successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant send information because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "send information failed", e.getMessage());
        }
    }

    @Override
    public Response<String> sendComplaint(int userId, String token, int orderId, String msg) {
        try {
            userAuth.checkUser(userId, token);
            userController.writeComplaintToMarket(orderId, msg, userId);
            return logAndRes(Event.LogStatus.Success, "user send complaint successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user send complaint successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant send complaint because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "send Complaint failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointManager(int userId, String token, String managerToAppoint, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointManager(userId, managerToAppoint, storeId);
            return logAndRes(Event.LogStatus.Success, "user appoint " + managerToAppoint + " to Manager in: " + storeId + " successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user appointManager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant appoint Manager because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "appoint Manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> fireManager(int userId, String token, int managerToFire, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.fireManager(userId, managerToFire, storeId);
            return logAndRes(Event.LogStatus.Success, "user fire manager successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user fire manager successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant fire manager because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "fire manager failed", e.getMessage());
        }
    }

    @Override
    public Response<String> appointOwner(int userId, String token, String owner, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.appointOwner(userId, owner, storeId);
            return logAndRes(Event.LogStatus.Success, "appointed user successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user appoint owner successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant appoint owner because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "appoint owner failed", e.getMessage());
        }
    }

    @Override
    public Response<String> fireOwner(int userId, String token, int ownerId, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.fireOwner(userId, ownerId, storeId);
            return logAndRes(Event.LogStatus.Success, "user fire owner successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user fire owner successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant fire owner because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "fire owner failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreInfo(int userId, String token, int storeId, String name, String description,
                                            String img, String isActive) {
        try {
            String ans;
            userAuth.checkUser(userId, token);
            if (!isActive.equals("null"))
                ans = changeStoreActive(userId, storeId, isActive);
            else
                ans = changeStoreAttributes(userId, storeId, name, description, img);
            return logAndRes(Event.LogStatus.Success, ans,
                    StringChecks.curDayString(), userController.getUserName(userId)
                    , ans, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "change store info failed because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "change info failed", e.getMessage());
        }
    }

    @Override
    public String changeStoreActive(int userId, int storeId, String isActive) throws Exception {
        return userController.changeStoreActive(userId, storeId, isActive);
    }

    @Override
    public String changeStoreAttributes(int userId, int storeId, String name, String description, String img) throws Exception {
        userController.checkPermission(userId, Action.changeStoreDetails, storeId);
        marketController.setStoreAttributes(storeId, name, description, img);
        return "the store attributes have been changed accordingly";
    }

    @Override
    public Response<String> changePurchasePolicy(int userId, String token, int storeId, String policy) {
        try {
            userAuth.checkUser(userId, token);
            marketController.setStorePurchasePolicy(storeId, policy);
            return logAndRes(Event.LogStatus.Success, "user change store purchase policy successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user change store purchase policy successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant change store purchase policy because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
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
//                logger.log(Event.LogStatus.Success, "user change store policy discount successfully on " + LocalDateTime.now());
//                return new Response<>("user change store discount policy successfully", null, null);
//            }
//            logger.log(Event.LogStatus.Fail, "cant change store policy discount because: user dont have access" + "on " + LocalDateTime.now());
//            return new Response<>(null, "change store policy discount failed", "user dont have access");
//        } catch (Exception e) {
//            logger.log(Event.LogStatus.Fail, "cant change store discount policy because: " + e.getMessage() + "on " + LocalDateTime.now());
//            return new Response<>(null, "change store discount policy failed", e.getMessage());
//        }
//    }

    @Override
    public Response<String> addPurchaseConstraint(int userId, String token, int storeId, String constraint) {
        try {
            userAuth.checkUser(userId, token);
//            marketController.addPurchaseConstraint(storeId, constraint);
            return logAndRes(Event.LogStatus.Success, "user add purchase constraint successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user add purchase constraint successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant add purchase constraint policy because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "add purchase constraint failed", e.getMessage());
        }
    }

    @Override
    public Response<Info> checkWorkerStatus(int userId, String token, int workerId, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            Info res = userController.getWorkerInformation(userId, workerId, storeId);
            return logAndRes(Event.LogStatus.Success, "user check worker status successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant check worker status because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "check worker status failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> checkWorkersStatus(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            List<Info> res = userController.getWorkersInformation(userId, storeId);
            return logAndRes(Event.LogStatus.Success, "user check worker status successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant check worker status because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "check worker status failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> viewQuestions(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            List<Message> res = marketController.getQuestions(storeId);
            return logAndRes(Event.LogStatus.Success, "user get questions successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant get questions because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "get questions failed", e.getMessage());
        }
    }

    @Override
    public Response<String> answerQuestion(int userId, String token, int storeId, int questionId, String answer) {
        try {
            userAuth.checkUser(userId, token);
            marketController.answerQuestion(storeId, questionId, answer);
            return logAndRes(Event.LogStatus.Success, "user answer question successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user answer question successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant answer question because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "answer question failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> seeStoreHistory(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.seeStoreHistory, storeId);
            List<OrderInfo> res = marketController.getStoreOrderHistory(storeId);
            return logAndRes(Event.LogStatus.Success, "user get store history successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant  get store history  because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, " get store history failed", e.getMessage());
        }
    }

    @Override
    public Response<Integer> addProduct(int userId, String token, int storeId, List<String> categories, String name, String description,
                                        int price, int quantity, String img) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.addProduct, storeId);
            int productId = marketController.addProduct(storeId, name, description, price, quantity, categories, img);
            return logAndRes(Event.LogStatus.Success, "Add product successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    productId, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "Cant add product because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "Product Addition Failed", e.getMessage());
        }

    }

    @Override
    public Response<String> deleteProduct(int userId, String token, int storeId, int productId) {
        try {
            userAuth.checkUser(userId, token);
            marketController.deleteProduct(storeId, productId);
            return logAndRes(Event.LogStatus.Success, "Delete product successful",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "product was successfully deleted", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "Cant delete product because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "Product deletion Failed", e.getMessage());
        }
    }

    @Override
    public Response<String> updateProduct(int userId, String token, int storeId, int productId, List<String> categories, String name, String description,
                                          int price, int quantity, String img) {
        try {
            userAuth.checkUser(userId, token);
            marketController.updateProduct(storeId, productId, categories, name, description, price, quantity, img);
            return logAndRes(Event.LogStatus.Success, "Update product successful",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "updated product was successful", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "Cant update product because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "Product Update Failed", e.getMessage());
        }
    }


    @Override
    public Response<String> addManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds) {
        try {
            userAuth.checkUser(ownerId, token);
            List<Action> actions = new ArrayList<>();
            for (int permissionId : permissionsIds)
                actions.add(actionIds.get(permissionId));
            userController.addManagerActions(ownerId, userId, actions, storeId);
            return logAndRes(Event.LogStatus.Success, "added all permissions to " + userId,
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "add manager permissions was successful", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "can't add all permissions because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "add permissions failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds) {
        try {
            userAuth.checkUser(ownerId, token);
            List<Action> actions = new ArrayList<>();
            for (int permissionId : permissionsIds)
                actions.add(actionIds.get(permissionId));
            userController.removeManagerActions(ownerId, userId, actions, storeId);
            return logAndRes(Event.LogStatus.Success, "removed all permissions from " + userId,
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "remove manager permissions was successful", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "can't remove all permissions because: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "remove permissions failed", e.getMessage());
        }
    }


    @Override
    public Response<AppHistory> getAppointments(int userId, String token, int storeId) {
        try {
            userAuth.checkUser(userId, token);
            AppHistory res = marketController.getAppointments(storeId);
            return logAndRes(Event.LogStatus.Success, "user get appointments",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    res, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant get appointments: " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "get appointments failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> getProducts() {
        try {
            List<ProductInfo> products = marketController.getAllProducts();
            return new Response<>(products, null, null);
        } catch (Exception e) {
            return new Response<>(null, "get store products failed", e.getMessage());
        }
    }

    @Override
    public Response<String> closeStorePermanently(int adminId, String token, int storeId) {
        try {
            userAuth.checkUser(adminId, token);
            userController.closeStorePermanently(adminId, storeId);
            return logAndRes(Event.LogStatus.Success, "the store: " + storeId + " has been permanently closed by admin: " + adminId,
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    "close was permanently closed", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "the user: " + adminId + "  cannot close store because: " + e.getMessage(),
                    StringChecks.curDayString(), "admin" + adminId,
                    null, "close permanent not successfully", e.getMessage());
        }
    }

    @Override
    public Response<String> addAdmin(int userId, String token, String email, String pass) {
        try {
            userAuth.checkUser(userId, token);
            String hashedPass = userAuth.hashPassword(email, pass);
            Admin admin = userController.addAdmin(userId, email, hashedPass, pass);
            admin.addControllers(userController, marketController);
            return logAndRes(Event.LogStatus.Success, "admin added new admin successfully",
                    StringChecks.curDayString(), "admin" + userId,
                    "admin added new admin successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "add admin failed, " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "add admin failed", e.getMessage());
        }
    }

    private void addAdmin(Admin a) {
        String hashedPass = userAuth.hashPassword(a.getName(), a.getPassword());
        Admin admin = userController.addAdmin(a, hashedPass);
        admin.addControllers(userController, marketController);
    }

    @Override
    public Response<String> removeAdmin(int adminId, String token) {
        try {
            userAuth.checkUser(adminId, token);
            userController.removeAdmin(adminId);
            return logAndRes(Event.LogStatus.Success, "admin removed himself successfully",
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    "u removed u self successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant remove admin because: " + e.getMessage(),
                    StringChecks.curDayString(), "admin" + adminId,
                    null, "remove admin failed", e.getMessage());
        }
    }

    @Override
    public Response<HashMap<Integer, Admin>> getAdmins(int adminId, String token) {
        try {
            userAuth.checkUser(adminId, token);
            HashMap<Integer, Admin> list = userController.getAdmins(adminId);
            return logAndRes(Event.LogStatus.Success, "admin get all admins successfully",
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    list, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "cant get admins because: " + e.getMessage(),
                    StringChecks.curDayString(), "admin" + adminId,
                    null, "get admins failed", e.getMessage());
        }
    }


    /**
     * return json of all the relevant information about the users: email, id, name
     */
    @Override
    public Response<List<PurchaseHistory>> getUsersPurchaseHistory(int adminId, String token) {
        try {
            userAuth.checkUser(adminId, token);
            List<PurchaseHistory> users = userController.getUsersPurchaseHistory(adminId);
            return logAndRes(Event.LogStatus.Success, "admin get users successfully",
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    users, null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user failed getting all users because :" + e.getMessage(),
                    StringChecks.curDayString(), "admin" + adminId,
                    null, "get users", e.getMessage());
        }
    }

    @Override
    public Response<String> answerComplaint(int adminId, String token, int complaintId, String ans) {
        try {
            userAuth.checkUser(adminId, token);
            userController.answerComplaint(adminId, complaintId, ans);
            return logAndRes(Event.LogStatus.Success, "admin answer complaint successfully",
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    "admin answer complaint", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user failed answer complaints because:" + e.getMessage(),
                    StringChecks.curDayString(), "admin" + adminId,
                    null, "answer complaint failed", e.getMessage());
        }
    }

    @Override
    public Response<List<? extends Information>> getComplaints(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            List<Complaint> complaints = userController.getComplaints(userId);
            return new Response<>(complaints, null, null);
        } catch (Exception e) {
            return new Response<>(null, "could not get complaints", e.getMessage());
        }
    }

    @Override
    public Response changeRegularDiscount(int userId, String token, int storeId, int prodId, int percentage, String discountType, String discountedCategory, List<String> predicatesLst) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.changeDiscountPolicy, storeId);
            marketController.changeRegularDiscount(storeId, prodId, percentage, discountType,
                    discountedCategory, predicatesLst);
            return logAndRes(Event.LogStatus.Success, "user changed discount successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user changed discount policy", null, null);
        } catch (Exception e) {
            return new Response<>(null, "could not get complaints", e.getMessage());
        }
    }

    @Override
    public Response placeBid(String token, int userId, int storeId, int prodId, double price,int quantity) {
        try {
            userAuth.checkUser(userId, token);
            // im assuming there is no need to check permission for this action
            Member user = userController.getMember(userId);
            marketController.placeBid(storeId, user, prodId, price,quantity);
            //usercontoller send notification to other store owners TODO ELI
            return logAndRes(Event.LogStatus.Success, "user placed his successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user placed his bid", null, null);
        } catch (Exception ex) {
            return new Response<>(null, "could not place bid", ex.getMessage());
        }
    }
    @Override
    public Response editBid(String token, int userId, int storeId, int prodId, double price,int quantity, int bidId) {
        try {
            userAuth.checkUser(userId, token);
            // im assuming there is no need to check permission for this action
            Member user = userController.getMember(userId);
            marketController.editBid(storeId, user, prodId, price,quantity);
            //usercontoller send notification to other store owners TODO ELI
            return logAndRes(Event.LogStatus.Success, "user placed his successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user placed his bid", null, null);
        } catch (Exception ex) {
            return new Response<>(null, "could not place bid", ex.getMessage());
        }
    }


    @Override
    public Response answerBid(String token, int userId, int storeId, boolean ans, int prodId, int bidId) {
        try {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.updateProduct, storeId);
            marketController.answerBid(userId, storeId, ans, prodId, bidId);
            return logAndRes(Event.LogStatus.Success, "user answer the bid",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user answer the bid", null, null);
        }
        catch (Exception ex) {
            return new Response<>(null, "could not answer bid", ex.getMessage());
        }
    }
    @Override
    public Response counterBid(String token, int userId, int storeId, int ans, int prodId, int bidId){
        try{
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.updateProduct, storeId);
            marketController.counterBid(userId, storeId, ans, prodId, bidId);
            return logAndRes(Event.LogStatus.Success, "user answer with counter bid",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user answer with counter bid", null, null);
    }
        catch (Exception ex) {
        return new Response<>(null, "could not answer bid", ex.getMessage());
    }
    }

    @Override
    public Response<String> cancelMembership(int adminId, String token, String userToRemove) {
        try {
            userAuth.checkUser(adminId, token);
            userController.cancelMembership(adminId, userToRemove);
            return logAndRes(Event.LogStatus.Success, "admin cancel Membership successfully",
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    "admin cancel Membership complaint", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user failed cancel Membership because:" + e.getMessage(),
                    StringChecks.curDayString(), "admin"+adminId,
                    null, "cancel Membership failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeUser(String userName) {
        try{
            userController.removeUser(userName);
            return new Response<>("the user was successfully removed", null, null);
        }catch (Exception e){
            return new Response<>(null, "cant remove user", e.getMessage());
        }
    }

    @Override
        public Response<List<? extends Information>> watchEventLog(int adminId, String token){
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            return new Response<>(logger.getEventMap(), null, null);
        }catch (Exception e){
            return new Response<>(null, "watch log failed", e.getMessage());
        }
    }

    @Override
    public Response<MarketInfo> watchMarketStatus(int adminId, String token) {
        try {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            marketInfo.calculateAverages();
            return logAndRes(Event.LogStatus.Success, "admin get market status successfully",
                    StringChecks.curDayString(), userController.getUserName(adminId),
                    marketInfo, null, null);
        }catch (Exception e){
            return logAndRes(Event.LogStatus.Fail, "cant watch market status because: " + e.getMessage(),
                    StringChecks.curDayString(), "admin"+adminId,
                    null, "watch market status failed", e.getMessage());
        }
    }

    //TODO: ADD logger to those functions
    @Override
    public Response setPaymentService(int adminId, String token, String paymentService) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.setRealPayment(paymentService);
            return new Response("Set payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Set Payment Service", "Set payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response getPaymentServicePossible(int adminId, String token) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            return new Response(proxyPayment.getPaymentServicesPossibleOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Possible Payment Service", "Get possible payment service fail: " + e.getMessage());
        }
    }

    //TODO: fix template for this function
    @Override
    public Response getPaymentServiceAvailable() {
        try{
            //TODO: userAuth.checkUser(userId);
            return new Response(proxyPayment.getPaymentServicesAvailableOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Available Payment Service", "Get available payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response addPaymentService(int adminId, String token, String paymentService) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.addPaymentService(paymentService);
            return new Response("Add payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Payment Service", "Add payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response removePaymentService(int adminId, String token, String paymentService) {
        Admin admin = null;
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.removePaymentService(paymentService);
            return new Response("Add payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Remove Payment Service", "Remove payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response setSupplierService(int adminId, String token, String supplierService) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxySupplier.setRealSupplier(supplierService);
            return new Response("Set supplier service to: " + supplierService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Set Supplier Service", "Set supplier service fail: " + e.getMessage());
        }
    }

    @Override
    public Response getSupplierServicePossible(int adminId, String token) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            return new Response(proxySupplier.getSupplierServicesPossibleOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Possible Supplier Service", "Get possible supplier service fail: " + e.getMessage());
        }
    }

    //TODO: fix template for this function
    @Override
    public Response getSupplierServiceAvailable() {
        try{
            return new Response(proxySupplier.getSupplierServicesAvailableOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get Available Supplier Service", "Get available supplier service fail: " + e.getMessage());
        }
    }

    @Override
    public Response addSupplierService(int adminId, String token, String supplierService) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxySupplier.addSupplierService(supplierService);
            return new Response("Add supplier service to: " + supplierService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Supplier Service", "Add supplier service fail: " + e.getMessage());
        }
    }


    @Override
    public Response removeSupplierService(int adminId, String token, String supplierService) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
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

    @Override
    public Response<List<ProductInfo>> getProducts(int storeId){
        try{
            List<ProductInfo> products = marketController.getStoreProducts(storeId);
            return new Response<>(products, null, null);
        }catch(Exception e){
            return new Response<>(null, "get store products failed", e.getMessage());
        }
    }

    public int getAdminSize(){
        return userController.getAdminSize();
    }

    //atomic function to log and get response
    public Response logAndRes(Event.LogStatus state, String content, String time, String userName,
                              Object value, String errorTi , String errorMsg) {
        Event event = new Event(state, content, time, userName);
        logger.log(event);
        DaoTemplate.save(event);
        return new Response<>(value, errorTi, errorMsg);
    }

      
    public Response<List<String>> getMemberNotifications(int userId, String token) {
        try {
            return new Response<List<String>>(displayNotifications(userId, token).getValue(), null, null);
        }catch (Exception e){
            return new Response<>(null, "get member notifications failed", e.getMessage());
        }
    }

    public Response<Notification> getNotification(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            return logAndRes(Event.LogStatus.Success, "Member get notification " + userId + " has successfully entered",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    userController.getNotification(userId), null, null);
        }catch (Exception e){
            return new Response<>(null, "get member notifications failed", e.getMessage());
        }
    }

    //database
    public Response<String> saveState(){
        try {
            for (Event e : logger.getEventMap())
                loggerDao.saveLog(new LoggerDto(e.getStatus().toString(), e.getContent(), e.getTime(), e.getUserName()));
            userController.saveState();
            return new Response<>("save state succeeded", null, null);
        }catch (Exception e){
            return new Response<>(null, "save state failed", e.getMessage());
        }
    }

    public Response<String> updateState() {
        try {
            for (Event e : logger.getEventMap())
                loggerDao.updateLog(new LoggerDto(e.getStatus().toString(), e.getContent(), e.getTime(), e.getUserName()));
            userController.updateState();
            return new Response<>("update state succeeded", null, null);
        }catch (Exception e){
            return new Response<>(null, "update state failed", e.getMessage());
        }
    }

}
