package domain.user;

import domain.states.Buyer;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import utils.Action;
import utils.Message;
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

    public HashMap<Integer, HashMap<Integer, Integer>> getCartContent() {
        return g.getCartContent();
    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator();
        stores.add(store);
        roles.put(store.getStoreid(), creator);


    }

    /**
     * creates a review and sends it to the store
     * @param content
     * @param grading
     * @param orderId
     * @return
     */
    public Message writeReview(String content, int grading, int orderId){
        if()
    }
}
