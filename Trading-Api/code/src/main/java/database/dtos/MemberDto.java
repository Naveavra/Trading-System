package database.dtos;
import domain.store.storeManagement.Store;
import domain.user.PurchaseHistory;
import domain.user.ShoppingCart;
import jakarta.persistence.*;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;
import utils.messageRelated.Notification;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class MemberDto {

    @Id
    private int id;
    private String email;
    private String birthday;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="memberDto")
    private List<NotificationDto> notifications;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="memberDto")
    private List<CartDto> cartProducts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="memberDto")
    private List<UserHistoryDto> purchases;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="memberDto")
    private List<StoreDto> stores;


    public MemberDto() {

    }
    public MemberDto(int id, String email, String password, String birthday){
        this.id = id;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    public List<StoreDto> getStores(){return stores;}

    public void setStores(List<Store> stores) {
        List<StoreDto> storeDtos = new ArrayList<>();
        for(Store s : stores) {
            StoreDto storeDto = new StoreDto(this, s.getStoreId(), s.getName(), s.getStoreDescription(), s.getImgUrl());
            storeDtos.add(storeDto);
            storeDto.setRoles(s.getAppHistory());
        }
        this.stores = storeDtos;
    }

    public void setNotifications(List<Notification> notifications) {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification n : notifications)
            notificationDtos.add(new NotificationDto(this, n.getNotification().toString(), n.getOpcode().toString()));
        this.notifications = notificationDtos;
    }

    public List<CartDto> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(ShoppingCart cart) {
        List<CartDto> cartDtos = new ArrayList<>();
        for(ProductInfo p : cart.getContent())
            cartDtos.add(new CartDto(this, p.getStoreId(), p.getId(), p.getQuantity()));
        this.cartProducts = cartDtos;
    }

    public List<UserHistoryDto> getPurchases() {
        return purchases;
    }

    public void setPurchases(PurchaseHistory p) {
        List<UserHistoryDto> userHistoryDtos = new ArrayList<>();
        for(Receipt r : p.getReceipts()){
            UserHistoryDto userHistoryDto = new UserHistoryDto(this, r.getOrderId(), r.getTotalPrice());
            List<ReceiptDto> receiptDtos = new ArrayList<>();
            for(ProductInfo product : r.getCart().getContent()){
                receiptDtos.add(new ReceiptDto(userHistoryDto, product.getStoreId(), product.getId(), product.getQuantity()));
            }
            userHistoryDto.setReceiptDtos(receiptDtos);
            userHistoryDtos.add(userHistoryDto);
        }
        this.purchases = userHistoryDtos;
    }
}
