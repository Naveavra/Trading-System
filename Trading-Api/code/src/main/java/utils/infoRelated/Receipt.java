package utils.infoRelated;

import database.daos.Dao;
import database.DbEntity;
import database.daos.StoreDao;
import database.daos.SubscriberDao;
import database.dtos.ReceiptDto;
import domain.store.storeManagement.Store;
import domain.user.*;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "receipts")
public class Receipt extends Information implements DbEntity {

    @Id
    private int orderId;
    @Transient
    private Member member;
    private int memberId;
    @Transient
    private ShoppingCart products; // a hashmap from storeId to hashmap from productId to quantity

    private double totalPrice;

    public Receipt(){
    }

    public Receipt(int orderId, ShoppingCart products, double totalPrice){
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public void saveReceiptProducts(Session session) throws Exception{
        for(ProductInfo productInfo : products.getContent())
            Dao.save(new ReceiptDto(orderId, productInfo.storeId, productInfo.id, productInfo.getQuantity()), session);
    }

    public Member getMember(){
        return member;
    }
    public int getMemberId(){return memberId;}
    public void setMember(Member member){
        this.member = member;
        this.memberId = member.getId();
    }
    public int getOrderId(){
        return orderId;
    }
    public double getTotalPrice(){return totalPrice;}

    public ShoppingCart getCart(){
        return products;
    }

    public List<Basket> getContent(){return products.getBaskets();}


    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", memberId);
        json.put("orderId", orderId);
        json.put("totalPrice", totalPrice);
        json.put("products", infosToJson(products.getContent()));
        return json;
    }

    @Override
    public void initialParams() throws Exception{
        getMemberFromDb();
        List<? extends DbEntity> prods = Dao.getListById(ReceiptDto.class, orderId,
                "ReceiptDto", "orderId");
        ShoppingCart tmpCart = new ShoppingCart();
        for (ReceiptDto rd : (List<ReceiptDto>) prods) {
            try {
                Store s = StoreDao.getStore(rd.getStoreId());
                if (s != null)
                    tmpCart.changeQuantityInCart(rd.getStoreId(), s.getProductInformation(rd.getProductId()), rd.getQuantity());
            } catch (Exception ignored) {
            }
            products = tmpCart;
        }
    }

    public void getMemberFromDb() throws Exception{
        if(member == null)
            member = SubscriberDao.getMember(memberId);
    }
}
