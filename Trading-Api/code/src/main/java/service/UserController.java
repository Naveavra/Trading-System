package service;

import domain.states.StoreManager;
import domain.states.StoreOwner;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import domain.user.*;

import org.json.JSONObject;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Message;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class UserController {

    private ConcurrentHashMap<Integer, Guest> guestList;
    private ConcurrentHashMap<Integer, Member> memberList;
    private ConcurrentHashMap<String, Integer> emailToId;
    private StringChecks checks;
    int messageIds;


    public UserController(){
        guestList = new ConcurrentHashMap<>();
        memberList = new ConcurrentHashMap<>();
        emailToId = new ConcurrentHashMap<>();
        checks = new StringChecks();
        messageIds = 0;
    }


    public User getUser(int id) throws Exception{
        if(guestList.containsKey(id))
            return guestList.get(id);
        else if(memberList.containsKey(id))
            return memberList.get(id);
        throw new Exception("the id given does not belong to any user");
    }

    public Guest getGuest(int id) throws Exception{
        if(guestList.containsKey(id))
            return guestList.get(id);
        throw new Exception("the id given does not belong to any guest");
    }

    public Member getMember(int id) throws Exception{
        if(memberList.containsKey(id))
                return memberList.get(id);
        throw new Exception("the id given does not belong to any member");
    }

    public Member getMember(String email) throws Exception{
        if(emailToId.containsKey(email))
            return memberList.get(emailToId.get(email));
        throw new Exception("no member has this email");
    }

    public Member getInActiveMember(int id) throws Exception{
        if(memberList.containsKey(id)) {
            if (!memberList.get(id).getIsConnected()) {
                return memberList.get(id);
            }
            throw new Exception("the id given belongs to an inactive member");
        }
        throw new Exception("the id given does not belong to any member");
    }
    public Member getActiveMember(int id) throws Exception{
        if(memberList.containsKey(id)) {
            if (memberList.get(id).getIsConnected()) {
                return memberList.get(id);
            }
            throw new Exception("the id given belongs to an inactive member");
        }
        throw new Exception("the id given does not belong to any member");
    }

    public synchronized int enterGuest(int id){
        Guest g = new Guest(id);
        guestList.put(id, g);
        return g.getId();
    }



    public void exitGuest(int id) throws Exception {
        Guest g = getGuest(id);
        g.emptyCart();
        guestList.remove(id);
    }

    public synchronized void register(int id, String email, String password, String hashedPass, String birthday) throws Exception{
        checks.checkRegisterInfo(email, password, birthday);
        if(emailToId.containsKey(email))
                throw new Exception("the email is already taken");
        Member m = new Member(id, email, hashedPass, birthday);
        emailToId.put(email, id);
        memberList.put(id, m);
    }

    //the answers given are for the security questions, if there are no security questions then put an empty list
    public synchronized int login(String email, String password) throws Exception{
        Member m = getInActiveMember(emailToId.get(email));
        m.login(password);
        return m.getId();
    }

    public LoginInformation getLoginInformation(int memberId, String token) throws Exception{
        Member m = getMember(memberId);
        return m.getLoginInformation(token);

    }

    //when logging out returns to main menu as guest
    public synchronized void logout(int memberId) throws Exception{
        Member m = getActiveMember(memberId);
        m.disconnect();
    }

    //adding the productId to the user's cart with the given quantity
    public synchronized void addProductToCart(int userId, int storeId, ProductInfo product, int quantity) throws Exception{
        User user = getUser(userId);
        user.addProductToCart(storeId, product, quantity);
    }

    //removing the productId from the user's cart
    public synchronized void removeProductFromCart(int userId, int storeId, int productId) throws Exception{
        User user = getUser(userId);
        user.removeProductFromCart(storeId, productId);
    }


    //adding the change quantity to the product's quantity in the user's cart
    public synchronized void changeQuantityInCart(int userId, int storeId, ProductInfo product, int change) throws Exception{
        User user = getUser(userId);
        user.changeQuantityInCart(storeId, product, change);
    }


    public synchronized ShoppingCart getUserCart(int userId) throws Exception{
        User user = getUser(userId);
        return user.getShoppingCart();
    }

    public synchronized void purchaseMade(int userId, int orderId, double totalPrice) throws Exception{
        User user = getUser(userId);
        user.purchaseMade(orderId, totalPrice);
    }


    public synchronized void canOpenStore(int userId) throws Exception{
        getActiveMember(userId);
    }


    public synchronized void openStore(int userId, Store store) throws Exception{
        Member m = getActiveMember(userId);
        m.openStore(store);
    }

    public synchronized Message writeReviewForStore(int orderId, int storeId, String content, int grading, int userId) throws Exception {
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        return m.writeReview(tmp, storeId, orderId, content, grading);

    }

    public synchronized Message writeReviewForProduct(int orderId, int storeId, int productId, String comment, int grading, int userId) throws Exception {
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds += 2;
        return m.writeReview(tmp, storeId, productId, orderId, comment, grading);
    }


    public synchronized Message writeComplaintToMarket(int orderId, int storeId, String comment,int userId)throws Exception{
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        return m.writeComplaint(tmp, orderId, storeId, comment);
    }


    public Message sendQuestionToStore(int userId, int storeId, String question) throws Exception {
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        return m.sendQuestion(tmp, storeId, question);
    }

    public synchronized void addNotification(int userId, Notification notification) throws Exception{
        Member m = getMember(userId);
        m.addNotification(notification);
    }
    public synchronized void addNotification(String userEmail, Notification notification) throws Exception{
        Member m = getActiveMember(emailToId.get(userEmail));
        m.addNotification(notification);
    }

    public synchronized List<String> displayNotifications(int userId) throws Exception{
        Member m = getActiveMember(userId);
        return m.displayNotifications();
    }


    public PurchaseHistory getUserPurchaseHistory(int userId, boolean isAdmin) throws Exception{
        Member m;
        if(isAdmin)
            m = getMember(userId);
        else
            m = getActiveMember(userId);
        return m.getUserPurchaseHistory();
    }


    public synchronized Info getUserPrivateInformation(int userId) throws Exception {
        Member m = getActiveMember(userId);
        return m.getPrivateInformation();

    }

    public synchronized void changeUserEmail(int userId, String newEmail) throws Exception {
        Member m = getActiveMember(userId);
        m.setNewEmail(newEmail);
        emailToId.put(newEmail, m.getId());
    }


    public synchronized void changeUserName(int userId, String newName) throws Exception {
        Member m = getActiveMember(userId);
        m.setNewName(newName);
    }


    public synchronized void changeUserPassword(int userId, String oldPass, String newPass, String newHashedPass) throws Exception {
        Member m = getActiveMember(userId);
        checks.checkPassword(newPass);
        m.setNewPassword(oldPass, newHashedPass);
    }

    //starting the functions connecting to the store
    public synchronized void appointOwner(int ownerId, String appointedEmail, int storeId) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedEmail);
        Store store = owner.appointToOwner(appointed.getId(), storeId);
        Notification<String> notify = new Notification<>("you have been appointed to owner in store: " + storeId);
        appointed.addNotification(notify);
        appointed.changeRoleInStore(new StoreOwner(appointed.getId(), store), store);
    }



    public void fireIds(Set<Integer> firedIds, int storeId) throws Exception{
        for (int firedId : firedIds) {
            Member fired = getMember(firedId);
            Notification<String> notify;
            if (fired.getRole(storeId).getRole() == Role.Owner)
               notify = new Notification<>("you have been fired from owner in store: " + storeId);
             else
                notify = new Notification<>("you have been fired from manager in store: " + storeId);
            fired.addNotification(notify);
            fired.removeRoleInStore(storeId);
        }
    }
    public synchronized void fireOwner(int ownerId, int appointedId, int storeId) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedId);
        Set<Integer> firedIds = owner.fireOwner(appointed.getId(), storeId);
        fireIds(firedIds, storeId);
    }

    public synchronized void appointManager(int ownerId, String appointedEmail, int storeId) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedEmail);
        Store store = owner.appointToManager(appointed.getId(), storeId);
        Notification<String> notify = new Notification<>("you have been appointed to manager in store: " + storeId);
        appointed.addNotification(notify);
        appointed.changeRoleInStore(new StoreManager(appointed.getId(), store), store);
    }

    public synchronized void fireManager(int ownerId, int appointedId, int storeId) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedId);
        owner.fireManager(appointed.getId(), storeId);
    }


    public synchronized void addManagerActions(int ownerId, int managerId, List<Action> actions, int storeId) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.addAction(a, storeId);
            Notification<String> notify = new Notification<>("the following action: " + a.toString() + "\n" +
                    "has been added for you for store: " + storeId);
            manager.addNotification(notify);
        }
    }


    public synchronized void removeManagerActions(int ownerId, int managerId, List<Action> actions, int storeId) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.removeAction(a, storeId);
            Notification<String> notify = new Notification<>("the following action: " + a.toString() + "\n" +
                    "has been added for you for store: " + storeId);
            manager.addNotification(notify);
        }
    }


    public synchronized void closeStore(int userId, int storeId) throws Exception {
        Member m = getActiveMember(userId);
        Set<Integer> workerIds = m.closeStore(storeId);
        for(int workerId : workerIds){
            Member worker = getMember(workerId);
            worker.changeToInActive(storeId);
            String notify = "the store: " + storeId + " has been temporarily closed";
            Notification<String> notification = new Notification<>(notify);
            addNotification(workerId, notification);
        }
    }


    public synchronized void reOpenStore(int userId, int storeId) throws Exception {
        Member m = getActiveMember(userId);
        Set<Integer> workerIds = m.reOpenStore(storeId);
        for(int workerId : workerIds){
            Member worker = getMember(workerId);
            worker.changeToActive(storeId);
            String notify = "the store: " + storeId + " has been reOpened";
            Notification<String> notification = new Notification<>(notify);
            addNotification(workerId, notification);
        }
    }

    public synchronized void checkPermission(int userId, Action action, int storeId) throws Exception {
        Member m = getActiveMember(userId);
        m.checkPermission(action, storeId);
    }

    public synchronized  Info getWorkerInformation(int userId, int workerId, int storeId) throws Exception{
        Member m = getActiveMember(userId);
        Member worker = getMember(workerId);
        if(m.getWorkerIds(storeId).contains(workerId))
            return worker.getInformation(storeId);
        else
            throw new Exception("the workerId given is not of a worker in the store");
    }

    public synchronized List<Info> getWorkersInformation(int userId, int storeId) throws Exception{
        Member m = getActiveMember(userId);
        List<Info> information = new LinkedList<>();
        Set<Integer> workerIds = m.getWorkerIds(storeId);
        for (int workerId : workerIds) {
            Member worker = getMember(workerId);
            Info info = worker.getInformation(storeId);
            information.add(info);
        }
        return information;
    }

    public synchronized String getUserEmail(int userId) throws Exception {
        Member m = getMember(userId);
        return m.getEmail();
    }


    public String getUserName(int id) throws Exception {
        Member m = getMember(id);
        return m.getName();
    }

    public void removeStoreRole(int userId, int storeId) throws Exception {
        Member m = getMember(userId);
        m.removeRoleInStore(storeId);
    }

    public List<Integer> cancelMembership(int userToRemove) throws Exception{
        Member m = getMember(userToRemove);
        Set<Integer> storeIds = m.getAllStoreIds();
        List<Integer> creatorStoreIds = new LinkedList<>();
        for(int storeId : storeIds){
            if(m.getRole(storeId).getRole() == Role.Manager)
                fireManager(userToRemove, userToRemove, storeId);
            else if(m.getRole(storeId).getRole() == Role.Owner)
                fireOwner(userToRemove, userToRemove, storeId);
            else
                creatorStoreIds.add(storeId);
        }
        memberList.remove(userToRemove);
        emailToId.remove(m.getEmail());
        return creatorStoreIds;
    }

    public List<PurchaseHistory> getUsersInformation() {
        List<PurchaseHistory> membersInformation = new LinkedList<>();
        for(Member m : memberList.values()) {
            PurchaseHistory history = m.getUserPurchaseHistory();
            membersInformation.add(history);
        }
        return membersInformation;
    }

    public void removeCart(int userId) throws Exception{
        User user = getUser(userId);
        user.emptyCart();
    }
}
