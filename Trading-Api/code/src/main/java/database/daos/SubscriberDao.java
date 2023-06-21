package database.daos;

import database.DbEntity;
import database.HibernateUtil;
import database.dtos.CartDto;
import domain.states.UserState;
import domain.store.product.Product;
import domain.user.Member;
import domain.user.PurchaseHistory;
import domain.user.ShoppingCart;
import domain.user.Subscriber;
import market.Admin;
import org.hibernate.Session;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;
import utils.messageRelated.Notification;

import java.util.*;

public class SubscriberDao {
    private static HashMap<Integer, Member> membersMap = new HashMap<>();
    private static boolean members = false;
    private static HashMap<Integer, Admin> adminsMap = new HashMap<>();
    private static boolean admins = false;
    private static Set<Integer> notifications = new HashSet<>();
    private static HashMap<Integer, HashMap<Integer, UserState>> rolesMap = new HashMap<>();
    private static Set<Integer> roles = new HashSet<>();
    private static HashMap<Integer, ShoppingCart> cartsMap = new HashMap<>();
    private static Set<Integer> carts = new HashSet<>();
    private static HashMap<Integer, PurchaseHistory> purchasesMap = new HashMap<>();
    private static Set<Integer> purchases = new HashSet<>();

    public static void saveSubscriber(Subscriber s, Session session) throws Exception{
        Dao.save(s, session);
    }

    public static Member getMember(int id) throws Exception{
        if(membersMap.containsKey(id))
            return membersMap.get(id);
        Member m = (Member) Dao.getById(Member.class, id);
        if(m != null) {
            membersMap.put(m.getId(), m);
            m.initialParams();
        }
        return m;
    }

    public static Member getMember(String name) throws Exception{
        for(Member m : membersMap.values())
            if(m.getName().equals(name))
                return m;
        Member m = (Member) Dao.getByParam(Member.class, "Member", String.format("email = '%s' ", name));
        if(m != null) {
            membersMap.put(m.getId(), m);
            m.initialParams();
        }
        return m;

    }

    public static List<Member> getAllMembers() throws Exception{
        if(!members) {
            List<? extends DbEntity> memberDto = Dao.getAllInTable("Member");
            for (Member m : (List<Member>) memberDto)
                if(membersMap.containsKey(m.getId())) {
                    membersMap.put(m.getId(), m);
                    m.initialParams();
                }
            members = true;
        }
        return new ArrayList<>(membersMap.values());
    }

    public static void removeMember(int id, Session session) throws Exception{
        Dao.removeIf("Member", String.format("id = %d", id), session);
        membersMap.remove(id);
        removeRoles(id, session);
        MessageDao.removeMemberMessage(id, session);
        removeNotifications(id, session);
        removeCart(id, session);
        removePurchases(id, session);
    }

    public static void removeMember(String name, Session session) throws Exception{
        int id = -1;
        for(Member m : membersMap.values())
            if(m.getName().equals(name))
                id = m.getId();
        membersMap.remove(id);
        Dao.removeIf("Member", String.format("email = '%s' ", name), session);
        removeRoles(id, session);
        MessageDao.removeMemberMessage(id, session);
        removeNotifications(id, session);
        removeCart(id, session);
        removePurchases(id, session);

    }

    public static Admin getAdmin(int id) throws Exception{
        if(adminsMap.containsKey(id))
            return adminsMap.get(id);
        Admin a = (Admin) Dao.getById(Admin.class, id);
        if(a != null) {
            adminsMap.put(a.getId(), a);
            a.initialParams();
        }
        return a;
    }

    public static Admin getAdmin(String name) throws Exception{
        for(Admin a : adminsMap.values())
            if(a.getName().equals(name))
                return a;
        Admin a = (Admin) Dao.getByParam(Admin.class, "Admin", String.format("email = '%s' ", name));
        if(a != null) {
            adminsMap.put(a.getId(), a);
            a.initialParams();
        }
        return a;
    }

    public static List<Admin> getAllAdmins() throws Exception{
        if(!admins) {
            List<? extends DbEntity> adminDto = Dao.getAllInTable("Admin");
            for (Admin a : (List<Admin>) adminDto)
                if(!adminsMap.containsKey(a.getId())) {
                    adminsMap.put(a.getId(), a);
                    a.initialParams();
                }
            admins = true;
        }
        return new ArrayList<>(adminsMap.values());
    }

