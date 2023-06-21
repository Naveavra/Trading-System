package domain.user;

import domain.store.product.Product;
import org.hibernate.Session;
import org.json.JSONObject;
import utils.Pair;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;

import java.util.HashMap;
import java.util.List;

public class Guest implements User{
    private int id;
    private ShoppingCart cart;


    public Guest(int id){
        this.id = id;
        cart = new ShoppingCart();
    }
    public int getAge(){
        return -1;
    }

    @Override
    public void setShoppingCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public int getId(){
        return id;
    }

    public void addProductToCart(int storeId, ProductInfo product, int quantity, Session session) throws Exception{
            cart.addProductToCart(storeId, product, quantity);
    }


    public void removeProductFromCart(int storeId, int productId, Session session) throws Exception{
        cart.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, ProductInfo product, int change, Session session) throws Exception{
        cart.changeQuantityInCart(storeId, product, change);
    }

    public List<ProductInfo> getCartContent() {
        return cart.getContent();
    }

    public void emptyCart() {
        cart.emptyCart();
    }

    @Override
    public String getName() {
        return "guest" + id;
    }

    public void purchaseMade(Receipt receipt, Session session){
        emptyCart();
    }

    public ShoppingCart getShoppingCart() {
        return cart;
    }
}
