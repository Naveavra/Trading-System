package service;


import database.daos.Dao;
import database.daos.SubscriberDao;
import database.dtos.ConstraintDto;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Bid;
import domain.user.Member;
import domain.user.ShoppingCart;
import domain.user.User;
import org.hibernate.Session;
import org.json.JSONObject;
import utils.Pair;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.StoreInfo;
import utils.messageRelated.ProductReview;
import utils.messageRelated.Question;
import utils.messageRelated.StoreReview;
import utils.orderRelated.Order;
import domain.store.order.OrderController;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;

import java.util.*;

import utils.messageRelated.Message;
import utils.infoRelated.OrderInfo;
import utils.orderRelated.Status;
import utils.infoRelated.Receipt;

public class MarketController {

    private StoreController storectrl;
    private OrderController orderctrl;
    public MarketController()
    {
        storectrl = new StoreController();
        orderctrl = new OrderController();
    }


    public int calculatePrice(ShoppingCart cart) throws Exception{
        return storectrl.calculatePrice(cart);
    }
    public Pair<Receipt, Set<Integer>> purchaseProducts(ShoppingCart shoppingCart, User user, int totalPrice, Session session) throws Exception
    {
        Order order = orderctrl.createNewOrder(user,shoppingCart, totalPrice);
        order.setStatus(Status.pending);
        Set<Integer> creatorIds = storectrl.purchaseProducts(shoppingCart, order, session);
        //ziv change
        if(creatorIds == null){
            order.setStatus(Status.canceled);
            throw new Exception("user violate store shopping's rule , purchase canceled");
        }
        order.setStatus(Status.submitted);
        Receipt receipt = new Receipt(order.getOrderId(), shoppingCart, order.getTotalPrice());
        SubscriberDao.saveReceipt(receipt, session);
        receipt.saveReceiptProducts(session);
        Pair<Receipt, Set<Integer>> ans = new Pair<>(receipt, creatorIds);
        return ans;
    }

    public Store openStore(Member user, String description, Session session) throws Exception
    {
        Store store = storectrl.openStore(description, user, session);
       return store;
    }

    public Store openStore(Member user, String name, String description, String img, Session session) throws Exception
    {
        Store store = storectrl.openStore(name, description, img, user, session);
        return store;
    }

    public String getProductName(int storeId ,int  productId) throws Exception {
        return storectrl.getProductName(storeId , productId);
    }

    public ArrayList<String> checkMessages(int storeID) throws Exception {

        return storectrl.checkMessages(storeID);
    }

    public int addReviewToStore(StoreReview m, Session session) throws Exception {
        return storectrl.writeReviewForStore(m, session);
    }

    public int writeReviewForProduct(ProductReview m, Session session) throws Exception{
        return storectrl.writeReviewForProduct(m, session);

    }

    public int addProduct(int storeId, String name, String description, int price, int quantity, List<String> categories, Session session) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity, categories, session);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        return id;
    }

    public int addProduct(int storeId, String name, String description, int price, int quantity,
                          List<String> categories, String img, Session session) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity, img, categories, session);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        return id;
    }
    public ProductInfo getProductInformation(int storeId, int productId) throws Exception {
        Store store = storectrl.getActiveStore(storeId);
        return store.getProductInformation(productId);
    }



    public StoreInfo getStoreInformation(int storeId) throws Exception{
        return storectrl.getStoreInformation(storeId);
    }
    public Store getStore(int storeId) throws Exception {
        return storectrl.getStore(storeId);
    }
    public List<StoreInfo> getStoresInformation(){
        List<StoreInfo> stores = new ArrayList<>();
        for(Store s:storectrl.storeList.values()){
            if(s.isActive())
                stores.add(s.getStoreInformation());
        }
        return stores;
    }


