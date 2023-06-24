package service;

import database.daos.MessageDao;
import database.daos.SubscriberDao;
import domain.store.storeManagement.Store;
import domain.user.*;

import domain.user.PurchaseHistory;
import market.Admin;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    private AtomicInteger messageIds;
    private ConcurrentHashMap<Integer, Guest> guestList;
    private ConcurrentHashMap<Integer, Member> memberList;
    private ConcurrentHashMap<Integer, Admin> admins;
    private StringChecks checks;
    private ConcurrentHashMap<Integer, Complaint> complaints; //complaintId,message

    public UserController(){
        ids = new AtomicInteger(SubscriberDao.getMaxId());
        messageIds = new AtomicInteger(MessageDao.getMaxId());
        guestList = new ConcurrentHashMap<>();
        memberList = new ConcurrentHashMap<>();
        admins = new ConcurrentHashMap<>();
        checks = new StringChecks();
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

    public boolean isGuest(int guestId)
    {
        return guestList.containsKey(guestId);
    }


    public Guest getGuest(int id) throws Exception{
        if(guestList.containsKey(id))
            return guestList.get(id);
        throw new Exception("the id given does not belong to any guest");
    }

    public Member getMember(int id) throws Exception{
        if(memberList.containsKey(id))
                return memberList.get(id);
        Member m = SubscriberDao.getMember(id);
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
        Member m = SubscriberDao.getMember(email);
        if(m != null) {
            memberList.put(m.getId(), m);
            return m;
        }
        throw new Exception("no member has this email");
    }
    public boolean isEmailTaken(String email) throws Exception{
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
            m = getMember(id);
            if(m != null)
                return m;
        }catch (Exception ignored){
        }
        try{
            a = getAdmin(id);
            if(a != null)
                return a;
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

    public synchronized void register(String email, String password, String hashedPass, String birthday, Session session) throws Exception{
        checks.checkRegisterInfo(email, password, birthday);
        if(isEmailTaken(email))
                throw new Exception("the email is already taken");
        Member m = new Member(ids.getAndIncrement(), email, hashedPass, birthday);
        SubscriberDao.saveSubscriber(m, session);
        memberList.put(m.getId(), m);
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
    public synchronized void addProductToCart(int userId, int storeId, ProductInfo product, int quantity, Session session) throws Exception{
        User user = getUser(userId);
        user.addProductToCart(storeId, product, quantity, session);
    }

    //removing the productId from the user's cart
    public synchronized void removeProductFromCart(int userId, int storeId, int productId, Session session) throws Exception{
        User user = getUser(userId);
        user.removeProductFromCart(storeId, productId, session);
    }


    //adding the change quantity to the product's quantity in the user's cart
    public synchronized void changeQuantityInCart(int userId, int storeId, ProductInfo product, int change, Session session) throws Exception{
        User user = getUser(userId);
        user.changeQuantityInCart(storeId, product, change, session);

    }


    public synchronized ShoppingCart getUserCart(int userId) throws Exception{
        User user = getUser(userId);
        return user.getShoppingCart();
    }

    public synchronized void purchaseMade(int userId, Receipt receipt, Session session) throws Exception{
        User user = getUser(userId);
        user.purchaseMade(receipt, session);
    }


    public synchronized void openStore(int userId, Store store, Session session) throws Exception{
        Member m = getActiveMember(userId);
        m.openStore(store, session);
        m.addNotification(new Notification(NotificationOpcode.GET_CLIENT_DATA, "null"), session);
    }

    public synchronized StoreReview writeReviewForStore(int orderId, int storeId, String content, int grading, int userId) throws Exception {
        Member m = getActiveMember(userId);
        if(grading > 5 || grading < 0)
            throw new Exception("the rating given is not between 0 and 5");
        return m.writeReview(messageIds.getAndIncrement(), storeId, orderId, content, grading);

    }

    public synchronized ProductReview writeReviewForProduct(int orderId, int storeId, int productId, String comment, int grading, int userId) throws Exception {
        Member m = getActiveMember(userId);
        if(grading > 5 || grading < 0)
            throw new Exception("the rating given is not between 0 and 5");
        return m.writeReview(messageIds.getAndIncrement(), storeId, productId, orderId, comment, grading);
    }


    public synchronized void writeComplaintToMarket(int orderId, String comment,int userId, Session session)throws Exception{
        Member m = getActiveMember(userId);
        String notify = "a complaint has been submitted";
        Notification notification = new Notification(NotificationOpcode.GET_ADMIN_DATA, notify);
        getAdminsFromDb();
        for(Admin a : admins.values())
            a.addNotification(notification, session);
        Complaint complaint = m.writeComplaint(messageIds.getAndIncrement(), orderId, comment);
        MessageDao.saveMessage(complaint, session);
        complaints.put(complaint.getMessageId(), complaint);
    }


    public Question sendQuestionToStore(int userId, int storeId, String question) throws Exception {
        Member m = getActiveMember(userId);
        return m.sendQuestion(messageIds.getAndIncrement(), storeId, question);
    }

    public synchronized void addNotification(int userId, Notification notification, Session session) throws Exception{
        Subscriber s = getSubscriber(userId);
        s.addNotification(notification, session);
    }
    public synchronized void addNotification(String userEmail, Notification notification, Session session) throws Exception{
        Subscriber s = getSubscriber(userEmail);
        s.addNotification(notification, session);
    }

    public synchronized void addNotificationToAdmins(Notification notification, Session session) throws Exception{
        for(Admin a : admins.values())
            a.addNotification(notification, session);
    }

    public Notification getNotification(int userId, Session session) throws Exception {
        Subscriber s = getActiveSubscriber(userId);
        return s.getNotification(session);
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



    public synchronized void changeMemberAttributes(int userId, String newEmail, String newBirthday, Session session) throws Exception {
        Member m = getActiveMember(userId);
        if (!newEmail.equals("null"))
            checks.checkEmail(newEmail);

        if(!newBirthday.equals("null"))
            checks.checkBirthday(newBirthday);

        m.setMemberAttributes(newEmail, newBirthday);
        SubscriberDao.saveSubscriber(m, session);
    }

    public void changeMemberPassword(int userId, String oldPass, String newPass, String newHashedPass, Session session) throws Exception{
        Member m = getActiveMember(userId);
        if (!oldPass.equals("null")) {
            checks.checkPassword(newPass);
            m.setMemberPassword(oldPass, newHashedPass);
            SubscriberDao.saveSubscriber(m, session);
        }
    }

    //starting the functions connecting to the store
    public synchronized void appointOwner(int ownerId, String appointedEmail, int storeId, Session session) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedEmail);
        List<String> creators = owner.appointToOwner(appointed, storeId, session);
        for(String creator : creators)
            addNotification(creator, new Notification(NotificationOpcode.GET_STORE_DATA, "a new owner appointment was added to store: " + storeId),session);
        addNotification(appointed.getId(), new Notification(NotificationOpcode.GET_CLIENT_DATA, "null"),session);
    }



    public void fireIds(Set<Integer> firedIds, int storeId, Session session) throws Exception{
        for (int firedId : firedIds) {
            Member fired = getMember(firedId);
            Notification notify;
            if (fired.getRole(storeId).getRole() == Role.Owner)
               notify = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA, "you have been fired from owner in store: " + storeId);
             else
                notify = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA,
                        "you have been fired from manager in store: " + storeId);
            fired.addNotification(notify, session);
            fired.removeRoleInStore(storeId, session);
        }
    }
    public synchronized void fireOwner(int ownerId, int appointedId, int storeId, Session session) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedId);
        Set<Integer> firedIds = owner.fireOwner(appointed.getId(), storeId, session);
        fireIds(firedIds, storeId, session);
    }

    public synchronized void appointManager(int ownerId, String appointedEmail, int storeId, Session session) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedEmail);
        List<String> owners = owner.appointToManager(appointed, storeId, session);
        for(String ownerName : owners)
            addNotification(ownerName, new Notification(NotificationOpcode.GET_STORE_DATA,
                    "a new manager appointment was added to store: " + storeId), session);
    }

    public synchronized void fireManager(int ownerId, int appointedId, int storeId, Session session) throws Exception {
        Member owner = getActiveMember(ownerId);
        Member appointed = getMember(appointedId);
        Set<Integer> ids = owner.fireManager(appointed.getId(), storeId, session);
        fireIds(ids, storeId, session);
    }


    public synchronized void addManagerActions(int ownerId, int managerId, List<Action> actions, int storeId, Session session) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.addAction(a, storeId, session);
            Notification notify = new Notification(NotificationOpcode.GET_CLIENT_DATA, "the following action: " + a.toString() + "\n" +
                    "has been added for you for store: " + storeId);
            manager.addNotification(notify, session);
        }
    }


    public synchronized void removeManagerActions(int ownerId, int managerId, List<Action> actions, int storeId, Session session) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.removeAction(a, storeId, session);
            Notification notify = new Notification(NotificationOpcode.GET_CLIENT_DATA, "the following action: " + a.toString() + "\n" +
                    "has been removed from you for store: " + storeId);
            manager.addNotification(notify, session);
        }
    }

    public synchronized String changeStoreActive(int userId, int storeId, String isActive, Session session) throws Exception{
        if(isActive.equals("false")){
            closeStore(userId, storeId, session);
            return "close store was successful";
        }
        else if(isActive.equals("true")){
            reOpenStore(userId, storeId, session);
            return "reopen store was successful";
        }
        throw new Exception("the isActive given is not boolean");
    }
    public synchronized void closeStore(int userId, int storeId, Session session) throws Exception {
        Member m = getActiveMember(userId);
        Set<Integer> workerIds = m.closeStore(storeId, session);
        for(int workerId : workerIds){
            Member worker = getMember(workerId);
            worker.changeToInActive(storeId);
            String notify = "the store: " + storeId + " has been temporarily closed";
            Notification notification = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA, notify);
            addNotification(workerId, notification, session);
        }
    }


    public synchronized void reOpenStore(int userId, int storeId, Session session) throws Exception {
        Member m = getActiveMember(userId);
        Set<Integer> workerIds = m.reOpenStore(storeId, session);
        for(int workerId : workerIds){
            Member worker = getMember(workerId);
            worker.changeToActive(storeId);
            String notify = "the store: " + storeId + " has been reOpened";
            Notification notification = new Notification(NotificationOpcode.GET_CLIENT_DATA,  notify);
            addNotification(workerId, notification, session);
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

    public void removeStoreRole(int userId, int storeId, Session session) throws Exception {
        Member m = getMember(userId);
        m.removeRoleInStore(storeId, session);
    }

    public List<Integer> cancelMembership(int userToRemove, Session session) throws Exception{
        Member m = getMember(userToRemove);
        Set<Integer> storeIds = m.getAllStoreIds();
        List<Integer> creatorStoreIds = new LinkedList<>();
        for(int storeId : storeIds){
            if(m.getRole(storeId).getRole() == Role.Manager)
                fireManager(userToRemove, userToRemove, storeId, session);
            else if(m.getRole(storeId).getRole() == Role.Owner)
                fireOwner(userToRemove, userToRemove, storeId, session);
            else
                creatorStoreIds.add(storeId);
        }
        addNotification(userToRemove, new Notification(NotificationOpcode.CANCEL_MEMBERSHIP, "you have been removed from the system"), session);
        //memberList.remove(userToRemove);
        return creatorStoreIds;
    }

    public List<PurchaseHistory> getUsersInformation()throws Exception{
        List<PurchaseHistory> membersInformation = new LinkedList<>();
        getMembersFromDb();
        for(Member m : memberList.values()){
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
        Admin a = SubscriberDao.getAdmin(adminId);
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
        Admin a = SubscriberDao.getAdmin(email);
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
    public boolean checkIsAdmin(int adminId) throws Exception{
        Admin a = SubscriberDao.getAdmin(adminId);
        return a != null;
    }

    public boolean checkIsAdmin(String email) throws Exception{
        for(Admin admin : admins.values())
            if(admin.getName().equals(email))
                return true;
        Admin a = SubscriberDao.getAdmin(email);
        return a != null;
    }

    private void checkRemoveAdmin() throws Exception{
        if(getAdminSize() == 1)
            throw new Exception("the admin cannot be removed because it is the only admin in the system");
    }
    public void closeStorePermanently(int adminId, int storeId, Session session) throws Exception{
        Admin admin = getActiveAdmin(adminId);
        admin.closeStorePermanently(storeId, -1, session);
    }
    public synchronized void addAdmin(int userId, String email, String hashPass ,String pass, Session session)throws Exception {
        if(isEmailTaken(email))
            throw new Exception("the email is already taken");
        if (userId != 0)
            getActiveAdmin(userId);
        if(!checks.checkEmail(email))
            throw new Exception("the email given does not match the email pattern");
        checks.checkPassword(pass);
        Admin a = new Admin(ids.getAndIncrement(), email, hashPass);
        SubscriberDao.saveSubscriber(a, session);
        admins.put(a.getId(), a);
    }
    public void addAdmin(Admin a, String hashedPass, String pass, Session session) throws Exception{
        checks.checkPassword(pass);
        if(!checks.checkEmail(a.getName()))
            throw new Exception("admin email given was not valid");
        if(isEmailTaken(a.getName()))
            throw new Exception("the email is already taken");
        Admin admin = new Admin(a.getId(), a.getName(), hashedPass);
        admin.saveAdmin(session);
        admins.put(admin.getId(), admin);
    }

    public void removeAdmin(int adminId, Session session) throws Exception{
        getActiveAdmin(adminId);
        checkRemoveAdmin();
        admins.remove(adminId);
        SubscriberDao.removeAdmin(adminId, session);
    }

    public HashMap<Integer, Admin> getAdmins(int adminId) throws Exception{
        getActiveAdmin(adminId);
        HashMap<Integer, Admin> list = new HashMap<>();
        getAdminsFromDb();
        for (Admin a : admins.values()) {
            list.put(a.getId(), a);
        }
        return list;
    }

    public List<PurchaseHistory> getUsersPurchaseHistory(int adminId) throws Exception{
        getActiveAdmin(adminId);
        List<PurchaseHistory> users = getUsersInformation();
        return users;
    }

    private void sendFeedback(int messageId, String ans, Session session) throws Exception{
        Complaint m = getComplaint(messageId);
        if (m != null) {
            m.sendFeedback(ans, session);
        }
        else
            throw new Exception("message does not found");

    }

    public void answerComplaint(int adminId, int complaintId, String ans, Session session) throws Exception{
        getActiveAdmin(adminId);
        sendFeedback(complaintId, "you got an answer for complaint: " + complaintId + ", answer is: " + ans, session);
    }

    public void cancelMembership(int adminId, String userToRemove, Session session) throws Exception{
        Admin admin = getActiveAdmin(adminId);
        Member m = getMember(userToRemove);
        admin.cancelMembership(m.getId(), session);
    }

    public void removeUser(String userName, Session session) throws Exception{
        Member m = getMember(userName);
        memberList.remove(m.getId());
        SubscriberDao.removeMember(userName, session);
    }

    public int getAdminSize()throws Exception{
        getAdminsFromDb();
        return admins.size();
    }

    //database

    public List<Complaint> getComplaints(int userId) throws Exception{
        getActiveAdmin(userId);
        List<Complaint> complaintsDto = MessageDao.getComplaints();
        for(Complaint complaint : complaintsDto)
            if(!complaints.containsKey(complaint.getMessageId()))
                complaints.put(complaint.getMessageId(), complaint);
        return new ArrayList<>(complaints.values());
    }

    private Complaint getComplaint(int complaintId) throws Exception{
        if(complaints.containsKey(complaintId))
            return complaints.get(complaintId);
        Complaint complaint = MessageDao.getComplaint(complaintId);
        if(complaint != null)
            return complaint;
        throw new Exception("the id does not belong to any complaint");
    }

    private void getMembersFromDb()throws Exception{
        if(memberList == null) {
            memberList = new ConcurrentHashMap<>();
            List<Member> memberDto = SubscriberDao.getAllMembers();
            for (Member m : memberDto)
                if (!memberList.containsKey(m.getId()))
                    memberList.put(m.getId(), m);
        }
    }

    private void getAdminsFromDb()throws Exception{
        if(admins == null) {
            admins = new ConcurrentHashMap<>();
            List<Admin> adminsDto = SubscriberDao.getAllAdmins();
            for (Admin a : adminsDto)
                if (!admins.containsKey(a.getId()))
                    admins.put(a.getId(), a);
        }
    }

    public List<String> editBid(int userId, int bidId, int storeId, double price, int quantity) throws Exception{
        Member member = getActiveMember(userId);
        return member.editBid(bidId, storeId, price, quantity);
    }
}
