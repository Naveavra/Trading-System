package domain.user;

import com.google.gson.Gson;
import domain.user.Basket;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    private HashMap<Integer, Basket> baskets; // saves the connection between a shop and its basket;

    public ShoppingCart (){
        baskets = new HashMap<>();
    }

    public void addProductToCart(int storeId, int productId, int quantity) {

        if(!baskets.containsKey(storeId))
            baskets.put(storeId, new Basket());
        baskets.get(storeId).addProductToCart(productId, quantity);
    }

    public void removeProductFromCart(int storeId, int productId) throws Exception {
        Basket basket = baskets.get(storeId);
        if(basket != null) {
            boolean check = basket.removeProductFromCart(productId);
            if(!check)
                baskets.remove(storeId);
        }
        else
            throw new Exception("the product isn't in the cart");

    }

    public void changeQuantityInCart(int storeId, int productId, int change) throws Exception{
        if(baskets.containsKey(storeId)) {
            boolean check = baskets.get(storeId).changeQuantityInCart(productId, change);
            if(!check)
                baskets.remove(storeId);

        }
        else
            throw new Exception("the user's cart does not contain the store");


    }

    public HashMap<Integer, HashMap<Integer, Integer>> getContent() {
        HashMap<Integer, HashMap<Integer, Integer>> cartContent = new HashMap<>();
        for(int storeId : baskets.keySet()) {
            HashMap<Integer, Integer> basketContent = baskets.get(storeId).getContent();
            cartContent.put(storeId, basketContent);
        }
        return cartContent;

    }

    public void emptyCart() {
        baskets = new HashMap<>();
    }
}
