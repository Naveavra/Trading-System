package domain.store.product;


import utils.Filter.ProductFilter;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Message;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    private static final int MAXRATING = 5;
    private int storeId;
    private ConcurrentHashMap<Integer, Product> productList; // <id, product>
    // ConcurrentHashMap<Product, ArrayList<String>> categories;
    private ConcurrentHashMap<String,ArrayList<Integer>> categories; // <Category String,<List<ProductID>>
    private ConcurrentHashMap<Product, CopyOnWriteArrayList<Integer>> productgrading;
    private ConcurrentHashMap<Integer, Message> productReviews;


    // AtomicInteger prod_id = new AtomicInteger();
    public Inventory(int storeId){
        this.storeId = storeId;
        productList = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
        productgrading = new ConcurrentHashMap<>();
        productReviews = new ConcurrentHashMap<>();
    }

    /**
     *  @param name String
     * @param description String
     * @param prod_id AtomicInteger
     * @param price int
     * @param quantity
     */
    public synchronized Product addProduct(String name, String description, AtomicInteger prod_id, int price, int quantity) throws Exception {
        Product p = null;
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            p = new Product(id,name,description);
            p.setPrice(price);
            p.replaceQuantity(quantity);
            for(Product product : productList.values())
                if(p.getName().equals(product.getName()) && p.getDescription().equals(product.getDescription())) {
                    prod_id.getAndDecrement();
                    throw new Exception("the product already exists in the system, aborting add");
                }
            productList.put(id,p);
        }
        return p;
    }

    public synchronized Product addProduct(String name, String description, AtomicInteger prod_id,
                                           int price, int quantity, String img) throws Exception {
        Product p = null;
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            p = new Product(id,name,description, img);
            p.setPrice(price);
            p.replaceQuantity(quantity);
            for(Product product : productList.values())
                if(p.getName().equals(product.getName()) && p.getDescription().equals(product.getDescription())) {
                    prod_id.getAndDecrement();
                    throw new Exception("the product already exists in the system, aborting add");
                }
            productList.put(id,p);
        }
        return p;
    }
    public synchronized Product addProduct(Product p) throws Exception{
        if(getProductByName(p.name) == null) productList.put(p.getID(), p);
        return p;
    }

    /**
     * returns all related categories for productId.
     * @param productId
     * @return ArrayList<Categories>
     */
    public synchronized ArrayList<String> getProductCategories(int productId){
        ArrayList<String> relatedCategories = new ArrayList<>();
        for(String category : categories.keySet()){
            if(categories.get(category).contains(productId)){
                relatedCategories.add(category);
            }
        }
        return relatedCategories;
    }

    public void addProductReview(Message m) throws Exception{
        if(productList.containsKey(m.getProductId())){
            productReviews.put(m.getMessageId(), m);
        }
        else{
            throw new Exception("the review given contains an illegal id");
        }
    }
    /**
     * gets product id and return list of the grading the product got by buyers
     */
    public HashMap<Integer, Message> getProductReviews(int productID){
        HashMap<Integer, Message> ans = new HashMap<>();
        Product p;
        try{
            p = getProduct(productID);
        }catch(Exception e){
            p = null;
        }
        if (p != null){
           for(Message m : productReviews.values())
                if(m.getProductId() == productID)
                    ans.put(m.getProductId(), m);
        }
        return ans;
    }
    public void setDescription(int prodID, String desc)  throws Exception{
        Product p = getProduct(prodID);
        p.setDescription(desc);

    }
    public synchronized void setPrice(int prodID, int price) throws Exception {
        if (price <= 0)
        {
            throw new Exception("price must be positive");
        }
        Product p = getProduct(prodID);
        if(p != null ){
            p.setPrice(price);
            return;
        }
        throw new Exception("product doesnt exist");
    }

    public void addQuantity(int prodID,int quantity) throws Exception{
        Product p = getProduct(prodID);
        p.setQuantity(quantity);
    }

    public Product getProduct(Integer productID) throws Exception{
        if(productList.containsKey(productID)){
            return productList.get(productID);
        }
        throw new Exception("Product not found, ID: " + productID);
    }

    public ArrayList<String> getAllCategories(){
        return new ArrayList<>(categories.keySet());
    }


    /**
     * @param prodID INTEGER
     * @throws Exception if doesn't exist
     */
    public int getQuantity(int prodID) throws Exception{
        Product p = getProduct(prodID);
        if(p != null){
            return p.getQuantity();
        }
        throw new Exception("Boy that product doesn't exist");
    }

    public Product getProductByName(String name){
        for(Product p : productList.values()){
            if(p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    public synchronized HashMap<Integer, Integer> getPrices() {
        HashMap<Integer,Integer> prices = new HashMap<>();
        for(Product p : this.productList.values()){
            prices.put(p.getID(),p.getPrice());
        }
        return prices;
    }

    public List<ProductInfo> getProducts(){
        List<ProductInfo> productInfos = new LinkedList<>();
        for (Product p : productList.values()){
            ProductInfo info = new ProductInfo(storeId, p.getID(), p.getCategories(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(),
                    p.getRating(), getProductReviews(p.getID()), p.getImgUrl());
            info.setCategories(getProductCategories(p.getID()));
            productInfos.add(info);
        }
        return productInfos;
    }
    public synchronized void addToCategory(String category, int productId) throws Exception {
        Product p = getProduct(productId);
        p.addCategory(category);
//        category =category.toLowerCase();
        if(categories.containsKey(category)){
            if(!categories.get(category).contains(productId)){
                categories.get(category).add(productId);
            }
        }else{
            categories.put(category,new ArrayList<>());
            categories.get(category).add(productId);
        }
    }

    public synchronized int removeProduct(int productId) {
        if(productList.containsKey(productId)){
            productList.remove(productId);
            for(ArrayList<Integer> prodIds : categories.values()){
                if(prodIds.contains(productId)){
                    prodIds.remove(Integer.valueOf(productId));
                }
            }
            return 0;
        }
        return -1;
    }

    public void updateProduct(int productId, List<String> categories,String name, String description,
                              int price, int quantity, String img) throws Exception{
        if(productList.containsKey(productId)){
            if(categories!=null){
                replaceCategories(productId,categories);
            }
            if(!name.equals("null")){
                setName(productId,name);
            }
            if(!description.equals("null")){
                setDescription(productId,description);
            }
            if(price > 0){
                setPrice(productId,price);
            }
            if(quantity > 0){
                replaceQuantity(productId,quantity);
            }
            if(!img.equals("null"))
                changeImg(productId, img);
        }
        else
            throw new Exception("the product does not exist in the store");
    }

    private void changeImg(int productId, String img) throws Exception{
        Product p = getProduct(productId);
        p.changeImg(img);
    }

    private void replaceQuantity(int productId, int quantity) throws Exception {
        Product p = getProduct(productId);
        p.replaceQuantity(quantity);
    }

    private void setName(int productId, String name) throws Exception{
        Product p = getProduct(productId);
        p.setName(name);
    }

    private void replaceCategories(int productId, List<String> categories) throws Exception {
        for(ArrayList<Integer> category: this.categories.values()){
            if(category.contains(productId)){
                category.remove(Integer.valueOf(productId));
            }
        }
        for(String category: categories){
            addToCategory(category,productId);
        }
    }

    public synchronized void setProductsRatings(){
        for(Product p : productgrading.keySet()){
            double sum = 0;
            for(int rating : productgrading.get(p)){
                sum += rating;
            }
            double avg = sum/productgrading.get(p).size();
            DecimalFormat df = new DecimalFormat("#.#");
            avg = Double.parseDouble(df.format(avg));
            p.setRating(avg);
        }
    }
    public synchronized void rateProduct(int productID, int rating) throws Exception{
        if(rating > MAXRATING)
            throw new Exception("Product rating is out of bounds, expected 0 to 5 but got: "+rating);
        Product p = getProduct(productID);
        if(productgrading.containsKey(p))
            productgrading.get(p).add(rating);
        else
            productgrading.put(p,new CopyOnWriteArrayList<>(List.of(rating)));

    }
    public ArrayList<ProductInfo> filterBy(ProductFilter filter,double storeRating) {
//        filter.setOp(this::getProduct);
        filter.setCategories(categories);
        filter.setStoreRating(storeRating);
        setProductsRatings();
        //need to set more relevant things here as soon as all filters are implemented.
        ArrayList<Product> filtered = filter.filter(new ArrayList<>(productList.values()));
        ArrayList<ProductInfo> result = new ArrayList<>();
        for(Product p: filtered){
            ProductInfo info = getProductInfo(storeId, p.getID());
            info.setCategories(getProductCategories(p.getID()));
            result.add(info);
        }
        return result;
    }

    public ProductInfo getProductInfo(int storeId, int productId){
        Product p = productList.get(productId);
        ProductInfo info = new ProductInfo(storeId, p.getID(), p.getCategories(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(),
                p.getRating(), getProductReviews(p.getID()), p.getImgUrl());
        return info;
    }

    public ConcurrentHashMap<Integer, Message> getProductReviews(){
        return productReviews;
    }
}