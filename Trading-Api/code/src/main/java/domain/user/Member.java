package domain.user;

import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import org.json.JSONObject;
import utils.infoRelated.LoginInformation;
import utils.messageRelated.Message;
import utils.messageRelated.MessageState;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


public class Member implements User{

    private transient Guest g;
    private int id;
    private String name;
    private String birthday;
    int age;
    private String email;
    private transient String password;

    private HashMap<Integer, UserState> roles; //connection between registered to the shops
    private HashMap<Integer, Store> stores;
    private transient ConcurrentLinkedDeque<Notification> notifications;
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
        roles = new HashMap<>();
        stores = new HashMap<>();
        userHistory = new UserHistory(this.email, this.name, this.password);
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

    public void changeRoleInStore(UserState userState, Store store) throws Exception{
        int storeId = store.getStoreId();
        if(roles.containsKey(storeId)) {
            if (roles.get(storeId).getRole() == userState.getRole())
                throw new Exception("the member already has this role in this store");
        }
        roles.put(storeId, userState);
        stores.put(storeId, store);
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


    public void addProductToCart(int storeId, int productId, int quantity) throws Exception{
            g.addProductToCart(storeId, productId, quantity);
    }
    public void emptyCart(){
        g.emptyCart();
    }

    public void removeProductFromCart(int storeId, int productId) throws Exception{
        g.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, int productId, int change) throws Exception{
        g.changeQuantityInCart(storeId, productId, change);
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getCartContent() {
        return g.getCartContent();
    }

    public void purchaseMade(int orderId, double totalPrice){
        userHistory.addPurchaseMade(orderId, totalPrice, g.getShoppingCart());
        g.emptyCart();
    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator();
        stores.put(store.getStoreId(), store);
        roles.put(store.getStoreId(), creator);
    }

    public Message writeReview(int messageId, int storeId, int orderId, String content, int grading) throws Exception{
        if (userHistory.checkOrderContainsStore(orderId, storeId)) {
            Message message = new Message(messageId, content, this, orderId, storeId, MessageState.reviewStore);
            message.addRating(grading);
            return message;
        }
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Message writeReview(int messageId, int storeId, int productId, int orderId, String content, int grading) throws Exception{
        if(userHistory.checkOrderContainsProduct(orderId, storeId, productId)){
            Message m = new Message(messageId, content, this, orderId, storeId, MessageState.reviewProduct);
            m.addRating(grading);
            m.addProductToReview(productId);
            return m;
        }
        else
            throw new Exception("the product isn't part of the order so you can't write a review about him");
    }

    public Message writeComplaint(int messageId, int orderId, int storeId, String comment) throws Exception {
        if(userHistory.checkOrderContainsStore(orderId, storeId))
            return new Message(messageId, comment, this, orderId, storeId, MessageState.complaint);
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Message sendQuestion(int messageId, int storeId, String question) {
        return new Message(messageId, question, this, -1, storeId, MessageState.question);
    }

    public synchronized void addNotification(Notification notification){
        notifications.add(notification);
    }

    public List<String> displayNotifications(){
        List<String> display = new LinkedList<>();
        for (Notification notification : notifications)
            display.add(notification.toString());
        notifications.clear();
        return display;
    }


    private UserState getActiveRole(int storeId) throws Exception{
        if(roles.containsKey(storeId)) {
            if(roles.get(storeId).isActive())
                return roles.get(storeId);
            throw new Exception("the role in the store is not active");
        }
        throw new Exception("the member does not have a role in this store");
    }

    private UserState getInActiveRole(int storeId) throws Exception{
        if(roles.containsKey(storeId)) {
            if (!roles.get(storeId).isActive())
                return roles.get(storeId);
            throw new Exception("the role in the store is active");
        }
        throw new Exception("the member does not have a role in this store");
    }

    public UserState getRole(int storeId) throws Exception{
        if(roles.containsKey(storeId))
            return roles.get(storeId);
        throw new Exception("the member does not have a role in this store");
    }

    public void checkPermission(Action action, int storeId) throws Exception{
        UserState role = getActiveRole(storeId);
        if (!role.checkPermission(action))
            throw new Exception("member does not have permission for this action");
    }

    public void checkPermissionInActive(Action action, int storeId) throws Exception{
        UserState role = getInActiveRole(storeId);
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

    public Store appointToManager(int appointedId, int storeId) throws Exception {
        checkPermission(Action.appointManager, storeId);
        stores.get(storeId).appointUser(id, appointedId, Role.Manager);
        return stores.get(storeId);
    }

    public Store appointToOwner(int appointedId, int storeId) throws Exception {
        checkPermission(Action.appointOwner, storeId);
        stores.get(storeId).appointUser(id, appointedId, Role.Owner);
        return stores.get(storeId);
    }
    public Set<Integer> fireOwner(int appointedId, int storeId) throws Exception{
        if(id == appointedId)
            return stores.get(storeId).fireUser(appointedId);
        checkPermission(Action.fireOwner, storeId);
        return stores.get(storeId).fireUser(appointedId);
    }

    public void removeRoleInStore(int storeId) {
        roles.remove(storeId);
        stores.remove(storeId);
    }

    public void fireManager(int appointedId, int storeId) throws Exception{
        if(id == appointedId)
            stores.get(storeId).fireUser(appointedId);
        checkPermission(Action.fireManager, storeId);
        stores.get(storeId).fireUser(appointedId);
    }

    public void addAction(Action a, int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        state.addAction(a);
    }

    public void removeAction(Action a, int storeId) throws Exception {
        UserState state = getActiveRole(storeId);
        state.removeAction(a);
    }

    public void changeToActive(int storeId){
        roles.get(storeId).serIsActive(true);
    }

    public void changeToInActive(int storeId){
        roles.get(storeId).serIsActive(false);
    }

    public Set<Integer> closeStore(int storeId) throws Exception {
        checkPermission(Action.closeStore, storeId);
        changeToInActive(storeId);
        Store store = stores.get(storeId);
        return store.closeStoreTemporary(id);
    }

    public Set<Integer> reOpenStore(int storeId) throws Exception {
        checkPermissionInActive(Action.reopenStore, storeId);
        changeToActive(storeId);
        Store store = stores.get(storeId);
        return store.reopenStore(id);
    }

    public Set<Integer> getWorkerIds(int storeId) throws Exception {
        checkPermission(Action.checkWorkersStatus, storeId);
        Store store = stores.get(storeId);
        return store.getUsersInStore();
    }

    public Info getInformation(int storeId) {

        UserState state = roles.get(storeId);
        Info info = new Info(id, name, email, birthday, age);
        if(state != null && state.getRole() == Role.Manager)
            info.addManagerActions(state.getActions());
        return info;
    }

    public Set<Integer> getAllStoreIds(){
        Set<Integer> storeIds = stores.keySet();
        return storeIds;
    }

    //used for login information

    public HashMap<Integer, Role> getRoles() {
        HashMap<Integer, Role> ans = new HashMap<>();
        for(int storeId : roles.keySet())
            ans.put(storeId, roles.get(storeId).getRole());
        return ans;
    }

    public HashMap<Integer, String> getNames() {
        HashMap<Integer, String> ans = new HashMap<>();
        for(int storeId : stores.keySet())
            ans.put(storeId, stores.get(storeId).getName());
        return ans;
    }

    public HashMap<Integer, String> getImgs(){
        HashMap<Integer, String> ans = new HashMap<>();
        for(int storeId : stores.keySet())
            ans.put(storeId, stores.get(storeId).getImgUrl());
        return ans;
    }

    public HashMap<Integer, List<Action>> getPermissions() {
        HashMap<Integer, List<Action>> ans = new HashMap<>();
        for(int storeId : roles.keySet())
            ans.put(storeId, roles.get(storeId).getActions());
        return ans;
    }

    public List<JSONObject> getCartJson(){
        return g.getCartJson();
    }


    public ShoppingCart getShoppingCart() {
        return g.getShoppingCart();
    }

    //for tests
    public HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> getUserPurchaseHistoryHash() {
        return userHistory.getUserPurchaseHistoryHash();
    }

    public LoginInformation getLoginInformation(String token) {
        return new LoginInformation(token, id, email, false, displayNotifications(),getRoles(),
                getNames(), getImgs(), getPermissions());
    }
}
