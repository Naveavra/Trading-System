package domain.store.storeManagement;

import domain.store.product.Product;
import domain.store.product.ProductController;
import domain.store.storeManagement.Store;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreController {

    public ConcurrentHashMap<Integer, Store> storeList; //storeid, Store
    AtomicInteger storescounter;

    public StoreController() {
        storeList = new ConcurrentHashMap<>();
        storescounter = new AtomicInteger(0);
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




}