//    public String getStoreDescription(int storeId) throws Exception{
//        Store store = storectrl.getStore(storeId);
//        if (store != null && store.isActive())
//        {
//            return store.getStoreDescription();
//        }
//        else {
//            throw new Exception("can not show store information");
//        }
//    }

    public int addQuestion(Question q, Session session) throws Exception {
        return storectrl.addQuestion(q, session);
    }

    public void setStoreAttributes(int storeId, String name, String description, String img, Session session) throws Exception{
        storectrl.setStoreAttributes(storeId, name, description, img, session);
    }






//    public int calculatePrice(HashMap<Integer, HashMap<Integer, Integer>> shoppingCart) {
//        int totalprice = 0;
//        for (Integer storeid : shoppingCart.keySet())
//        {
//            Store store = storectrl.getStore(storeid);
//            try {
//                totalprice += store.calculatePrice(shoppingCart.get(storeid));
//            } catch (Exception e) {
//                return  -1;
//            }
//        }
//        return totalprice;
//    }

    public List<ProductInfo> getStoreProducts(int storeId) throws Exception {
        return storectrl.getProducts(storeId);
    }

//    public void setStoreDiscountPolicy(int storeId, String policy) throws Exception {
//        Store store = storectrl.getStore(storeId);
//        if (store != null )
//        {
//            store.setStoreDiscountPolicy(policy);
//        }
//        else {
//            throw new Exception("store does not exist");
//        }
//    }

//    public void addPurchaseConstraint(int storeId, String constraint) throws Exception {
//        Store s;
//        if((s= storectrl.getStore(storeId) )!= null){
//            s.addPurchaseConstraint(constraint);
//        }
//        else{
//            throw new Exception("store doesn't exist, sorry bruh :(");
//        }
//    }

    public List<Message> getQuestions(int storeId) throws Exception {
        return storectrl.getQuestions(storeId);

    }

    public void answerQuestion(int storeId, int questionId, String answer, Session session) throws Exception{
        storectrl.answerQuestion(storeId, questionId, answer, session);
    }

    public List<OrderInfo> getStoreOrderHistory(int storeId) throws Exception
    {
        return storectrl.getStoreOrderHistory(storeId);
    }

    public void answerAppointment(String userName, int storeId, String fatherName, String childName, String ans, Session session) throws Exception{
        storectrl.answerAppointment(userName, storeId, fatherName, childName, ans, session);
    }

    public AppHistory getAppointments(int storeId) throws Exception {
        return storectrl.getAppointments(storeId);
    }

    public Set<Integer> closeStorePermanently(int storeId, Session session) throws Exception {
        return storectrl.closeStorePermanently(storeId, session);
    }

    public void deleteProduct(int storeId, int productId, Session session) throws Exception {
        storectrl.removeProduct(storeId,productId, session);
    }

    public void updateProduct(int storeId, int productId, List<String> categories, String name, String description,
                              int price, int quantity, String img, Session session) throws Exception {
        storectrl.updateProduct(storeId,productId,categories,name,description,price,quantity, img, session);
    }

    public List<StoreReview> viewReviews(int storeId) throws Exception {
        return storectrl.viewReviews(storeId);
    }

    public ArrayList<String> showFilterOptions() {
       return storectrl.showFilterOptions();
    }

    /**
     * returns the filtered items by filterOptions.
     * @param filterOptions
     * @return json string representation of the selected products.
     */
    public ArrayList<ProductInfo> filterBy(HashMap<String,String> filterOptions) throws Exception{
        return storectrl.filterBy(filterOptions);
    }

    public void purchaseMade(Receipt receipt, Session session) throws Exception {
        storectrl.purchaseMade(receipt, session);
    }

    public List<ProductInfo> getAllProducts() throws Exception{
        return storectrl.getAllProducts();
    }


    public void checkProductInStore(int storeId, int productId) throws Exception{
        storectrl.checkProductInStore(storeId, productId);
    }

    public void changeRegularDiscount(int storeId, int prodId, int percentage, String discountType, String discountedCategory, List<String> predicatesLst,String content, Session session) throws Exception {
        storectrl.changeRegularDiscount(storeId, prodId, percentage, discountType,
                discountedCategory, predicatesLst,content, session);
    }

    public int getStoreId(String storeName) throws Exception{
        return storectrl.getStoreId(storeName);
    }

