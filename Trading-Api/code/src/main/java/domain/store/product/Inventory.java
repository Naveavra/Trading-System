package domain.store.product;


import data.PositionInfo;
import utils.Filter.ProductFilter;
import utils.ProductInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inventory {

    ConcurrentHashMap<Integer, Product> productList; // <id, product>
    // ConcurrentHashMap<Product, ArrayList<String>> categories;
    ConcurrentHashMap<String,ArrayList<Integer>> categories; // <Category String,<List<ProductID>>
    ConcurrentHashMap<Product, ArrayList<Integer>> productgrading;

    // AtomicInteger prod_id = new AtomicInteger();
    public Inventory(){
        productList = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
        productgrading = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param name String
     * @param description String
     * @param prod_id AtomicInteger
     */
    public synchronized Product addProduct(String name,String description,AtomicInteger prod_id) throws Exception {
        Product p = null;
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            p = new Product(id,name,description);
            for(Product product : productList.values())
                if(p.getName().equals(product.getName()) && p.getDescription().equals(product.getDescription())) {
                    prod_id.getAndDecrement();
                    throw new Exception("the product already exists in the system, aborting add");
                }
            productList.put(id,p);
// categories.put(p,new ArrayList<>());
        }
        return p;
    }
    public synchronized Product addProduct(Product p){
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
    /**
     * gets product id and return list of the grading the product got by buyers
     */
    public ArrayList<Integer> getProductReviews(int productID) throws Exception {
        Product p = getProduct(productID);
        if (p != null){return productgrading.get(p);}
        throw new Exception("product doesnt exist G");
    }
    public void setDescription(int prodID, String desc){
        Product p = getProduct(prodID);
        if(p != null){
            p.setDescription(desc);
        }
    }
    public void setPrice(int prodID, int price) throws Exception {
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

    public void addQuantity(int prodID,int quantity){
        Product p = getProduct(prodID);
        if(p != null){
            p.setQuantity(quantity);
        }
    }

    public Product getProduct(Integer productID){
        if(productList.containsKey(productID)){
            return productList.get(productID);
        }
        return null;
    }

    public ArrayList<String> getAllCategories(){
        return new ArrayList<>(categories.keySet());
    }

//    /**
//     * This method takes an ArrayList of categories as input and returns an ArrayList
//     * of matching products sorted by the number of categories that they match (from most to least)
//     */
//    public ArrayList<Product> getProductByCategories(ArrayList<String> categories) {
//        ArrayList<Product> matchingProducts = new ArrayList<>();
//        Map<Product, Integer> productToNumMatches = new HashMap<>();
//        for(String cat: categories){
//            ArrayList<Integer> prod_ids = this.categories.get(cat);
//            if(prod_ids!= null){
//                for(Integer prodid : prod_ids){
//                    Product p = getProduct(prodid);
//                    if(p!= null && !matchingProducts.contains(p)){
//                        matchingProducts.add(p);
//                        productToNumMatches.put(p,1);
//                    } else if (p != null) {
//                        productToNumMatches.put(p,productToNumMatches.get(p)+1);
//                    }
//                }
//            }
//        }
//
//        matchingProducts.sort((p1, p2) -> {
//            int numMatches1 = productToNumMatches.get(p1);
//            int numMatches2 = productToNumMatches.get(p2);
//            return Integer.compare(numMatches2, numMatches1);
;//        });
//
//        return matchingProducts;
//    }

//    public ArrayList<Product> getProductByKeywords(ArrayList<String> keywords) {
//        Set<Product> temp = new HashSet<>();
//        Map<Product, Integer> productToNumMatches = new HashMap<>();
//        for(String word : keywords) {
//            String regex = "\\b" + Pattern.quote(word) + "\\b";
//            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//            for (Product p : productList.values()) {
//                Matcher matcher = pattern.matcher(p.getDescription());
//                if(matcher.find()){
//                    if(temp.add(p)){
//                        productToNumMatches.put(p,1);
//                    }
//                    else{
//                        productToNumMatches.put(p,productToNumMatches.get(p)+1);
//                    }
//                }
//            }
//        }
//        ArrayList<Product> matchingProducts = new ArrayList<>(temp);
//
//        matchingProducts.sort((p1, p2) -> {
//            int numMatches1 = productToNumMatches.get(p1);
//            int numMatches2 = productToNumMatches.get(p2);
//            return Integer.compare(numMatches2, numMatches1);
//        });
//
//        return matchingProducts;
//    }

//    public ArrayList<Product> getAllFromCategory(String category){
//        ArrayList<Product> result = new ArrayList<>();
//        ArrayList<Integer> prod_ids = categories.get(category);
//        if(prod_ids!= null){
//            for(Integer id : prod_ids){
//                Product p = getProduct(id);
//                if(p!=null){
//                    result.add(p);
//                }
//            }
//        }
//        return result;
//    }

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
            if(Objects.equals(p.name, name)){
                return p;
            }
        }
        return null;
    }

    public synchronized HashMap<Integer, Integer> getPrices() {
        HashMap<Integer,Integer> prices = new HashMap<>();
        for(Product p : this.productList.values()){
            prices.put(p.getID(),p.getQuantity());
        }
        return prices;
    }

    public List<ProductInfo> getProducts() {
        List<ProductInfo> productInfos = new LinkedList<>();
        for (Product p : productList.values()){
            ProductInfo info = new ProductInfo(p.getID(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity());
            info.setCategories(getProductCategories(p.getID()));
            productInfos.add(info);
        }
        return productInfos;
    }
    public void addToCategory(String category, int productId){
        category =category.toLowerCase();
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

    public void updateProduct(int productId, List<String> categories,String name, String description, int price, int quantity) throws Exception{
        if(productList.containsKey(productId)){
            if(categories!=null){
                replaceCategories(productId,categories);
            }
            else
                throw new Exception("categories empty");
            if(name!=null){
                setName(productId,name);
            }
            else
                throw new Exception("name is empty");
            if(description!=null){
                setDescription(productId,description);
            }
            else
                throw new Exception("description is empty");
            if(price > 0){
                setPrice(productId,price);
            }
            else
                throw new Exception("price is illegal");
            if(quantity > 0){
                replaceQuantity(productId,quantity);
            }
            else
                throw new Exception("quantity is illegal");
        }
    }

    private void replaceQuantity(int productId, int quantity) {
        Product p = getProduct(productId);
        p.replaceQuantity(quantity);
    }

    private void setName(int productId, String name) {
        Product p = getProduct(productId);
        p.setName(name);
    }

    private void replaceCategories(int productId, List<String> categories) {
        for(ArrayList<Integer> category: this.categories.values()){
            if(category.contains(productId)){
                category.remove(Integer.valueOf(productId));
            }
        }
        for(String category: categories){
            addToCategory(category,productId);
        }
    }

    public void setProductsRatings(){
        for(Product p : productgrading.keySet()){
            double sum = 0;
            for(int rating : productgrading.get(p)){
                sum += rating;
            }
            p.setRating(sum/productgrading.get(p).size());
        }
    }
    public ArrayList<ProductInfo> filterBy(ProductFilter filter,double storeRating) {
        filter.setOp(this::getProduct);
        filter.setCategories(categories);
        filter.setStoreRating(storeRating);
        setProductsRatings();
        //need to set more relevant things here as soon as all filters are implemented.
        ArrayList<Product> filtered = filter.filter(new ArrayList<>(productList.values()));
        ArrayList<ProductInfo> result = new ArrayList<>();
        for(Product p: filtered){
            ProductInfo info = p.getProductInfo();
            info.setCategories(getProductCategories(p.getID()));
            result.add(info);
        }
        return result;
    }
}