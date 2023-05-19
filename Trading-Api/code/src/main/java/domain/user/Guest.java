package domain.user;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Guest implements User{
    private int id;
    private ShoppingCart cart;




    public Guest(int id){
        this.id = id;
        cart = new ShoppingCart();
    }

    public int getId(){
        return id;
    }

    public void addProductToCart(int storeId, int productId, int quantity) throws Exception{
            cart.addProductToCart(storeId, productId, quantity);
    }


    public void removeProductFromCart(int storeId, int productId) throws Exception{
        cart.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, int productId, int change) throws Exception{
        cart.changeQuantityInCart(storeId, productId, change);
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getCartContent() {
        return cart.getContent();
    }

    public List<JSONObject> getCartJson(){
        return cart.toJson();
    }

    public void emptyCart() {
        cart.emptyCart();
    }
    public void purchaseMade(int orderId, double totalPrice){
        emptyCart();
    }

    public ShoppingCart getShoppingCart() {
        return cart;
    }
}
