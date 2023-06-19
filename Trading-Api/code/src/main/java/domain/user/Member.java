package domain.user;

import database.daos.Dao;
import database.DbEntity;
import database.daos.SubscriberDao;
import database.dtos.CartDto;
import domain.states.StoreCreator;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import jakarta.persistence.*;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;
import utils.messageRelated.*;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;
import domain.store.storeManagement.Bid;



import java.util.*;


@Entity
@Table(name = "members")
public class Member extends Subscriber implements User{

    @Transient
    private ShoppingCart cart;
    @Transient
    private List<UserState> roles; //connection between registered to the shops

    @Transient
    private List<Bid> bids;
    @Transient
    private PurchaseHistory purchaseHistory;

    public Member(){
    }
    public Member(int id, String email, String password, String birthday) {
        super(id, email, password);
        this.birthday = birthday;
        SubscriberDao.saveSubscriber(this);
        roles = new ArrayList<>();
        purchaseHistory = new PurchaseHistory(id);
        cart = new ShoppingCart();
        bids = new ArrayList<>();
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
            SubscriberDao.saveRole(userState);
            roles.add(userState);
            return;
        }
        if (state.getRole() == userState.getRole())
            throw new Exception("the member already has this role in this store");
        SubscriberDao.saveRole(userState);
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
        SubscriberDao.saveSubscriber(this);
    }

    private void setNewBirthday(String newBirthday){
        this.birthday = newBirthday;
        SubscriberDao.saveSubscriber(this);
    }

    public void setMemberPassword(String oldPassword, String newPassword) throws Exception {
        if(password.equals(oldPassword)){
            password = newPassword;
            SubscriberDao.saveSubscriber(this);
        }
        else
            throw new Exception("wrong password entered, can't change to a new password");
    }


    public void addProductToCart(int storeId, ProductInfo product, int quantity) throws Exception{
        cart.addProductToCart(storeId, product, quantity);
        Dao.save(new CartDto(id, storeId, product.id, quantity));
    }
    public void emptyCart(){
        cart.emptyCart();
    }

    @Override
    public int getAge() {
        return StringChecks.calculateAge(birthday);
    }

    public void removeProductFromCart(int storeId, int productId) throws Exception{
        cart.removeProductFromCart(storeId, productId);
        SubscriberDao.removeCartProduct(id, storeId, productId);
    }

    public void changeQuantityInCart(int storeId, ProductInfo product, int change) throws Exception{
        cart.changeQuantityInCart(storeId, product, change);
        SubscriberDao.updateCartProduct(id, storeId, product.getId(), cart.getBasket(storeId).getProduct(product.id).getQuantity());
    }

    public List<ProductInfo> getCartContent() {
        return cart.getContent();
    }

    public void purchaseMade(Receipt receipt){
        purchaseHistory.addPurchaseMade(receipt);
        SubscriberDao.removeCart(id);
        cart.emptyCart();
    }

    public void openStore(Store store) {
        UserState creator = new StoreCreator(id, email, store);
        SubscriberDao.saveRole(creator);
        roles.add(creator);
    }

    public StoreReview writeReview(int messageId, int storeId, int orderId, String content, int grading) throws Exception{
        if (purchaseHistory.checkOrderContainsStore(orderId, storeId)) {
            return new StoreReview(messageId, content, this, orderId, storeId, grading);
        }
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public ProductReview writeReview(int messageId, int storeId, int productId, int orderId, String content, int grading) throws Exception{
        if(purchaseHistory.checkOrderContainsProduct(orderId, storeId, productId)){
            return new ProductReview(messageId, content, this, orderId, storeId, productId, grading);
        }
        else
            throw new Exception("the product isn't part of the order so you can't write a review about him");
    }

    public Complaint writeComplaint(int messageId, int orderId, String comment) throws Exception {
        if(purchaseHistory.checkOrderOccurred(orderId))
            return new Complaint(messageId, comment, this, orderId);
        else
            throw new Exception("can't write a review because the store wasn't part of the order");
    }

    public Question sendQuestion(int messageId, int storeId, String question) {
        return new Question(messageId, question, this, storeId);
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
            if (state.getStoreId() == storeId)
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
        SubscriberDao.removeRole(state.getUserId(), state.getStoreId());
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
        Info info = new Info(id, email, birthday, StringChecks.calculateAge(birthday), purchaseHistory);
        try {
            UserState state = getRole(storeId);
            info.addRole(state.getRole());
            info.addActions(state.getActions());
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

    public List<Store> getStores(){
        List<Store> ans = new ArrayList<>();
        for(UserState state : roles){
            if (state.getRole() == Role.Creator){
                ans.add(state.getStore());
            }
        }
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
            return cart;
        throw new Exception("the member is not connected");
    }


    @Override
    public LoginInformation getLoginInformation(String token){
        return new LoginInformation(token, id, email, false, displayNotifications(),getRoles(),
                getStoreNames(), getStoreImgs(), getPermissions(), getUserPurchaseHistory(), StringChecks.calculateAge(birthday), birthday,getBids());
    }
    @Override
    public void setShoppingCart(ShoppingCart cart) {this.cart = cart;}


    public void addBid(Bid bid)
    {
        bids.add(bid);
    }
    public List<Bid> getBids(){return this.bids;}

    public List<String> editBid(int bidId, int storeId, double offer, int quantity) throws Exception{
        for(Bid bid : bids)
            if(bid.getBidId() == bidId && bid.getStoreId() == storeId) {
                bid.editBid(offer, quantity);
                return bid.getApprovers();
            }
        throw new Exception("the bidId given does not belong to the member");
    }

    //database

    @Override
    public void initialParams(){
        initialNotificationsFromDb();
        getCartFromDb();
        getPurchaseHistoryFromDb();
        getStatesFromDb();
        bids = new ArrayList<>();
    }


    public void getCartFromDb(){
        if(cart == null)
            cart = SubscriberDao.getCart(id);
    }

    public void getStatesFromDb(){
        if(roles == null)
            roles = SubscriberDao.getRoles(id);
    }

    public void getPurchaseHistoryFromDb(){
        if(purchaseHistory == null)
            purchaseHistory =  SubscriberDao.getReceipts(id);
    }
}
