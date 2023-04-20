package domain.store.storeManagement;

import domain.store.product.Product;
import domain.store.product.ProductController;
import domain.store.storeManagement.Store;
import utils.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreController {

    public ConcurrentHashMap<Integer, Store> storeList; //storeid, Store
    AtomicInteger storescounter;
    AtomicInteger productIDs = new AtomicInteger(0);
    ArrayList<Product> products; //for fast access

    public StoreController() {
        storeList = new ConcurrentHashMap<>();
        storescounter = new AtomicInteger(0);
        products = new ArrayList<>();

    }
    public synchronized void addProduct(int storeid, String name, String desc, AtomicInteger productIDs){
        Store st;
        if((st = getStore(storeid))!=null){
            Product p = st.addNewProduct(name,desc,productIDs).clone();
            addToProducts(p);
        }
    }

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

    public boolean PurchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart){

        for (Integer storeid : shoppingcart.keySet())
        {
            Store store = storeList.get(storeid);
            if (!(store.PurchaseProducts(shoppingcart.get(storeid))))
            {
                return false;
            }
        }
        return true;
    }
    private synchronized boolean addToProducts(Product prod){
        for(Product p : products){
            if(p.getID() == prod.getID()){
                return false;
            }
        }
        products.add(prod);
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




}
