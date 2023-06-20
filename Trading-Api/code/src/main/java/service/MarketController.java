package service;


import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Bid;
import domain.user.Member;
import domain.user.ShoppingCart;
import domain.user.User;
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
    public Pair<Receipt, Set<Integer>> purchaseProducts(ShoppingCart shoppingCart, User user, int totalPrice) throws Exception
    {
        Order order = orderctrl.createNewOrder(user,shoppingCart, totalPrice);
        order.setStatus(Status.pending);
        Set<Integer> creatorIds = storectrl.purchaseProducts(shoppingCart, order);
        //ziv change
        if(creatorIds == null){
            order.setStatus(Status.canceled);
            throw new Exception("user violate store shopping's rule , purchase canceled");
        }
        order.setStatus(Status.submitted);
        Receipt receipt = new Receipt(order.getOrderId(), shoppingCart, order.getTotalPrice());
        Pair<Receipt, Set<Integer>> ans = new Pair<>(receipt, creatorIds);
        return ans;
    }

    public Store openStore(Member user, String description) throws Exception
    {
        Store store = storectrl.openStore(description, user);
       return store;
    }

    public Store openStore(Member user, String name, String description, String img) throws Exception
    {
        Store store = storectrl.openStore(name, description, img, user);
        return store;
    }

    public String getProductName(int storeId ,int  productId) throws Exception {
        return storectrl.getProductName(storeId , productId);
    }

    public ArrayList<String> checkMessages(int storeID) throws Exception {

        return storectrl.checkMessages(storeID);
    }

    public int addReviewToStore(StoreReview m) throws Exception {
        return storectrl.writeReviewForStore(m);
    }

    public int writeReviewForProduct(ProductReview m) throws Exception{
        return storectrl.writeReviewForProduct(m);

    }

    public int addProduct(int storeId, String name, String description, int price, int quantity, List<String> categories) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity, categories);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        return id;
    }

    public int addProduct(int storeId, String name, String description, int price, int quantity,
                          List<String> categories, String img) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity, img, categories);
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

    public int addQuestion(Question q) throws Exception {
        return storectrl.addQuestion(q);
    }

    public void setStoreAttributes(int storeId, String name, String description, String img) throws Exception{
        storectrl.setStoreAttributes(storeId, name, description, img);
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

    public void answerQuestion(int storeId, int questionId, String answer) throws Exception{
        storectrl.answerQuestion(storeId, questionId, answer);
    }

    public List<OrderInfo> getStoreOrderHistory(int storeId) throws Exception
    {
        return storectrl.getStoreOrderHistory(storeId);
    }

    public void answerAppointment(String userName, int storeId, String fatherName, String childName, String ans) throws Exception{
        storectrl.answerAppointment(userName, storeId, fatherName, childName, ans);
    }

    public AppHistory getAppointments(int storeId) throws Exception {
        return storectrl.getAppointments(storeId);
    }

    public Set<Integer> closeStorePermanently(int storeId) throws Exception {
        return storectrl.closeStorePermanently(storeId);
    }

    public void deleteProduct(int storeId, int productId) throws Exception {
        storectrl.removeProduct(storeId,productId);
    }

    public void updateProduct(int storeId, int productId, List<String> categories, String name, String description,
                              int price, int quantity, String img) throws Exception {
        storectrl.updateProduct(storeId,productId,categories,name,description,price,quantity, img);
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

    public void purchaseMade(Receipt receipt) throws Exception {
        storectrl.purchaseMade(receipt);
    }

    public List<ProductInfo> getAllProducts() {
        return storectrl.getAllProducts();
    }


    public void checkProductInStore(int storeId, int productId) throws Exception{
        storectrl.checkProductInStore(storeId, productId);
    }

    public void changeRegularDiscount(int storeId, int prodId, int percentage, String discountType, String discountedCategory, List<String> predicatesLst,String content) throws Exception {
        storectrl.changeRegularDiscount(storeId, prodId, percentage, discountType,
                discountedCategory, predicatesLst,content);
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

    public List<String> placeBid(int storeId, Member user, int prodId, double price,int quantity) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        return s.placeBid(user,prodId,price,quantity);

    }

    /**
     * store owner reply for the bid suggestion
     * @return true if he was the last to approve false otherwise
     */
    public boolean answerBid(String userName, int storeId, boolean ans, int prodId, int bidId) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        Bid bid = s.answerBid(bidId,userName,prodId,ans);
        return bid != null;
    }

    public List<String> counterBid(String userName, int storeId, double counterOffer, int prodId, int bidId) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        return s.counterBid(bidId,counterOffer,userName);
    }

//    public List<String> editBid(int storeId, int bidId, double price, int quantity) throws Exception {
//        Store s = storectrl.getActiveStore(storeId);
//        Bid bid = s.editBid(bidId,price,quantity);
//        return bid.getApprovers();
//        //send a message to all shop owners and people who need to approve this bid.
//    }

    public void addPurchaseConstraint(int storeId, String purchasePolicy,String content)throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        s.addPurchasePolicy(purchasePolicy,content);
    }

    public void deletePurchaseConstraint(int storeId, int purchasePolicyId) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        s.removeConstraint(purchasePolicyId);
    }

    public Pair<Receipt, Set<Integer>> purchaseBid(User user, int storeId, int prodId, double price, int quantity) throws Exception {
        Store s = storectrl.getActiveStore(storeId);
        ShoppingCart sc = new ShoppingCart();
        sc.addProductToCart(storeId,getProductInformation(storeId,prodId),quantity);
        Order or = orderctrl.createNewOrder(user,sc,price);
        or.setStatus(Status.pending);
        Set<Integer> creatorIds = storectrl.purchaseProductsBid(sc,or);
        or.setStatus(Status.submitted);
        Receipt receipt = new Receipt(or.getOrderId(),sc,or.getTotalPrice());
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

    public void addCompositeDiscount(String body) throws Exception {
        JSONObject req = new JSONObject(body);
        int storeId = Integer.parseInt(req.get("storeId").toString());
        Store s = storectrl.getActiveStore(storeId);
        s.addCompositeDiscount(req);
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
