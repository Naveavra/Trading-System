package domain.user;

import database.daos.SubscriberDao;
import domain.store.product.Product;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.infoRelated.ProductInfo;

import java.util.*;

public class Basket{
    private int storeId;
    private List<ProductInfo> productList; //a list of all the product ids and their quantities related to a specific shop


    public Basket(int storeId){
        this.storeId = storeId;
        productList = new ArrayList<>();
    }

    public Basket(Basket b){
        this.storeId = b.storeId;
        productList = new ArrayList<>();
        productList.addAll(b.productList);

    }


    public int getStoreId(){
        return storeId;
    }
    public void addProductToCart(ProductInfo product, int quantity) throws Exception{
        if(quantity < 1)
            throw new Exception("the quantity given is negative");
        product.quantity = quantity;
        productList.add(product);
    }

    public int removeProductFromCart(int productId) {
        ProductInfo p = getProduct(productId);
        productList.remove(p);
        return productList.size();

    }

    public ProductInfo getProduct(int productId){
        for(ProductInfo productInfo : productList)
            if (productInfo.id == productId)
                return productInfo;
        return null;
    }

    public int changeQuantityInCart(ProductInfo product, int change) throws Exception{
        ProductInfo p = getProduct(product.id);
        if(p != null) {
            int prevQuantity = p.quantity;
            int newQuantity = prevQuantity + change;
            if(newQuantity < 0)
                throw new Exception("the new quantity is negative");
            else if(newQuantity == 0)
                return removeProductFromCart(p.id);
            else
                p.quantity = newQuantity;
        }
        else
            addProductToCart(product, change);
        return productList.size();
    }

    public List<ProductInfo> getContent() {
        return productList;
    }

    public void clear(){
        productList.clear();
    }

    public boolean hasProduct(int productId) {
        ProductInfo p = getProduct(productId);
        return p!= null;

    }

    public double getTotalPrice() {
        double total = 0;
        for (ProductInfo pI: productList){
            total += pI.price * pI.quantity;
        }
        return total;
    }

    public List<ProductInfo> getProductList()
    {
        return this.productList;
    }
}
