package domain.store.storeManagement;

import datastructres.Pair;
import domain.store.product.Product;
import utils.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {
    private final int storeid;
    private boolean isActive;
    private final int creatorId;
    private String storeDescription;
    private final AppHistory appHistory; //first one is always the store creator
    private final ConcurrentHashMap<Product,AtomicInteger> inventory; //<productID,<product, quantity>>
    public Store(int id, String description, int creatorId){
        Pair<Integer, Role > creatorNode = new Pair<>(creatorId, Role.Creator);
        appHistory = new AppHistory(creatorNode);
        this.storeid = id;
        this.storeDescription = description;
        this.creatorId = creatorId;
        this.inventory = new ConcurrentHashMap<>();
    }

    public int getStoreid()
    {
        return storeid;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    /**
     *
     * @param userinchargeid the user who wants to appoint another user
     * @param newuserid the new user whom still doesn't have any role on the store
     * @param role the new user role
     * @return true if successfully else throws exception
     */
    public boolean appointUser(int userinchargeid, int newuserid, Role role) throws Exception {
        Pair<Integer, Role> node = new Pair<>(newuserid, role);
        return appHistory.addNode(userinchargeid, node);
    }

    /**
     * fire user from the store appointment tree
     * @param userinchrageid the user who wants to fire another user
     * @param joblessuser the user we want to fire
     * @return set aff all the other users who lost their role in our store
     * @throws Exception if the action isn't valid will throw exception
     */
    public Set<Integer> fireUser(int userinchrageid, int joblessuser) throws Exception
    {
        if (!appHistory.isChild(userinchrageid, joblessuser))
        {
            throw new Exception("user cant fire the other user");
        }
        return new HashSet<>(appHistory.removeChild(joblessuser));
    }

    public ConcurrentHashMap<Product,AtomicInteger> getInventory()
    {
        return inventory;
    }

    /**
     * update product and quantity in the store inventory
     * @param p product that needed to be updated
     * @param quantity hew much we need to add
     */
    public void addProduct(Product p, int quantity)
    {
//        if (inventory.containsKey(p.))
//        {
//            inventory.replace(p,new AtomicInteger(inventory.get(p).getAndAdd(quantity)));
//        }
//        else
//        {
//            AtomicInteger newProductQuantity =  new AtomicInteger(quantity);
//            inventory.put(p, newProductQuantity);
//        }
    }

    /**
     * getter
     * @param p product
     * @return null if the product doesn't exist otherwise return the product quantity
     */
    public AtomicInteger getQuantityOfProduct(Product p)
    {
        return inventory.getOrDefault(p, null);
    }

    public ArrayList<Product> searchProductByCategory(ArrayList<String> categories)
    {
        //        for (Product p: inventory.keySet()) {
//            if (p.getCategories().contains())
//
//        }
        return new ArrayList<>();
    }










}
