package market;

import utils.Response;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: can remove admins but at least one has to be in the system.
//TODO: need to add those different functions: gettingInformationOnStore(int storeId), searchProduct(String name, ...),
//TODO: getProduct(int storeId, int productId), checkProductAvailability(int storeId, int productId, int quantity),
//TODO: addProduct(int userId, int storeId, int productId, int quantity), changeQuantity(int userId, int storeId, int productId, int quantity),
//TODO: gettingInformationOnProduct(int storeId, int productId), removeFromCart(int userId, int storeId, int productId),
//TODO: getCartContent(int userId), purchaseCart(int userId), openStore(int userId),
public class Market implements MarketInterface{
    private ConcurrentLinkedDeque<Admin> admins;
    public Market (Admin admin){
        admins = new ConcurrentLinkedDeque<>();
        admins.add(admin);
    }


    @Override
    public Response<String> register(String name, String mail, String pass, String birthday) {
        return null;
    }

    @Override
    public Response<String> addProductToCart(int storeId, int productId, int quantity) {
        return null;
    }

    @Override
    public Response<String> removeProductFromCart(int storeId, int productId) {
        return null;
    }

    @Override
    public Response<String> changeQuantityInCart(int storeId, int productId, int change) {
        return null;
    }

    @Override
    public Response<String> getCart(int userId) {
        return null;
    }

    @Override
    public Response<String> buy(int userId) {
        pair<info, cart> = uc.getUserCart(userId);
        String recipt = mc.purchase(info, cart);
        return null;
    }

    @Override
    public Response<String> login(String email, String pass) {
        return null;
    }

    @Override
    public Response<String> logout(int userId) {
        return null;
    }

    @Override
    public Response<String> changeDetails(int userId, String name, String email, String birthday) {
        return null;
    }

    @Override
    public Response<String> changePassword(int userId, String oldPass, String newPass) {
        return null;
    }

    @Override
    public Response<String> openStore(int userId) {
        return null;
    }

    @Override
    public Response<String> getMember(int userId) {
        return null;
    }

    @Override
    public Response<String> getUserOrders(int userId) {
        return null;
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
}
