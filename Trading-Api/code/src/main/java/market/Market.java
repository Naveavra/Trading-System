package market;

import database.HibernateUtil;
import database.daos.Dao;
import database.daos.LoggerDao;
import domain.states.Permissions;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Bid;
import domain.user.Member;
import domain.user.StringChecks;
import domain.user.PurchaseHistory;
import domain.user.ShoppingCart;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import server.Config.ESConfig;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Supplier.SupplierAdapter;
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


public class Market implements MarketInterface {
    private static UserController userController;
    private static MarketController marketController;
    //services
    private ProxyPayment proxyPayment;
    private ProxySupplier proxySupplier;
    private UserAuth userAuth;
    private final Logger logger;

    private HashMap<Integer, Action> actionIds;
    private MarketInfo marketInfo;

    public Market(Admin admin, ESConfig payment, ESConfig supply) {

        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();

        userAuth = new UserAuth();

        try {
            proxyPayment = new ProxyPayment(new ESConfig());
        } catch (Exception e) {
            System.out.println("Error with the connection to the external payment service: " + e.getMessage());
            System.exit(-1);
        }

        try {
            proxySupplier = new ProxySupplier(new ESConfig());
        } catch (Exception e) {
            System.out.println("Error with the connection to the external supplier service: " + e.getMessage());
            System.exit(-1);
        }

        marketInfo = new MarketInfo();

        actionIds = Permissions.getActionIds();

        addAdmin(admin);
    }

    public Market(Admin admin) {

        logger = Logger.getInstance();
        userController = new UserController();
        marketController = new MarketController();

        userAuth = new UserAuth();
//        TODO
//        try {
//            proxyPayment = new ProxyPayment(payment);
//            proxySupplier = new ProxySupplier(supply);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        try {
            proxyPayment = new ProxyPayment(new ESConfig());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        try {
            proxySupplier = new ProxySupplier(new ESConfig());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        marketInfo = new MarketInfo();

        actionIds = Permissions.getActionIds();

        Response<String> res = addAdmin(admin);
        if(res.errorOccurred()) {
            //TODO: what to throw here
            //throw new Exception(res.getErrorMessage());
        }
    }


    @Override
    public Response<Integer> enterGuest() {
        int guestId = userController.enterGuest();
        marketInfo.addGuestIn();
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hashedPassword = userAuth.hashPassword(email, pass);
            userController.register(email, pass, hashedPassword, birthday, session);
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
            marketInfo.addMemberIn();
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            String senderEmail = userController.getUserEmail(userId);
            Notification notification = new Notification(opcode, notify + ". from " + senderEmail);
            userController.addNotification(receiverEmail, notification, session);
            return logAndRes(Event.LogStatus.Success, "notification was sent to " + receiverEmail,
                    StringChecks.curDayString(), senderEmail,
                    "notification sent", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "notification was not sent to " + receiverEmail,
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "notification not sent", e.getMessage());
        }
    }


    public void addNotification(int userId,NotificationOpcode opcode, String notify, Session session) throws Exception{
        Notification notification = new Notification(opcode, notify);
        userController.addNotification(userId, notification, session);
    }

