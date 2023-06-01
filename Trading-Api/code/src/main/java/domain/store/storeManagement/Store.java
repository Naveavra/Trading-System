package domain.store.storeManagement;

import com.google.gson.Gson;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.discount.Discount;
import domain.store.discount.DiscountFactory;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.product.Inventory;

import domain.user.Basket;
import domain.user.Member;
import org.json.JSONObject;
import utils.Filter.FilterStrategy;
import utils.Filter.ProductFilter;
import utils.infoRelated.*;
import utils.messageRelated.Message;
import utils.messageRelated.MessageState;
import utils.Pair;
import utils.orderRelated.Order;
import domain.store.product.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Store extends Information{
    private final int storeid;
    private String storeName;
    private boolean isActive;
    private transient Member creator;
    private String storeDescription;
    private final AppHistory appHistory; //first one is always the store creator //n
    private final Inventory inventory; //<productID,<product, quantity>>
    private final ConcurrentHashMap<Integer, Order> storeOrders;    //orederid, order
    private final ConcurrentHashMap<Integer, Message> storeReviews; //<messageid, message>
    private final ConcurrentHashMap<Integer, Message> questions;

    private String imgUrl;
//    private DiscountPolicy discountPolicy;
    private domain.store.purchase.PurchasePolicy2Delete purchasePolicy;
    private ArrayList<Discount> discounts;
    private DiscountFactory discountFactory;

    Gson gson ;
    public Store(int id, String description, Member creator){
        Pair<Member, UserState > creatorNode = new Pair<>(creator, new StoreCreator(creator.getId(), creator.getName(), this));
        this.storeid = id;
        appHistory = new AppHistory(storeid, creatorNode);
        this.storeDescription = description;
        this.creator = creator;
        this.inventory = new Inventory(storeid);
        this.storeReviews = new ConcurrentHashMap<>();
        this.storeOrders = new ConcurrentHashMap<>();
//        this.discountPolicy = new DiscountPolicy();
        this.purchasePolicy = new domain.store.purchase.PurchasePolicy2Delete();
        this.questions = new ConcurrentHashMap<>();
        this.isActive = true;
        gson = new Gson();
        discountFactory = new DiscountFactory(storeid,inventory::getProduct,inventory::getProductCategories);
        discounts = new ArrayList<>();
    }

    public Store(int storeid, String storeName, String description, String imgUrl, Member creator){
        Pair<Member, UserState > creatorNode = new Pair<>(creator, new StoreCreator(creator.getId(), creator.getName(), this));
        this.storeid = storeid;
        appHistory = new AppHistory(storeid, creatorNode);
        this.storeDescription = description;
        this.creator = creator;
        this.inventory = new Inventory(storeid);
        this.storeReviews = new ConcurrentHashMap<>();
        this.storeOrders = new ConcurrentHashMap<>();
//        this.discountPolicy = new DiscountPolicy();
        this.purchasePolicy = new domain.store.purchase.PurchasePolicy2Delete();
        this.questions = new ConcurrentHashMap<>();
        this.isActive = true;
        gson = new Gson();
        discountFactory = new DiscountFactory(storeid,inventory::getProduct,inventory::getProductCategories);
        discounts = new ArrayList<>();
        this.storeName = storeName;
        this.imgUrl = imgUrl;
    }

    public void changeName(String storeName){
        this.storeName = storeName;
    }

    public void changeImg(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getName(){return storeName;}
    public String getImgUrl(){
        return imgUrl;
    }

    public double getStoreRating(){
        double sum = 0.0;
        for(Message msg: storeReviews.values()){
            sum+= msg.getRating();
        }
        if(storeReviews.size() == 0)
            return 0.0;
        else
            return sum / storeReviews.size();
    }

    public int addQuestion(Message m)
    {
        questions.put(m.getMessageId(), m);
        return creator.getId();
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

    public synchronized void AddDiscount(DiscountDataObject discountData){
        Discount dis = discountFactory.createDiscount(discountData);
        if(dis!=null && !discounts.contains(dis)){
            discounts.add(dis);
        }
    }
    public synchronized void AddDiscount(CompositeDataObject discountData) throws Exception {
        Discount dis = discountFactory.createDiscount(discountData);
        if(dis!=null && !discounts.contains(dis)){
            discounts.add(dis);
        }
    }

    public int addProductReview(Message m) throws Exception
    {
        if (storeOrders.containsKey(m.getOrderId()))
        {
            inventory.addProductReview(m);
            //productReviews.put(m.getMessageId(), m);
            return creator.getId();
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

    public int getCreator() {
        return creator.getId();
    }

    public HashMap<Integer, Message> getStoreReviews() {
        HashMap<Integer, Message> ans = new HashMap<>();
        for(int messageId : storeReviews.keySet())
            ans.put(messageId, storeReviews.get(messageId));
        for(int messageId : inventory.getProductReviews().keySet())
            ans.put(messageId,  inventory.getProductReviews().get(messageId));
        return ans;
    }
    public HashMap<Integer, Message> getStoreQuestions() {
        HashMap<Integer, Message> ans = new HashMap<>();
        for(int messageId : questions.keySet())
            ans.put(messageId, questions.get(messageId));
        return ans;
    }
    public List<OrderInfo> getOrdersHistory() {
        List<OrderInfo> orderInfos = new LinkedList<>();
        for(int orderId : storeOrders.keySet()){
            Order order = storeOrders.get(orderId);
            OrderInfo orderInfo = new OrderInfo(orderId, order.getUserId(), order.getShoppingCart(), order.getTotalPrice());
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


    public void appointUser(int userinchargeid, Member newUser, UserState role) throws Exception {
        Pair<Member, UserState> node = new Pair<>(newUser, role);
        appHistory.addNode(userinchargeid, node);
    }

    public int addReview(int orderId, Message review) throws Exception {
        if (storeOrders.containsKey(orderId))
        {
            storeReviews.put(review.getMessageId(), review);
            return creator.getId();
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
     * @param price int
     * @param quantity
     */
    public synchronized Product addNewProduct(String name, String description, AtomicInteger pid, int price, int quantity) throws Exception {
        return inventory.addProduct(name, description, pid,price,quantity);
    }

    public synchronized Product addNewProduct(String name, String description, AtomicInteger pid, int price,
                                              int quantity, String img) throws Exception {
        return inventory.addProduct(name, description, pid,price,quantity, img);
    }
    public synchronized Product addNewExistingProduct(Product p) throws Exception{
        return inventory.addProduct(p);
    }
    /**
     * adds the quantity to the product previous quantity
     * @param pid product quantity
     */
    public void setProductQuantity(int pid, int quantity) throws Exception
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
        if (creator.getId() == userID){
            isActive = false;
            return appHistory.getUsers();
        }
        throw new Exception("user isn't authorized to close this store");
    }

    public Set<Integer> reopenStore(int userID) throws Exception{
        if (creator.getId() == userID){
            isActive = true;
            return appHistory.getUsers();
        }
        throw new Exception("user isn't authorized to reopen this store");
    }

//    /**
//     * function that gets the basket user wants to buy from the store
//     * @param basket built from productid and quantity
//     * @return the basket's price
//     * @throws Exception if the quantity is higher than the quantity in the inventory of product doesn't exit
//     */
//    public int createOrder(HashMap<Integer, Integer> basket) throws Exception {
//        int purchaseingprice = 0;
//        for (Integer productid : basket.keySet())
//        {
//            Product p = inventory.getProduct(productid);
//            if (p != null)
//            {
//                if(basket.get(productid) <= p.getQuantity()) {
//                    int discount = handleDiscounts(basket, inventory.getPrices());
//                    purchaseingprice += p.price * basket.get(productid) - discount;
//                }
//                else {
//                    throw new Exception("not enough units in store for " + p.getName() +
//                            " there is only " + p.quantity + " in the store");
//                }
//            }
//            else throw new Exception("product isn't available");
//        }
//        return purchaseingprice;
//    }


    /**
     * purchasing confirmed so this function adjust the quantity in the store inventory
     * @return true if success else false
     */
    public boolean makeOrder(Basket basket) throws Exception{
        for (ProductInfo product : basket.getContent())
        {
            Product p = inventory.getProduct(product.id);
            if (!(p != null && product.quantity <= p.getQuantity()))
            {
                return false;
            }
            inventory.getProduct(product.id).setQuantity(product.quantity * (-1));
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
            return inventory.getProductInfo(storeid, productId);
        }
        throw new Exception("cant get product information");
    }

    //TODO HANDLE DISCOUNTS FROM HERE MAYBE?
    public synchronized int calculatePrice(Basket basket) throws Exception {
        int purchaseingprice = 0;
        for (ProductInfo product : basket.getContent())
        {
            Product p = inventory.getProduct(product.id);
            if (p != null && product.quantity <= p.getQuantity())
            {
//                int discount = discountPolicy.handleDiscounts(basket,inventory.getPrices());
                purchaseingprice += p.price * product.quantity;
//                p.setQuantity(p.getQuantity()-basket.get(productid));
            }
            else throw new Exception("product isn't available");
        }
        return purchaseingprice;
    }

    public List<ProductInfo> getProducts(){
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

    public void addToCategories(int productId, List<String> categories) throws Exception{
        for(String category: categories){
            inventory.addToCategory(category,productId);
        }
    }

    public void removeProduct(int productId) throws Exception{
        if(!(inventory.removeProduct(productId)>-1)){
            throw new Exception("Unable to remove Product, productId doesn't exist.");
        }
    }

    public void updateProduct(int productId, List<String> categories, String name, String description,
                              int price, int quantity, String img) throws Exception {
        inventory.updateProduct(productId,categories,name,description,price,quantity, img);
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
        StoreInfo info = new StoreInfo(storeid, storeName, storeDescription, isActive, creator.getId(), getStoreRating(), imgUrl);
        return info;
    }

    public double handleDiscount(Order order) throws Exception {
        double totalAmountToBeSubtracted = 0;
        for(Discount dis: discounts){
            totalAmountToBeSubtracted += dis.handleDiscount(order.getShoppingCart().getBasket(storeid),order);
        }
        order.setTotalPrice(order.getTotalPrice() - totalAmountToBeSubtracted);
        return order.getTotalPrice();
    }

    public List<Pair<Info, List<Info>>> getApp() throws Exception{
        return appHistory.getAppHistory();
    }

    public List<JSONObject> getAppJson(){
        return appHistory.toJson();
    }
    public List<UserState> getRoles(){
        return appHistory.getRoles();
    }
    public AppHistory getAppHistory(){
        return appHistory;
    }

    public void checkProductInStore(int productId) throws Exception{
        inventory.getProduct(productId);
    }

    @Override
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("storeId", getStoreId());
        json.put("storeName", getName());
        json.put("description", getStoreDescription());
        json.put("isActive", isActive());
        json.put("creatorId", getCreator());
        json.put("appHistory", getAppJson());
        json.put("inventory", infosToJson(getProducts()));
        json.put("storeOrders", infosToJson(getOrdersHistory()));
        json.put("reviews", hashMapToJson(getStoreReviews(), "messageId", "review"));
        json.put("questions", hashMapToJson(getStoreQuestions(), "messageId", "question"));
        json.put("img", getImgUrl());
        json.put("roles", infosToJson(getRoles()));
        return json;
    }

    public void setStoreAttributes(String name, String description, String img) {
        if(!description.equals("null"))
            setStoreDescription(description);
        if(!name.equals("null"))
            changeName(name);
        if(!img.equals("null"))
            changeImg(img);
    }

//    public void setStoreDiscountPolicy(String policy) throws Exception {
//        try {
//            addDiscountConstraint(policy);
//        } catch (Exception e) {
//            throw new Exception("Couldn't create a new policy");
//        }
//    }
//
//    private void addDiscountConstraint(String policy) throws Exception {
//        if(!discountPolicy.createConstraint(policy)){
//            throw new Exception("Couldn't create the constraint");
//        }
//    }
}
