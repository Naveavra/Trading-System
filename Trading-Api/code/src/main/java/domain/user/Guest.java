package domain.user;

import domain.store.product.Product;
import org.json.JSONObject;
import utils.Pair;
import utils.infoRelated.ProductInfo;

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

    public void addProductToCart(int storeId, ProductInfo product, int quantity) throws Exception{
            cart.addProductToCart(storeId, product, quantity);
    }


    public void removeProductFromCart(int storeId, int productId) throws Exception{
        cart.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, ProductInfo product, int change) throws Exception{
        cart.changeQuantityInCart(storeId, product, change);
    }

    public List<Basket> getCartContent() {
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
