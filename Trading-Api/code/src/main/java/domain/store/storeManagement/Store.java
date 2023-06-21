package domain.store.storeManagement;

import database.daos.Dao;
import database.DbEntity;
import database.daos.MessageDao;
import database.daos.StoreDao;
import database.daos.SubscriberDao;
import database.dtos.Appointment;
import database.dtos.ConstraintDto;
import database.dtos.DiscountDto;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.discount.Discount;
import domain.store.discount.DiscountFactory;
import domain.store.discount.DiscountOnItem;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.product.Inventory;

import domain.store.purchase.PurchasePolicy;
import domain.store.purchase.PurchasePolicyDataObject;
import domain.store.purchase.PurchasePolicyFactory;
import domain.user.Basket;
import domain.user.Member;
import domain.user.ShoppingCart;
import domain.user.User;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mockito.internal.matchers.Or;
import utils.Filter.FilterStrategy;
import utils.Filter.ProductFilter;
import utils.infoRelated.*;
import utils.messageRelated.Message;
import utils.Pair;
import utils.messageRelated.ProductReview;
import utils.messageRelated.Question;
import utils.messageRelated.StoreReview;
import utils.orderRelated.Order;
import domain.store.product.Product;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;



@Entity
@Table(name = "stores")
public class Store extends Information implements DbEntity {
    @Id
    private int storeId;
    private String storeName;
    private boolean isActive;

    private int creatorId;
    @Transient
    private Member creator;

    private String storeDescription;
    @Transient
    private AppHistory appHistory; //first one is always the store creator
    @Transient
    private List<Appointment> appointments;
    @Transient
    private Inventory inventory; //<productID,<product, quantity>>
    @Transient
    private ConcurrentHashMap<Integer, Order> storeOrders; //orederid, order
    @Transient
    private ConcurrentHashMap<Integer, StoreReview> storeReviews; //<messageid, message>
    @Transient
    private ConcurrentHashMap<Integer, Question> questions;
    @Transient
    private AtomicInteger bidIds;
    @Transient
    private ArrayList<Bid> bids;
    private String imgUrl;
    @Transient
    private ArrayList<PurchasePolicy> purchasePolicies;
    @Transient
    private ArrayList<Discount> discounts;
    @Transient
    private DiscountFactory discountFactory;

    @Transient
    private AtomicInteger policyIds;

    public Store(){
    }
    public Store(int storeId, String description, Member creator){
        this.storeId = storeId;
        StoreCreator sc = new StoreCreator(creator.getId(), creator.getName(), this);
        Pair<Member, UserState > creatorNode = new Pair<>(creator, sc);
        this.storeDescription = description;
        this.creator = creator;
        this.creatorId = creator.getId();
        this.storeReviews = new ConcurrentHashMap<>();
        this.storeOrders = new ConcurrentHashMap<>();
        this.purchasePolicies = new ArrayList<>();
        this.questions = new ConcurrentHashMap<>();
        this.isActive = true;
        discounts = new ArrayList<>();
        bids = new ArrayList<>();
        bidIds = new AtomicInteger();
        Dao.save(this);
        appHistory = new AppHistory(storeId, creatorNode);
        appointments = new ArrayList<>();
        this.inventory = new Inventory(storeId);
        discountFactory = new DiscountFactory(storeId,inventory::getProduct,inventory::getProductCategories);
        policyIds = new AtomicInteger();

    }

    public Store(int storeId, String storeName, String description, String imgUrl, Member creator){
        this.storeId = storeId;
        StoreCreator sc = new StoreCreator(creator.getId(), creator.getName(), this);
        Pair<Member, UserState > creatorNode = new Pair<>(creator, sc);
        this.storeDescription = description;
        this.creator = creator;
        this.creatorId = creator.getId();
        this.storeReviews = new ConcurrentHashMap<>();
        this.storeOrders = new ConcurrentHashMap<>();
//        this.discountPolicy = new DiscountPolicy();
        this.purchasePolicies = new ArrayList<>();
        this.questions = new ConcurrentHashMap<>();
        this.isActive = true;
        discounts = new ArrayList<>();
        this.storeName = storeName;
        this.imgUrl = imgUrl;
        bidIds = new AtomicInteger();
        bids = new ArrayList<>();
        Dao.save(this);
        appHistory = new AppHistory(storeId, creatorNode);
        appointments = new ArrayList<>();
        this.inventory = new Inventory(storeId);
        discountFactory = new DiscountFactory(storeId,inventory::getProduct,inventory::getProductCategories);
        policyIds = new AtomicInteger();

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
        for(StoreReview msg: storeReviews.values()){
            sum+= msg.getRating();
        }
        if(storeReviews.size() == 0)
            return 0.0;
        else
            return sum / storeReviews.size();
    }

