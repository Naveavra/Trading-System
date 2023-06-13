package database;

import database.daos.*;
import database.dtos.*;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import domain.user.Member;
import domain.user.PurchaseHistory;
import domain.user.ShoppingCart;
import domain.user.Subscriber;
import market.Admin;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;

import java.util.HashMap;
import java.util.List;

public class DbConnector {
    private MemberDao memberDao;
    private ComplaintDao complaintDao;
    private AdminDao adminDao;
    private StoreDao storeDao;

    HashMap<Integer, HashMap<Integer, Product>> storesInventory; //<storeid,<prodid, prod>>

    HashMap<Integer, PurchaseHistory> purchaseHistoryHashMap; //<memberId, purchaseHisory>
    HashMap<Integer, ShoppingCart> shoppingCartHashMap; //<userId, shoppingcart>
    HashMap<Integer, Store> storeHashMap; //<storeId, Store>
    public DbConnector(){
        memberDao = new MemberDao();
        complaintDao=  new ComplaintDao();
        adminDao = new AdminDao();
        storeDao = new StoreDao();
        storesInventory = new HashMap<>();
        purchaseHistoryHashMap = new HashMap<>();
        shoppingCartHashMap = new HashMap<>();
        storeHashMap = new HashMap<>();
    }


    public void checkSaveSubscriber(Subscriber s){
        DaoTemplate.save(s);
    }
    public Member getMemberFromDb(int memberId) throws Exception{
        MemberDto memberDto = memberDao.getMemberById(memberId);
        if(memberDto != null)
            return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getPassword(), memberDto.getBirthday());
        throw new Exception("the id does not belong to any member in db");
    }

    public Admin getAdminFromDb(int adminId) throws Exception{
        AdminDto adminDto = adminDao.getAdminById(adminId);
        if(adminDto != null) {
            MemberDto memberDto = memberDao.getMemberById(adminId);
            return new Admin(adminDto.getId(), memberDto.getEmail(), memberDto.getPassword());
        }
        throw new Exception("the id does not belong to any admin in db");
    }

    public void addNotificationsToSubscriber(Subscriber subscriber){
        List<NotificationDto> notificationDtos = memberDao.getMemberNotifications(subscriber.getId());
        for(NotificationDto n : notificationDtos){
            Notification notification = new Notification(NotificationOpcode.values()[n.getOpcode()], n.getContent());
            subscriber.addNotification(notification);
        }
    }

    public void addUserHistory(Member member) throws Exception {
        if (purchaseHistoryHashMap.containsKey(member.getId())){
            member.setPurchaseHistory(purchaseHistoryHashMap.get(member.getId()));
            return;
        }
        List<UserHistoryDto> userHistoryDtos = memberDao.getMemberHistory(member.getId());
        PurchaseHistory purchaseHistory = new PurchaseHistory(member.getId());
        for (UserHistoryDto userHistoryDto : userHistoryDtos){
            ShoppingCart cart = new ShoppingCart();
            List<ReceiptDto> receiptDtos = userHistoryDto.getReceiptDtos();
            for (ReceiptDto receiptDto : receiptDtos)
            {
                Product p = getStoreProduct(receiptDto.getStoreId(), receiptDto.getProductId());
                ProductInfo productInfo = new ProductInfo(receiptDto.getStoreId(), p, receiptDto.getQuantity());
                cart.changeQuantityInCart(receiptDto.getStoreId(), productInfo, receiptDto.getQuantity());
            }
            //purchaseHistory.addPurchaseMade(userHistoryDto.getReceiptId(), userHistoryDto.getTotalPrice(), cart);
        }
        purchaseHistoryHashMap.put(member.getId(), purchaseHistory);
        member.setPurchaseHistory( purchaseHistory);
    }



    public void addMemberCart(Member member) throws Exception{
        if (shoppingCartHashMap.containsKey(member.getId())){
            member.setShoppingCart(shoppingCartHashMap.get(member.getId()));
            return;
        }
        List<CartDto> cartDtos = memberDao.getMemberCart(member.getId());
        ShoppingCart cart = new ShoppingCart();
        for(CartDto c : cartDtos){
            cart.changeQuantityInCart(c.getStoreId(), new ProductInfo(c.getStoreId(), getStoreProduct(c.getStoreId(),
                    c.getProductId()), c.getQuantity()), c.getQuantity());
        }
        shoppingCartHashMap.put(member.getId(), cart);
        member.setShoppingCart(cart);
    }

    public Store getStore(int creatorId, int storeId) throws Exception{
        if (storeHashMap.containsKey(storeId)){
            return storeHashMap.get(storeId);
        }
        StoreDto storeDto = storeDao.getStoreById(storeId);
        if(storeDto != null){
            Store s = new Store(storeDto.getId(), storeDto.getName(), storeDto.getDescription(),
                    storeDto.getImage(), getMemberFromDb(creatorId));
            storeHashMap.put(storeId, s);
            return s;
        }
        throw new Exception("the id given does not belong to any store");
    }

    public Product getStoreProduct(int storeId, int productId) throws Exception{
        Product p;
        boolean storeExist = false;
        if (storesInventory.containsKey(storeId)) {
            storeExist = true;
            p = storesInventory.get(storeId).getOrDefault(productId, null);
            if (p!=null){return p;}
        }
        InventoryDto inventoryDto = storeDao.getStoreProduct(storeId, productId);
        if(inventoryDto != null) {
            Product product = new Product(inventoryDto.getStoreDto().getId(), inventoryDto.getProductId(), inventoryDto.getName(),
                    inventoryDto.getDescription(), inventoryDto.getImg());
            if (!storeExist){
                storesInventory.put(storeId, new HashMap<>());
            }
            storesInventory.get(storeId).put(productId, product);
            return product;
        }
        throw new Exception("the id given does not belong to any product");
    }

    public MemberDao getMemberDao() {
        return memberDao;
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public ComplaintDao getComplaintDao() {
        return complaintDao;
    }

    public void setComplaintDao(ComplaintDao complaintDao) {
        this.complaintDao = complaintDao;
    }

    public AdminDao getAdminDao() {
        return adminDao;
    }

    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    public StoreDao getStoreDao() {
        return storeDao;
    }

    public void setStoreDao(StoreDao storeDao) {
        this.storeDao = storeDao;
    }
}
