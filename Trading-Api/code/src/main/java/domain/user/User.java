package domain.user;

import domain.store.product.Product;
import org.hibernate.Session;
import org.json.JSONObject;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;

import java.util.HashMap;
import java.util.List;

public interface User {

    public int getId();
    public void addProductToCart(int storeId, ProductInfo product, int quantity, Session session) throws Exception;
    public void removeProductFromCart(int storeId, int productId, Session session) throws Exception;
    public void changeQuantityInCart(int storeId, ProductInfo product, int change, Session session) throws Exception;
    public List<ProductInfo> getCartContent();
    public ShoppingCart getShoppingCart() throws Exception;
    public void purchaseMade(Receipt receipt, Session session) throws Exception;
    public void emptyCart();
    public String getName();
    public int getAge();

    public void setShoppingCart(ShoppingCart cart);
}
