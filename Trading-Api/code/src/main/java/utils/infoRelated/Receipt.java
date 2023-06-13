package utils.infoRelated;

import database.daos.DaoTemplate;
import database.dtos.ReceiptDto;
import domain.user.*;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receipts")
public class Receipt extends Information{

    @Id
    private int orderId;

    private int memberId;
    @Transient
    private ShoppingCart products; // a hashmap from storeId to hashmap from productId to quantity

    private double totalPrice;

    public Receipt(int orderId, ShoppingCart products, double totalPrice){
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
        for(ProductInfo productInfo : products.getContent())
            DaoTemplate.save(new ReceiptDto(orderId, productInfo.storeId, productInfo.id, productInfo.getQuantity()));
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
}
