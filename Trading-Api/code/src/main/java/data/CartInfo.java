package data;

import utils.infoRelated.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartInfo {
    List<ProductInfo> cart;

    public CartInfo(List<ProductInfo> cart) {
        this.cart = new ArrayList<>();
        this.cart.addAll(cart);
    }

    public int getCountOfProduct()
    {
        return cart.size();
    }
}