    public static void removeAdmin(int id, Session session) throws Exception{
        Dao.removeIf("Admin", String.format("id = %d", id), session);
        membersMap.remove(id);
    }

    public static void removeAdmin(String name, Session session) throws Exception{
        Dao.removeIf("Admin", String.format("email = '%s' ", name), session);
        for(Admin a : adminsMap.values())
            if(a.getName().equals(name))
                adminsMap.remove(a.getId());
    }

    //notifications

    public static void saveNotification(Notification n, Session session) throws Exception{
        Dao.save(n, session);
    }
    public static List<Notification> getNotifications(int subId) throws Exception{
        if(!notifications.contains(subId)) {
            List<? extends DbEntity> notifics = Dao.getListById(Notification.class, subId, "Notification", "subId");
            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                Dao.removeIf("Notification", String.format("subId = %d", subId), session);
            }
            notifications.add(subId);
            return (List<Notification>) notifics;
        }
        return new ArrayList<>();
    }

    public static void removeNotification(int id, Session session) throws Exception{
        Dao.removeIf("Notification", String.format("id = %d", id), session);
    }

    public static void removeNotifications(int subId, Session session) throws Exception{
        Dao.removeIf("Notification", String.format("subId = %d", subId), session);
    }

    //roles

    public static void saveRole(UserState state, Session session) throws Exception{
        Dao.save(state, session);
    }

    public static UserState getRole(int userId, int storeId) throws Exception{
        if(rolesMap.containsKey(userId))
            if(rolesMap.get(userId).containsKey(storeId))
                return rolesMap.get(userId).get(storeId);
        UserState state = (UserState) Dao.getByParam(UserState.class, "UserState",
                String.format("userId = %d AND storeId = %d", userId, storeId));
        if(state != null){
            if(!rolesMap.containsKey(userId))
                rolesMap.put(userId, new HashMap<>());
            rolesMap.get(userId).put(storeId, state);
            state.initialParams();
        }
        return state;
    }

    public static List<UserState> getRoles(int userId) throws Exception{
        if(!roles.contains(userId)){
            if(!rolesMap.containsKey(userId))
                rolesMap.put(userId, new HashMap<>());
            List<? extends DbEntity> rolesTmp = Dao.getListById(UserState.class, userId, "UserState", "userId");
            for(UserState state : (List<UserState>) rolesTmp)
                if(!rolesMap.get(userId).containsKey(state.getStoreId())) {
                    rolesMap.get(userId).put(state.getStoreId(), state);
                    state.initialParams();
                }
            roles.add(userId);
        }
        return new ArrayList<>(rolesMap.get(userId).values());
    }

    public static void removeRole(int userId, int storeId, Session session) throws Exception{
        Dao.removeIf("UserState", String.format("userId = %d AND storeId = %d", userId, storeId), session);
        if(rolesMap.containsKey(userId))
            rolesMap.get(userId).remove(storeId);
    }

    private static void removeRoles(int userId, Session session) throws Exception{
        Dao.removeIf("UserState", String.format("userId = %d", userId), session);
        rolesMap.remove(userId);
        roles.remove(userId);
    }

    //carts
    public static void saveCartProduct(CartDto p, Session session) throws Exception{
        Dao.save(p, session);
    }

    public static void updateCartProduct(int userId, int storeId, int productId, int quantity, Session session) throws Exception{
        String sets = String.format("quantity = %d", quantity);
        String conditions = String.format("memberId = %d AND storeId = %d AND productId = %d", userId, storeId, productId);
        Dao.updateFor("CartDto", sets, conditions, session);
    }

    public static ProductInfo getCartProduct(int userId, int storeId, int productId) throws Exception{
        if(cartsMap.containsKey(userId))
            if(cartsMap.get(userId).hasProduct(storeId, productId))
                return cartsMap.get(userId).getBasket(storeId).getProduct(productId);

        CartDto p = (CartDto) Dao.getByParam(CartDto.class, "CartDto",
                String.format("memberId = %d AND storeId = %d AND productId = %d", userId, storeId, productId));
        ProductInfo productInfo = null;
        if(p != null){
            if(!cartsMap.containsKey(userId))
                cartsMap.put(userId, new ShoppingCart());
            Product product = StoreDao.getProduct(p.getStoreId(), p.getProductId());
            if(product != null) {
                productInfo = product.getProductInfo();
                cartsMap.get(userId).changeQuantityInCart(p.getStoreId(), productInfo, p.getQuantity());
            }
        }
        return productInfo;
    }

    public static void removeCartProduct(int userId, int storeId, int productId, Session session) throws Exception{
        Dao.removeIf("CartDto", String.format("memberId = %d AND storeId = %d AND productId = %d",
                userId, storeId, productId), session);
        if(cartsMap.containsKey(userId))
            cartsMap.get(userId).removeProductFromCart(storeId, productId);
    }

    public static ShoppingCart getCart(int userId) throws Exception{
        if(!carts.contains(userId)){
            if(!cartsMap.containsKey(userId))
                cartsMap.put(userId, new ShoppingCart());
            List<? extends DbEntity> dtos = Dao.getListById(CartDto.class, userId, "CartDto", "memberId");
            for(CartDto cartDto : (List<CartDto>) dtos) {
                if (!cartsMap.get(userId).hasProduct(cartDto.getStoreId(), cartDto.getProductId())) {
                    Product product = StoreDao.getProduct(cartDto.getStoreId(), cartDto.getProductId());
                    cartsMap.get(userId).addProductToCart(cartDto.getStoreId(), product.getProductInfo(), cartDto.getQuantity());
                }
            }
            carts.add(userId);
        }
        return cartsMap.get(userId);
    }

    public static void removeCart(int userId, Session session) throws Exception{
        Dao.removeIf("CartDto", String.format("memberId = %d", userId), session);
        cartsMap.remove(userId);
        carts.remove(userId);
    }


    //receipts
    public static void saveReceipt(Receipt receipt, Session session) throws Exception{
        Dao.save(receipt, session);
    }

    public static Receipt getReceipt(int orderId) throws Exception{
        Receipt receipt = (Receipt) Dao.getById(Receipt.class, orderId);
        if(receipt!= null) {
            if(!purchasesMap.containsKey(receipt.getMember().getId()))
                purchasesMap.put(receipt.getMember().getId(), new PurchaseHistory(receipt.getMember().getId()));
            purchasesMap.get(receipt.getMember().getId()).addPurchaseMade(receipt);
            receipt.initialParams();
        }
        return receipt;
    }
    public static Receipt getReceipt(int userId, int orderId) throws Exception{
        if(purchasesMap.containsKey(userId))
            if(purchasesMap.get(userId).checkOrderOccurred(orderId))
                return purchasesMap.get(userId).getReceipt(orderId);
        return getReceipt(orderId);
    }

    public static PurchaseHistory getReceipts(int userId) throws Exception{
        if(!purchases.contains(userId)){
            if(!purchasesMap.containsKey(userId))
                purchasesMap.put(userId, new PurchaseHistory(userId));
            List<? extends DbEntity> receiptsDto = Dao.getListById(Receipt.class, userId, "Receipt", "memberId");
            for(Receipt receipt : (List<Receipt>)  receiptsDto)
                if(!purchasesMap.get(userId).checkOrderOccurred(receipt.getOrderId())) {
                    purchasesMap.get(userId).addPurchaseMade(receipt);
                    receipt.initialParams();
                }
            purchases.add(userId);
        }
        return purchasesMap.get(userId);
    }

    public static void removeReceipt(int userId, int orderId, Session session) throws Exception{
        Dao.removeIf("Receipt", String.format("receiptId = %d AND memberId = %d", orderId, userId), session);
        if(purchasesMap.containsKey(userId))
            purchasesMap.get(userId).removeReceipt(orderId);
    }

    public static void removePurchases(int userId, Session session) throws Exception{
        Dao.removeIf("Receipt", String.format("memberId = %d", userId), session);
        purchasesMap.remove(userId);
        purchases.remove(userId);
    }
}
