package domain.user;

import com.google.gson.Gson;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import utils.messageRelated.Message;
import utils.messageRelated.MessageState;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.userInfoRelated.Info;
import utils.userInfoRelated.PrivateInfo;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


public class Member {

    private transient Guest g;
    private int id;
    private String name;
    private String birthday;
    int age;
    private String email;
    private transient String password;

    private HashMap<Integer, UserState> activeRoles; //connection between registered to the shops
    private transient HashMap<Integer, UserState> inActiveRoles;
    private HashMap<Integer, Store> activeStores; //saves all the stores it has a job at
    private transient HashMap<Integer, Store> inActiveStores;
    private transient ConcurrentLinkedDeque<Notification> notifications;

    private UserHistory userHistory;
    private boolean isConnected;
    private transient Gson gson ;

    private transient ConcurrentHashMap<String, String> securityQuestions;
    //new Member(1, "eliben123@gmail.com", "aBc123", "24/02/2002")
    public Member(int id, String email, String password, String birthday){
        this.id = id;
        String[] emailParts = email.split("@");
        this.name = emailParts[0];
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        age = calculateAge();
        activeRoles = new HashMap<>();
        inActiveRoles = new HashMap<>();
        activeStores = new HashMap<>();
        inActiveStores = new HashMap<>();
        userHistory = new UserHistory();
        userHistory.addEmail(this.email);
        userHistory.addName(this.name);
        userHistory.addPassword(this.password);
        g = new Guest(id);
        notifications = new ConcurrentLinkedDeque<>();
        securityQuestions = new ConcurrentHashMap<>();
        gson = new Gson();
    }

