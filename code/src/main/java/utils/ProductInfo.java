package utils;

import java.util.List;

public class ProductInfo {
    public final int id;
    private List<String> categories;
    public String name;
    public String description;
    public int price; //for one product
    public int quantity;


    public ProductInfo(int productId , List<String> categories,String name , String desc ,int price , int quantity){
        this.id = productId;
        this.categories = categories;
        this.name =name;
        this.description =desc;
        this.price = price;
        this.quantity =quantity;
    }

    public List<String> getCategories() {
        return categories;
    }
}
