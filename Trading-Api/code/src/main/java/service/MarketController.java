package service;


import domain.store.storeManagement.AppHistory;
import domain.user.Member;
import domain.user.ShoppingCart;
import domain.user.User;
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
        order.setStatus(Status.submitted);
        Receipt receipt = new Receipt(user.getId(), order.getOrderId(), shoppingCart, order.getTotalPrice());
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
        int id = storectrl.addProduct(storeId,name,description,price,quantity);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        storectrl.addToCategory(storeId,id,categories);
        return id;
    }

    public int addProduct(int storeId, String name, String description, int price, int quantity,
                          List<String> categories, String img) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity, img);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        storectrl.addToCategory(storeId,id,categories);
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
        if(storectrl.storeList.containsKey(storeId))
            return storectrl.storeList.get(storeId);
        throw new Exception("the storeId given does not belong to any store");
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

    public void setStorePurchasePolicy(int storeId,String policy) throws Exception{
        Store store = storectrl.getActiveStore(storeId);
        store.setStorePolicy(policy);
//        Store store = storectrl.getStore(storeId);
//        if (store != null )
//        {
//            store.setStorePolicy(policy);
//        }
//        else {
//            throw new Exception("store does not exist");
//        }
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
    public ArrayList<ProductInfo> filterBy(HashMap<String,String> filterOptions) {
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
}
