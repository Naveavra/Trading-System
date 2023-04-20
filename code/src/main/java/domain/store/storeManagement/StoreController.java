package domain.store.storeManagement;

import domain.store.product.ProductController;
import domain.store.storeManagement.Store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreController {

    public ConcurrentHashMap<Integer, Store> storeList; //stordeid, Store
    AtomicInteger storescounter;

    public StoreController() {
        storeList = new ConcurrentHashMap<>();
        storescounter = new AtomicInteger(0);
    }

    public Store createNewStore(int creatorid, String description){
        Store store = new Store(storescounter.get(), description, creatorid);
        storescounter.getAndIncrement();
        return store;
    }
}
