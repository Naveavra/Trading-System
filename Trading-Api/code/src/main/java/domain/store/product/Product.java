package domain.store.product;

import utils.ProductInfo;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Product {
    public final int id;
    private List<String> categories;
    public String name;
    public String description;
    public int price; //for one product
    public int quantity;
    public double rating;
    //private ConcurrentLinkedDeque<String> categories;
    //private double discount;  no need for discount here, the discount is calculated by the policy

    /**
     * Quantity, Price and Categories are set manually.
     * @param prod_id Unique
     * @param _name String
     * @param desc String
     */
    public Product(int prod_id, String _name, String desc){
        id = prod_id;
        name = _name;
        description = desc;
        price = 0;
        categories = new LinkedList<>();
    }

    public String getName(){return name;}
    public int getQuantity(){
        return quantity;
    }

    public void setDescription(String desc){
        this.description = desc;
    }


    public void setQuantity(int amount){
        if(quantity + amount>=0){
            quantity += amount;
        }
        //else maybe should throw exception
    }
    public void replaceQuantity(int newQuantity){
        if(newQuantity>0){
            quantity = newQuantity;
        }
    }
    public void setPrice(int newPrice){
        if(newPrice>0){
            price = newPrice;
        }
        //else maybe should throw exception
    }

    /**
     * This function returns a deep copy of the product,
     * should be used when inserting products to the store Inventory.
     * @return new deep copy of the product.
     */
    public synchronized Product clone(){
        Product clone = new Product(id,name,description);
        clone.setPrice(price);
//        clone.setQuantity(quantity);
        return clone;
    }

    public int getID(){
        return this.id;
    }

    public String getDescription() { return description;
    }

    public int getPrice() { return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductInfo getProductInfo() {
        return new ProductInfo(getID(), getName(), getDescription(), getPrice(), getQuantity());
    }

    public List<String> getCategories(){
        return categories;
    }

    public void setRating(double v) {
        rating = v;
    }
}