package domain.store.product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductController {

    ConcurrentHashMap<Integer, Product> productList; // <id, product>
//    ConcurrentHashMap<Product, ArrayList<String>> categories;
    ConcurrentHashMap<String,ArrayList<Integer>> categories; // <Category String,<List<ProductID>>
    ConcurrentHashMap<Product, ArrayList<Integer>> productgrading;
//    AtomicInteger prod_id = new AtomicInteger();
    public ProductController(){
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
    public void addProduct(String name,String description,AtomicInteger prod_id){
        if(getProductByName(name)==null){
            int id = prod_id.getAndIncrement();
            Product p = new Product(id,name,description);
            productList.put(id,p);
//            categories.put(p,new ArrayList<>());
        }
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
        Product p = getProduct(prodID);
        if(p != null){
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
//    public void addCategory(Integer prodID,String category){
//        category = category.toLowerCase();
//        Product p = getProduct(prodID);
//        if(p!=null && !categories.get(p).contains(category)){
//            categories.get(p).add(category);
//        }
//    }
    public ArrayList<String> getAllCategories(){
//        ArrayList<String> res = new ArrayList<>();
//        for(Product p: categories.keySet()){
//            for(String cat : categories.get(p)){
//                if(!res.contains(cat)){
//                    res.add(cat);
//                }
//            }
//        }
//        return  res;
        return new ArrayList<>(categories.keySet());
    }

    /**
     * This method takes an ArrayList of categories as input and returns an ArrayList
     * of matching products sorted by the number of categories that they match (from most to least)
     */
    public ArrayList<Product> getProductByCategories(ArrayList<String> categories) {
        ArrayList<Product> matchingProducts = new ArrayList<>();
        Map<Product, Integer> productToNumMatches = new HashMap<>();
        for(String cat: categories){
            ArrayList<Integer> prod_ids = this.categories.get(cat);
            if(prod_ids!= null){
                for(Integer prodid : prod_ids){
                    Product p = getProduct(prodid);
                    if(p!= null && !matchingProducts.contains(p)){
                        matchingProducts.add(p);
                        productToNumMatches.put(p,1);
                    } else if (p != null) {
                        productToNumMatches.put(p,productToNumMatches.get(p)+1);
                    }
                }
            }
        }

        matchingProducts.sort((p1, p2) -> {
            int numMatches1 = productToNumMatches.get(p1);
            int numMatches2 = productToNumMatches.get(p2);
            return Integer.compare(numMatches2, numMatches1);
        });

        return matchingProducts;
    }

    public ArrayList<Product> getProductByDescription(ArrayList<String> keywords) {
        ArrayList<Product> matchingProducts = new ArrayList<>();
        Map<Product, Integer> productToNumMatches = new HashMap<>();
        for(Integer p : productList.keySet())
//        for (Map.Entry<Integer, Product> entry : productList.entrySet()) {
//            Product product = entry.getValue();
//            int numMatches = 0;
//            String description = product.getDescription().toLowerCase();
//
//            for (String searchString : keywords) {
//                if (description.contains(searchString.toLowerCase())) {
//                    numMatches++;
//                }
//            }
//
//            if (numMatches > 0) {
//                matchingProducts.add(product);
//                productToNumMatches.put(product, numMatches);
//            }
//        }

        matchingProducts.sort((p1, p2) -> {
            int numMatches1 = productToNumMatches.get(p1);
            int numMatches2 = productToNumMatches.get(p2);
            return Integer.compare(numMatches2, numMatches1);
        });

        return matchingProducts;
    }

    public ArrayList<Product> getAllFromCategory(String category){
        ArrayList<Product> prodForCategory = new ArrayList<>();
        for(Product p : categories.keySet()){
            if(categories.get(p).contains(category)){
                prodForCategory.add(p);
            }
        }
        return prodForCategory;
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
//    public List<String> getCategories(Integer prodID){
//        Product p = getProduct(prodID);
//        if(p != null){
//            return categories.get(p);
//        }
//        return null;
//    }
    public Product getProductByName(String name){
        for(Product p : productList.values()){
            if(Objects.equals(p.name, name)){
                return p;
            }
        }
        return null;
    }
}
