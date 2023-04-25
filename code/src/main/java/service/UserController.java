package service;

import domain.states.StoreManager;
import domain.states.StoreOwner;
import domain.store.storeManagement.Store;
import domain.user.Guest;
import domain.user.Member;
import utils.Action;
import utils.Message;
import utils.Notification;
import utils.Role;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;


public class UserController {

    ConcurrentHashMap<Integer, Guest> guestList;
    int guestIds;
    ConcurrentHashMap<String, Member> memberList;
    ConcurrentHashMap<Integer, String> idToEmail;
    int memberIds;
    int messageIds;


    public UserController(){
        guestList = new ConcurrentHashMap<>();
        guestIds = 0;
        memberList = new ConcurrentHashMap<>();
        idToEmail = new ConcurrentHashMap<>();
        memberIds = 1;
        messageIds = 0;
    }



    public synchronized int enterGuest(){
        Guest g = new Guest(guestIds);
        guestList.put(guestIds, g);
        guestIds+=2;
        return g.getId();
    }



    public void exitGuest(int id){
        guestList.remove(id);
    }

    public synchronized void register(String email, String password, String birthday) throws Exception{
        if(!checkEmail(email))
            throw new Exception("invalid email");
        for (Member m : memberList.values())
            if(m.getEmail().equals(email))
                throw new Exception("the email is already taken");
        if(!checkPassword(password))
            throw new Exception("password not meeting requirements");
        if(!checkBirthday(birthday))
            throw new Exception("birthday not legal");

        Member m = new Member(memberIds, email, password, birthday);
        memberList.put(email, m);
        idToEmail.put(memberIds, email);
        memberIds+=2;
    }

    //the answers given are for the security questions, if there are no security questions then put an empty list
    public synchronized int login(String email, String password, List<String> answers) throws Exception{
        if(!checkEmail(email))
            throw new Exception("invalid email");
        if(!checkPassword(password))
            throw new Exception("invalid password");
        Member m = memberList.get(email);
        if(m == null)
            throw new Exception("no such email");
        try {
            if(m.login(password, answers))
                return m.getId();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return -1;
    }

    //when logging out returns to main menu as guest
    public synchronized int logout(int memberId) throws Exception{
        String email = idToEmail.get(memberId);
        if(email != null){
            return logout(email);
        }
        else
            throw new Exception("no such id for a member");
    }

    public synchronized int logout(String email) throws Exception{
        Member m = memberList.get(email);
        if (m != null) {
            if (m.getIsConnected()) {
                m.disconnect();
                return enterGuest();
            }
            else
                throw new Exception("member is disconnected already");
        }
        else
            throw new Exception("member not found");

    }

    /**
     * @return list off all the usernames in our system
     */
    public synchronized String getAllUserNames()
    {
        Gson gson = new Gson();
        return gson.toJson(memberList.keySet());
//
//        for (Member obj : memberList.values()) {
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("name", ((Member)obj).getName());
//            jsonArray.add(jsonObj);
//        }

 //       return jsonArray;
    }

    //adding the productId to the user's cart with the given quantity
    public synchronized void addProductToCart(int userId, int storeId, int productId, int quantity) throws Exception{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            g.addProductToCart(storeId, productId, quantity);
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                addProductToCart(email, storeId, productId, quantity);
            else
                throw new Exception("no such member exists");
        }

    }