    public int addQuestion(Question q)
    {
        questions.put(q.getMessageId(), q);
        return creatorId;
    }


    public List<Bid> getBids()
    {
        return bids;
    }
    public ArrayList<Discount> getDiscounts(){return this.discounts;}

    public List<String> getDiscountsContent(){
        List<String> ans = new ArrayList<>();
        for(Discount d : discounts)
            ans.add(d.getContent());
        return ans;
    }

    public void answerQuestion(int messageID, String answer) throws Exception {
        Question msg = questions.get(messageID);
        if(msg != null) {
            msg.sendFeedback(answer);
        }
        else
            throw new Exception("the id given does not belong to any question that was sent to store");
    }

    public synchronized void addDiscount(DiscountDataObject discountData,String content){
        Discount dis = discountFactory.createDiscount(discountData);
        if(dis!=null && !discounts.contains(dis)){
            dis.setContent(content);
            dis.setDescription(new JSONObject(content).get("description").toString());
            discounts.add(dis);
            //TODO: check if need to add here to db
            Dao.save(new DiscountDto(storeId, dis.getDiscountID(), dis.getContent()));
        }
    }
    public synchronized void addDiscount(CompositeDataObject discountData,String content) throws Exception {
        Discount dis = discountFactory.createDiscount(discountData);
        if(dis!=null && !discounts.contains(dis)){
            dis.setDescription(new JSONObject(content).get("description").toString());
            dis.setContent(content);
            discounts.add(dis);
            StoreDao.saveDiscount(new DiscountDto(storeId, dis.getDiscountID(), dis.getContent()));
        }
    }

    public int addProductReview(ProductReview m) throws Exception
    {
        if (storeOrders.containsKey(m.getOrderId()))
        {
            inventory.addProductReview(m);
            return creatorId;
        }
        else
            throw new Exception("cant add review for this product");
    }

    public int getStoreId()
    {
        return storeId;
    }

    public int getCreator() {
        return creatorId;
    }

