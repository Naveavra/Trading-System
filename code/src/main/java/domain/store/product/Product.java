package domain.store.product;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Product {
    public final int id;
    private List<String> categories;
    public String name;
    public String description;
    public int price; //for one product
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
        quantity = 0;
        price = 0;
        categories = new ArrayList<>();
    }
    public void addCategory(String _category){
        if(!categories.contains(_category)){
            categories.add(_category);
        }
    }

    public List<String> getCategories(){
        return this.categories;
    }

    private void setCategories(List<String> categories){
        this.categories = categories;
    }

    public void setQuantity(int amount){
        if(amount>0){
            quantity = amount;
        }
        //else maybe should throw exception
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
    public Product clone(){
        Product clone = new Product(id,name,description);
        clone.setPrice(price);
        clone.setQuantity(quantity);
        for(String cat: categories){
            clone.addCategory(cat);
        }
        return clone;
    }

    public int getID(){
        return this.id;
    }
    
}
