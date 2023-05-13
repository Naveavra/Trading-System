package domain.store.product;

import utils.ProductInfo;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Product {
    public final int id;
    private ArrayList<String> categories;
    public String name;
    public String description;
    public int price; //for one product
    public int quantity;
    public double rating;
//    public ArrayList<String> categories;
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
        categories = new ArrayList<>();
        rating = 5;
    }

    public String getName(){return name;}
    public int getQuantity(){
        return quantity;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    /**
     * adds the quantity given (amount) to the current quantity.
     * @param amount int, to add
     */
    public void setQuantity(int amount) throws Exception{
        if(quantity + amount>=0){
            quantity += amount;
        }else {
            throw new Exception("Invalid Quantity: New quantity for product <= 0.");
        }
    }
    public void replaceQuantity(int newQuantity) throws Exception{
        if(newQuantity>0){
            quantity = newQuantity;
        }else {
            throw new Exception("Invalid Quantity: New quantity for product <= 0.");
        }
    }
    public void setPrice(int newPrice) throws Exception {
        if (newPrice > 0) {
            price = newPrice;
        } else {
            throw new Exception("Invalid Price: New price for product <= 0.");
        }
    }

    /**
     * This function returns a deep copy of the product,
     * should be used when inserting products to the store Inventory.
     * @return new deep copy of the product.
     */
    public synchronized Product clone() {
        Product clone = new Product(id,name,description);
        try {
            clone.setPrice(price);
        }catch (Exception e){
            return null;
        }
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

    public ArrayList<String> getCategories(){
        return categories;
    }

    public void setRating(double v) {
        rating = v;
    }

    public double getRating() {
        return rating;
    }

    public void addCategory(String category) {
        if(!categories.contains(category))
            categories.add(category);
    }

    public boolean belongsToCategory(String discountedCategory) {
        return getCategories().contains(discountedCategory);
    }
}
