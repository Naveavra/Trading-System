package domain.store.product;

import domain.store.product.Product;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductController {

    ConcurrentHashMap<Integer, Product> productList; // <id, product>
    AtomicInteger prod_id = new AtomicInteger();
    public ProductController(){
        productList = new ConcurrentHashMap<Integer,Product>();
    }
    
    public void addProduct(String name,String description){
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            productList.put(id,new Product(id, name, description));
        }
    }
    
    public Product getProduct(int productID){
        if(productList.contains(productID)){
            return productList.get(productID);
        }
        return null;
    }
    
    public Product getProductByName(String name){
        for(Product p : productList.values()){
            if(p.name == name){
                return p;
            }
        }
        return null;
    }
}
