package bridge;

import data.*;
import data.ProductInfo;
import data.StoreInfo;
import domain.user.PurchaseHistory;
import market.Admin;
import market.Market;

import java.util.*;

import org.json.JSONObject;
import utils.infoRelated.*;
import utils.Response;

public class BridgeImplement implements Bridge {
    private Market market;
    private Admin mainAdmin;
    private String token;

    public BridgeImplement() {
        mainAdmin = new Admin(1, "admin@gmail.com", "admin");
        market = new Market(mainAdmin);
    }

    @Override
    public boolean initTradingSystem() {
        mainAdmin = new Admin(1, "admin@gmail.com", "admin");
        this.market = new Market(mainAdmin);
        token = market.addTokenForTests();
        return true;
    }

    @Override
    public boolean shutDownTradingSystem() {
        this.market = null;
        return true;
    }


    @Override
    public int addAdmin(int adminId, String email, String password) {
        Response<String> res = market.addAdmin(adminId, token, email, password);
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
        Response<LoginInformation> res = market.login(email, password);
        if (res != null && !res.errorOccurred()) {
            return res.getValue().getUserId();
        }
        return -1;
    }

    @Override
    public LoginData loginAndGetData(String email, String password) {
        Response<LoginInformation> res = market.login(email, password);
        if (res != null && !res.errorOccurred()) {
            return new LoginData(res.getValue());
        }
        return null;
    }

