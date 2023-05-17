package domain.user;

import domain.store.product.Product;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Basket {
    private HashMap<Integer, Integer> productList; //a list of all the product ids and their quantities related to a specific shop


    public Basket(){
        productList = new HashMap<>();
    }


    public void addProductToCart(int productId, int quantity){
        productList.put(productId, quantity);
    }

    public boolean removeProductFromCart(int productId) {
        productList.remove(productId);
        return productList.size() != 0;

    }

    public boolean changeQuantityInCart(int productId, int change) throws RuntimeException{
        if(productList.containsKey(productId)) {
            if(change == 0)
                return removeProductFromCart(productId);
            else
                productList.put(productId, change);
        }
        else
            throw new RuntimeException("the product isn't in the user's cart");
        return true;
    }
    public boolean addQuantityInCart(int productId, int change) throws Exception{
        if(productList.containsKey(productId)) {
            int prevQuantity = productList.get(productId);
            if (change > 0) {
                int newQuantity = prevQuantity + change;
                if(newQuantity == 0)
                    return removeProductFromCart(productId);
                else
                    productList.put(productId, newQuantity);

            }
            else
                throw new Exception("the quantity given in negative");
        }
        else {
            if(change > 0) {
                addProductToCart(productId, change);
            }
            else
                throw new Exception("the quantity given is negative");
        }
        return productList.size() > 0;
    }

    public boolean removeQuantityInCart(int productId, int change) throws RuntimeException{
        if(productList.containsKey(productId)) {
            int prevQuantity = productList.get(productId);
            int newQuantity = prevQuantity - change;
            if (newQuantity < 0){throw new RuntimeException("quantity cant be negative");}
            else if (newQuantity == 0)
            {
                return removeProductFromCart(productId);
            }
            else {
                productList.put(productId, newQuantity);
            }
        }
        else
            throw new RuntimeException("the product isn't in the user's cart");
        return true;
    }

    public HashMap<Integer, Integer> getContent() {
        return productList;
    }
}
