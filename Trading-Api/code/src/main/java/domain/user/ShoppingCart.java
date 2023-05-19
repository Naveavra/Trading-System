package domain.user;

import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONObject;
import utils.infoRelated.Information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart{
    private HashMap<Integer, Basket> baskets; // saves the connection between a shop and its basket;

    public ShoppingCart (){
        baskets = new HashMap<>();
    }

    public ShoppingCart(ShoppingCart cart){
        baskets = new HashMap<>();
        for(int storeId : cart.baskets.keySet())
            baskets.put(storeId, new Basket(cart.baskets.get(storeId)));
    }

    public void addProductToCart(int storeId, int productId, int quantity) throws Exception {
        if (quantity < 1) {
            throw new Exception("quantity must be bigger then 0");
        }
        if(!baskets.containsKey(storeId))
            baskets.put(storeId, new Basket(storeId));
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
            addProductToCart(storeId, productId, change);
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getContent(){
        HashMap<Integer, HashMap<Integer, Integer>> cartContent = new HashMap<>();
        for(int storeId : baskets.keySet()) {
            HashMap<Integer, Integer> basketContent = baskets.get(storeId).getContent();
            cartContent.put(storeId, basketContent);
        }
        return cartContent;
    }

    public boolean hasStore(int storeId){
        return baskets.containsKey(storeId);
    }

    public boolean hasProduct(int storeId, int productId) {
        if(hasStore(storeId))
            return baskets.get(storeId).hasProduct(productId);
        return false;
    }


    public List<JSONObject> toJson()
    {
        List<JSONObject> ans = new ArrayList();
        for (Basket b : baskets.values()) {
            JSONObject basketJson = b.toJson();
            ans.add(basketJson);
        }
        return ans;
    }

    public void emptyCart() {
        for(Basket b : baskets.values())
            b.clear();
        baskets.clear();
    }

    public HashMap<Integer, Basket> getBaskets() {
        return baskets;
    }
}