    @Override
    public int logout(int user) {
        Response<String> res = market.logout(user);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int createStore(int user, String name, String description, String img) {
        Response<Integer> res = market.openStore(user, token,name, description, img);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return -1;
    }

    @Override
    public List<String> getPossibleExternalSupplierService(int admin) {
        Response<List<String>> res = market.getSupplierServicePossible(admin, token);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return null;
    }

    @Override
    public List<String> getAvailableExternalSupplierService(int user) {
        Response<List<String>> res = market.getSupplierServiceAvailable(user);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return null;
    }

    @Override
    public int addExternalSupplierService(int admin, String esSupplier) {
        Response<String> res = market.addSupplierService(admin,token, esSupplier);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int removeExternalSupplierService(int admin, String es) {
        Response<String> res = market.removeSupplierService(admin, token, es);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int replaceExternalSupplierService(int admin, String esSupplier) {
        Response<String> res = market.setSupplierService(admin, token, esSupplier);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public List<String> getPossibleExternalPaymentService(int admin) {
        Response<List<String>> res = market.getSupplierServicePossible(admin, token);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return null;
    }

    @Override
    public List<String> getAvailableExternalPaymentService(int user) {
        Response<List<String>> res = market.getPaymentServiceAvailable(user);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return null;
    }

    @Override
    public int addExternalPaymentService(int admin, String esPayment) {
        Response<String> res = market.addPaymentService(admin,token, esPayment);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int removeExternalPaymentService(int admin, String es) {
        Response<String> res = market.removePaymentService(admin,token, es);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    @Override
    public int replaceExternalPaymentService(int admin, String esPayment) {
        Response<String> res = market.setPaymentService(admin,token, esPayment);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
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
    public int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price,
                          int quantity, String img) {
        Response<Integer> res = market.addProduct(userId, token, storeId, categories, name, description, price, quantity, img);
        if (res != null && !res.errorOccurred()) {
            return res.getValue();
        }
        return -1;
    }

    @Override
    public boolean removeProduct(int user, int store, int product) {
        Response<String> res = market.deleteProduct(user, token, store, product);
        return res != null && !res.errorOccurred();
    }

    @Override
    public int updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity) {
        Response<String> res = market.updateProduct(userId, token, storeId, productId, categories, name, description, price, quantity, null);
        if (res != null && !res.errorOccurred()) {
            return 1;
        }
        return -1;
    }

    private List<ProductInfo> toProductsInfoList(List<utils.infoRelated.ProductInfo> products)
    {
        List<ProductInfo> ps = new ArrayList<>();
        for (utils.infoRelated.ProductInfo p: products)
        {
            ps.add(new ProductInfo(p));
        }
        return ps;
    }

    @Override
    public List<ProductInfo> getProductsInStore(int store) {
        Response<List<? extends Information>> res = market.getStoreProducts(store);
        if (res != null && !res.errorOccurred()) {
            return toProductsInfoList((List<utils.infoRelated.ProductInfo>)res.getValue());
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
    public int appointmentOwnerInStore(int user, int store, String newOwner) {
        Response<String> res = market.appointOwner(user, token, newOwner, store);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public int appointmentMangerInStore(int user, int store, String manger) {
        Response<String> res = market.appointManager(user, token, manger, store);
        if(res != null && !res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean closeStore(int user, int store) {
        try {
            String res = market.changeStoreActive(user, store, "false");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int reopenStore(int user, int store) {
        try {
            String res = market.changeStoreActive(user, store, "true");
            return 1;
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public List<PositionInfo> getPositionInStore(int user, int store) {
        Response<List<? extends Information>> res = market.checkWorkersStatus(user, token, store);
        if(res != null && !res.errorOccurred())
        {
            List<PositionInfo> ans = new LinkedList<>();
            for(Information info : res.getValue())
                ans.add(new PositionInfo((Info) info));
            return ans;
        }
        return null;
    }



    @Override
    public int addStoreManagerPermissions(int user, int store, int managerId, int permissionsIds) {
        List<Integer> ids = new ArrayList<>();
        ids.add(permissionsIds);
        Response<String> res = market.addManagerPermissions(user, token, managerId, store, ids);
        if(res == null || res.errorOccurred())
        {
            return -1;
        }
        return 1;
    }

    @Override
    public int removeStoreManagerPermissions(int user, int store, int managerId,int permissionsIds) {
        List<Integer> ids = new ArrayList<>();
        ids.add(permissionsIds);
        Response<String> res = market.removeManagerPermissions(user, token, managerId, store, ids);
        if(res == null || res.errorOccurred())
        {
            return -1;
        }
        return 1;
    }

    @Override
    public int addStoreManagerPermissions(int user, int store, int managerId, List<Integer> permissionsIds) {
        Response<String> res = market.addManagerPermissions(user, token, managerId, store, permissionsIds);
        if(res == null || res.errorOccurred())
        {
            return -1;
        }
        return 1;
    }

    @Override
    public PermissionInfo getManagerPermissionInStore(int user, int store, int manager) {
        Response<Info> res = market.checkWorkerStatus(user, token, manager, store); //TODO : market.seeStoreHistory(user, store);
        if(res != null && !res.errorOccurred())
        {
            return new PermissionInfo(res.getValue().getActions());
        }
        return null;
    }

    @Override
    public List<PurchaseInfo> getStorePurchasesHistory(int user, int store) {
        Response<List<? extends Information>> res = market.seeStoreHistory(user, token, store);
        if(!res.errorOccurred())
        {
            List<PurchaseInfo> purchases = new LinkedList<>();
            for(Information order :  res.getValue())
                purchases.add(new PurchaseInfo(((OrderInfo)order).getProductsInStores()));
            return purchases;
        }
        return null;
    }

    public List<PurchaseInfo> toBuyerPurchaseHistoryList(List<PurchaseHistory> purchaseHistory)
    {
        List<PurchaseInfo> piList = new ArrayList<>();
        for (PurchaseHistory entry : purchaseHistory)
        {
            for (Receipt receipt : entry.getPurchaseHistory().values())
                piList.add(new PurchaseInfo(receipt.getCart().getContent()));
        }
        return piList;
    }

    @Override
    public List<PurchaseInfo> getBuyerPurchasesHistory(int user, int buyer) {
        Response<List<PurchaseHistory>> res = market.getUsersPurchaseHistory(user, token);
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
        Response<HashMap<Integer,Admin>> res = market.getAdmins(adminId, token);
        if(!res.errorOccurred())
        {
            return toAdminList(res.getValue());
        }
        return null;
    }

    private List<StoreInfo> toStoresList(List<? extends Information> stores)
    {
        List<StoreInfo> storeInfos = new ArrayList<>();
        for (Information store : stores)
        {
            storeInfos.add(new StoreInfo((utils.infoRelated.StoreInfo)store));
        }
        return storeInfos;
    }


    @Override
    public List<StoreInfo> getAllStores() {

        Response<List<? extends Information>>res = market.getStoresInformation();
        if(!res.errorOccurred())
        {
            return toStoresList(res.getValue());
        }
        return null;
    }

    @Override
    public int removeAdmin(int adminId) {
        Response<String> res = market.removeAdmin(adminId, token);
        if(!res.errorOccurred())
        {
            return 1;
        }
        return -1;
    }

    @Override
    public StoreInfo getStore(int storeId) {
        Response<utils.infoRelated.StoreInfo> res = market.getStoreInformation(storeId);
        if(!res.errorOccurred())
        {
            return new StoreInfo(res.getValue());
        }
        return null;
    }

    private List<ProductInfo> toProductInfoList(List<utils.infoRelated.ProductInfo> products)
    {
        List<ProductInfo> toReturnProduct = new ArrayList<>();
        for (utils.infoRelated.ProductInfo product: products) {
            toReturnProduct.add(new ProductInfo(product));
        }
        return toReturnProduct;
    }

    @Override
    public List<ProductInfo> getProductInStore(int storeId) {
        Response<List<utils.infoRelated.ProductInfo>> res = market.getProducts(storeId);
        if(!res.errorOccurred())
        {
            return toProductInfoList(res.getValue());
        }
        return null;
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
        Response<List<? extends Information>> res = market.getCart(user);
        if(!res.errorOccurred())
        {
            return new CartInfo((List<utils.infoRelated.ProductInfo>)res.getValue());
        }
        return null;
    }

    @Override
    public int makePurchase(int user, JSONObject payment, JSONObject supplier) {
        Response<Receipt> res = market.makePurchase(user, payment, supplier);
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

    @Override
    public List<String> getNotifications(int userId) {
        Response<List<String>> res = market.displayNotifications(userId, token);
        if(!res.errorOccurred())
        {
            return res.getValue();
        }
        return null;
    }

    private StoreInfo getStoreFromList(int storeId, List<utils.infoRelated.StoreInfo> stores)
    {
        for (utils.infoRelated.StoreInfo store: stores)
        {
            if (store.getStoreId() == storeId)
            {
                return new StoreInfo(store);
            }
        }
        return null;
    }
    @Override
    public StoreInfo getStoreInfo(int storeId) {
        Response<List<? extends Information>> res = market.getStoresInformation();
        if(!res.errorOccurred())
        {
            return getStoreFromList(storeId, (List<utils.infoRelated.StoreInfo>) res.getValue());
        }
        return null;
    }

}