    public List<StoreReview> getStoreReviews() {
        List<StoreReview> ans = new ArrayList<>(storeReviews.values());
        ans.addAll(inventory.getProductReviews());
        return ans;
    }
    public List<OrderInfo> getOrdersHistory() {
        List<OrderInfo> orderInfos = new LinkedList<>();
        for(int orderId : storeOrders.keySet()){
            Order order = storeOrders.get(orderId);
            OrderInfo orderInfo = new OrderInfo(orderId, order.getUser().getId(), order.getShoppingCart(), order.getTotalPrice());
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

    public void addAppointment(Appointment appointment) {
        if(!appointment.getApproved())
            appointments.add(appointment);
    }

    public void answerAppointment(String userName, String fatherName, String childName, String ans) throws Exception{
        Appointment app = null;
        for(Appointment appointment : appointments)
            if (appointment.getFatherName().equals(fatherName) && appointment.getChildName().equals(childName))
                app = appointment;
        if(app != null) {
            if (ans.equals("true")) {
                app.approve(userName);
                if (app.getApproved())
                    appointments.remove(app);
            } else if (ans.equals("false")) {
                if (app.canDeny(userName))
                    appointments.remove(app);
            }
        }
    }

    public void canAppointUser(int father, Member appointed, Role role) throws Exception{
        appHistory.canAddNode(father, appointed, role);
    }
    public void appointUser(int userinchargeid, Member newUser, UserState role) throws Exception {
        Pair<Member, UserState> node = new Pair<>(newUser, role);
        appHistory.addNode(userinchargeid, node);
        //Member father = appHistory.getNode(userinchargeid).getData().getFirst();
    }
    public int addReview(int orderId, StoreReview review) throws Exception {
        if (storeOrders.containsKey(orderId))
        {
            storeReviews.put(review.getMessageId(), review);
            return creatorId;
        }
        throw new Exception("order doesnt exist");
    }

    public void addOrder(ShoppingCart cart, int orderId, User user) throws Exception {
        ShoppingCart newCart = new ShoppingCart();
        for (Basket basket : cart.getBaskets())
        {
            if (basket.getStoreId() == storeId)
            {
                for (ProductInfo p : basket.getProductList())
                {
                    newCart.addProductToCart(storeId, p, p.quantity);
                }
                Order order = new Order(orderId, user, cart);
                storeOrders.put(orderId, order);
            }
        }
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
        String joblessName = appHistory.getNode(joblessuser).getData().getFirst().getName();
        Set<Integer> ans = new HashSet<>(appHistory.removeChild(joblessuser));
        appointments.removeIf(app -> app.getFatherId() == joblessuser || app.getChildId() == joblessuser);
        for(Appointment app : appointments)
            if(app.containsInApprove(joblessName))
                app.removeApprover(joblessName);
        for(Bid bid: bids)
            if(bid.containsInApprove(joblessName))
                bid.removeApprover(joblessName);
        StoreDao.removeAppointment(storeId, joblessuser, joblessName);
        return ans;
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
    public synchronized Product addNewProduct(String name, String description, AtomicInteger pid, int price,
                                              int quantity, List<String> categories) throws Exception {
        return inventory.addProduct(name, description, pid,price,quantity, categories);
    }

    public synchronized Product addNewProduct(String name, String description, AtomicInteger pid, int price,
                                              int quantity, String img, List<String> categories) throws Exception {
        return inventory.addProduct(name, description, pid,price,quantity, img, categories);
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
     * @param newPrice new price should be a positive integer
     */
    public void setPrice(int pid, int newPrice) throws Exception  {
        inventory.setPrice(pid, newPrice);
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
            return inventory.getProductInfo(storeId, productId);
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

//    public synchronized void setStorePolicy(String policy) throws Exception {
//        try {
//            purchasePolicies.add(new PurchasePolicyFactory().createPolicy());
//        } catch (Exception e) {
//            throw new Exception("Couldn't create a new policy");
//        }
//    }

    public ArrayList<PurchasePolicy> getPurchasePolicies(){
        return purchasePolicies;
    }

    public List<Question> getAllQuestions(){
        return new ArrayList<>(questions.values());
    }
    public List<Message> getQuestions() {
        List<Message> questionsToAnswer = new ArrayList<>();
        for (Message message : this.questions.values())
        {
            if (!message.getSeen())
            {
                questionsToAnswer.add(message);
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
        inventory.removeProduct(productId);
    }

    public void updateProduct(int productId, List<String> categories, String name, String description,
                              int price, int quantity, String img) throws Exception {
        inventory.updateProduct(productId,categories,name,description,price,quantity, img);
    }



    public ArrayList<ProductInfo> filterBy(HashMap<String, String> filterOptions) throws Exception{
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
        StoreInfo info = new StoreInfo(storeId, storeName, storeDescription, isActive, creator.getId(), getStoreRating(),
                imgUrl, bids, appointments, discounts, purchasePolicies);
        return info;
    }

    public double handleDiscount(Order order) throws Exception {
        double totalAmountToBeSubtracted = 0;
        for(Discount dis: discounts){
            totalAmountToBeSubtracted += dis.handleDiscount(order.getShoppingCart().getBasket(storeId),order);
        }
        order.setTotalPrice(order.getTotalPrice() - totalAmountToBeSubtracted);
        return order.getTotalPrice();
    }

    public List<Pair<Info, List<Info>>> getApp(){
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
        json.put("reviews", infosToJson(getStoreReviews()));
        json.put("questions", infosToJson(getQuestions()));
        json.put("img", getImgUrl());
        json.put("roles", infosToJson(getRoles()));
        json.put("bids", infosToJson(getBids()));
        json.put("discounts", infosToJson(getDiscounts()));
        json.put("purchasePolicies",infosToJson(getPurchasePolicies()));
        json.put("appointments", infosToJson(appointments));
        return json;
    }


    public void setStoreAttributes(String name, String description, String img) {
        if(!description.equals("null"))
            setStoreDescription(description);
        if(!name.equals("null"))
            changeName(name);
        if(!img.equals("null"))
            changeImg(img);
        Dao.save(this);
    }

    //sets the bid flag on a product to true; meaning that potential costumers can now bid on the product.
//    public void createBid(int prodId) throws Exception {
//        inventory.getProduct(prodId).setBid();
//    }


    public List<String> placeBid(Member user, int prodId, double price,int quantity) throws Exception {
        for(Bid bid : this.bids){
            if(bid.getUser().getId() == user.getId() && bid.getProduct().getID() == prodId)
                throw new Exception("Cannot place a bid on the same item more than once.");
        }
        Bid b = new Bid(bidIds.getAndIncrement(),user,storeId,inventory.getProduct(prodId),price,quantity,
                (ArrayList<String>) appHistory.getStoreWorkersWithPermission(Action.updateProduct));
        bids.add(b);
        user.addBid(b);
        return b.getApprovers();
    }

    public Bid answerBid(int bidId,String userName, int prodId, boolean ans) throws Exception {
        for(Bid bid : this.bids){
            if(bid.getBidId() == bidId && bid.isPending()){
                if(ans){
                    bid.approveBid(userName);
                    if(bid.isApproved()){
                        return bid;
                    }
                    return null;
                }
                bid.declineBid();
                //eli needs to send message to the user that placed the bid
                return null;
            }
        }
        return null;
    }

    public List<String> counterBid(int bidId, double counterOffer, String userName) throws Exception {
        for(Bid bid : this.bids){
            if(bid.getBidId() == bidId){
                bid.counterBid(counterOffer,userName);
                List<String> ans = new ArrayList<>(bid.getApprovers());
                ans.add(bid.getUser().getName());
                return ans;
            }
        }
        throw new Exception("Bid doesnt exist "+bidId);
    }

    public Bid editBid(int bidId, double price, int quantity) throws Exception {
        for(Bid bid: this.bids){
            if(bid.getBidId() == bidId){
                bid.editBid(price,quantity);
                return bid;
            }
        }
        throw new Exception("Bid doesnt exist "+bidId);
    }

    public void addPurchasePolicy(String data,String content) throws Exception {
        PurchasePolicyFactory factory = new PurchasePolicyFactory(policyIds,storeId);
        JSONObject request = new JSONObject(data);
        String description = request.getString("description");
        JSONArray policies = request.getJSONArray("type");
        PurchasePolicyDataObject head = null;
        PurchasePolicyDataObject prev = null;
        for(int i = 0 ; i<policies.length() ; i++){
//            PurchasePolicyDataObject actualPolicy = null;
            JSONObject policy = policies.getJSONObject(i);
            String type = policy.getString("type");
            switch (type){
                case "item" -> prev = factory.parseItem(policy,prev);
                case "category" -> prev = factory.parseCategory(policy,prev);
                case "dateTime" -> prev = factory.parseDateTime(policy,prev);
                case "user" -> prev = factory.parseUser(policy,prev);
                case "basket" -> prev = factory.parseBasket(policy,prev);
            }
            if(i==0 && prev!=null) {
                head = prev;
            }
        }
        if(head!=null){
            PurchasePolicy policy = factory.createPolicy(head);
            policy.setDescription(description);
            policy.setContent(content);
            purchasePolicies.add(policy);
            //TODO: need to check if is ok by nave/miki
            Dao.save(new ConstraintDto(storeId, policy.getId(), policy.getContent()));
            return;
        }
        throw new Exception("Something went wrong when creating the policy, please contact us if the problem persists.\nYours truly, the developers A-team");

    }

    public synchronized boolean handlePolicies(Order order) throws Exception {
        boolean res = true;
        for(PurchasePolicy policy : purchasePolicies){
            res = policy.validate(order);
            if(!res)
                return res;
        }
        return res;
    }

    public void removePolicy(int policyId) {
        purchasePolicies.removeIf(policy -> policy.policyID == policyId);
    }

    public Set<Integer> getStoreCreatorsOwners() {
        return appHistory.getStoreCreatorsOwners();
    }

    public int getBidClient(int bidId) throws Exception {
        for (Bid bid : bids)
        {
            if (bid.getBidId() == bidId){return bid.getUser().getId();}
        }
        throw new Exception("cant find bid Id");
    }

    public List<String> getApprovers(int bidId) throws Exception {
        for (Bid bid : bids)
        {
            if (bid.getBidId() == bidId){return bid.getApprovers();}
        }
        throw new Exception("bid doesnt exist");
    }

    public void clientAcceptCounter(int bidId) {
        for (Bid bid : bids){
            if (bid.getBidId() == bidId){bid.clientAcceptCounter();}
        }
    }

    //database
    @Override
    public void initialParams() {
        getCreatorFromDb();
        getAppHistoryFromDb();
        getInventoryFromDb();

        getStoreReviewsFromDb();
        getQuestionsFromDb();

        getOrdersFromDb();
        getBidsFromDb();
        getConstraintsFromDb();
        getDiscountsFromDb();

//        storeOrders = new ConcurrentHashMap<>();
//        bids = new ArrayList<>();
//        purchasePolicies = new ArrayList<>();
//        discounts = new ArrayList<>();
//        discountFactory = new DiscountFactory(storeId,inventory::getProduct,inventory::getProductCategories);
    }


    private void getCreatorFromDb(){
        if(creator == null){
            creator = SubscriberDao.getMember(creatorId);
        }
    }

    private void getAppHistoryFromDb(){
        if(appHistory == null)
            appHistory = StoreDao.getAppHistory(storeId, creatorId);
        if(appointments == null)
            appointments = StoreDao.getAppointments(storeId);
    }

    private void getInventoryFromDb(){
        if(inventory == null){
            inventory = new Inventory(storeId);
            inventory.initialParams();
        }
    }

    private void getStoreReviewsFromDb(){
        if(storeReviews == null){
            storeReviews = new ConcurrentHashMap<>();
            List<StoreReview> storeReviewsDto = MessageDao.getStoreReviews(storeId);
            for(StoreReview storeReview : storeReviewsDto)
                storeReviews.put(storeReview.getMessageId(), storeReview);
        }
    }

    private void getQuestionsFromDb() {
        if(questions == null){
            questions = new ConcurrentHashMap<>();
            List<Question> storeQuestions = MessageDao.getQuestions(storeId);
            for(Question question : storeQuestions)
                questions.put(question.getMessageId(), question);
        }
    }

    private void getOrdersFromDb() {
        if(storeOrders == null){
            storeOrders = new ConcurrentHashMap<>();
            List<Order> orders = StoreDao.getOrders(storeId);
            for(Order order : orders)
                storeOrders.put(order.getOrderId(), order);
        }
    }

    private void getBidsFromDb() {
        if(bids == null){
            bids = (ArrayList<Bid>) StoreDao.getBids(storeId);
        }
    }

    private void getConstraintsFromDb() {
        if(purchasePolicies == null) {
            purchasePolicies = new ArrayList<>();
            List<ConstraintDto> constraintDtos = StoreDao.getConstraints(storeId);
            for (ConstraintDto constraintDto : constraintDtos) {
                try {
                    parsePurchasePolicy(constraintDto.getContent());
                } catch (Exception ignored) {
                }
            }
        }

    }

    private void getDiscountsFromDb() {
        if(discounts == null){
            discounts = new ArrayList<>();
            discountFactory = new DiscountFactory(storeId,inventory::getProduct,inventory::getProductCategories);
            List<DiscountDto> discountDtos = StoreDao.getDiscounts(storeId);
            for(DiscountDto discountDto : discountDtos)
                parseDiscounts(discountDto.getContent());
        }
    }

    public void addCompositeDiscount(JSONObject req) throws Exception {
        CompositeDataObject dis = discountFactory.parseCompositeDiscount(req);
        addDiscount(dis,req.toString());
    }
    public void parsePurchasePolicy(String content) throws Exception {
        JSONObject obj = new JSONObject(content);
        String purchasePolicy = obj.get("purchasePolicy").toString();
        addPurchasePolicy(purchasePolicy,content);
    }
    public void parseDiscounts(String content){
        discountFactory.parse(content,discounts);
    }
  
    public Bid getBid(int bidId) throws Exception{
        for(Bid b : bids)
            if(b.getBidId() == bidId)
                return b;
        throw new Exception("the id given does not belong to any bid in store");
    }

    public void removeConstraint(int constraintId) throws Exception {
        for(PurchasePolicy policy: purchasePolicies){
            if(policy.getId() == constraintId) {
                purchasePolicies.remove(policy);
                StoreDao.removeConstraint(storeId, constraintId);
            }
        }
        throw new Exception("the id given does not belong to any policy in store");
    }

    public void removeDiscount(int discountId) throws Exception {
        for(Discount dis: discounts){
            if(dis.getDiscountID() == discountId) {
                discounts.remove(dis);
                StoreDao.removeDiscount(storeId, discountId);
                return;
            }
        }
        throw new Exception("Discount ID doesn't exist Homeie");
    }

//    public void clientAcceptCounter(int bidId) {
//        Member user;
//        int prodId;
//        double price;
//        int quantity;
//        for (Bid bid : bids)
//        {
//           if (bid.bidId == bidId)
//           {
//               user = bid.user;
//               prodId = bid.product.productId;
//               price = bid.offer;
//               quantity = bid.quantity;
//               Bid newBid = new Bid
//           }
//        }
//
//    }
}
