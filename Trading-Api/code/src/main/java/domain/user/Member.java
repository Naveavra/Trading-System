package domain.user;

import database.daos.MemberDao;
import database.dtos.MemberDto;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Message;
import utils.messageRelated.NotificationOpcode;
import utils.messageRelated.MessageState;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;


import java.util.*;

import static utils.messageRelated.NotificationOpcode.PRODUCT_REVIEW;


public class Member extends Subscriber implements User{

    private transient Guest g;
    private String birthday;

    private List<UserState> roles; //connection between registered to the shops
    private PurchaseHistory purchaseHistory;
    public Member(int id, String email, String password, String birthday){
        super(id, email, password);
        this.birthday = birthday;
        roles = new ArrayList<>();
        purchaseHistory = new PurchaseHistory(this.id);
        g = new Guest(id);
        memberDto.setBirthday(birthday);
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
    public String getBirthday(){return birthday;}

    public void setMemberAttributes(String email, String newBirthday){
        if(!email.equals("null"))
            setNewEmail(email);
        if(!newBirthday.equals("null"))
            setNewBirthday(newBirthday);
    }
    private void setNewEmail(String newEmail){
        email = newEmail;
        memberDto.setEmail(newEmail);
    }

    private void setNewBirthday(String newBirthday){
        this.birthday = newBirthday;
        memberDto.setBirthday(newBirthday);
    }

    public void setMemberPassword(String oldPassword, String newPassword) throws Exception {
        if(password.equals(oldPassword)){
            password = newPassword;
            memberDto.setPassword(newPassword);
        }
        else
            throw new Exception("wrong password entered, can't change to a new password");
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
        purchaseHistory.addPurchaseMade(orderId, totalPrice, g.getShoppingCart());
        g.emptyCart();
    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator(id, email, store);
        roles.add(creator);
    }

    public Message writeReview(int messageId, int storeId, int orderId, String content, int grading) throws Exception{
        if (purchaseHistory.checkOrderContainsStore(orderId, storeId)) {
            Message message = new Message(messageId, NotificationOpcode.STORE_REVIEW, content, this, orderId, MessageState.reviewStore);
            message.addStore(storeId);
            message.addRating(grading);
            return message;
        }
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Message writeReview(int messageId, int storeId, int productId, int orderId, String content, int grading) throws Exception{
        if(purchaseHistory.checkOrderContainsProduct(orderId, storeId, productId)){
            Message m = new Message(messageId, PRODUCT_REVIEW, content, this, orderId, MessageState.reviewProduct);
            m.addStore(storeId);
            m.addRating(grading);
            m.addProductToReview(productId);
            return m;
        }
        else
            throw new Exception("the product isn't part of the order so you can't write a review about him");
    }

    public Message writeComplaint(int messageId, int orderId, String comment) throws Exception {
        if(purchaseHistory.checkOrderOccurred(orderId))
            return new Message(messageId, NotificationOpcode.COMPLAINT, comment, this, orderId, MessageState.complaint);
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Message sendQuestion(int messageId, int storeId, String question) {
        Message m = new Message(messageId, NotificationOpcode.QUESTION, question, this, -1, MessageState.question);
        m.addStore(storeId);
        return m;
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

    @Override
    public void checkPermission(Action action, int storeId) throws Exception{
        UserState role = getActiveRole(storeId);
        if (!role.checkPermission(action))
            throw new Exception("member does not have permission for this action");
    }

    public PurchaseHistory getUserPurchaseHistory(){
        return purchaseHistory;
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
        Info info = new Info(id, email, birthday, StringChecks.calculateAge(birthday));
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


    @Override
    public LoginInformation getLoginInformation(String token) {
        return new LoginInformation(token, id, email, false, displayNotifications(),getRoles(),
                getStoreNames(), getStoreImgs(), getPermissions(), getUserPurchaseHistory(), StringChecks.calculateAge(birthday), birthday);
    }
}
