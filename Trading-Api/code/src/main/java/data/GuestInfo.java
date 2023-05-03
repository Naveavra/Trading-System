package data;


public class GuestInfo {

    private int id;
    private CartInfo cart;

    public GuestInfo()
    {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CartInfo getCart() {
        return cart;
    }

    public void setCart(CartInfo cart) {
        this.cart = cart;
    }
}
