package domain.store.product;


import database.daos.Dao;
import database.DbEntity;
import database.daos.StoreDao;
import database.dtos.CategoryDto;
import domain.store.storeManagement.Store;
import domain.user.Member;
import utils.Filter.ProductFilter;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.ProductReview;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory{
    private int storeId;
    private ConcurrentHashMap<Integer, Product> productList; // <id, product>
    private ConcurrentHashMap<String,List<Integer>> categories; // <Category String,<List<ProductID>>


    // AtomicInteger prod_id = new AtomicInteger();
    public Inventory(int storeId){
        this.storeId = storeId;
        productList = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
    }

    /**
     *  @param name String
     * @param description String
     * @param prod_id AtomicInteger
     * @param price int
     * @param quantity
     */
    public synchronized Product addProduct(String name, String description, AtomicInteger prod_id, int price,
                                           int quantity, List<String> categories) throws Exception {
        Product p = null;
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            p = new Product(storeId, id,name,description, "");
            p.setPrice(price);
            p.replaceQuantity(quantity);
            productList.put(id,p);
            addToCategories(id, categories);
        }
        else
            throw new Exception("the product already exists in the system, aborting add");
        return p;
    }

    public synchronized Product addProduct(String name, String description, AtomicInteger prod_id,
                                           int price, int quantity, String img, List<String> categories) throws Exception {
        Product p = null;
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            p = new Product(storeId,id,name,description, img);
            p.setPrice(price);
            p.replaceQuantity(quantity);
            productList.put(id,p);
            addToCategories(id, categories);
        }
        else
            throw new Exception("the product already exists in the system, aborting add");
        return p;
    }

    public synchronized Product addProduct(Product p) throws Exception{
        if(getProductByName(p.name) == null) {
            productList.put(p.getID(), p);
            addToCategories(p.getID(), p.getCategories());
        }
        else
            throw new Exception("the product already exists in the system, aborting add");
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

    public void addProductReview(ProductReview m) throws Exception{
        Product p = getProduct(m.getProductId());
        p.addReview(m);
    }
    /**
     * gets product id and return list of the grading the product got by buyers
     */
    public List<ProductReview> getProductReviews(int productID){
        List<ProductReview> ans = new ArrayList<>();
        Product p;
        try{
            p = getProduct(productID);
        }catch(Exception e){
            p = null;
        }
        if (p != null){
           ans.addAll(p.getReviews());
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

    public Product getProduct(int productId) throws Exception{
        if(productList.containsKey(productId)){
            return productList.get(productId);
        }
        Product p = StoreDao.getProduct(storeId, productId);
        if(p != null) {
            productList.put(p.productId, p);
            return p;
        }
        throw new Exception("Product not found, Id: " + productId);
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
        Product p = StoreDao.getProduct(storeId, name);
        if(p != null){
            productList.put(p.productId, p);
            return p;
        }
        return null;
    }

    public synchronized HashMap<Integer, Integer> getPrices() {
        HashMap<Integer,Integer> prices = new HashMap<>();
        for(Product p : productList.values()){
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
        if(categories.containsKey(category)){
            if(!categories.get(category).contains(productId)){
                categories.get(category).add(productId);
                p.addCategory(category);
                Dao.save(new CategoryDto(storeId, productId, category));
            }
        }else{
            categories.put(category,new ArrayList<>());
            categories.get(category).add(productId);
            p.addCategory(category);
            Dao.save(new CategoryDto(storeId, productId, category));
        }
    }

    private void addToCategories(int productId, List<String> categories) throws Exception {
        for(String category : categories)
            addToCategory(category, productId);
    }

    public synchronized void removeProduct(int productId) throws Exception{
        Product p = getProduct(productId);
        productList.remove(productId);
        StoreDao.removeProduct(storeId, productId);
        for(List<Integer> prodIds : categories.values()){
            if(prodIds.contains(productId)){
                prodIds.removeIf(id -> id == productId);
            }
        }
    }

    public void updateProduct(int productId, List<String> categories,String name, String description,
                              int price, int quantity, String img) throws Exception{
        Product p = getProduct(productId);
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
        if(img != null && !img.equals("null"))
            changeImg(productId, img);
        Dao.save(p);
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

    private void replaceCategories(int productId, List<String> cats) throws Exception {
        for(List<Integer> category: categories.values()){
            if(category.contains(productId)){
                category.remove(Integer.valueOf(productId));
            }
        }
        Dao.removeIf("CategoryDto", String.format("productId = %d", productId));
        for(String category: cats){
            addToCategory(category,productId);
        }
    }

    //for tests
    public synchronized void rateProduct(int productID, int rating) throws Exception{
        if(rating < 0 || rating > 5)
            throw new Exception("the rating given is not between 0 and 5");
        Product p = getProduct(productID);
        p.addReview(new ProductReview("not important", new Member("ni", "ni", "ni"), -1, storeId, productID, rating));

    }
    public ArrayList<ProductInfo> filterBy(ProductFilter filter,double storeRating) throws Exception{
//        filter.setOp(this::getProduct);
        filter.setCategories(categories);
        filter.setStoreRating(storeRating);
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

    public ProductInfo getProductInfo(int storeId, int productId) throws Exception{
        Product p = getProduct(productId);
        ProductInfo info = new ProductInfo(storeId, p.getID(), p.getCategories(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(),
                p.getRating(), getProductReviews(p.getID()), p.getImgUrl());
        return info;
    }

    public List<ProductReview> getProductReviews(){
       List<ProductReview> ans = new ArrayList<>();
       for (Product p : productList.values())
       {
           ans.addAll(p.getReviews());
       }
       return ans;
    }

    //database

    public void initialParams(){
        getProductsFromDb();
        getCategoriesFromDb();
    }
    public void getCategoriesFromDb(){
        if(categories == null){
            HashMap<String, Set<Integer>> tmp = StoreDao.getAllCategories(storeId);
            categories = new ConcurrentHashMap<>();
            for(String cat : tmp.keySet()) {
                categories.put(cat, new ArrayList<>());
                for(int id : tmp.get(cat))
                    categories.get(cat).add(id);
            }
        }
    }

    public void getProductsFromDb(){
        List<Product> products = StoreDao.getProducts(storeId);
        for(Product p : products)
            if(!productList.containsKey(p.productId))
                productList.put(p.productId, p);
    }
}