    @Override
    public Response<List<Notification>> displayNotifications(int userId, String token) {
        try {
            userAuth.checkUser(userId, token);
            List<Notification> notifications = userController.displayNotifications(userId);
            return logAndRes(Event.LogStatus.Success, "user got notifications successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    notifications, null, null);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            marketController.checkProductInStore(storeId, productId);
            userController.addProductToCart(userId, storeId, marketController.getProductInformation(storeId, productId), quantity, session);
            String productName = marketController.getProductName(storeId, productId);
            if(!userController.isGuest(userId))
                addNotification(userId,NotificationOpcode.GET_CLIENT_DATA,"null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userController.removeProductFromCart(userId, storeId, productId, session);
            String productName = marketController.getProductName(storeId, productId);
            if(!userController.isGuest(userId))
                addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userController.changeQuantityInCart(userId, storeId, marketController.getProductInformation(storeId, productId), change, session);
            String productName = marketController.getProductName(storeId, productId);
            if(!userController.isGuest(userId))
                addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userController.removeCart(userId);
            if(!userController.isGuest(userId))
                addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "user cleaned his cart",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "remove cart succeeded", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant remove his cart because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "remove cart failed", e.getMessage());
        }
    }

    private JSONObject getStorePaymentDetails(int storeId)
    {
        JSONObject storeDetails = new JSONObject();
        storeDetails.put("Account Number", 0);
        return storeDetails;
    }

    @Override
    public synchronized Response<Receipt> purchaseBid(String token, int userId, int storeId, int bidId, JSONObject paymentDetails, JSONObject supplierDetails) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            Bid bid = marketController.getBid(storeId, bidId);
            proxyPayment.makePurchase(paymentDetails, getStorePaymentDetails(storeId), bid.getOffer());
            proxySupplier.orderSupplies(supplierDetails, storeId, bid.getProductId(), bid.getQuantity());
            Pair<Receipt, Set<Integer>> ans = marketController.purchaseBid(userController.getUser(userId), storeId,
                    bid.getProductId(), bid.getOffer(), bid.getQuantity(), session);
            bid.setStatus(Bid.status.Completed, session);
            addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
            return getReceiptResponse(userId, ans, session);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant make purchase " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user cant make purchase " + e.getMessage(), "make purchase failed", e.getMessage());
        }
    }

    @Override
    public Response clientAcceptCounter(String token, int bidId, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(marketController.getBidClient(bidId, storeId), token);
            List<String> creators = marketController.getBidApprovers(bidId, storeId);
            marketController.clientAcceptCounter(bidId, storeId);
            for(String name : creators)
                userController.addNotification(name, new Notification(NotificationOpcode.GET_STORE_DATA,
                        "a new bid was placed in store: " + storeId), session);
            addNotification(marketController.getBidClient(bidId, storeId), NotificationOpcode.GET_CLIENT_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "user accept counter successfully",
                    StringChecks.curDayString(), userController.getUserName(marketController.getBidClient(bidId, storeId)),
                    "user accept counter successfully", null, null);
        } catch (Exception ex) {
            return new Response<>(null, "could not place bid", ex.getMessage());
        }
    }

    @Override
    public Response addCompositeDiscount(String token, String body) throws Exception {
        int userId = -1;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            JSONObject request = new JSONObject(body);
            userId = Integer.parseInt(request.get("userId").toString());
            userAuth.checkUser(userId, token);
            marketController.addCompositeDiscount(body, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "user added a composite discount successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),"Added a composite discount",null,null);
        }
        catch (Exception e){
            return logAndRes(Event.LogStatus.Fail, "User failed to add a composite discount " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "add composite discount failed", e.getMessage());
        }
    }

    private Response<Receipt> getReceiptResponse(int userId, Pair<Receipt, Set<Integer>> ans, Session session) throws Exception {
        Receipt receipt = ans.getFirst();
        Set<Integer> creatorIds = ans.getSecond();
        userController.purchaseMade(userId, receipt, session);
        for (int creatorId : creatorIds)
            addNotification(creatorId, NotificationOpcode.GET_STORE_DATA, "a new purchase was made in your store", session);
        marketInfo.addPurchaseCount();
        return logAndRes(Event.LogStatus.Success, "user made purchase",
                StringChecks.curDayString(), userController.getUserName(userId),
                "success make purchase , u can see the receipt in the personal area", null, null);
    }

    @Override
    public synchronized Response<Receipt> makePurchase(int userId, JSONObject payment, JSONObject supplier) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ShoppingCart cart = new ShoppingCart(userController.getUserCart(userId));
            int totalPrice = marketController.calculatePrice(cart);
            proxyPayment.makePurchase(payment, getStorePaymentDetails(cart), totalPrice);
            proxySupplier.orderSupplies(supplier, cart);
            Pair<Receipt, Set<Integer>> ans = marketController.purchaseProducts(cart, userController.getUser(userId), totalPrice, session);
            if (!userController.isGuest(userId))
                userController.addNotification(userId, new Notification(NotificationOpcode.GET_CLIENT_DATA, "your purchase has been approved"), session);
            return getReceiptResponse(userId, ans, session);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "user cant make purchase " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user cant make purchase because "+ e.getMessage(), "make purchase failed", e.getMessage());
        }
    }

    private JSONObject getStorePaymentDetails(ShoppingCart cart) {
        return new JSONObject();
    }


    @Override
    public Response<String> changeMemberAttributes(int userId, String token, String newEmail, String newBirthday) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.changeMemberAttributes(userId, newEmail, newBirthday, session);
            addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            String oldHashedPass = userAuth.hashPassword(userController.getUserName(userId), oldPass);
            String newHashedPass = userAuth.hashPassword(userController.getUserName(userId), newPass);
            userController.changeMemberPassword(userId, oldHashedPass, newPass, newHashedPass, session);
            addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            Store store = marketController.openStore(userController.getActiveMember(userId), storeName, des, img, session);
            userController.openStore(userId, store, session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            int storeId = marketController.getStoreId(storeName);
            StoreReview m = userController.writeReviewForStore(orderId, storeId, content, grading, userId);
            int creatorId = marketController.addReviewToStore(m, session);
            addNotification(creatorId, NotificationOpcode.GET_STORE_DATA, "a review of has been added for store: " + storeId, session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            ProductReview p = userController.writeReviewForProduct(orderId, storeId, productId, content, grading, userId);
            int creatorId = marketController.writeReviewForProduct(p, session);
            addNotification(creatorId, NotificationOpcode.GET_STORE_DATA, "a review of has been added for product: " + productId + " in store: " + storeId, session);
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
    public Response<List<? extends Information>> filterBy(HashMap<String, String> filterOptions){
        try {
            ArrayList<ProductInfo> result = marketController.filterBy(filterOptions);
            if (result.isEmpty()) {
                return new Response<>(null, "No products found by those filter options", "result array is empty, no products found");
            }
            return new Response<>(result, null, null);
        }catch (Exception e){
            return new Response<>(null, "filterFailed", e.getMessage());
        }
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            Question q = userController.sendQuestionToStore(userId, storeId, msg);
            int creatorId = marketController.addQuestion(q, session);
            addNotification(creatorId, NotificationOpcode.GET_STORE_DATA, "a question of has been added for store: " + storeId, session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.writeComplaintToMarket(orderId, msg, userId, session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.appointManager(userId, managerToAppoint, storeId, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.fireManager(userId, managerToFire, storeId, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.appointOwner(userId, owner, storeId, session);
            addNotification(userId,NotificationOpcode.GET_STORE_DATA,"null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.fireOwner(userId, ownerId, storeId, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
    public Response<String> answerAppointment(int userId, String token, int storeId, String fatherName, String childName, String ans) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            marketController.answerAppointment(userController.getUserName(userId), storeId, fatherName, childName, ans, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "user answered appointment successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user answered appointment successfully", null, null);
        }catch (Exception e){
            return logAndRes(Event.LogStatus.Fail, "answer appointment faild because " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "answer appointment failed", e.getMessage());
        }
    }

    @Override
    public Response<String> changeStoreInfo(int userId, String token, int storeId, String name, String description,
                                            String img, String isActive) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String ans;
            userAuth.checkUser(userId, token);
            if (!isActive.equals("null"))
                ans = changeStoreActive(userId, storeId, isActive, session);
            else
                ans = changeStoreAttributes(userId, storeId, name, description, img, session);
            addNotification(userId,NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA,"null", session);
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
    public String changeStoreActive(int userId, int storeId, String isActive, Session session) throws Exception {
        return userController.changeStoreActive(userId, storeId, isActive, session);
    }

    @Override
    public String changeStoreAttributes(int userId, int storeId, String name, String description, String img, Session session) throws Exception {
        userController.checkPermission(userId, Action.changeStoreDetails, storeId);
        marketController.setStoreAttributes(storeId, name, description, img, session);
        return "the store attributes have been changed accordingly";
    }


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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            marketController.answerQuestion(storeId, questionId, answer, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.addProduct, storeId);
            int productId = marketController.addProduct(storeId, name, description, price, quantity, categories, img, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.removeProduct, storeId);
            marketController.deleteProduct(storeId, productId, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            marketController.updateProduct(storeId, productId, categories, name, description, price, quantity, img, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(ownerId, token);
            List<Action> actions = new ArrayList<>();
            for (int permissionId : permissionsIds)
                actions.add(actionIds.get(permissionId));
            userController.addManagerActions(ownerId, userId, actions, storeId, session);
            addNotification(userId,NotificationOpcode.GET_STORE_DATA,"null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(ownerId, token);
            List<Action> actions = new ArrayList<>();
            for (int permissionId : permissionsIds)
                actions.add(actionIds.get(permissionId));
            userController.removeManagerActions(ownerId, userId, actions, storeId, session);
            addNotification(userId,NotificationOpcode.GET_STORE_DATA,"null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.closeStorePermanently(adminId, storeId, session);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            String hashedPass = userAuth.hashPassword(email, pass);
            userController.addAdmin(userId, email, hashedPass, pass, session);
            addNotification(userId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "admin added new admin successfully",
                    StringChecks.curDayString(), "admin" + userId,
                    "admin added new admin successfully", null, null);
        } catch (Exception e) {
            return logAndRes(Event.LogStatus.Fail, "add admin failed, " + e.getMessage(),
                    StringChecks.curDayString(), userController.getUserName(userId),
                    null, "add admin failed", e.getMessage());
        }
    }

    private Response<String> addAdmin(Admin a) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hashedPass = userAuth.hashPassword(a.getName(), a.getPassword());
            userController.addAdmin(a, hashedPass, a.getPassword(), session);
            addNotification(a.getId(), NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return new Response<>("admin was added successfully", null, null);
        }catch (Exception e){
            return new Response<>(null, "add admin failed", e.getMessage());
        }
    }

    @Override
    public Response<String> removeAdmin(int adminId, String token) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.removeAdmin(adminId, session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.answerComplaint(adminId, complaintId, ans, session);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
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
    public Response changeRegularDiscount(int userId, String token, int storeId, int prodId, int percentage, String discountType,
                                          String discountedCategory, List<String> predicatesLst,String content) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.deleteDiscountPolicy, storeId);
            marketController.changeRegularDiscount(storeId, prodId, percentage, discountType,
                    discountedCategory, predicatesLst,content, session);
            return logAndRes(Event.LogStatus.Success, "user changed discount successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user changed discount policy", null, null);
        } catch (Exception e) {
            return new Response<>(null, "could not get complaints", e.getMessage());
        }
    }

    @Override
    public Response placeBid(String token, int userId, int storeId, int prodId, double price,int quantity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            // im assuming there is no need to check permission for this action
            Member user = userController.getMember(userId);
            List<String> workerNames = marketController.placeBid(storeId, user, prodId, price,quantity, session);
            for(String name : workerNames)
                userController.addNotification(name, new Notification(NotificationOpcode.GET_STORE_DATA,
                        "a new bid was placed in store: " + storeId +" for product: " + prodId), session);
            addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "user placed his successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user placed his bid", null, null);
        } catch (Exception ex) {
            return new Response<>(null, "could not place bid", ex.getMessage());
        }
    }
    @Override
    public Response editBid(String token, int userId,  int bidId, int storeId, double price,int quantity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            // im assuming there is no need to check permission for this action
            List<String> workerNames = userController.editBid(userId, bidId, storeId, price, quantity);
            for(String name : workerNames)
                userController.addNotification(name, new Notification(NotificationOpcode.GET_STORE_DATA,
                        "a new bid was placed in store: " + storeId +" for bid: " + bidId), session);
            addNotification(userId, NotificationOpcode.GET_CLIENT_DATA, "null", session);

            return logAndRes(Event.LogStatus.Success, "user placed his successfully",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user placed his bid", null, null);
        } catch (Exception ex) {
            return new Response<>(null, "could not place bid", ex.getMessage());
        }
    }


    @Override
    public Response answerBid(String token, int userId, int storeId, boolean answer, int prodId, int bidId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.updateProduct, storeId);
            int clientId = marketController.getBidClient(bidId, storeId);
            boolean ans = marketController.answerBid( userController.getUser(userId).getName(), storeId, answer, prodId,
                    bidId, session);
            if (ans)
            {
                addNotification(clientId, NotificationOpcode.GET_CLIENT_DATA, "your bid has been approved, please continue for payment", session);
                Set<Integer> usersInStore = marketController.getStoreCreatorsOwners(storeId);
                for (int creatorId : usersInStore)
                {
                    addNotification(creatorId, NotificationOpcode.GET_STORE_DATA, "bid number : " + bidId + " was approved by all.", session);
                }
            }
            if (!answer) {
                addNotification(clientId, NotificationOpcode.GET_CLIENT_DATA, "your bid has been declined.",session);
                Set<Integer> usersInStore = marketController.getStoreCreatorsOwners(storeId);
                for (int creatorId : usersInStore)
                {
                    addNotification(creatorId, NotificationOpcode.GET_STORE_DATA, "bid number : " + bidId + " was declined by " + userId, session);
                }
            }
            return logAndRes(Event.LogStatus.Success, "user answer the bid",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "user answer the bid", null, null);
        }
        catch (Exception ex) {
            return new Response<>(null, "could not answer bid", ex.getMessage());
        }
    }
    @Override
    public Response counterBid(String token, int userId, int storeId, double counterOffer, int prodId, int bidId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int clientId = marketController.getBidClient(bidId, storeId);
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.updateProduct, storeId);
            List<String> ans = marketController.counterBid(userController.getUserName(userId), storeId, counterOffer, prodId, bidId, session);
            for(String name : ans)
                userController.addNotification(name, new Notification(NotificationOpcode.GET_STORE_DATA,
                        "a counter bid was placed in store: " + storeId + " for bid: " + bidId), session);
            addNotification(clientId, NotificationOpcode.GET_CLIENT_DATA, "a counter bid was placed in store: " + storeId + " for bid: " + bidId, session);
            return logAndRes(Event.LogStatus.Success, "u answer with counter bid",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    "u answer with counter bid", null, null);
    } catch (Exception ex) {
        return new Response<>(null, "could not answer bid", ex.getMessage());
    }
    }

    @Override
    public Response<String> cancelMembership(int adminId, String token, String userToRemove) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.cancelMembership(adminId, userToRemove, session);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userController.removeUser(userName, session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.setRealPayment(paymentService);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.addPaymentService(paymentService);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return new Response("Add payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Payment Service", "Add payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response<String> addPaymentService(int adminId, String token, String esPayment, PaymentAdapter paymentAdapter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.addPaymentService(esPayment, paymentAdapter);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return new Response("Add payment service to: " + esPayment + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Payment Service", "Add payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response removePaymentService(int adminId, String token, String paymentService) {
        Admin admin = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxyPayment.removePaymentService(paymentService);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return new Response("Add payment service to: " + paymentService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Remove Payment Service", "Remove payment service fail: " + e.getMessage());
        }
    }

    @Override
    public Response setSupplierService(int adminId, String token, String supplierService) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxySupplier.setRealSupplier(supplierService);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
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

    public Response<List<String>> getPaymentServicesPossibleOptions(int adminId, String token) {
        try{
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            return new Response( proxyPayment.getPaymentServicesPossibleOptions(), null, null);
        }
        catch (Exception e){
            return new Response(null, "Get All possible Payment Services", "Get All possible Payment Services fail: " + e.getMessage());
        }
    }

    @Override
    public Response addSupplierService(int adminId, String token, String supplierService) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxySupplier.addSupplierService(supplierService);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return new Response("Add supplier service to: " + supplierService + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Supplier Service", "Add supplier service fail: " + e.getMessage());
        }
    }

    public Response<String> addSupplierService(int adminId, String token, String esSupplier, SupplierAdapter supplierAdapter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxySupplier.addSupplierService(esSupplier, supplierAdapter);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
            return new Response("Add supplier service to: " + esSupplier + " success", null, null);
        }
        catch (Exception e){
            return new Response(null, "Add Supplier Service", "Add supplier service fail: " + e.getMessage());
        }
    }


    @Override
    public Response removeSupplierService(int adminId, String token, String supplierService) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(adminId, token);
            userController.getActiveAdmin(adminId);
            proxySupplier.removeSupplierService(supplierService);
            addNotification(adminId, NotificationOpcode.GET_ADMIN_DATA, "null", session);
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

    public int getAdminSize() throws Exception{
        return userController.getAdminSize();
    }

    //atomic function to log and get response
    public Response logAndRes(Event.LogStatus state, String content, String time, String userName,
                              Object value, String errorTi , String errorMsg) {
        if(errorMsg != null && errorMsg.contains("Error calling"))
            errorMsg = "the db is down at the moment so this function is not possible";
        Event event = new Event(state, content, time, userName);
        logger.log(event);
        LoggerDao.saveEvent(event);
        return new Response<>(value, errorTi, errorMsg);
    }

      
    public Response<List<Notification>> getMemberNotifications(int userId, String token) {
        try {
            return new Response<List<Notification>>(displayNotifications(userId, token).getValue(), null, null);
        }catch (Exception e){
            return new Response<>(null, "get member notifications failed", e.getMessage());
        }
    }

    public Response<Notification> getNotification(int userId, String token) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            Notification notification = userController.getNotification(userId, session);
            return logAndRes(Event.LogStatus.Success, "Member get notification " + userId + " has successfully entered",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    notification, null, null);
        }catch (Exception e){
            return new Response<>(null, "get member notifications failed", e.getMessage());
        }
    }
    @Override
    public Response addShoppingRule(int userId, String token, int storeId, String purchasePolicy,String content){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.addPurchaseConstraint, storeId);
            marketController.addPurchaseConstraint(storeId, purchasePolicy,content, session);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "Member added shopping constraint " + userId + " has successfully entered",
                    StringChecks.curDayString(), userController.getUserName(userId),
                   "success adding shopping rule", null, null);
        }catch (Exception e){
            return logAndRes(Event.LogStatus.Fail, "cant add shopping rule because: " + e.getMessage(),
                    StringChecks.curDayString(), "user"+userId,
                    null, "add shopping rule failed", e.getMessage());
        }
    }
    @Override
     public Response deletePurchasePolicy(String token, int userId, int storeId, int purchasePolicyId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userAuth.checkUser(userId, token);
            userController.checkPermission(userId, Action.addPurchaseConstraint, storeId);
            marketController.deletePurchaseConstraint(userId, storeId, purchasePolicyId);
            addNotification(userId, NotificationOpcode.GET_STORE_DATA, "null", session);
            return logAndRes(Event.LogStatus.Success, "Member deleted shopping constraint " + userId + " has successfully entered",
                    StringChecks.curDayString(), userController.getUserName(userId),
                    purchasePolicyId, null, null);
        }catch (Exception e){
            return logAndRes(Event.LogStatus.Fail, "cant delete shopping rule because: " + e.getMessage(),
                    StringChecks.curDayString(), "user"+userId,
                    null, "delete shopping rule failed", e.getMessage());
        }
    }




    public static Pair<UserController, MarketController> getControllers(){
        if(userController == null)
            userController = new UserController();
        if(marketController ==null)
            marketController = new MarketController();
        return new Pair<>(userController, marketController);

    }

}
