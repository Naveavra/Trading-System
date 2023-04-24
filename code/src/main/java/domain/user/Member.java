package domain.user;

import domain.states.Buyer;
import domain.states.StoreCreator;
import domain.states.StoreManager;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import utils.*;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: change all the void functions to return a value in case of success/failure
//TODO: need to add to functions an addToUserHistory so that it will save all the information
public class Member {

    private Guest g;
    private int id;
    private String name;
    private String birthday;
    private String email;
    private String password;

    private HashMap<Integer, UserState> roles; //connection between registered to the shops
    private HashMap<Integer, Store> stores; //saves all the stores it has a job at
    private ConcurrentLinkedDeque<Notification> notifications;

    private UserHistory userHistory;
    private boolean isConnected;

    private List<Pair<String, String>> securityQuestions;
    //new Member(1, "eliben123@gmail.com", "aBc123", "24/02/2002")
    public Member(int id, String email, String password, String birthday){
        this.id = id;
        String[] emailParts = email.split("@");
        this.name = emailParts[0];
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        roles = new HashMap<>();
        stores = new HashMap<>();
        userHistory = new UserHistory();
        userHistory.addEmail(this.email);
        userHistory.addName(this.name);
        userHistory.addPassword(this.password);
        g = new Guest(id);
        notifications = new ConcurrentLinkedDeque<>();
        securityQuestions = new LinkedList<>();
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
        for(int i = 0; i<securityQuestions.size(); i++)
            if(!answers.get(i).equals(securityQuestions.get(i).getSecond()))
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
        stores.put(store.getStoreid(), store);
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
        UserState us = roles.get(storeId);
        return us.checkPermission(Action.viewMessages);
    }


    /**
     * checks if the member can answer messages
     * @param storeId
     * @return
     */
    public boolean canGiveFeedback(int storeId) {
        UserState us = roles.get(storeId);
        return us.checkPermission(Action.answerMessage);
    }

    public String getUserPurchaseHistory() {
        return userHistory.getUserPurchaseHistory(name);
    }

    public String getInformation() {
        return userHistory.getInformation();
    }

    /**
     * set security questions
     * @param question
     * @param answer
     */
    public void addQuestionForLogin(String question, String answer) throws Exception {
        for(Pair<String, String> secQuestion : securityQuestions){
            if(question.equals(secQuestion.getFirst()))
                throw new Exception("you already added this question");
        }

        Pair<String, String> secQuestion = new Pair<>(question, answer);
        securityQuestions.add(secQuestion);
        userHistory.addQuestion(secQuestion);
    }

    public void changeAnswerForQuestion(String question, String newAnswer) throws Exception {
        boolean check = false;
        for(Pair<String, String> secQuestion : securityQuestions){
            if(question.equals(secQuestion.getFirst())) {
                check = true;
                secQuestion.setSecond(newAnswer);
                userHistory.changeAnswerForQuestion(question, newAnswer);
            }
        }
        if(!check)
            throw new Exception("the question you gave is not part of your security questions");
    }

    public Store appointToManager(int appointedId, int storeId) throws Exception {
        UserState storeState = roles.get(storeId);
        if(storeState != null){
            if(storeState.checkPermission(Action.appointManager)){
                stores.get(storeId).appointUser(id, appointedId, Role.Manager);
                return stores.get(storeId);
            }
            else
                throw new Exception("the member does not have permission to appoint a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public Store appointToOwner(int appointedId, int storeId) throws Exception {
        UserState storeState = roles.get(storeId);
        if(storeState != null){
            if(storeState.checkPermission(Action.appointOwner)){
                stores.get(storeId).appointUser(id, appointedId, Role.Owner);
                return stores.get(storeId);
            }
            else
                throw new Exception("the member does not have permission to appoint a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public Role checkRoleInStore(int storeId) {
        UserState state = roles.get(storeId);
        if(state != null)
            return state.getRole();
        else
            return null;
    }

    public void fireOwner(int appointedId, int storeId) throws Exception{
        UserState storeState = roles.get(storeId);
        if(storeState != null){
            if(storeState.checkPermission(Action.fireOwner)){
                stores.get(storeId).fireUser(appointedId);
            }
            else
                throw new Exception("the member does not have permission to appoint a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }

    public void removeRoleInStore(int storeId) {
        roles.remove(storeId);
        stores.remove(storeId);
    }

    public void fireManager(int appointedId, int storeId) throws Exception{
        UserState storeState = roles.get(storeId);
        if(storeState != null){
            if(storeState.checkPermission(Action.fireManager)){
                stores.get(storeId).fireUser(appointedId);
            }
            else
                throw new Exception("the member does not have permission to appoint a manager in this store: " + storeId);
        }
        else
            throw new Exception("the member does not have a role in this store: " + storeId);
    }
}