    private int calculateAge() {
        String[] splitBDay = birthday.split("/");
        int[] bDay = {Integer.parseInt(splitBDay[0]), Integer.parseInt(splitBDay[1]), Integer.parseInt(splitBDay[2])};
        String currentDate = String.valueOf(java.time.LocalDate.now());
        String[] splitCurrentDay = currentDate.split("-");
        int[] curDay = {Integer.parseInt(splitCurrentDay[0]), Integer.parseInt(splitCurrentDay[1]),
                Integer.parseInt(splitCurrentDay[2])};
        int tmp = curDay[0];
        curDay[0] = curDay[2];
        curDay[2] = tmp;

        int ans = 0;
        ans = ans + curDay[2] - bDay[2];
        if(curDay[1] > bDay[1] || (curDay[1] == bDay[1] && curDay[0] > bDay[0]))
            ans = ans + 1;
        return ans;
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

    public void changeRoleInStore(int storeId, UserState userState, Store store){
        activeRoles.put(storeId, userState);
        activeStores.put(storeId, store);
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


    public boolean login(String password, List<String> answers) throws Exception{
        if (this.password.equals(password)) {
            if (!getIsConnected()) {
                if(checkSecurityAnswers(answers)) {
                    connect();
                    return true;
                }
                else
                    throw new Exception("the wrong answers were given");
            }
            else
                throw new Exception("member is connected already");
        }
        else
            throw new Exception("wrong password");
    }

    private boolean checkSecurityAnswers(List<String> answers) {
        if(answers.size() != securityQuestions.size())
            return false;
        for(String answer : securityQuestions.values())
            if(!answers.contains(answer))
                return false;
        return true;
    }


    public void addProductToCart(int storeId, int productId, int quantity) throws Exception{
            g.addProductToCart(storeId, productId, quantity);
    }

    public void removeProductFromCart(int storeId, int productId) throws Exception{
        g.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, int productId, int change) throws Exception{
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
    public void purchaseMade(int orderId, int totalPrice){
        userHistory.addPurchaseMade(orderId, totalPrice, g.getCartContent());
        g.emptyCart();

    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator();
        activeStores.put(store.getStoreId(), store);
        activeRoles.put(store.getStoreId(), creator);
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
                Message message = new Message(messageId, content, this, orderId, storeId, MessageState.reviewStore);
                message.addRating(grading);
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
                    Message m = new Message(messageId, content, this, orderId, storeId, MessageState.reviewProduct);
                    m.addRating(grading);
                    m.addProductToReview(productId);
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

    public Message writeComplaint(int messageId, int orderId, int storeId, String comment) throws Exception {
        if(userHistory.checkOrderOccurred(orderId)){
            if(userHistory.checkOrderContainsStore(orderId, storeId)){
                Message m = new Message(messageId, comment, this, orderId, storeId, MessageState.complaint);
                return m;
            }
            else
                throw new Exception("can't write a review because the store wasn't part of the order");
        }
        else
            throw new Exception("can't write a review for an order that didn't occur");
    }

    public Message sendQuestion(int messageId, int storeId, String question) {
        Message message = new Message(messageId, question, this, -1, storeId, MessageState.question);
        return message;
    }

    public synchronized void addNotification(Notification notification){
        notifications.add(notification);
    }

    public List<String> displayNotifications(){
        List<String> display = new LinkedList<>();
        for (Notification notification : notifications)
            display.add(notification.toString());
        notifications = new ConcurrentLinkedDeque<>();
        return display;
    }


    /**
     * checks if a member can see messages of a store
     * @param storeId
     * @return
     */
    public boolean canCheckMessages(int storeId) {
        UserState us = activeRoles.get(storeId);
        return us.checkPermission(Action.viewMessages);
    }


    /**
     * checks if the member can answer messages
     * @param storeId
     * @return
     */
    public boolean canGiveFeedback(int storeId) {
        UserState us = activeRoles.get(storeId);
        return us.checkPermission(Action.answerMessage);
    }

    public HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> getUserPurchaseHistory() {
        return userHistory.getUserPurchaseHistory();
    }

    public Info getPrivateInformation() {
        PrivateInfo info = new PrivateInfo(id, name, email, birthday, age);
        userHistory.getInformation(info);
        return info;
    }

    /**
     * set security questions
     * @param question
     * @param answer
     */
    public void addQuestionForLogin(String question, String answer) throws Exception {
        if(securityQuestions.containsKey(question))
            throw new Exception("you already added this question");

        securityQuestions.put(question, answer);
        userHistory.addQuestion(question, answer);
    }

    public void changeAnswerForQuestion(String question, String newAnswer) throws Exception {
        if(securityQuestions.containsKey(question)) {
            securityQuestions.put(question, newAnswer);
            userHistory.changeAnswerForQuestion(question, newAnswer);
        }
        else
            throw new Exception("the question you gave is not part of your security questions");
    }

    public void removeSecurityQuestion(String question) throws Exception {
        if(securityQuestions.containsKey(question)) {
            securityQuestions.remove(question);
            userHistory.removeSecurityQuestion(question);
        }
        else
            throw new Exception("the question: " + question +" is not a security question");
    }

    public Store appointToManager(int appointedId, int storeId) throws Exception {
        UserState storeState = activeRoles.get(storeId);
        if(storeState != null){
            if(storeState.checkPermission(Action.appointManager)){
                activeStores.get(storeId).appointUser(id, appointedId, Role.Manager);
                return activeStores.get(storeId);
            }
            else
                throw new Exception("the member does not have permission to appoint a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public Store appointToOwner(int appointedId, int storeId) throws Exception {
        UserState storeState = activeRoles.get(storeId);
        if(storeState != null){
            if(storeState.checkPermission(Action.appointOwner)){
                activeStores.get(storeId).appointUser(id, appointedId, Role.Owner);
                return activeStores.get(storeId);
            }
            else
                throw new Exception("the member does not have permission to appoint a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public Role checkRoleInStore(int storeId) {
        UserState state = activeRoles.get(storeId);
        if(state != null)
            return state.getRole();
        else
            return null;
    }

    public Set<Integer> fireOwner(int appointedId, int storeId) throws Exception{
        UserState storeState = activeRoles.get(storeId);
        if(storeState != null){
            if(id == appointedId || checkPermission(Action.fireOwner, storeId)){
                return activeStores.get(storeId).fireUser(appointedId);
            }
            else
                throw new Exception("the member does not have permission to fire an owner in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public void removeRoleInStore(int storeId) {
        activeRoles.remove(storeId);
        inActiveRoles.remove(storeId);
        activeStores.remove(storeId);
        inActiveStores.remove(storeId);
    }

    public void fireManager(int appointedId, int storeId) throws Exception{
        UserState storeState = activeRoles.get(storeId);
        if(storeState != null){
            if(id == appointedId || checkPermission(Action.fireManager, storeId)){
                activeStores.get(storeId).fireUser(appointedId);
            }
            else
                throw new Exception("the member does not have permission to fire a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public void addAction(Action a, int storeId) throws Exception {
        UserState state = activeRoles.get(storeId);
        if(state != null) {
            if (state.getRole() == Role.Manager) {
                if (!state.checkPermission(a)) {
                    if (state.checkHasAvailableAction(a)) {
                        state.addAction(a);
                    }
                    else
                        throw new Exception("manager can't have this permission");
                }
                else
                    throw new Exception("the manager already has this action");
            }
            else
                throw new Exception("the role of the member in this store is not manager");
        }
        else
            throw new Exception("the member does not work in this store");
    }

    public void removeAction(Action a, int storeId) throws Exception {
        UserState state = activeRoles.get(storeId);
        if(state != null) {
            if (state.getRole() == Role.Manager) {
                if (state.checkPermission(a)) {
                        state.removeAction(a);
                }
                else
                    throw new Exception("the manager already does not have this action");
            }
            else
                throw new Exception("the role of the member in this store is not manager");
        }
        else
            throw new Exception("the member does not work in this store");
    }

    public Set<Integer> closeStore(int storeId) throws Exception {
        UserState state = activeRoles.get(storeId);
        if(state != null){
            if(state.checkPermission(Action.closeStore)){
                Store store = activeStores.get(storeId);
                if(store != null) {
                    //changeToInactive(storeId);
                    return store.closeStoreTemporary(id);
                }
                else
                    throw new Exception("the store does not exist");
            }
            else
                throw new Exception("the member is not allowed to close the store");
        }
        else
            throw new Exception("tne member does not work in this store: " + storeId);
    }

    public Set<Integer> reOpenStore(int storeId) throws Exception {

        UserState state = inActiveRoles.get(storeId);
        if(state != null){
            if(state.checkPermission(Action.reopenStore)){
                Store store = inActiveStores.get(storeId);
                if(store != null) {
                    //changeToActive(storeId);
                    return store.reopenStore(id);
                }
                else
                    throw new Exception("the store is not closed");
            }
            else
                throw new Exception("the member is not allowed to open the store");
        }
        else
            throw new Exception("tne member does not work in this store: " + storeId);
    }

    public boolean checkPermission(Action action, int storeId) {
        UserState role = activeRoles.get(storeId);
        if(role != null)
            return role.checkPermission(action);
        else
            return false;
    }

    public void changeToInactive(int storeId) {
        inActiveRoles.put(storeId, activeRoles.get(storeId));
        activeRoles.remove(storeId);
        inActiveStores.put(storeId, activeStores.get(storeId));
        activeStores.remove(storeId);
    }

    public void changeToActive(int storeId) {
        activeRoles.put(storeId, inActiveRoles.get(storeId));
        inActiveRoles.remove(storeId);
        activeStores.put(storeId, inActiveStores.get(storeId));
        inActiveStores.remove(storeId);
    }

    public Set<Integer> getWorkerIds(int storeId) throws Exception {
        Store store = activeStores.get(storeId);
        UserState state = activeRoles.get(storeId);
        if(store != null && state.getRole() == Role.Creator)
            return store.getUsersInStore();
        else
            throw new Exception("the member is not allowed to get the worker ids in store: " + storeId);
    }

    public Info getInformation(int storeId) {
        Info info = new Info(id, name, email, birthday, age);
        UserState state = activeRoles.get(storeId);
        if(state.getRole() == Role.Manager)
            info.addManagerActions(state.getActions());
        return info;
    }

    public Set<Integer> getAllStoreIds(){
        Set<Integer> storeIds = activeStores.keySet();
        storeIds.addAll((inActiveRoles.keySet()));
        return storeIds;
    }
}