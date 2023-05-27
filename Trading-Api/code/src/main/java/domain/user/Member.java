package domain.user;

import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import domain.user.history.PurchaseHistory;
import domain.user.history.UserHistory;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Message;
import utils.messageRelated.NotificationOpcode;
import utils.messageRelated.MessageState;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static utils.messageRelated.NotificationOpcode.PRODUCT_REVIEW;


public class Member implements User{

    private transient Guest g;
    private int id;
    private String name;
    private String birthday;
    int age;
    private String email;
    private transient String password;

    private List<UserState> roles; //connection between registered to the shops
    private transient BlockingQueue<Notification> notifications;
    private UserHistory userHistory;
    private boolean isConnected;
    public Member(int id, String email, String password, String birthday){
        this.id = id;
        String[] emailParts = email.split("@");
        this.name = emailParts[0];
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        age = StringChecks.calculateAge(birthday);
        roles = new ArrayList<>();
        userHistory = new UserHistory(this.id, this.email, this.name, this.password);
        g = new Guest(id);
        notifications = new LinkedBlockingQueue<>();
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

    public void changeRoleInStore(UserState userState, Store store) throws Exception{
        int storeId = store.getStoreId();
        UserState state;
        try {
            state = getRole(storeId);
        }catch (Exception e){
            roles.add(userState);
            return;
        }
        if (state.getRole() == userState.getRole())
            throw new Exception("the member already has this role in this store");
        roles.add(userState);
    }


    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public void setNewEmail(String  newEmail){
        email = newEmail;
        userHistory.addEmail(email);
    }
    public void setNewName(String  newName){
        name = newName;
        userHistory.addName(name);
    }

    public void setNewPassword(String oldPassword, String newPassword) throws Exception {
        if(password.equals(oldPassword)){
            password = newPassword;
            userHistory.addPassword(password);
        }
        else
            throw new Exception("wrong password entered, can't change to a new password");
    }

    public int getId(){
        return id;
    }


    public void login(String password) throws Exception{
        if (this.password.equals(password)) {
            connect();
            return;
        }
        throw new Exception("wrong password");
    }


    public void addProductToCart(int storeId, ProductInfo product, int quantity) throws Exception{
            g.addProductToCart(storeId, product, quantity);
    }
    public void emptyCart(){
        g.emptyCart();
    }

    public void removeProductFromCart(int storeId, int productId) throws Exception{
        g.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, ProductInfo product, int change) throws Exception{
        g.changeQuantityInCart(storeId, product, change);
    }

    public List<ProductInfo> getCartContent() {
        return g.getCartContent();
    }

    public void purchaseMade(int orderId, double totalPrice){
        userHistory.addPurchaseMade(orderId, totalPrice, g.getShoppingCart());
        g.emptyCart();
    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator(id, name, store);

        roles.add(creator);
    }

    public Message writeReview(int messageId, int storeId, int orderId, String content, int grading) throws Exception{
        if (userHistory.checkOrderContainsStore(orderId, storeId)) {
            Message message = new Message(messageId, NotificationOpcode.STORE_REVIEW, content, this, orderId, storeId, MessageState.reviewStore);
            message.addRating(grading);
            return message;
        }
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Message writeReview(int messageId, int storeId, int productId, int orderId, String content, int grading) throws Exception{
        if(userHistory.checkOrderContainsProduct(orderId, storeId, productId)){
            Message m = new Message(messageId, PRODUCT_REVIEW, content, this, orderId, storeId, MessageState.reviewProduct);
            m.addRating(grading);
            m.addProductToReview(productId);
            return m;
        }
        else
            throw new Exception("the product isn't part of the order so you can't write a review about him");
    }

    public Message writeComplaint(int messageId, int orderId, int storeId, String comment) throws Exception {
        if(userHistory.checkOrderContainsStore(orderId, storeId))
            return new Message(messageId, NotificationOpcode.COMPLAINT, comment, this, orderId, storeId, MessageState.complaint);
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Message sendQuestion(int messageId, int storeId, String question) {
        return new Message(messageId, NotificationOpcode.QUESTION, question, this, -1, storeId, MessageState.question);
    }

    public synchronized void addNotification(Notification notification){
            notifications.offer(notification);
    }

    public List<Notification> displayNotifications(){
        List<Notification> display = new LinkedList<>();
        for (Notification notification : notifications)
            display.add(notification);
        notifications.clear();
        return display;
    }

    public Notification getNotification() throws InterruptedException {
        synchronized (notifications) {
            return notifications.take();
        }
    }

    private UserState getActiveRole(int storeId) throws Exception {
        for (UserState state : roles) {
            if (state.getStore().getStoreId() == storeId) {
                if (state.isActive()) {
                    return state;
                }
                else throw new Exception("the role in the store is not active");
            }
        }
        throw new Exception("the member does not have a role in this store");
    }

    private UserState getInActiveRole(int storeId) throws Exception{
        for (UserState state : roles) {
            if (state.getStore().getStoreId() == storeId) {
                if (!state.isActive()) {
                    return state;
                }
                else throw new Exception("the role in the store is active");
            }
        }
        throw new Exception("the member does not have a role in this store");
    }

    public UserState getRole(int storeId) throws Exception{
        for (UserState state : roles)
            if (state.getStore().getStoreId() == storeId)
                    return state;
        throw new Exception("the member does not have a role in this store");
    }

    public void checkPermission(Action action, int storeId) throws Exception{
        UserState role = getActiveRole(storeId);
        if (!role.checkPermission(action))
            throw new Exception("member does not have permission for this action");
    }

    public PurchaseHistory getUserPurchaseHistory(){
        return userHistory.getUserPurchaseHistory();
    }

    public Info getPrivateInformation() {
        Info info = new Info(id, name, email, birthday, age);
        return userHistory.getInformation(info);
    }

    public void appointToManager(Member appointed, int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        state.appointManager(appointed);
    }

    public Set<Integer> fireManager(int appointedId, int storeId) throws Exception{
        UserState state = getActiveRole(storeId);
        return state.fireManager(appointedId);
    }
    public void appointToOwner(Member appointed, int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        state.appointOwner(appointed);
    }
    public Set<Integer> fireOwner(int appointedId, int storeId) throws Exception{
        UserState state = getActiveRole(storeId);
        return state.fireOwner(appointedId);
    }

    public void removeRoleInStore(int storeId) throws Exception{
        UserState state = getRole(storeId);
        roles.remove(state);
    }

    public void addAction(Action a, int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        state.addAction(a);
    }

    public void removeAction(Action a, int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        state.removeAction(a);
    }

    public void changeToActive(int storeId) throws Exception{
        UserState state = getInActiveRole(storeId);
        state.setIsActive(true);
    }

    public void changeToInActive(int storeId) throws Exception{
        UserState state = getActiveRole(storeId);
        state.setIsActive(false);
    }

    public Set<Integer> closeStore(int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        return state.closeStore();
    }

    public Set<Integer> reOpenStore(int storeId) throws Exception {
        UserState state = getInActiveRole(storeId);
        return state.reOpenStore();
    }

    public Set<Integer> getWorkerIds(int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        return state.getWorkerIds();
    }

    public Info getInformation(int storeId){
        Info info = new Info(id, name, email, birthday, age);
        try {
            UserState state = getRole(storeId);
            info.addRole(state.getRole());
            if (state.getRole() == Role.Manager)
                info.addManagerActions(state.getActions());
        }catch (Exception e){
        }
        return info;
    }

    public Set<Integer> getAllStoreIds(){
        Set<Integer> storeIds = new HashSet<>();
        for(UserState state : roles)
            storeIds.add(state.getStore().getStoreId());
        return storeIds;
    }

    //used for login information

    public HashMap<Integer, Role> getRoles() {
        HashMap<Integer, Role> ans = new HashMap<>();
        for(UserState state : roles) {
            if(state.isActive() || state.getRole() == Role.Creator)
                ans.put(state.getStore().getStoreId(), state.getRole());
        }
        return ans;
    }

    public HashMap<Integer, String> getStoreNames() {
        HashMap<Integer, String> ans = new HashMap<>();
        for(UserState state : roles)
            ans.put(state.getStore().getStoreId(), state.getStore().getName());
        return ans;
    }

    public HashMap<Integer, String> getStoreImgs(){
        HashMap<Integer, String> ans = new HashMap<>();
        for(UserState state : roles)
            ans.put(state.getStore().getStoreId(), state.getStore().getImgUrl());
        return ans;
    }

    public HashMap<Integer, List<Action>> getPermissions() {
        HashMap<Integer, List<Action>> ans = new HashMap<>();
        for(UserState state : roles)
            ans.put(state.getStore().getStoreId(), state.getActions());
        return ans;
    }

    public ShoppingCart getShoppingCart() throws Exception {
        if(isConnected)
            return g.getShoppingCart();
        throw new Exception("the member is not connected");
    }


    public LoginInformation getLoginInformation(String token) {
        return new LoginInformation(token, id, email, false, displayNotifications(),getRoles(),
                getStoreNames(), getStoreImgs(), getPermissions(), getUserPurchaseHistory(), age, birthday);
    }
}
