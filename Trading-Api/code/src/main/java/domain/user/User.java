package domain.user;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public interface User {

    public void addProductToCart(int storeId, int productId, int quantity) throws Exception;
    public void removeProductFromCart(int storeId, int productId) throws Exception;
    public void changeQuantityInCart(int storeId, int productId, int change) throws Exception;
    public HashMap<Integer, HashMap<Integer, Integer>> getCartContent();
    public List<JSONObject> getCartJson();
    public ShoppingCart getShoppingCart();
    public void purchaseMade(int orderId, double totalPrice);
    public void emptyCart();
}
