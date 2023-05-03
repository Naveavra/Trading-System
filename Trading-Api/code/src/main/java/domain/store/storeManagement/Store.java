package domain.store.storeManagement;

import com.google.gson.Gson;
import domain.store.discount.DiscountPolicy;
import domain.store.product.Inventory;
import domain.store.purchase.PurchasePolicy;

import utils.Filter.FilterStrategy;
import utils.Filter.ProductFilter;
import utils.ProductInfo;
import utils.StoreInfo;
import utils.messageRelated.Message;
import utils.messageRelated.MessageState;
import utils.Pair;
import utils.orderRelated.Order;
import domain.store.product.Product;
import utils.orderRelated.OrderInfo;
import utils.stateRelated.Role;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Store {
    private final int storeid;
    private boolean isActive;
    private final transient int creatorId;
    private String storeDescription;
    private final AppHistory appHistory; //first one is always the store creator
    private final Inventory inventory; //<productID,<product, quantity>>
    private final ConcurrentHashMap<Integer, Order> storeOrders;    //orederid, order
    private final ConcurrentHashMap<Integer, Message> storeReviews; //<messageid, message>
    private final ConcurrentHashMap<Integer, Message> questions;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;

    private final ConcurrentHashMap<Integer, Message> productReviews; //maybe need to remove from here, i think this is unnecessary
    Gson gson ;
    public Store(int id, String description, int creatorId){
        Pair<Integer, Role > creatorNode = new Pair<>(creatorId, Role.Creator);
        appHistory = new AppHistory(creatorNode);
        this.storeid = id;
        this.storeDescription = description;
        this.creatorId = creatorId;
        this.inventory = new Inventory();
        this.storeReviews = new ConcurrentHashMap<>();
        this.storeOrders = new ConcurrentHashMap<>();
        this.discountPolicy = new DiscountPolicy();
        this.purchasePolicy = new PurchasePolicy();
        this.productReviews = new ConcurrentHashMap<>();//hash map between messageId to message for product
        this.questions = new ConcurrentHashMap<>();
        this.isActive = true;
        gson = new Gson();
    }


    public double getStoreRating(){
        double sum = 0.0;
        for(Message msg: storeReviews.values()){
            sum+= msg.getRating();
        }
        return sum/storeReviews.size();
    }
    public AppHistory getAppHistory(){return appHistory;}

    public int addQuestion(Message m)
    {
        questions.put(m.getMessageId(), m);
        return creatorId;
    }

    public void answerQuestion(int messageID, String answer) throws Exception {
        Message msg = questions.get(messageID);
        if (msg != null && msg.getState() == MessageState.question && !msg.getSeen())
        {
            msg.sendFeedback(answer);
            return;
        }
        throw new Exception("cant answer question");
    }


    public int addProductReview(Message m) throws Exception
    {
        if (storeOrders.containsKey(m.getOrderId()))
        {
            productReviews.put(m.getMessageId(), m);
            return creatorId;
        }
        else
        {
            throw new Exception("cant add review for this product");
        }
    }

    public int getStoreId()
    {
        return storeid;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public HashMap<Integer, Message> getStoreReviews() {
        HashMap<Integer, Message> ans = new HashMap<>();
        for(int messageId : storeReviews.keySet())
            ans.put(messageId, storeReviews.get(messageId));
        for(int messageId : productReviews.keySet())
            ans.put(messageId, productReviews.get(messageId));
        return ans;
    }
    public List<OrderInfo> getOrdersHistory() {
        List<OrderInfo> orderInfos = new LinkedList<>();
        for(int orderId : storeOrders.keySet()){
            Order order = storeOrders.get(orderId);
            OrderInfo orderInfo = new OrderInfo(orderId, order.getUserId(), order.getProductsInStores(), order.getTotalPrice());
            orderInfos.add(orderInfo);
        }
        return orderInfos;
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

    public int addReview(int orderId, Message review) throws Exception {
        if (storeOrders.containsKey(orderId))
        {
            storeReviews.put(review.getMessageId(), review);
            return creatorId;
        }
        throw new Exception("order doesnt exist");
    }

    public void addOrder(Order order){
        storeOrders.put(order.getOrderId(), order);
    }

    /**
     * @return the users that has a role in the store
     */
    public Set<Integer> getUsersInStore(){
        return appHistory.getUsers();
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

    public Inventory getInventory()
    {
        return inventory;
    }

    /**
     * creates a new product for this inventory
     * @param name new name of the product
     * @param pid product id
     */
    public synchronized Product addNewProduct(String name, String description, AtomicInteger pid) throws Exception {
        return inventory.addProduct(name, description, pid);
    }
    public synchronized Product addNewExistingProduct(Product p){
        return inventory.addProduct(p);
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

//    public ArrayList<Product> getProductByCategories(ArrayList<String> categories) {
//        return inventory.getProductByCategories(categories);
//    }

    /**
     * @param userID creator id
     * @return all the users that have a role in this store
     * @throws Exception if the user isn't the store creator
     */
    public Set<Integer> closeStoreTemporary(int userID) throws Exception {
        if (creatorId == userID){
            isActive = false;
            return appHistory.getUsers();
        }
        throw new Exception("user isn't authorized to close this store");
    }

    public Set<Integer> reopenStore(int userID) throws Exception{
        if (creatorId == userID){
            isActive = true;
            return appHistory.getUsers();
        }
        throw new Exception("user isn't authorized to reopen this store");
    }

    /**
     * function that gets the basket user wants to buy from the store
     * @param basket built from productid and quantity
     * @return the basket's price
     * @throws Exception if the quantity is higher than the quantity in the inventory of product doesn't exit
     */
    public int createOrder(HashMap<Integer, Integer> basket) throws Exception {
        int purchaseingprice = 0;
        for (Integer productid : basket.keySet())
        {
            Product p = inventory.getProduct(productid);
            if (p != null && basket.get(productid) <= p.getQuantity())
            {
                int discount = discountPolicy.handleDiscounts(basket,inventory.getPrices());
                purchaseingprice += p.price * basket.get(productid) -  discount;
//                p.setQuantity(p.getQuantity()-basket.get(productid));
            }
            else throw new Exception("product isn't available");
        }
        return purchaseingprice;
    }


    /**
     * purchasing confirmed so this function adjust the quantity in the store inventory
     * @return true if success else false
     */
    public boolean makeOrder(HashMap<Integer, Integer> basket){
        for (Integer productId : basket.keySet())
        {
            Product p = inventory.getProduct(productId);
            if (!(p != null && basket.get(productId) <= p.getQuantity()))
            {
                return false;
            }
            inventory.getProduct(productId).setQuantity(basket.get(productId) * (-1));
        }
        return true;
    }




    public String getProductName(int productId) throws Exception{
        Product p = inventory.getProduct(productId);
        if (p!= null)
        {
            return p.getName();
        }
        throw new Exception("product doesnt exist");
    }


    public ArrayList<String> checkMessages() {
        ArrayList<String> messagesToRead = new ArrayList<>();
        for (Message m : storeReviews.values()){
            if (!m.getSeen())
            {
                messagesToRead.add(m.getContent());
                m.markAsRead();
            }
        }
        return messagesToRead;

    }


    public ProductInfo getProductInformation(int productId) throws Exception{

        if (inventory.getProduct(productId) != null)
        {
            Product p = inventory.getProduct(productId);
            return new ProductInfo(p.getID(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity());
        }
        throw new Exception("cant get product information");
    }


    public synchronized int caclulatePrice(HashMap<Integer, Integer> basket) throws Exception {
        int purchaseingprice = 0;
        for (Integer productid : basket.keySet())
        {
            Product p = inventory.getProduct(productid);
            if (p != null && basket.get(productid) <= p.getQuantity())
            {
                int discount = discountPolicy.handleDiscounts(basket,inventory.getPrices());
                purchaseingprice += p.price * basket.get(productid) -  discount;
//                p.setQuantity(p.getQuantity()-basket.get(productid));
            }
            else throw new Exception("product isn't available");
        }
        return purchaseingprice;
    }

    public List<ProductInfo> getProducts() {
        return inventory.getProducts();
    }

    public void setStorePolicy(String policy) throws Exception {
        // i think the policy holds the constraints. or in different words, constraints define the policies.
        try {
            addPurchaseConstraint(policy);
        } catch (Exception e) {
            throw new Exception("Couldn't create a new policy");
        }
    }

    public void addPurchaseConstraint(String constraint)throws Exception {
        if(!purchasePolicy.createConstraint(constraint)){
            throw new Exception("Couldn't create the constraint");
        }
    }

    public HashMap<Integer, Message> getQuestions() { //<messageids, message>
        HashMap<Integer, Message> questionsToAnswer = new HashMap<>();
        for (Message message : this.questions.values())
        {
            if (!message.getSeen())
            {
                questionsToAnswer.put(message.getMessageId(), message);
            }
        }
        return questionsToAnswer;
    }

    public void addToCategories(int productId, List<String> categories){
        for(String category: categories){
            inventory.addToCategory(category,productId);
        }
    }

    public void removeProduct(int productId) throws Exception{
         if(!(inventory.removeProduct(productId)>-1)){
             throw new Exception("Unable to remove Product, productId doesn't exist.");
         }
    }

    public void updateProduct(int productId, List<String> categories, String name, String description, int price, int quantity) throws Exception {
        inventory.updateProduct(productId,categories,name,description,price,quantity);
    }



    public ArrayList<ProductInfo> filterBy(HashMap<String, String> filterOptions) {
        ProductFilter filter = new ProductFilter();
        for (String option:filterOptions.keySet()){
            FilterStrategy next = filter.createStrategy(filter.getStrategy(option),filterOptions.get(option));
            if(next!=null) {
                filter.addStrategy(next);
            }
        }
        return inventory.filterBy(filter,getStoreRating());
    }

    public StoreInfo getStoreInformation() {
        StoreInfo info = new StoreInfo(storeid, storeDescription, isActive, creatorId, getStoreRating());
        return info;
    }

    public void setStoreDiscountPolicy(String policy) throws Exception {
        try {
            addDiscountConstraint(policy);
        } catch (Exception e) {
            throw new Exception("Couldn't create a new policy");
        }
    }

    private void addDiscountConstraint(String policy) throws Exception {
        if(!discountPolicy.createConstraint(policy)){
            throw new Exception("Couldn't create the constraint");
        }
    }
}