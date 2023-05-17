package domain.store.storeManagement;

import utils.ProductInfo;
import utils.Filter.ProductFilter;
import utils.StoreInfo;
import utils.orderRelated.Order;
import domain.store.product.Product;
import utils.messageRelated.Message;
import utils.orderRelated.OrderInfo;
import utils.userInfoRelated.Receipt;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreController {

    public ConcurrentHashMap<Integer, Store> storeList; //storeid, Store
    AtomicInteger storescounter;
    AtomicInteger productIDs = new AtomicInteger(0);
    ConcurrentHashMap<Integer, Product> products; //for fast access

    public StoreController() {
        storeList = new ConcurrentHashMap<>();
        storescounter = new AtomicInteger(0);
        products = new ConcurrentHashMap<>();
    }

    /**
     * adds a new product to a store.
     */
    public synchronized int addProduct(int storeid, String name, String desc, int price, int quantity) throws Exception {
        Store st;
        //Product prod;
        int id = -1;
        if ((st = getStore(storeid)) != null) {
            Product p = st.addNewProduct(name, desc, productIDs,price,quantity);
            p.replaceQuantity(quantity);
            addToProducts(p.clone());
            id = p.getID();
        }
        return id;
    }

    public synchronized int addProduct(int storeid, String name, String desc, int price, int quantity, String img) throws Exception {
        Store st;
        //Product prod;
        int id = -1;
        if ((st = getStore(storeid)) != null) {
            Product p = st.addNewProduct(name, desc, productIDs,price,quantity, img);
            p.replaceQuantity(quantity);
            addToProducts(p.clone());
            id = p.getID();
        }
        return id;
    }
    public void addToCategory(int storeId, int productId, List<String> categories) throws Exception{
        Store st;
        if((st = getStore(storeId))!= null){
            st.addToCategories(productId,categories);
        }
    }

    private Product getExistingProductByName(String prodName) {
        for (Product p : products.values()) {
            if (p.getName().equalsIgnoreCase(prodName)) {
                return p;
            }
        }
        return null;
    }

    public int addQuestion(Message m) throws Exception
    {
        Store store = storeList.get(m.getStoreId());
        if (store != null && store.isActive())
        {
            return store.addQuestion(m);
        }
        throw new Exception("store does not exist or is not open");
    }

    public Store openStore(String desc, int userID) {
        Store store = new Store(storescounter.getAndIncrement(), desc, userID);
        storeList.put(store.getStoreId(), store);
        return store;
    }

    public Store openStore(String name, String desc, String img, int userID) {
        Store store = new Store(storescounter.getAndIncrement(), name, desc, img, userID);
        storeList.put(store.getStoreId(), store);
        return store;
    }

    /**
     * @return the store creator id if the store or order doesn't exist return -1
     */
    public int writeReviewForStore(Message message) throws Exception {
        Store store = storeList.get(message.getStoreId());
        if (store != null && store.isActive()) {
            return store.addReview(message.getOrderId(), message);
        }
        throw new Exception("the storeId given does not belong to any active store");
    }

    public int writeReviewForProduct(Message message) throws Exception {
        Store store = storeList.get(message.getStoreId());
        if (store != null && store.isActive()) {
            return store.addProductReview(message);
        }
        throw new Exception("the storeId given does not belong to any active store");
    }

    public int calculatePrice(HashMap<Integer,HashMap<Integer,Integer>> basket) throws Exception{
        int total = 0;
        for(Integer id : basket.keySet()){
            total += getStore(id).calculatePrice(basket.get(id));
        }
        return total;
    }
    public void setPrices(Order or) throws Exception {
        HashMap<Integer,HashMap<Integer,Integer>> shoppingCart = or.getProductsInStores();
        HashMap<Integer,HashMap<Integer,Integer>> prices = or.getPrices();
        for(Integer storeId: shoppingCart.keySet()){
            prices.put(storeId,new HashMap<>());
            for(Integer prodId : shoppingCart.get(storeId).keySet()){
                int quantity = shoppingCart.get(storeId).get(prodId);
                Store s = getStore(storeId);
                Product p = s.getInventory().getProduct(prodId);
                prices.get(storeId).put(prodId,p.getPrice() * quantity);
            }
        }
    }
//    /**
//     * checks if the purchasing is possible
//     *
//     * @param shoppingcart the client shopping cart
//     * @return if the purchasing is possible returns the total price else return -1
//     */
//    public int createOrder(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart) {
//        int totalprice = 0;
//        for (Integer storeid : shoppingcart.keySet()) {
//            Store store = storeList.get(storeid);
//            try {
//                totalprice += store.createOrder(shoppingcart.get(storeid));
//            } catch (Exception e) {
//                return -1;
//            }
//        }
//        return totalprice;
//    }

    /**
     * performs the purchasing
     * @param shoppingCart the client shopping cart
     * @return if successful returns the store owners ids else null
     */
    public synchronized Set<Integer> purchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingCart, Order order) throws Exception {
        Set<Integer> storeOwnersIDS = new HashSet<>();
        //should apply discounts here
        for (Integer storeId : shoppingCart.keySet()) {
            Store store = storeList.get(storeId);
            store.handleDiscount(order);
            if (!(store.makeOrder(shoppingCart.get(storeId)))) {
                return null;
            }
            storeOwnersIDS.add(store.getCreatorId());
            store.addOrder(order);
        }
        return storeOwnersIDS;
    }

    /**
     * adds the product to the store controller to view which products exists in the market and create
     *
     * @param prod
     * @return
     */
    private synchronized boolean addToProducts(Product prod) {
        if (products.containsKey(prod.getID())) {
            return false;
        }
        products.put(prod.getID(), prod);
        return true;
    }

    public Store getStore(int storeId) {
        return storeList.get(storeId);
    }


    public Store createNewStore(int creatorid, String description) {
        Store store = new Store(storescounter.get(), description, creatorid);
        int storeid = storescounter.getAndIncrement();
        storeList.put(storeid, store);
        return store;
    }

