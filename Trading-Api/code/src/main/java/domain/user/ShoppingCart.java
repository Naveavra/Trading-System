package domain.user;

import domain.store.product.Product;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.infoRelated.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart{
    private List<Basket> baskets; // saves the connection between a shop and its basket;

    public ShoppingCart (){
        baskets = new ArrayList<>();
    }

    public ShoppingCart(ShoppingCart cart){
        baskets = new ArrayList<>();
        for(Basket b: cart.baskets)
            baskets.add(new Basket(b));
    }
    public Basket getBasket(int storeId){
        for(Basket b : baskets)
            if(b.getStoreId() == storeId)
                return b;
        return null;
    }
    public void addProductToCart(int storeId, ProductInfo product, int quantity) throws Exception {
        if (quantity < 1) {
            throw new Exception("quantity must be bigger then 0");
        }
        Basket b = getBasket(storeId);
        if(b == null) {
            b = new Basket(storeId);
            baskets.add(b);
        }
        b.addProductToCart(product, quantity);
    }

    public void removeProductFromCart(int storeId, int productId) throws Exception {
        Basket basket = getBasket(storeId);
        if(basket != null) {
            boolean check = basket.removeProductFromCart(productId);
            if(!check)
                baskets.remove(getBasket(storeId));
        }
        else
            throw new Exception("the product isn't in the cart");
    }

    public void changeQuantityInCart(int storeId, ProductInfo product, int change) throws Exception{
        Basket b = getBasket(storeId);
        if(b != null) {
            boolean check = b.changeQuantityInCart(product, change);
            if(!check)
                baskets.remove(storeId);
        }
        else
            addProductToCart(storeId, product, change);
    }

    public boolean hasStore(int storeId){
        return getBasket(storeId) != null;
    }

    public boolean hasProduct(int storeId, int productId) {
        Basket b = getBasket(storeId);
        if(b != null)
            return b.hasProduct(productId);
        return false;
    }

    public List<ProductInfo> getContent()
    {
        List<ProductInfo> ans = new ArrayList();
        for (Basket b : baskets) {
            List<ProductInfo> basketJson = b.getContent();
            ans.addAll(basketJson);
        }
        return ans;
    }

    public List<Basket> getBaskets(){return baskets;}

    public void emptyCart() {
        for(Basket b : baskets)
            b.clear();
        baskets.clear();
    }
}
