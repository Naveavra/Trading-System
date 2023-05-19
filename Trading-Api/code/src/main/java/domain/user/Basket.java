package domain.user;

import org.json.JSONObject;

import java.util.*;

public class Basket {
    private int storeId;
    private HashMap<Integer, Integer> productList; //a list of all the product ids and their quantities related to a specific shop


    public Basket(int storeId){
        this.storeId = storeId;
        productList = new HashMap<>();
    }

    public Basket(Basket b){
        this.storeId = b.storeId;
        productList = new HashMap<>();
        for(int productId : b.productList.keySet())
            productList.put(productId, b.productList.get(productId));

    }


    public void addProductToCart(int productId, int quantity) throws Exception{
        if(quantity < 1)
            throw new Exception("the quantity given is negative");
        productList.put(productId, quantity);
    }

    public boolean removeProductFromCart(int productId) {
        productList.remove(productId);
        return productList.size() != 0;

    }

    public boolean changeQuantityInCart(int productId, int change) throws Exception{
        if(productList.containsKey(productId)) {
            int prevQuantity = productList.get(productId);
            int newQuantity = prevQuantity + change;
            if(newQuantity < 0)
                throw new Exception("the new quantity is negative");
            if(newQuantity == 0)
                return removeProductFromCart(productId);
            else
                productList.put(productId, newQuantity);
        }
        else
            addProductToCart(productId, change);
        return true;
    }

    public HashMap<Integer, Integer> getContent() {
        return productList;
    }

    public JSONObject toJson(){
        JSONObject basketJson = new JSONObject();
        basketJson.put("storeId", storeId);
        List<JSONObject> bucketList = new ArrayList();
        for (Map.Entry<Integer, Integer> productEntry : productList.entrySet()) {
            JSONObject productJson = new JSONObject();
            productJson.put("productId", productEntry.getKey());
            productJson.put("quantity", productEntry.getValue());
            bucketList.add(productJson);
        }
        basketJson.put("products", bucketList);
        return basketJson;
    }

    public void clear(){
        productList.clear();
    }

    public boolean hasProduct(int productId) {
        return productList.containsKey(productId);
    }
}
