package utils.infoRelated;

import database.Dao;
import database.DbEntity;
import database.dtos.ReceiptDto;
import domain.store.storeManagement.Store;
import domain.user.*;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.List;

@Entity
@Table(name = "receipts")
public class Receipt extends Information implements DbEntity {

    @Id
    private int orderId;

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
        for(ProductInfo productInfo : products.getContent())
            Dao.save(new ReceiptDto(orderId, productInfo.storeId, productInfo.id, productInfo.getQuantity()));
    }

    public void setMemberId(int memberId){
        this.memberId = memberId;
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
    public void initialParams() {
        List<? extends DbEntity> prods = Dao.getListById(ReceiptDto.class, orderId,
                "ReceiptDto", "orderId");
        ShoppingCart tmpCart = new ShoppingCart();
        for (ReceiptDto rd : (List<ReceiptDto>) prods) {
            try {
                Store s = (Store) Dao.getById(Store.class, rd.getStoreId());
                if (s != null)
                    tmpCart.changeQuantityInCart(rd.getStoreId(), s.getProductInformation(rd.getProductId()), rd.getQuantity());
            } catch (Exception ignored) {
            }
            products = tmpCart;
        }
    }
}
