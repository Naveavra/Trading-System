package domain.store.storeManagement;

import utils.Message;
import utils.Pair;
import domain.store.order.Order;
import domain.store.product.Product;
import domain.store.product.ProductController;
import utils.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {
    private final int storeid;
    private boolean isActive;
    private final int creatorId;
    private String storeDescription;
    private final AppHistory appHistory; //first one is always the store creator
    private final ProductController inventory; //<productID,<product, quantity>>

    private final ConcurrentHashMap<Integer, Order> storeorders;    //orederid, order

    //private final ConcurrentHashMap<Product, ArrayList<String>> productreviews;

    private final ConcurrentLinkedDeque<Message> messages; //<orderid, review>
    public Store(int id, String description, int creatorId){
        Pair<Integer, Role > creatorNode = new Pair<>(creatorId, Role.Creator);
        appHistory = new AppHistory(creatorNode);
        this.storeid = id;
        this.storeDescription = description;
        this.creatorId = creatorId;
        this.inventory = new ProductController();
        this.messages = new ConcurrentLinkedDeque<>();
        this.storeorders = new ConcurrentHashMap<>();
        //this.productreviews = new ConcurrentHashMap<>();
    }

    public int getStoreid()
    {
        return storeid;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public ConcurrentLinkedDeque<Message> getMessages() {
        return this.messages;
    }
    public ConcurrentHashMap<Integer, Order> getOrdersHistory() {
        return this.storeorders;
    }

    /**
     * gets all reviews written about that product
     * @return Arraylist of all the reviews if there is none return null
     */
//    public ArrayList<String> getProductreviews(Product product)
//    {
//        if (productreviews.containsKey(product))
//        {
//            return productreviews.get(product);
//        }
//        return null;
//    }



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

    public void addReview(int orderId, Message review) throws Exception {
        if (storeorders.containsKey(orderId))
        {
            messages.add(review);
            return;
        }
        throw new Exception("order doesnt exist");
    }

    /**
     * fire user from the store appointment tree
     * @param joblessuser the user we want to fire
     * @return set aff all the other users who lost their role in our store
     * @throws Exception if the action isn't valid will throw exception
     */
    public Set<Integer> fireUser(int joblessuser) throws Exception
    {
        return new HashSet<>(appHistory.removeChild(joblessuser));
    }

    public ProductController getInventory()
    {
        return inventory;
    }

    /**
     * creates a new product for this inventory
     * @param name new name of the product
     * @param pid product id
     */
    public Product addNewProduct(String name, String description, AtomicInteger pid)
    {
        return inventory.addProduct(name, description, pid);
    }

    /**
     * adds the quantity to the product previous quantity
     * @param pid product quantity
     */
    public void setProductQuantity(int pid, int quantity)
    {
       inventory.addQuantity(pid, quantity);
    }

    /**
     * this function meant for the store owner only to change description of product p
     * @param pid product id
     */
    public void setDescription(int pid, String description) throws Exception {
        if (inventory.getProduct(pid)!= null)
        {
            inventory.setDescription(pid, description);
            return;
        }
        throw new Exception("product isn't available at this store");
    }

    /**
     * this function meant for the store owner only to change the price of product p
     * @param pid product id
     * @param newprice new price should be a positive integer
     */
    public void setPrice(int pid, int newprice) throws Exception  {
       inventory.setPrice(pid, newprice);
    }


    public int getQuantityOfProduct(int pid) throws Exception {
        return inventory.getQuantity(pid);
    }

    public ArrayList<Product> getProductByCategories(ArrayList<String> categories) {
        return inventory.getProductByCategories(categories);
    }

    /**
     * function that gets the basket user wants to buy from the store
     * @param basket built from productid and quantity
     * @return the basket's price
     * @throws Exception if the quantity is higher than the quantity in the inventory of product doesn't exit
     */
    public int checkPurchaseProducts(HashMap<Integer, Integer> basket) throws Exception {
        int purchaseingprice = 0;
        for (Integer productid : basket.keySet())
        {
            Product p = inventory.getProduct(productid);
            if (p != null && basket.get(productid) >= p.getQuantity())
            {
                purchaseingprice += p.price * basket.get(productid);
            }
            else throw new Exception("product isn't available");
        }
        return purchaseingprice;
    }

    /**
     * purchasing confirmed so this function adjust the quantity in the store inventory
     * @return true if success else false
     */
    public boolean PurchaseProducts(HashMap<Integer, Integer> basket){
        for (Integer productid : basket.keySet())
        {
            Product p = inventory.getProduct(productid);
            if (!(p != null && basket.get(productid) >= p.getQuantity()))
            {
                return false;
            }
            inventory.getProduct(productid).setQuantity(basket.get(productid) * (-1));
        }
        return true;
    }

    public ArrayList<Product> getProductByKeywords(ArrayList<String> keywords){
        return inventory.getProductByKeywords(keywords);
    }












}
