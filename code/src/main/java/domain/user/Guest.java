package domain.user;

import domain.states.Buyer;
import domain.states.UserState;
import utils.Action;

import java.util.HashMap;

public class Guest {
    private int id;
    private ShoppingCart cart;




    public Guest(int id){
        this.id = id;
        cart = new ShoppingCart();
    }

    public int getId(){
        return id;
    }

    public void addProductToCart(int storeId, int productId, int quantity) throws RuntimeException{
            cart.addProductToCart(storeId, productId, quantity);
    }


    public void removeProductFromCart(int storeId, int productId) {
        cart.removeProductFromCart(storeId, productId);
    }

    public void changeQuantityInCart(int storeId, int productId, int change) throws RuntimeException{
        cart.changeQuantityInCart(storeId, productId, change);
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getCartContent() {
        return cart.getContent();
    }

    public void emptyCart() {
        cart.emptyCart();
    }
}