//    public ArrayList<Product> getProductByCategories(ArrayList<String> categories) {
//        ArrayList<Product> products = new ArrayList<>();
//        for (Store store :
//                storeList.values()) {
//            products.addAll(store.getProductByCategories(categories));
//        }
//        return products;
//    }

//    public ArrayList<Product> getProductByKeyWords(ArrayList<String> keywords) {
//        ArrayList<Product> products = new ArrayList<>();
//        for (Store store :
//                storeList.values()) {
//            products.addAll(store.getProductByKeywords(keywords));
//        }
//        return products;
//    }

    public String getProductName(int storeId, int productId) throws Exception {
        Store store = storeList.get(storeId);
        if (store != null && store.isActive()) {
            return store.getProductName(productId);
        } else {
            throw new Exception("store doesnt Exist or Open");
        }
    }

    public ArrayList<String> checkMessages(int storeID) throws Exception {
        Store store = storeList.get(storeID);
        if (store != null && store.isActive()) {
            return store.checkMessages();
        } else {
            throw new Exception("store doesnt Exist or Open");
        }
    }


    public HashMap<Integer, Message> getQuestions(int storeId) throws Exception {
        Store store = storeList.get(storeId);
        if (store != null && store.isActive())
        {
            return store.getQuestions();
        }
        else {
            throw new Exception("store doesnt Exist or Open");
        }
    }

    public void answerQuestion(int storeId, int questionId, String answer) throws Exception{
        Store store = storeList.get(storeId);
        if (store != null && store.isActive())
        {
            store.answerQuestion(questionId, answer);
        }
    }

    public List<OrderInfo> getStoreOrderHistory(int storeId) throws Exception {
        Store store = storeList.get(storeId);
        if (store != null && store.isActive())
        {
            return store.getOrdersHistory();
        }
        throw new Exception("store doesnt Exist or Open");
    }

    public AppHistory getAppointments(int storeId) throws Exception{
        Store store = storeList.get(storeId);
        if (store != null && store.isActive())
        {
            return store.getAppHistory();
        }
        throw new Exception("store doesnt Exist or Open");
    }


    public Set<Integer> closeStorePermanently(int storeId) throws Exception {
        Store store = storeList.get(storeId);
        if(store != null){
            storeList.remove(storeId);
            return store.getUsersInStore();
        }
        else
            throw new Exception("the store does not exist in the system");
    }

    public void removeProduct(int storeId, int productId) throws Exception  {
        Store st;
        if((st = storeList.get(storeId))!=null){
            st.removeProduct(productId);
        }
        else{
            throw new Exception("Store id doesn't exist.");
        }
    }

    public void updateProduct(int storeId, int productId, List<String> categories, String name, String description,
                              int price, int quantity, String img) throws Exception {
        Store st;
        if((st = storeList.get(storeId))!=null){
            st.updateProduct(productId, categories, name,  description, price, quantity, img);
        }
        else{
            throw new Exception("Store id doesn't exist.");
        }
    }

    public HashMap<Integer, Message> viewReviews(int storeId) throws Exception {
        Store store = storeList.get(storeId);
        if (store != null && store.isActive())
        {
            return store.getStoreReviews();
        }
        else {
            throw new Exception("store doesnt Exist or Open");
        }
    }

    public ArrayList<String> showFilterOptions() {
        return new ProductFilter().getNames();
    }

    public ArrayList<ProductInfo> filterBy(HashMap<String,String> filterOptions) {
        ArrayList<ProductInfo> result = new ArrayList<>();
        for(Store st : storeList.values()){
            result.addAll(st.filterBy(filterOptions));
        }
        return result;
    }

    public List<ProductInfo> getProducts(int storeId) throws Exception {
        Store s = getStore(storeId);
        if(s != null){
            return s.getProducts();
        }
        else
            throw new Exception("the id given does not match any store");
    }

    public StoreInfo getStoreInformation(int storeId){
        Store store = getStore(storeId);
        return store.getStoreInformation();

    }

    public ConcurrentHashMap<Integer, Store> getStoresInformation() {
        return storeList;
    }

    public void purchaseMade(Receipt receipt) throws Exception {
        HashMap<Integer, HashMap<Integer, Integer>> cart = receipt.getProducts();
        for(int storeId : cart.keySet()){
            Store store = getStore(storeId);
            for(int productId : cart.get(storeId).keySet()){
                store.setProductQuantity(productId, store.getQuantityOfProduct(productId) - cart.get(storeId).get(productId));
            }
        }
    }

    public List<ProductInfo> getAllProducts() {
        List<ProductInfo> products = new ArrayList<>();
        for(Store s : storeList.values()){
            if(s.isActive())
                products.addAll(s.getProducts());
        }
        return products;
    }
}
