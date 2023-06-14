package service;

import database.Dao;
import domain.store.storeManagement.Store;
import domain.user.*;

import domain.user.PurchaseHistory;
import market.Admin;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;
import utils.messageRelated.*;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class UserController {

    private AtomicInteger ids;
    private ConcurrentHashMap<Integer, Guest> guestList;
    private ConcurrentHashMap<Integer, Member> memberList;
    private ConcurrentHashMap<Integer, Admin> admins;
    private StringChecks checks;
    private int messageIds;
    private ConcurrentHashMap<Integer, Complaint> complaints; //complaintId,message

    public UserController(){
        ids = new AtomicInteger(2);
        guestList = new ConcurrentHashMap<>();
        memberList = new ConcurrentHashMap<>();
        admins = new ConcurrentHashMap<>();
        checks = new StringChecks();
        messageIds = 0;
        complaints = new ConcurrentHashMap<>();
    }


    public User getUser(int id) throws Exception{
        if(guestList.containsKey(id))
            return guestList.get(id);
        Member m = getMember(id);
        if(m != null)
            return m;
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
        Member m = (Member) Dao.getById(Member.class, id);
        if(m != null) {
            memberList.put(m.getId(), m);
            return m;
        }
        throw new Exception("the id given does not belong to any member");
    }

    public Member getMember(String email) throws Exception{
        for(Member m : memberList.values())
            if(m.getName().equals(email))
                return m;
        Member m = (Member) Dao.getByParam(Member.class,"Member", String.format("email = '%s' ", email));
        if(m != null) {
            memberList.put(m.getId(), m);
            return m;
        }
        throw new Exception("no member has this email");
    }
    public boolean isEmailTaken(String email){
        if(checkIsAdmin(email))
            return true;
        try {
            return getMember(email) != null;
        }catch (Exception e){
            return false;
        }
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

    public Subscriber getSubscriber(int id) throws Exception{
        Admin a = null;
        Member m = null;
        try {
            a = getAdmin(id);
            if(a != null)
                return a;
        }catch (Exception ignored){
        }
        try{
            m = getMember(id);
            if(m != null)
                return m;
        }catch (Exception e){
        }
        throw new Exception("the id given does not belong to any user");
    }
    public Subscriber getSubscriber(String email) throws Exception{
        Member m = null;
        Admin a = null;
        try {
            m = getMember(email);
            if (m != null)
                return m;
        }catch (Exception ignored){
        }
        try{
            a = getAdmin(email);
            if(a != null)
                return a;
        }catch (Exception ignored){
        }
        throw new Exception("no user has this email");
    }

    public Subscriber getActiveSubscriber(int id) throws Exception{
        if(memberList.containsKey(id)) {
            if (memberList.get(id).getIsConnected()) {
                return memberList.get(id);
            }
            throw new Exception("the id given belongs to an inactive member");
        }
        if(admins.containsKey(id)){
            if (admins.get(id).getIsConnected()) {
                return admins.get(id);
            }
            throw new Exception("the id given belongs to an inactive admin");
        }
        throw new Exception("the id given does not belong to any member");
    }

    public synchronized int enterGuest(){
        int id = ids.getAndIncrement();
        Guest g = new Guest(id);
        guestList.put(id, g);
        return g.getId();
    }


    public void exitGuest(int id) throws Exception {
        Guest g = getGuest(id);
        g.emptyCart();
        guestList.remove(id);
    }

    public synchronized void register(String email, String password, String hashedPass, String birthday) throws Exception{
        int id = ids.getAndIncrement();
        checks.checkRegisterInfo(email, password, birthday);
        if(isEmailTaken(email))
                throw new Exception("the email is already taken");
        Member m = new Member(id, email, hashedPass, birthday);
        Dao.save(m);
        memberList.put(id, m);
    }

    //the answers given are for the security questions, if there are no security questions then put an empty list
    public synchronized int login(String email, String password) throws Exception{
        Subscriber s = getSubscriber(email);
        s.login(password);
        return s.getId();
    }

    public LoginInformation getLoginInformation(int memberId, String token) throws Exception{
        Subscriber s = getSubscriber(memberId);
        return s.getLoginInformation(token);

    }

    //when logging out returns to main menu as guest
    public synchronized void logout(int memberId) throws Exception{
        try {
            Subscriber s = getActiveSubscriber(memberId);
            s.disconnect();
        }catch (Exception e){
            exitGuest(memberId);
        }
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

    public synchronized void purchaseMade(int userId, Receipt receipt) throws Exception{
        User user = getUser(userId);
        user.purchaseMade(receipt);
    }


    public synchronized void openStore(int userId, Store store) throws Exception{
        Member m = getActiveMember(userId);
        m.openStore(store);
    }

    public synchronized StoreReview writeReviewForStore(int orderId, int storeId, String content, int grading, int userId) throws Exception {
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        return m.writeReview(tmp, storeId, orderId, content, grading);

    }

    public synchronized ProductReview writeReviewForProduct(int orderId, int storeId, int productId, String comment, int grading, int userId) throws Exception {
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds ++;
        return m.writeReview(tmp, storeId, productId, orderId, comment, grading);
    }


    public synchronized void writeComplaintToMarket(int orderId, String comment,int userId)throws Exception{
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        String notify = "a complaint has been submitted";
        Notification notification = new Notification(NotificationOpcode.GET_ADMIN_DATA, notify);
        for(Admin a : getAdmins())
            a.addNotification(notification);
        Complaint complaint = m.writeComplaint(tmp, orderId, comment);
        Dao.save(complaint);
        complaints.put(complaint.getMessageId(), complaint);
    }


    public Question sendQuestionToStore(int userId, int storeId, String question) throws Exception {
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        return m.sendQuestion(tmp, storeId, question);
    }

    public synchronized void addNotification(int userId, Notification notification) throws Exception{
        Subscriber s = getSubscriber(userId);
        s.addNotification(notification);
    }
    public synchronized void addNotification(String userEmail, Notification notification) throws Exception{
        Subscriber s = getSubscriber(userEmail);
        s.addNotification(notification);
    }

    public Notification getNotification(int userId) throws Exception {
        Subscriber s = getActiveSubscriber(userId);
        return s.getNotification();
    }
    public synchronized List<Notification> displayNotifications(int userId) throws Exception{
        Subscriber s = getActiveSubscriber(userId);
        return s.displayNotifications();
    }



    public PurchaseHistory getUserPurchaseHistory(int userId, int buyerId) throws Exception{
        if(checkIsAdmin(userId))
            return getMember(buyerId).getUserPurchaseHistory();
        return getActiveMember(buyerId).getUserPurchaseHistory();
    }



    public synchronized void changeMemberAttributes(int userId, String newEmail, String newBirthday) throws Exception {
        Member m = getActiveMember(userId);
        if (!newEmail.equals("null"))
            checks.checkEmail(newEmail);

        if(!newBirthday.equals("null"))
            checks.checkBirthday(newBirthday);

        m.setMemberAttributes(newEmail, newBirthday);
    }

    public void changeMemberPassword(int userId, String oldPass, String newPass, String newHashedPass) throws Exception{
        Member m = getActiveMember(userId);
        if (!oldPass.equals("null")) {
            checks.checkPassword(newPass);
            m.setMemberPassword(oldPass, newHashedPass);
        }
    }

    //starting the functions connecting to the store
    public synchronized void appointOwner(int ownerId, String appointedEmail, int storeId) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedEmail);
        owner.appointToOwner(appointed, storeId);
        Notification notify = new Notification(NotificationOpcode.GET_STORE_DATA, "you have been appointed to owner in store: " + storeId);
        appointed.addNotification(notify);
    }



    public void fireIds(Set<Integer> firedIds, int storeId) throws Exception{
        for (int firedId : firedIds) {
            Member fired = getMember(firedId);
            Notification notify;
            if (fired.getRole(storeId).getRole() == Role.Owner)
               notify = new Notification(NotificationOpcode.GET_STORE_DATA, "you have been fired from owner in store: " + storeId);
             else
                notify = new Notification(NotificationOpcode.GET_STORE_DATA, "you have been fired from manager in store: " + storeId);
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
        owner.appointToManager(appointed, storeId);
        Notification notify = new Notification(NotificationOpcode.GET_STORE_DATA, "you have been appointed to manager in store: " + storeId);
        appointed.addNotification(notify);
    }

    public synchronized void fireManager(int ownerId, int appointedId, int storeId) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedId);
        Set<Integer> ids = owner.fireManager(appointed.getId(), storeId);
        fireIds(ids, storeId);
    }


    public synchronized void addManagerActions(int ownerId, int managerId, List<Action> actions, int storeId) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.addAction(a, storeId);
            Notification notify = new Notification(NotificationOpcode.GET_STORE_DATA, "the following action: " + a.toString() + "\n" +
                    "has been added for you for store: " + storeId);
            manager.addNotification(notify);
        }
    }


    public synchronized void removeManagerActions(int ownerId, int managerId, List<Action> actions, int storeId) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.removeAction(a, storeId);
            Notification notify = new Notification(NotificationOpcode.GET_STORE_DATA, "the following action: " + a.toString() + "\n" +
                    "has been removed from you for store: " + storeId);
            manager.addNotification(notify);
        }
    }

    public synchronized String changeStoreActive(int userId, int storeId, String isActive) throws Exception{
        if(isActive.equals("false")){
            closeStore(userId, storeId);
            return "close store was successful";
        }
        else if(isActive.equals("true")){
            reOpenStore(userId, storeId);
            return "reopen store was successful";
        }
        throw new Exception("the isActive given is not boolean");
    }
    public synchronized void closeStore(int userId, int storeId) throws Exception {
        Member m = getActiveMember(userId);
        Set<Integer> workerIds = m.closeStore(storeId);
        for(int workerId : workerIds){
            Member worker = getMember(workerId);
            worker.changeToInActive(storeId);
            String notify = "the store: " + storeId + " has been temporarily closed";
            Notification notification = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA, notify);
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
            Notification notification = new Notification(NotificationOpcode.GET_CLIENT_DATA,  notify);
            addNotification(workerId, notification);
        }
    }

    public synchronized void checkPermission(int userId, Action action, int storeId) throws Exception {
        Subscriber s = getActiveSubscriber(userId);
        s.checkPermission(action, storeId);
    }

    public synchronized Info getWorkerInformation(int userId, int workerId, int storeId) throws Exception{
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
        Subscriber s = getSubscriber(userId);
        return s.getName();
    }


    public String getUserName(int id){
        try {
            Subscriber s = getSubscriber(id);
            return s.getName();
        }catch (Exception e) {
            try {
                User user = getUser(id);
                return user.getName();
            } catch (Exception e2) {
                return "illegal id" + id;
            }
        }
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
        addNotification(userToRemove, new Notification(NotificationOpcode.CANCEL_MEMBERSHIP, "you have been removed from the system"));
        //memberList.remove(userToRemove);
        return creatorStoreIds;
    }

    public List<PurchaseHistory> getUsersInformation() {
        List<PurchaseHistory> membersInformation = new LinkedList<>();
        for(Member m : (List<Member>) Dao.getAllInTable("Member")){
            PurchaseHistory history = m.getUserPurchaseHistory();
            membersInformation.add(history);
        }
        return membersInformation;
    }

    public void removeCart(int userId) throws Exception{
        User user = getUser(userId);
        user.emptyCart();
    }

    //admin functions
    public Admin getAdmin(int adminId) throws Exception {
        if(admins.containsKey(adminId))
                return admins.get(adminId);
        Admin a = (Admin) Dao.getById(Admin.class, adminId);
        if(a != null) {
            admins.put(a.getId(), a);
            return a;
        }
        throw new Exception("the id given does not belong to any admin");
    }
    public Admin getAdmin(String email) throws Exception{
        for(Admin a : admins.values())
            if(a.getName().equals(email))
                return a;
        Admin a = (Admin) Dao.getByParam(Member.class,"Admin", String.format("email = '%s' ", email));
        if(a != null) {
            admins.put(a.getId(), a);
            return a;
        }
        throw new Exception("no admin has this name");
    }
    public Admin getActiveAdmin(int adminId) throws Exception {
        if(admins.containsKey(adminId)) {
            if (admins.get(adminId).getIsConnected())
                return admins.get(adminId);
            throw new Exception("the id given belongs to an inActive admin");
        }
        throw new Exception("the id given does not belong to any admin");
    }

    //check if admin
    public boolean checkIsAdmin(int adminId){
        Admin a = (Admin) Dao.getById(Admin.class, adminId);
        return a != null;
    }

    public boolean checkIsAdmin(String email){
        Admin a = (Admin) Dao.getByParam(Admin.class, "Admin", String.format("email = '%s'", email));
        return a != null;
    }

    private void checkRemoveAdmin() throws Exception{
        if(getAdminSize() == 1)
            throw new Exception("the admin cannot be removed because it is the only admin in the system");
    }
    public void closeStorePermanently(int adminId, int storeId) throws Exception{
        Admin admin = getActiveAdmin(adminId);
        admin.closeStorePermanently(storeId, -1);
    }
    public synchronized void addAdmin(int userId, String email, String hashPass ,String pass)throws Exception {
        if(isEmailTaken(email))
            throw new Exception("the email is already taken");
        if (userId != 0)
            getActiveAdmin(userId);
        if(!checks.checkEmail(email))
            throw new Exception("the email given does not match the email pattern");
        checks.checkPassword(pass);
        Admin a = new Admin(ids.getAndIncrement(), email, hashPass);
        Dao.save(a);
        admins.put(a.getId(), a);
    }
    public void addAdmin(Admin a, String pass) {
        Admin admin = new Admin(a.getId(), a.getName(), pass);
        Dao.save(admin);
        admins.put(a.getId(), admin);
    }

    public void removeAdmin(int adminId) throws Exception{
        getActiveAdmin(adminId);
        checkRemoveAdmin();
        admins.remove(adminId);
        Dao.removeIf("Admin", String.format("id = %d", adminId));
    }

    private List<Admin> getAdmins(){
        List<Admin> list = new ArrayList<>();
        for (Admin a : (List<Admin>)Dao.getAllInTable("Admin")) {
            list.add(a);
        }
        return list;
    }

    public HashMap<Integer, Admin> getAdmins(int adminId) throws Exception{
        getActiveAdmin(adminId);
        HashMap<Integer, Admin> list = new HashMap<>();
        for (Admin a : (List<Admin>) Dao.getAllInTable("Admin")) {
            list.put(a.getId(), a);
        }
        return list;
    }

    public List<PurchaseHistory> getUsersPurchaseHistory(int adminId) throws Exception{
        getActiveAdmin(adminId);
        List<PurchaseHistory> users = getUsersInformation();
        return users;
    }

    private void sendFeedback(int messageId, String ans) throws Exception{
        Complaint m = getComplaint(messageId);
        if (m != null) {
            m.sendFeedback(ans);
            Dao.update(m);
        }
        else
            throw new Exception("message does not found");

    }

    public void answerComplaint(int adminId, int complaintId, String ans) throws Exception{
        getActiveAdmin(adminId);
        sendFeedback(complaintId, "you got an answer for complaint: " + complaintId + ", answer is: " + ans);
    }

    public void cancelMembership(int adminId, String userToRemove) throws Exception{
        Admin admin = getActiveAdmin(adminId);
        Member m = getMember(userToRemove);
        admin.cancelMembership(m.getId());
    }

    public void removeUser(String userName) throws Exception{
        Member m = getMember(userName);
        memberList.remove(m.getId());
        Dao.removeIf("Member", String.format("email = %s", userName));
    }

    public int getAdminSize() {
        return Dao.getAllInTable("Admin").size();
    }

    //database

    public List<Complaint> getComplaints(int userId) throws Exception{
        getActiveAdmin(userId);

        return new ArrayList<>((List<Complaint>)Dao.getAllInTable("Complaint"));
    }

    private Complaint getComplaint(int complaintId) throws Exception{
        if(complaints.containsKey(complaintId))
            return complaints.get(complaintId);
        Complaint complaint = (Complaint) Dao.getById(Complaint.class, complaintId);
        if(complaint != null)
            return complaint;
        throw new Exception("the id does not belong to any complaint");
    }

}
