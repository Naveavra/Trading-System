package bridge;

import data.*;
import domain.store.storeManagement.Store;
import market.Admin;
import market.Market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import utils.marketRelated.Response;

public class BridgeImplement implements Bridge {
    private Market market;
    private Admin mainAdmin;

    public BridgeImplement() {
        mainAdmin = new Admin(-1, "admin@gmail.com", "admin");
        //market = new Market(mainAdmin);
    }

    @Override
    public int initTradingSystem() {
        this.market = new Market(this.mainAdmin);
        return 1;
    }

    @Override
    public int shutDownTradingSystem() {
        this.market = null;
        return 1;
    }


    @Override
    public int addAdmin(int adminId, String email, String password) {
        Response<String> res = market.addAdmin(adminId, email, password);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int register(String email, String password, String birthday) {
        Response<String> res = market.register(email, password, birthday);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int login(String email, String password) {
        Response<Integer> res = market.login(email, password, new ArrayList<>());
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return -1;
    }

    @Override
    public int logout(int user) {
        Response<Integer> res = market.logout(user);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int createStore(int user, String description) {
        Response<Integer> res = market.openStore(user, description);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return -1;
    }

    @Override
    public int addExternalSupplierService(int admin, int esSupplier) {
        return 0;
    }

    @Override
    public int removeExternalSupplierService(int admin, int es) {
        return 0;
    }

    @Override
    public int replaceExternalSupplierService(int admin, int es, int esSupplier) {
        return 0;
    }

    @Override
    public int addExternalPaymentService(int admin, int esPayment) {
        return 0;
    }

    @Override
    public int removeExternalPaymentService(int admin, int es) {
        return 0;
    }

    @Override
    public int replaceExternalPaymentService(int admin, int es, int esPayment) {
        return 0;
    }

    @Override
    public List<MessageInfo> getUnreadMessages(String user) {
        return null;
    }

    @Override
    public List<MessageInfo> getAllMessages(int user) {
        return null;
    }

    @Override
    public int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price, int quantity) {
        Response<Integer> res = market.addProduct(userId, storeId, categories, name, description, price, quantity);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return -1;
    }

    @Override
    public int removeProduct(int user, int store, int product) {
        Response<String> res = market.deleteProduct(user, store, product);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity) {
        Response<String> res = market.updateProduct(userId, storeId, productId, categories, name, description, price, quantity);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public List<ProductInfo> getProductsInStore(int store) {
        Response<List<utils.ProductInfo>> res = market.getStoreProducts(store);
        if (res != null && !res.errorOccurred()) {
            return null;//TODO: toProductInfo
        }
        return null;
    }

    @Override
    public int changeDiscountPolicy(int userId, int storeId, String policy) {
        return 0;
    }

    @Override
    public int changePurchasePolicy(int userId, int storeId, String policy) {
        return 0;
    }

    @Override
    public int appointmentOwnerInStore(int user, int store, int owner) {
        Response<String> res = market.appointOwner(owner, store, user);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return 0;
    }

    @Override
    public int appointmentMangerInStore(int user, int store, int manger) {
        Response<String> res = market.appointManager(user, store, manger);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return 0;
    }

    @Override
    public int closeStore(int user, int store) {
        Response<String> res = market.closeStore(user, store);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return 0;
    }

    @Override
    public int reopenStore(int user, int store) {
        Response<String> res = market.reopenStore(user, store);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return 0;
    }

    @Override
    public List<PositionInfo> getPositionInStore(int user, int store) {
        Response<String> res = market.getAppointments(user, store);
        if(res != null && !res.errorOccurred())
        {
            return null;//TODO: res.getValue();
        }
        return null;
    }

    @Override
    public int changeStoreManagerPermissions(int user, int store, List<Integer> permissionsIds) {
        Response<List<PermissionInfo>> res = null; //TODO : market.changeStoreManagerPermissions(user, store);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public List<PermissionInfo> getManagerPermissionInStore(int user, int store, int manager) {
        Response<List<PermissionInfo>> res = null; //TODO : market.seeStoreHistory(user, store);
        if(res != null && !res.errorOccurred())
        {
            return res.getValue();
        }
        return null;
    }

    @Override
    public List<PurchaseInfo> getStorePurchasesHistory(int user, int store) {
        Response<utils.StoreInfo> res = market.seeStoreHistory(user, store);
        if(!res.errorOccurred())
        {
            return new ArrayList<>();//TODO
        }
        return null;
    }

    public List<PurchaseInfo> toBuyerPurchaseHistoryList(String purchaseHistory)
    {
        Gson gson = new Gson();
        List<PurchaseInfo> piList = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> historyList = null;
        historyList = gson.fromJson(purchaseHistory, historyList.getClass());
        for (ConcurrentHashMap.Entry<Integer, HashMap<Integer, HashMap<Integer, Integer>>> entry : historyList.entrySet())
        {
            piList.add(new PurchaseInfo(entry.getValue()));
        }
        return piList;
    }

    @Override
    public List<PurchaseInfo> getBuyerPurchasesHistory(int user, int buyer) {
        Response<String> res = market.getUserPurchaseHistory(user);
        if(!res.errorOccurred())
        {
            return toBuyerPurchaseHistoryList(res.getValue());
        }
        return null;
    }

    private List<AdminInfo> toAdminList(HashMap<Integer, Admin> admins) {
        List<AdminInfo> adminList = new ArrayList<>();

        for (Map.Entry<Integer, Admin> entry : admins.entrySet()) {
            Admin admin = entry.getValue();
            AdminInfo adminInfo = new AdminInfo(admin);
            adminList.add(adminInfo);
        }
        return adminList;
    }


    @Override
    public List<AdminInfo> getAllAdmins(int adminId) {
        Response<HashMap<Integer,Admin>> res = market.getAdmins(adminId);
        if(!res.errorOccurred())
        {
            return toAdminList(res.getValue());
        }
        return null;
    }

    private List<StoreInfo> toStoresList(ConcurrentHashMap<Integer, Store> stores)
    {
        List<StoreInfo> storeInfos = new ArrayList<>();
        for (ConcurrentHashMap.Entry<Integer, Store> entry : stores.entrySet())
        {
            storeInfos.add(new StoreInfo(entry.getValue()));
        }
        return storeInfos;
    }


    @Override
    public List<StoreInfo> getAllStores() {
        Response<ConcurrentHashMap<Integer, Store>> res = market.getStores();
        if(!res.errorOccurred())
        {
            return toStoresList(res.getValue());
        }
        return null;
    }

    @Override
    public int removeAdmin(int adminId) {
        Response<String> res = market.removeAdmin(adminId);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public StoreInfo getStore(int storeId) {
        Response<utils.StoreInfo> res = market.getStore(storeId);
        if(!res.errorOccurred())
        {
            return new StoreInfo(res.getValue());
        }
        return null;
    }

    private List<ProductInfo> toProductInfoList(List<utils.ProductInfo> products)
    {
        List<ProductInfo> toReturnProduct = new ArrayList<>();
        for (utils.ProductInfo product: products) {
            toReturnProduct.add(new ProductInfo(product));
        }
        return toReturnProduct;
    }

    @Override
    public List<ProductInfo> getProductInStore(int storeId) {
        Response<List<utils.ProductInfo>> res = market.getProducts(storeId);
        if(!res.errorOccurred())
        {
            return toProductInfoList(res.getValue());
        }
        return null;
    }

    @Override
    public int adminLogin(String email, String password) {
        Response<Integer> res = market.adminLogin(email, password);
        if(!res.errorOccurred())
        {
            return res.getValue();
        }
        return 0;
    }

    @Override
    public int adminLogout(int admin) {
        Response<String> res = market.adminLogout(admin);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public int enterSystem() {
        Response<Integer> res = market.enterGuest();
        if(!res.errorOccurred())
        {
            return res.getValue();
        }
        return -1;
    }

    @Override
    public int addProductToCart(int user, int store, int productID, int quantity) {
        Response<String> res = market.addProductToCart(user, store, productID, quantity);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public CartInfo getCart(int user) {
        Response<HashMap<Integer, HashMap<Integer, Integer>>> res = market.getCart(user);
        if(!res.errorOccurred())
        {
            return new CartInfo(res.getValue());
        }
        return null;
    }

    @Override
    public int makePurchase(int user, String accountNumber) {
        Response<String> res = market.makePurchase(user, accountNumber);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public int exitSystem(int guestId) {
        Response<String> res = market.exitGuest(guestId);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public int removeProductFromCart(int userId, int storeId, int productId) {
        Response<String> res = market.removeProductFromCart(userId, storeId, productId);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public int changeQuantityInCart(int userId, int storeId, int productId, int change) {
        Response<String> res = market.changeQuantityInCart(userId, storeId, productId, change);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

}