//    public void createBidOnProduct(int storeId,int prodId) throws Exception {
//        Store s = storectrl.getActiveStore(storeId);
//        s.createBid(prodId);
//    }

    public Bid getBid(int storeId, int bidId) throws Exception{
        Store s = getStore(storeId);
        return s.getBid(bidId);
    }

    public List<String> placeBid(int storeId, Member user, int prodId, double price,int quantity, Session session) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        return s.placeBid(user,prodId,price,quantity, session);

    }

    /**
     * store owner reply for the bid suggestion
     * @return true if he was the last to approve false otherwise
     */
    public boolean answerBid(String userName, int storeId, boolean ans, int prodId, int bidId, Session session) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        Bid bid = s.answerBid(bidId,userName,prodId,ans, session);
        return bid != null;
    }

    public List<String> counterBid(String userName, int storeId, double counterOffer, int prodId, int bidId, Session session) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        return s.counterBid(bidId,counterOffer,userName, session);
    }

//    public List<String> editBid(int storeId, int bidId, double price, int quantity) throws Exception {
//        Store s = storectrl.getActiveStore(storeId);
//        Bid bid = s.editBid(bidId,price,quantity);
//        return bid.getApprovers();
//        //send a message to all shop owners and people who need to approve this bid.
//    }

    public void addPurchaseConstraint(int storeId, String purchasePolicy,String content, Session session)throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        s.addPurchasePolicy(purchasePolicy,content, session);
    }

    public void deletePurchaseConstraint(int storeId, int purchasePolicyId) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        s.removeConstraint(purchasePolicyId);
    }

    public Pair<Receipt, Set<Integer>> purchaseBid(User user, int storeId, int prodId, double price, int quantity, Session session) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        ShoppingCart sc = new ShoppingCart();
        sc.addProductToCart(storeId,getProductInformation(storeId,prodId),quantity);
        Order or = orderctrl.createNewOrder(user,sc,price);
        or.setStatus(Status.pending);
        Set<Integer> creatorIds = storectrl.purchaseProductsBid(sc,or, session);
        or.setStatus(Status.submitted);
        Receipt receipt = new Receipt(or.getOrderId(),sc,or.getTotalPrice());
        SubscriberDao.saveReceipt(receipt, session);
        receipt.saveReceiptProducts(session);
        Pair<Receipt,Set<Integer>> res = new Pair<>(receipt,creatorIds);
        return res;
    }

    public Set<Integer> getStoreCreatorsOwners(int storeId) throws Exception {
        return storectrl.getStoreCreatorsOwners(storeId);
    }

    public int getBidClient(int bidId, int storeId) throws Exception {
        return storectrl.getActiveStore(storeId).getBidClient(bidId);
    }

    public List<String> getBidApprovers(int bidId, int storeId) throws Exception {
        Store s= storectrl.getActiveStore(storeId);
        return s.getApprovers(bidId);
    }

    public void clientAcceptCounter(int bidId, int storeId) throws Exception {
        Store store = storectrl.getActiveStore(storeId);
        store.clientAcceptCounter(bidId);
    }

    public void addCompositeDiscount(String body, Session session) throws Exception {
        JSONObject req = new JSONObject(body);
        int storeId = Integer.parseInt(req.get("storeId").toString());
        Store s = storectrl.getActiveStore(storeId);
        s.addCompositeDiscount(req, session);
    }

    public void removeDiscount(int storeId, int discountId) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        s.removeDiscount(discountId);
    }

//    public Set<Integer> clientAcceptCounter(int bidId, int storeId) throws Exception {
//        Store s = storectrl.getActiveStore(storeId);
//        s.clientAcceptCounter(bidId);
//    }
}
