package database;

import database.daos.AdminDao;
import database.daos.ComplaintDao;
import database.daos.MemberDao;
import database.daos.StoreDao;
import database.dtos.*;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import domain.user.Member;
import domain.user.Subscriber;
import market.Admin;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;

import java.util.List;

public class DbConnector {
    private MemberDao memberDao;
    private ComplaintDao complaintDao;
    private AdminDao adminDao;
    private StoreDao storeDao;
    public DbConnector(){
        memberDao = new MemberDao();
        complaintDao=  new ComplaintDao();
        adminDao = new AdminDao();
        storeDao = new StoreDao();
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

    public void addMemberCart(Member member) throws Exception{
        List<CartDto> cartDtos = memberDao.getMemberCart(member.getId());
        for(CartDto c : cartDtos)
            member.addProductToCart(c.getStoreId(), new ProductInfo(c.getStoreId(), getStoreProducts(c.getStoreId(), c.getProductId()), c.getQuantity()), c.getQuantity());
    }

    public Store getStore(int creatorId, int storeId) throws Exception{
        StoreDto storeDto = storeDao.getStoreById(storeId);
        if(storeDto != null)
            return new Store(storeDto.getId(), storeDto.getName(), storeDto.getDescription(),
                storeDto.getImage(), getMemberFromDb(creatorId));
        throw new Exception("the id given does not belong to any store");
    }

    public Product getStoreProducts(int storeId, int productId) throws Exception{
        InventoryDto inventoryDto = storeDao.getStoreProduct(storeId, productId);
        if(inventoryDto != null)
            return new Product(inventoryDto.getProductId(), inventoryDto.getName(),
                    inventoryDto.getDescription(), inventoryDto.getImg());
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