    public synchronized void addProductToCart(String email, int storeId, int productId, int quantity) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if (m.getIsConnected())
                m.addProductToCart(storeId, productId, quantity);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no such member exists");
    }

    //removing the productId from the user's cart
    public synchronized void removeProductFromCart(int userId, int storeId, int productId) throws Exception{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            if(g != null)
                g.removeProductFromCart(storeId, productId);
            else
                throw new Exception("no such guest exists");
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                removeProductFromCart(email, storeId, productId);
            else
                throw new Exception("no such member exists");
        }
    }

    public synchronized void removeProductFromCart(String email, int storeId, int productId) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.removeProductFromCart(storeId, productId);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no such member exists");
    }


    //adding the change quantity to the product's quantity in the user's cart
    public synchronized void changeQuantityInCart(int userId, int storeId, int productId, int change) throws Exception{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            if(g != null)
                g.changeQuantityInCart(storeId, productId, change);
            else
                throw new Exception("no such guest exists");
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                changeQuantityInCart(email, storeId, productId, change);
            else
                throw new Exception("no such member exists");
        }
    }
    public synchronized void changeQuantityInCart(String email, int storeId, int productId, int change) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.changeQuantityInCart(storeId, productId, change);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no such member exists");
    }

    /**
     * the return of the function is a hashmap between storeId to hashmap of productId to quantity. meaning that it displays the
     * product and quantity for each store.
     * @param userId
     * @return
     * @throws Exception
     */
    public synchronized String getUserCart(int userId) throws Exception{
        Gson gson = new Gson();
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            if(g != null)
                return  gson.toJson(g.getCartContent());
            else
                throw new Exception("no such guest exists");
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                return getUserCart(email);
            else
                throw new Exception("no such member exists");
        }
    }

    public synchronized String getUserCart(String email) throws Exception{
        Member m = memberList.get(email);
        Gson gson = new Gson();
        if(m != null) {
            if(m.getIsConnected())
                return gson.toJson(m.getCartContent());
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no such member exists");
    }

    public synchronized void purchaseMade(int orderId, int userId, int totalPrice) throws Exception{
        if (userId % 2 == 1) {
            String email = idToEmail.get(userId);
            if(email != null)
                purchaseMade(orderId, email, totalPrice);
            else
                throw new Exception("no member has such id");
        }
    }

    public synchronized void purchaseMade(int orderId, String email, int totalPrice) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.purchaseMade(orderId, totalPrice);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has that email");
    }


    /**
     * opening a new store in the market
     * @param userId
     * @return
     */
    public synchronized boolean canOpenStore(int userId){

        String email = idToEmail.get(userId);
        if(email == null)
            return false;
        return memberList.get(email) != null;
    }


    public synchronized void openStore(int userId, Store store) throws Exception{
        if(userId % 2 == 0){
            throw new Exception("a guest can't open a store");
        }
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                openStore(email, store);
            else
                throw new Exception("the id does not match any member");
        }
    }

    public synchronized void openStore(String email, Store store) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.openStore(store);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("the member does not exist");
    }


    /**
     * write a review for a store (can be part of a bigger order).
     * @param orderId
     * @param storeId
     * @param content
     * @param grading
     * @param userId
     * @return
     * @throws Exception
     */
    public synchronized Message writeReviewForStore(int orderId, int storeId, String content, int grading, int userId) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("a guest can't write reviews");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return writeReviewForStore(orderId, storeId, content, grading, email);
            else
                throw new Exception("no member has such id");
        }

    }

    public synchronized Message writeReviewForStore(int orderId, int storeId, String content, int grading, String email) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected()) {
                int tmp = memberIds;
                messageIds += 2;
                return m.writeReview(tmp, storeId, orderId, content, grading);
            }
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");

    }

    /**
     * write a review for a product in a store.
     * @param orderId
     * @param storeId
     * @param productId
     * @param comment
     * @param grading
     * @param userId
     * @return
     * @throws Exception
     */
    public synchronized Message writeReviewForProduct(int orderId, int storeId, int productId, String comment, int grading, int userId) throws Exception {
        if (userId % 2 == 0)
            throw new Exception("a guest can't write reviews");
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                return writeReviewForProduct(orderId, storeId, productId, comment, grading, email);
            else
                throw new Exception("no member has such id");
        }
    }

    public synchronized Message writeReviewForProduct(int orderId, int storeId, int productId, String comment, int grading, String email) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected()) {
                int tmp = messageIds;
                messageIds += 2;
                return m.writeReview(tmp, storeId, productId, orderId, comment, grading);
            }
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }


    /**
     * write a complaint to a store after a purchase
     * @param orderId
     * @param storeId
     * @param comment
     * @param userId
     * @return
     */
    public synchronized Message writeComplaintToMarket(int orderId, int storeId, String comment,int userId)throws Exception{
        if(userId % 2 == 0)
            throw new Exception("guest can't write complaints");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return writeComplaintToMarket(orderId, storeId, comment, email);
            else
                throw new Exception("no member has this id");
        }
    }


    //TODO:remember to add the storeCreator's email to the message (either later or in a function here)
    public synchronized Message writeComplaintToMarket(int orderId, int storeId, String comment,String email) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected()) {
                int tmp = messageIds;
                messageIds += 2;
                return m.writeComplaint(tmp, orderId, storeId, comment);
            }
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }


    public Message sendQuestionToStore(int storeId, String question, int userId) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest can't write a question to a store");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return sendQuestionToStore(storeId, question, email);
            else
                throw new Exception("no member has this id");
        }
    }


    //TODO:need to check before that the storeId is legal
    public Message sendQuestionToStore(int storeId, String question, String email) throws Exception {
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected()) {
                int tmp = messageIds;
                messageIds += 2;
                return m.sendQuestion(tmp, storeId, question);
            }
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }

    /**
     * functions to check the permissions of the user
     * @param userId
     * @param storeId
     * @return
     * @throws Exception
     */
    public synchronized boolean canCheckMessages(int userId, int storeId) throws Exception{
        if(userId % 2 == 0)
            return false;
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return canCheckMessages(email, storeId);
            else
                throw new Exception("no member has this id");

        }
    }


    public synchronized boolean canCheckMessages(String email, int storeId) throws Exception {
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                return m.canCheckMessages(storeId);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");

    }

    public synchronized boolean canGiveFeedback(int userId, int storeId) throws Exception {
        if(userId % 2 == 0)
            return false;
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return canGiveFeedback(email, storeId);
            else
                throw new Exception("no member has this id");

        }
    }

    public synchronized boolean canGiveFeedback(String email, int storeId) throws Exception {
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                return m.canGiveFeedback(storeId);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }
    /**
     * add notification for a user.
     * @param userId
     * @param notification
     * @throws Exception
     */
    public synchronized void addNotification(int userId, Notification notification) throws Exception{
        if(userId % 2 == 0)
            throw new Exception("guest can't get Notification");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                addNotification(email, notification);
            else
                throw new Exception("no member has this id");
        }
    }

    public synchronized void addNotification(String email, Notification notification) throws Exception{
        Member m = memberList.get(email);
        if(m != null) {
            if (m.getIsConnected())
                m.addNotification(notification);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }


    /**
     * displays the user's notifications
     * @param userId
     * @return
     * @throws Exception
     */
    public synchronized List<String> displayNotifications(int userId) throws Exception{
        if(userId % 2 == 0)
            throw new Exception("guest doesn't have notifications");
        else{
            String email = idToEmail.get(userId);
            if(email != null){
                return displayNotifications(email);
            }
            else
                throw new Exception("no member has this id");
        }
    }

    public synchronized List<String> displayNotifications(String email) throws Exception{
        Member m = memberList.get(email);
        if(m != null){
            if(m.getIsConnected())
                return m.displayNotifications();
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has such email");
    }


    public synchronized String getUserPurchaseHistory(int userId) throws Exception {
        if(userId % 2 ==0)
            throw new Exception("guest can't access the user history");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return getUserPurchaseHistory(email);
            else
                throw new Exception("no member has this id");

        }
    }

    /**
     * @param email
     * @return returns json
     */
    public synchronized String getUserPurchaseHistory(String email) throws Exception{
        Member m = memberList.get(email);
        if(m!= null)
            return m.getUserPurchaseHistory();
        else
            throw new Exception("no member has this email");
    }

    //TODO:remember to add into login the option to answer the security questions(if needed)

    /**
     * returns all the user's information
     * @param userId
     * @return
     * @throws Exception
     */
    public synchronized String getUserInformation(int userId) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest does not have an email");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return getUserInformation(email);
            else
                throw new Exception("no member has this id");
        }

    }

    public synchronized String getUserInformation(String email) throws Exception {
        Member m = memberList.get(email);
        if(m != null){
            if(m.getIsConnected())
                return m.getInformation();
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");

    }

    /**
     * function to change the user's email
     * @param userId
     * @param newEmail
     */
    public synchronized void changeUserEmail(int userId, String newEmail) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest does not have an email");
        else{
            String email = idToEmail.get(userId);
            if(email != null && checkEmail(newEmail))
                changeUserEmail(email, newEmail);
            else
                throw new Exception("no member has this id");
        }
    }
    public synchronized void changeUserEmail(String email, String newEmail) throws Exception {
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.setNewEmail(newEmail);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }

    /**
     * function to change the user's name
     * @param userId
     * @param newName
     */
    public synchronized void changeUserName(int userId, String newName) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest does not have a name");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                changeUserName(email, newName);
            else
                throw new Exception("no member has this id");
        }
    }
    public synchronized void changeUserName(String email, String newName) throws Exception {
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.setNewName(newName);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }


    /**
     * function to change the user's password
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    public synchronized void changeUserPassword(int userId, String oldPassword, String newPassword) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest does not have a name");
        else{
            String email = idToEmail.get(userId);
            if(email != null && checkPassword(newPassword))
                changeUserPassword(email, oldPassword, newPassword);
            else
                throw new Exception("no member has this id");
        }
    }
    public synchronized void changeUserPassword(String email, String oldPassword, String newPassword) throws Exception {
        Member m = memberList.get(email);
        if(m != null) {
            if(m.getIsConnected())
                m.setNewPassword(oldPassword, newPassword);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");
    }

    public synchronized void addSecurityQuestion(int userId, String question, String answer) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest does not login");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                addSecurityQuestion(email, question, answer);
            else
                throw new Exception("no member has this email");
        }
    }


    public synchronized void addSecurityQuestion(String email, String question, String answer) throws Exception{
        Member m = memberList.get(email);
        if(m != null){
            if(m.getIsConnected())
                m.addQuestionForLogin(question, answer);
            else
                throw new Exception("the member is not connected");
        }
        else
            throw new Exception("no member has this email");

    }


    //starting the functions connecting to the store

    /**
     * appointing a new owner to a store
     * @param ownerId
     * @param appointedId
     * @param storeId
     * @throws Exception
     */
    public synchronized void appointOwner(int ownerId, int appointedId, int storeId) throws Exception {
        if(ownerId % 2 == 0)
            throw new Exception("guest cannot appoint people to a role in a store");
        else{
            String ownerEmail = idToEmail.get(ownerId);
            String appointedEmail = idToEmail.get(appointedId);
            if(ownerEmail != null){
                if(appointedEmail != null){
                    appointOwner(ownerEmail, appointedEmail, storeId);
                }
                else
                    throw new Exception("the appointedId given does not belong to any member");
            }
            else
                throw new Exception("the ownerId given does not belong to any member");
        }
    }

    public synchronized void appointOwner(String ownerEmail, String appointedEmail, int storeId) throws Exception{
        Member owner = memberList.get(ownerEmail);
        Member appointed = memberList.get(appointedEmail);
        if(owner != null){
            if(appointed != null){
                if(owner.getIsConnected()){
                    if(appointed.checkRoleInStore(storeId) != Role.Owner) {
                        Store store = owner.appointToOwner(appointed.getId(), storeId);
                        Notification<String> notify = new Notification<>("you have been appointed to owner in store: " + storeId);
                        appointed.addNotification(notify);
                        appointed.changeRoleInStore(storeId, new StoreOwner(), store);
                    }
                    else
                        throw new Exception("the member already is a owner in this store");
                }
                else
                    throw new Exception("the member is not connected so he can't appoint");
            }
            else
                throw new Exception("no member has this email: "+appointedEmail);
        }
        else
            throw new Exception("no member has this email: "+ownerEmail);
    }

    /**
     * firing an owner from the store
     * @param ownerId
     * @param appointedId
     * @param storeId
     * @throws Exception
     */
    public synchronized void fireOwner(int ownerId, int appointedId, int storeId) throws Exception {
        if(ownerId % 2 == 0)
            throw new Exception("guest cannot fire people from a role in a store");
        else{
            String ownerEmail = idToEmail.get(ownerId);
            String appointedEmail = idToEmail.get(appointedId);
            if(ownerEmail != null){
                if(appointedEmail != null){
                    fireOwner(ownerEmail, appointedEmail, storeId);
                }
                else
                    throw new Exception("the appointedId given does not belong to any member");
            }
            else
                throw new Exception("the ownerId given does not belong to any member");
        }
    }

    public synchronized void fireOwner(String ownerEmail, String appointedEmail, int storeId) throws Exception {
        Member owner = memberList.get(ownerEmail);
        Member appointed = memberList.get(appointedEmail);
        if(owner != null){
            if(appointed != null){
                if(owner.getIsConnected()){
                    if(appointed.checkRoleInStore(storeId) == Role.Owner) {
                        owner.fireOwner(appointed.getId(), storeId);
                        Notification<String> notify = new Notification<>("you have been fired from owner in store: " + storeId);
                        appointed.addNotification(notify);
                        appointed.removeRoleInStore(storeId);
                    }
                    else
                        throw new Exception("the member is not a owner in this store");
                }
                else
                    throw new Exception("the member is not connected so he can't appoint");
            }
            else
                throw new Exception("no member has this email: "+appointedEmail);
        }
        else
            throw new Exception("no member has this email: "+ownerEmail);
    }

    /**
     * appointing a new manager to a store
     * @param ownerId
     * @param appointedId
     * @param storeId
     * @throws Exception
     */
    public synchronized void appointManager(int ownerId, int appointedId, int storeId) throws Exception {
        if(ownerId % 2 == 0)
            throw new Exception("guest cannot appoint people to a role in a store");
        else{
            String ownerEmail = idToEmail.get(ownerId);
            String appointedEmail = idToEmail.get(appointedId);
            if(ownerEmail != null){
                if(appointedEmail != null){
                    appointManager(ownerEmail, appointedEmail, storeId);
                }
                else
                    throw new Exception("the appointedId given does not belong to any member");
            }
            else
                throw new Exception("the ownerId given does not belong to any member");
        }

    }

    public synchronized void appointManager(String ownerEmail, String appointedEmail, int storeId) throws Exception{
        Member owner = memberList.get(ownerEmail);
        Member appointed = memberList.get(appointedEmail);
        if(owner != null){
            if(appointed != null){
                if(owner.getIsConnected()) {
                    if (appointed.checkRoleInStore(storeId) != Role.Manager) {
                        Store store = owner.appointToManager(appointed.getId(), storeId);
                        Notification<String> notify = new Notification<>("you have been appointed to manager in store: " + storeId);
                        appointed.addNotification(notify);
                        appointed.changeRoleInStore(storeId, new StoreManager(), store);
                    }
                    else
                        throw new Exception("the member already is a manager in this store");
                }
                else
                    throw new Exception("the owner is not connected so he can't appoint");
            }
            else
                throw new Exception("no member has this email: "+appointedEmail);
        }
        else
            throw new Exception("no member has this email: "+ownerEmail);
    }

    /**
     * firing a manager from the store
     * @param ownerId
     * @param appointedId
     * @param storeId
     * @throws Exception
     */
    public synchronized void fireManager(int ownerId, int appointedId, int storeId) throws Exception {
        if(ownerId % 2 == 0)
            throw new Exception("guest cannot fire people from a role in a store");
        else{
            String ownerEmail = idToEmail.get(ownerId);
            String appointedEmail = idToEmail.get(appointedId);
            if(ownerEmail != null){
                if(appointedEmail != null){
                    fireManager(ownerEmail, appointedEmail, storeId);
                }
                else
                    throw new Exception("the appointedId given does not belong to any member");
            }
            else
                throw new Exception("the ownerId given does not belong to any member");
        }
    }

    public synchronized void fireManager(String ownerEmail, String appointedEmail, int storeId) throws Exception {
        Member owner = memberList.get(ownerEmail);
        Member appointed = memberList.get(appointedEmail);
        if(owner != null){
            if(appointed != null){
                if(owner.getIsConnected()){
                    if(appointed.checkRoleInStore(storeId) == Role.Manager) {
                        owner.fireManager(appointed.getId(), storeId);
                        Notification<String> notify = new Notification<>("you have been fired from manager in store: " + storeId);
                        appointed.addNotification(notify);
                        appointed.removeRoleInStore(storeId);
                    }
                    else
                        throw new Exception("the member is not a manager in this store");
                }
                else
                    throw new Exception("the member is not connected so he can't appoint");
            }
            else
                throw new Exception("no member has this email: "+appointedEmail);
        }
        else
            throw new Exception("no member has this email: "+ownerEmail);
    }


    /**
     * adding a new permission to manager
     * @param ownerId
     * @param managerId
     * @param a
     * @param storeId
     * @throws Exception
     */
    public synchronized void addManagerAction(int ownerId, int managerId, Action a, int storeId) throws Exception {
        String managerEmail = idToEmail.get(managerId);
        String ownerEmail = idToEmail.get(ownerId);
        if(ownerEmail != null) {
            if (managerEmail != null){
                addManagerAction(ownerEmail, managerEmail, a, storeId);
            }
            else
                throw new Exception("the managerId: " + managerId + " given goes not belong to any member");
        }
        else
            throw new Exception("the ownerId: " + ownerId + " given goes not belong to any member");
    }

    public synchronized void addManagerAction(String ownerEmail, String managerEmail, Action a, int storeId) throws Exception {
        Member owner = memberList.get(ownerEmail);
        Member manager = memberList.get(managerEmail);
        if(owner != null){
            if(manager != null){
                if(owner.getIsConnected()){
                    if(manager.checkRoleInStore(storeId) == Role.Manager) {
                        manager.addAction(a, storeId);
                        Notification<String> notify = new Notification<>("the following action: "  + a.toString() + "\n" +
                                "has been added for you for store: " + storeId);
                        manager.addNotification(notify);
                    }
                    else
                        throw new Exception("the member is not a manager in this store");
                }
                else
                    throw new Exception("the member is not connected so he can't appoint");
            }
            else
                throw new Exception("no member has this email: "+managerEmail);
        }
        else
            throw new Exception("no member has this email: "+ownerEmail);
    }

    /**
     * remove an action for the manager
     * @param ownerId
     * @param managerId
     * @param a
     * @param storeId
     * @throws Exception
     */
    public synchronized void removeManagerAction(int ownerId, int managerId, Action a, int storeId) throws Exception {
        String managerEmail = idToEmail.get(managerId);
        String ownerEmail = idToEmail.get(ownerId);
        if(ownerEmail != null) {
            if (managerEmail != null){
                removeManagerAction(ownerEmail, managerEmail, a, storeId);
            }
            else
                throw new Exception("the managerId: " + managerId + " given goes not belong to any member");
        }
        else
            throw new Exception("the ownerId: " + ownerId + " given goes not belong to any member");
    }

    public synchronized void removeManagerAction(String ownerEmail, String managerEmail, Action a, int storeId) throws Exception {
        Member owner = memberList.get(ownerEmail);
        Member manager = memberList.get(managerEmail);
        if(owner != null){
            if(manager != null){
                if(owner.getIsConnected()){
                    if(manager.checkRoleInStore(storeId) == Role.Manager) {
                        manager.removeAction(a, storeId);
                        Notification<String> notify = new Notification<>("the following action: "  + a.toString() + "\n" +
                                "has been removed for you for store: " + storeId);
                        manager.addNotification(notify);
                    }
                    else
                        throw new Exception("the member is not a manager in this store");
                }
                else
                    throw new Exception("the member is not connected so he can't appoint");
            }
            else
                throw new Exception("no member has this email: "+managerEmail);
        }
        else
            throw new Exception("no member has this email: "+ownerEmail);
    }


    /**
     * closing a store temporarily
     * @param userId
     * @param storeId
     * @throws Exception
     */
    public synchronized void closeStore(int userId, int storeId) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest can't close stores");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                closeStore(email, storeId);
            else
                throw new Exception("no member has this id: " + userId);
        }

    }

    public synchronized void closeStore(String userEmail, int storeId) throws Exception {
        Member m = memberList.get(userEmail);
        if(m != null) {
            Set<Integer> workerIds = m.closeStore(storeId);
            workerIds.remove(m.getId());
            for(int workerId : workerIds){
                String email = idToEmail.get(workerId);
                if(email != null) {
                    Member worker = memberList.get(email);
                    if(worker != null) {
                        worker.changeToInactive(storeId);
                        String notify = "the store: " + storeId + " has been temporarily closed";
                        Notification<String> notification = new Notification<>(notify);
                        addNotification(workerId, notification);
                    }
                    else
                        throw new Exception("the set for closeStore contains an id that its corresponding email does not belong" +
                                " to any member");
                }
                else
                    throw new Exception("the set for closeStore contains an id of a non existent member");
            }
        }
        else
            throw new Exception("no member has this email: " + userEmail);
    }


    /**
     * reopening a store
     * @param userId
     * @param storeId
     * @throws Exception
     */
    public synchronized void reOpenStore(int userId, int storeId) throws Exception {
        if(userId % 2 == 0)
            throw new Exception("guest can't close stores");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                reOpenStore(email, storeId);
            else
                throw new Exception("no member has this id: " + userId);
        }

    }

    public synchronized void reOpenStore(String userEmail, int storeId) throws Exception {
        Member m = memberList.get(userEmail);
        if(m != null) {
            Set<Integer> workerIds = m.reOpenStore(storeId);
            workerIds.remove(m.getId());
            for(int workerId : workerIds){
                String email = idToEmail.get(workerId);
                if(email != null) {
                    Member worker = memberList.get(email);
                    if(worker != null) {
                        worker.changeToActive(storeId);
                        String notify = "the store: " + storeId + " has been reOpened";
                        Notification<String> notification = new Notification<>(notify);
                        addNotification(workerId, notification);
                    }
                    else
                        throw new Exception("the set for closeStore contains an id that its corresponding email does not belong" +
                                " to any member");
                }
                else
                    throw new Exception("the set for closeStore contains an id of a non existent member");
            }
        }
        else
            throw new Exception("no member has this email: " + userEmail);
    }


    /**
     * checks if the user can do a specific action in a store
     * @param userId
     * @param action
     * @param storeId
     * @return
     * @throws Exception
     */
    public synchronized boolean checkPermission(int userId, Action action, int storeId) throws Exception {
        if(userId % 2 == 0)
            return false;
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return checkPermission(email, action, storeId);
            else
                throw new Exception("no member has this id: " + userId);
        }

    }

    public synchronized boolean checkPermission(String userEmail, Action action, int storeId) throws Exception {
        Member m = memberList.get(userEmail);
        if(m != null)
            return m.checkPermission(action, storeId);
        else
            throw new Exception("no member has this email: " + userEmail);
    }


    /**
     * getting information about the workers in a store
     * @param userId
     * @param storeId
     * @return
     */
    public synchronized String getWorkersInformation(int userId, int storeId) throws Exception{
        if(userId % 2 == 0)
            throw new Exception("guest can't see workers information");
        else{
            String email = idToEmail.get(userId);
            if(email != null)
                return getWorkersInformation(email, storeId);
            else
                throw new Exception("no member has this id");
        }
        return null;
    }


    public synchronized String getWorkersInformation(String email, int storeId) throws Exception {
        Member m = memberList.get(storeId);
        if(m != null){

        }
        else
            throw new Exception("no member has this email: " + email);
    }


    //check that the format user@domain.com exists
    public boolean checkEmail(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z0-9-]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }


    //the password length is between 6 and 20, need 1 small letter, 1 big letter and 1 number at least.
    // all other characters are not allowed
    public boolean checkPassword(String password){
        int countSmall = 0;
        int countBig = 0;
        int countNum = 0;
        if(password.length()<6 || password.length() > 20)
            return false;
        for(int i = 0; i<password.length(); i++){
            if(password.charAt(i)>=48 && password.charAt(i)<=57)
                countNum++;
            else if(password.charAt(i)>=65 && password.charAt(i)<=90)
                countBig++;
            else if(password.charAt(i)>=97 && password.charAt(i)<=122)
                countSmall++;
            else
                return false;
        }
        return countNum != 0 && countBig != 0 && countSmall != 0;
    }

    //birthdays are written in the format: dd/mm/yyyy. only integers and '/'
    //check for 1<=month<=12 and that the day is good accordingly
    //checks for an age that makes sense (0<=age<=120).
    public boolean checkBirthday(String birthday){
        int[] validDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for(int i = 0; i<birthday.length(); i++)
            if(birthday.charAt(i) < 47 || birthday.charAt(i) > 57)
                return false;

        String[] splitBDay = birthday.split("/");
        int[] bDay = {Integer.parseInt(splitBDay[0]), Integer.parseInt(splitBDay[1]), Integer.parseInt(splitBDay[2])};
        if(bDay[1] <1 || bDay[1] > 12)
            return false;
        if(validDay[bDay[1] - 1] < bDay[0] || bDay[0] < 1)
            return false;

        String currentDate = String.valueOf(java.time.LocalDate.now());
        String[] splitCurrentDay = currentDate.split("-");
        int[] curDay = {Integer.parseInt(splitCurrentDay[0]), Integer.parseInt(splitCurrentDay[1]),
                Integer.parseInt(splitCurrentDay[2])};
        int tmp = curDay[0];
        curDay[0] = curDay[2];
        curDay[2] = tmp;

        if(bDay[2] > curDay[2] || (bDay[2] == curDay[2] && bDay[1] > curDay[1]) ||
                (bDay[2] == curDay[2] && bDay[1] == curDay[1] && bDay[0] > curDay[0]))
                return false;
        if(bDay[2] < (curDay[2] - 120))
            return false;

        return true;
    }

    public String getUserName(int id) throws Exception
    {
        if (id%2==0 && guestList.containsKey(id))
        {
            if (guestList.containsKey(id))
            {
                return String.valueOf(guestList.get(id).getId());
            }
            throw new Exception("guest doesn't exist");
        }
        else {
            String email = idToEmail.get(id);
            if (email != null)
            {
                Member m = memberList.get(email);
                if(m != null && m.getIsConnected())
                    return memberList.get(email).getName();
            }
            throw new Exception("user doesnt exist");

        }
    }



//    public synchronized void closeStore(int userID, int storeID) throws Exception
//    {
//        if (userID%2==0 )
//        {
//            throw new Exception("guest can not close stores");
//        }
//        String email = idToEmail.get(userID);
//        if (email != null)
//        {
//            Member m = memberList.get(email);
//            if(m != null ) //this method can also be invoked by the admin so there is no need to check if the user is logged in
//            {
//                m.closeStore(int storeID);
//            }
//        }
//
//    }
}
