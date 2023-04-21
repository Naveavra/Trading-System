package domain.user;

import domain.states.Buyer;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import utils.Action;
import utils.Message;
import utils.MessageState;
import utils.Notification;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: change all the void functions to return a value in case of success/failure
public class Member {

    private Guest g;
    private int id;
    private String name;
    private String birthday;
    private String email;
    private String password;

    private HashMap<Integer, UserState> roles; //connection between registered to the shops
    private List<Store> stores; //saves all the stores it has a job at
    private ConcurrentLinkedDeque<Notification> notifications;
    private UserState currentState;

    private UserHistory userHistory;
    private int currentStoreId;

    private boolean isConnected;

    public Member(int id, String email, String password, String birthday){
        this.id = id;
        this.name = email;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        roles = new HashMap<>();
        stores = new LinkedList<>();
        currentState = new Buyer();
        currentStoreId = -1;
        userHistory = new UserHistory();
        g = new Guest(id);
        notifications = new ConcurrentLinkedDeque<>();
    }


    public boolean getIsConnected(){
        return isConnected;
    }

    public void connect(){
        isConnected = true;
    }

    public void disconnect(){
        isConnected = false;
    }

    public void changeState(int storeId){ // if the user is not a specific store(such as main page) storeId == -1
        if(roles.containsKey(storeId))
            currentState = roles.get(storeId);
        else
            currentState = new Buyer();
        currentStoreId = storeId;
    }

    public void changeRoleInStore(int storeId, UserState userState){
        roles.put(storeId, userState);
    }

    public void addActionToStore(int storeId){
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }


    public int getId(){
        return id;
    }


    public boolean login(String password) throws RuntimeException{
        if (password.equals(password)) {
            if (!getIsConnected()) {
                connect();
                return true;
            }
            else
                throw new RuntimeException("member is connected already");
        }
        else
            throw new RuntimeException("wrong password");
    }


    public void addProductToCart(int storeId, int productId, int quantity) throws RuntimeException{
        if(currentState.checkPermission(Action.buyProduct))
            g.addProductToCart(storeId, productId, quantity);
        else
            throw new RuntimeException("not allowed to buy");
    }

    public void removeProductFromCart(int storeId, int productId) {
        g.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, int productId, int change) throws RuntimeException{
        g.changeQuantityInCart(storeId, productId, change);
    }


    /**
     * when purchasing this is the value the order will get
     * @return
     */
    public HashMap<Integer, HashMap<Integer, Integer>> getCartContent() {
        return g.getCartContent();
    }

    /**
     * if the purchase was successful then add it to history
     * @param orderId
     */
    public void purchaseMade(int orderId){
        userHistory.addPurchaseMade(orderId, g.getCartContent());
        g.emptyCart();

    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator();
        stores.add(store);
        roles.put(store.getStoreid(), creator);


    }

    /**
     * creates a review about the store and sends it to the store
     * @param storeId
     * @param orderId
     * @param content
     * @param grading
     * @return
     */
    public Message writeReview(int messageId, int storeId, int orderId, String content, int grading) throws Exception{
        if(userHistory.checkOrderOccurred(orderId)) {
            if (userHistory.checkOrderContainsStore(orderId, storeId)) {
                Message message = new Message(messageId, content, grading, this, orderId, storeId, MessageState.reviewStore);
                return message;
            }
            else
                throw new Exception("can't write a review because the store wasn't part of the order");
        }
        else
            throw new Exception("can't write a review for an order that didn't occur");
    }

    /**
     * creates a review about a product in the store and sends it to the store
     * @param messageId
     * @param storeId
     * @param productId
     * @param orderId
     * @param content
     * @param grading
     * @return
     */
    public Message writeReview(int messageId, int storeId, int productId, int orderId, String content, int grading) throws Exception{
        if(userHistory.checkOrderOccurred(orderId)){
            if(userHistory.checkOrderContainsStore(orderId, storeId)){
                if(userHistory.checkOrderContainsProduct(orderId, storeId, productId)){
                    Message m = new Message(messageId, content, grading, this, orderId, storeId, MessageState.reviewProduct);
                    return m;
                }
                else
                    throw new Exception("the product isn't part of the order so you can't write a review about him");
            }
            else
                throw new Exception("can't write a review because the store wasn't part of the order");
        }
        else
            throw new Exception("can't write a review for an order that didn't occur");
    }
}
