package domain.store.storeManagement;

import domain.store.product.Product;
import domain.store.product.ProductController;
import domain.store.storeManagement.Store;
import utils.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreController {

    public ConcurrentHashMap<Integer, Store> storeList; //storeid, Store
    AtomicInteger storescounter;
    AtomicInteger productIDs = new AtomicInteger(0);
    ConcurrentHashMap<Integer,Product> products; //for fast access

    public StoreController() {
        storeList = new ConcurrentHashMap<>();
        storescounter = new AtomicInteger(0);
        products = new ConcurrentHashMap<>();
    }

    /**
     * adds a new product to a store.
     */
    public synchronized void addProduct(int storeid, String name, String desc, int price,int quantity){
        Store st;
        Product prod;
        if((st = getStore(storeid))!=null && (prod = getExistingProductByName(name))!=null){
            Product prod_ = prod.clone();
            prod_.setDescription(desc);
            prod_.setPrice(price);
            prod_.setQuantity(quantity);
            st.addNewProduct(prod_);
        }
        else if((st = getStore(storeid))!=null){
            Product p = st.addNewProduct(name,desc,productIDs);
            p.setPrice(price);
            p.setQuantity(quantity);
            addToProducts(p.clone());
        }
    }
    private Product getExistingProductByName(String prodName){
        for(Product p : products.values()){
            if(p.getName().equalsIgnoreCase(prodName)){
                return p;
            }
        }
        return null;
    }
    /**
     * @return the store creator id if the store or order doesn't exist return -1
     */
    public int writeReviewForStore(Message message){
        Store store = storeList.get(message.getStoreId());
        if (store != null && store.isActive())
        {
            try {
                return store.addReview(message.getOrderId(), message);
            }
            catch (Exception e)
            {
                return -1;
            }
        }
        return -1;
    }


    /**
     * checks if the purchasing is possible
     * @param shoppingcart the client shopping cart
     * @return if the purchasing is possible returns the total price else return -1
     */
    public int checkPurchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart){
        int totalprice = 0;
        for (Integer storeid : shoppingcart.keySet())
        {
            Store store = storeList.get(storeid);
            try {
                totalprice += store.checkPurchaseProducts(shoppingcart.get(storeid));
            } catch (Exception e) {
                return  -1;
            }
        }
        return totalprice;
    }

    /**
     * performs the purchasing
     * @param shoppingcart the client shopping cart
     * @return if successful returs the store owners ids else null
     */
    public Set<Integer> PurchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart){
        Set<Integer> storeOwnersIDS = new HashSet<>();
        for (Integer storeid : shoppingcart.keySet())
        {
            Store store = storeList.get(storeid);
            if (!(store.PurchaseProducts(shoppingcart.get(storeid))))
            {
                return null;
            }
            storeOwnersIDS.add(store.getCreatorId());

        }
        return storeOwnersIDS;
    }

    /**
     * adds the product to the store controller to view which products exists in the market and create
     * @param prod
     * @return
     */
    private synchronized boolean addToProducts(Product prod){
        if(products.containsKey(prod.getID())) {return false;}
        products.put(prod.getID(),prod);
        return true;
    }

    public Store getStore(int storeid)
    {
        return storeList.get(storeid);
    }


    public Store createNewStore(int creatorid, String description){
        Store store = new Store(storescounter.get(), description, creatorid);
        int storeid = storescounter.getAndIncrement();
        storeList.put(storeid, store);
        return store;
    }

    public ArrayList<Product> getProductByCategories(ArrayList<String> categories)
    {
        ArrayList<Product> products = new ArrayList<>();
        for (Store store:
             storeList.values()) {
                products.addAll(store.getProductByCategories(categories));
        }
        return products;
    }

    public ArrayList<Product> getProductByKeyWords(ArrayList<String> keywords){
        ArrayList<Product> products = new ArrayList<>();
        for (Store store:
                storeList.values()) {
            products.addAll(store.getProductByKeywords(keywords));
        }
        return products;
    }

    public String getProductName(int storeId, int productId) throws Exception {
        Store store = storeList.get(storeId);
        if (store != null && store.isActive())
        {
            return store.getProductName(productId);
        }
        else {
            throw new Exception("store doesnt Exist or Open");
        }
    }

    public ArrayList<String> checkMessages(int storeID) throws Exception{
        Store store = storeList.get(storeID);
        if (store != null && store.isActive())
        {
            return store.checkMessages();
        }
        else
        {
            throw new Exception("store doesnt Exist or Open");
        }
    }

    /**
     * the store owner replies to the reviews on his store

     */
    public void giveFeedback(int storeID, int messageID, String feedback) throws Exception {
        Store store = storeList.get(storeID);
        if (store != null && store.isActive())
        {
            store.giveFeedback(messageID, feedback);
        }
        else
        {
            throw new Exception("store doesnt Exist or Open");
        }
    }
}
