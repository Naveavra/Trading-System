package service;

import database.daos.AdminDao;
import database.daos.MemberDao;
import database.dtos.*;
import domain.store.storeManagement.Store;
import domain.user.*;

import domain.user.PurchaseHistory;
import market.Admin;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.ProductInfo;
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
    private MemberDao memberDao;
    private AdminDao adminDao;

    public UserController(){
        ids = new AtomicInteger(2);
        guestList = new ConcurrentHashMap<>();
        memberList = new ConcurrentHashMap<>();
        admins = new ConcurrentHashMap<>();
        checks = new StringChecks();
        messageIds = 0;
        complaints = new ConcurrentHashMap<>();
        memberDao = new MemberDao();
        adminDao = new AdminDao();
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
        for(Member m : memberList.values())
            if(m.getName().equals(email))
                return m;
        throw new Exception("no member has this email");
    }
    public boolean isEmailTaken(String email){
        try {
            return getMember(email) != null || checkIsAdmin(email);
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
        if(admins.containsKey(id))
            return admins.get(id);
        else if(memberList.containsKey(id))
            return memberList.get(id);
        throw new Exception("the id given does not belong to any user");
    }
    public Subscriber getSubscriber(String email) throws Exception{
        for(Member m : memberList.values())
            if(m.getName().equals(email))
                return m;
        for(Admin a : admins.values())
            if(a.getName().equals(email))
                return a;
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

    public Notification getNotification(int userId) throws Exception {
        if(memberList.containsKey(userId)) {
            if (memberList.get(userId).getIsConnected()) {
                return memberList.get(userId).getNotification();
            }
            throw new Exception("the id given belongs to an inactive member");
        }
        throw new Exception("the id given does not belong to any member");
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

    public synchronized void purchaseMade(int userId, int orderId, double totalPrice) throws Exception{
        User user = getUser(userId);
        user.purchaseMade(orderId, totalPrice);
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
        messageIds += 2;
        return m.writeReview(tmp, storeId, productId, orderId, comment, grading);
    }


    public synchronized void writeComplaintToMarket(int orderId, String comment,int userId)throws Exception{
        Member m = getActiveMember(userId);
        int tmp = messageIds;
        messageIds++;
        String notify = "a complaint has been submitted";
        Notification notification = new Notification(NotificationOpcode.COMPLAINT, notify);
        for(Admin a : admins.values())
            a.addNotification(notification);
        Complaint complaint = m.writeComplaint(tmp, orderId, comment);
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
        Notification<String> notify = new Notification<>(NotificationOpcode.APPOINT_OWNER, "you have been appointed to owner in store: " + storeId);
        appointed.addNotification(notify);
    }



    public void fireIds(Set<Integer> firedIds, int storeId) throws Exception{
        for (int firedId : firedIds) {
            Member fired = getMember(firedId);
            Notification<String> notify;
            if (fired.getRole(storeId).getRole() == Role.Owner)
               notify = new Notification<>(NotificationOpcode.FIRE_OWNER, "you have been fired from owner in store: " + storeId);
             else
                notify = new Notification<>(NotificationOpcode.FIRE_MANAGER, "you have been fired from manager in store: " + storeId);
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
        Notification<String> notify = new Notification<>(NotificationOpcode.APPOINT_MANAGER, "you have been appointed to manager in store: " + storeId);
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
            Notification<String> notify = new Notification<>(NotificationOpcode.ADD_MANGER_PERMISSTION, "the following action: " + a.toString() + "\n" +
                    "has been added for you for store: " + storeId);
            manager.addNotification(notify);
        }
    }


    public synchronized void removeManagerActions(int ownerId, int managerId, List<Action> actions, int storeId) throws Exception {
        getActiveMember(ownerId);
        Member manager = getMember(managerId);
        for(Action a : actions) {
            manager.removeAction(a, storeId);
            Notification<String> notify = new Notification<>(NotificationOpcode.REMOVE_MANGER_PERMISSTION, "the following action: " + a.toString() + "\n" +
                    "has been added for you for store: " + storeId);
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
            Notification<String> notification = new Notification<>(NotificationOpcode.CLOSE_STORE, notify);
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
            Notification<String> notification = new Notification<>(NotificationOpcode.OPEN_STORE,  notify);
            addNotification(workerId, notification);
        }
    }

    public synchronized void checkPermission(int userId, Action action, int storeId) throws Exception {
        Subscriber s = getActiveSubscriber(userId);
        s.checkPermission(action, storeId);
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
        memberList.remove(userToRemove);
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

    //admin functions
    public Admin getAdmin(int adminId) throws Exception {
        if(admins.containsKey(adminId))
                return admins.get(adminId);
        throw new Exception("the id given does not belong to any admin");
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
        return admins.containsKey(adminId);
    }

    public boolean checkIsAdmin(String email){
        for(Admin a : admins.values())
            if(a.checkEmail(email))
                return true;
        return false;
    }

    private void checkRemoveAdmin() throws Exception{
        if(admins.size() == 1)
            throw new Exception("the admin cannot be removed because it is the only admin in the system");
    }
    public void closeStorePermanently(int adminId, int storeId) throws Exception{
        Admin admin = getActiveAdmin(adminId);
        admin.closeStorePermanently(storeId, -1);
    }
    public synchronized Admin addAdmin(int userId, String email, String pass)throws Exception {
        if(isEmailTaken(email))
            throw new Exception("the email is already taken");
        if (userId != 0)
            getActiveAdmin(userId);
        if(checks.checkEmail(email))
            throw new Exception("the email given does not match the email pattern");
        checks.checkPassword(pass);
        Admin a = new Admin(ids.getAndIncrement(), email, pass);
        admins.put(a.getId(), a);
        return a;
    }
    public Admin addAdmin(Admin a, String pass) {
        Admin admin = new Admin(a.getId(), a.getName(), pass);
        admins.put(a.getId(), admin);
        return admin;
    }

    public void removeAdmin(int adminId) throws Exception{
        getActiveAdmin(adminId);
        checkRemoveAdmin();
        admins.remove(adminId);
    }

    public HashMap<Integer, Admin> getAdmins(int adminId) throws Exception{
        getActiveAdmin(adminId);
        HashMap<Integer, Admin> list = new HashMap<>();
        for (int key : admins.keySet())
            list.put(key, admins.get(key));
        return list;
    }

    public List<PurchaseHistory> getUsersPurchaseHistory(int adminId) throws Exception{
        getActiveAdmin(adminId);
        List<PurchaseHistory> users = getUsersInformation();
        return users;
    }

    private void sendFeedback(int messageId, String ans) throws Exception{
        Complaint m = complaints.get(messageId);
        if (m != null)
            m.sendFeedback(ans);
        else
            throw new Exception("message does not found");

    }

    public void answerComplaint(int adminId, int complaintId, String ans) throws Exception{
        getActiveAdmin(adminId);
        sendFeedback(complaintId, ans);
    }

    public void cancelMembership(int adminId, String userToRemove) throws Exception{
        Admin admin = getActiveAdmin(adminId);
        Member m = getMember(userToRemove);
        admin.cancelMembership(m.getId());
    }

    public int getAdminSize() {
        return admins.size();
    }

    //database

    //users
    public void saveMemberState(int userId) throws Exception{
        Member m = getMember(userId);
        memberDao.saveMember(m.getDto());
    }
    public void updateMemberState(int userId) throws Exception{
        Member m = getMember(userId);
        memberDao.updateMember(m.getDto());
    }

    public MemberDto getMemberDto(int id){
        MemberDto m = memberDao.getMemberById(id);
        return m;
    }

    public List<CartDto> getMemberCartDto(int id){
        List<CartDto> clist = memberDao.getMemberCart(id);
        return clist;
    }

    public List<UserHistoryDto> getMemberUserHistoryDto(int id){
        List<UserHistoryDto> ulist = memberDao.getMemberHistory(id);
        return ulist;
    }

    public List<NotificationDto> getSubscriberNotificationsDto(int id){
        List<NotificationDto> nlist = memberDao.getMemberNotifications(id);
        return nlist;
    }



    //admins
    public void saveAdminState(int userId) throws Exception{
        Admin a = getAdmin(userId);
        adminDao.saveAdmin(a.getAdminDto());
        memberDao.saveMember(a.getDto());
    }
    public void updateAdminState(int userId) throws Exception{
        Admin a = getAdmin(userId);
        adminDao.updateAdmin(a.getAdminDto());
        memberDao.updateMember(a.getDto());
    }

    public AdminDto getAdminDto(int id){
        AdminDto a = adminDao.getAdminById(id);
        return a;
    }

    public void saveState() throws Exception{
        for(Member m : memberList.values())
            saveMemberState(m.getId());
        for(Admin a : admins.values())
            saveAdminState(a.getId());

    }

    public void updateState() throws Exception{
        for(Member m : memberList.values())
            updateMemberState(m.getId());
        for(Admin a : admins.values())
            updateAdminState(a.getId());
    }

    public List<Complaint> getComplaints(int userId) throws Exception{
        getActiveAdmin(userId);
        return new ArrayList<>(complaints.values());
    }
}
