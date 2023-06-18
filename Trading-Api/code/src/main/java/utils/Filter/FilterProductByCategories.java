package utils.Filter;

import domain.store.product.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * filters by categories, need to set categories first with setCategories() function;
 */
public class FilterProductByCategories extends FilterStrategy{
    private ConcurrentHashMap<String,List<Integer>> categories;
    private ArrayList<String> categoriesToSearchBy;

    public FilterProductByCategories() {
        this.name = "Categories";
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        ArrayList<Product> matchingProducts = new ArrayList<>();
        Map<Product, Integer> productToNumMatches = new HashMap<>();
        for(String cat: categoriesToSearchBy){
            List<Integer> prod_ids = this.categories.get(cat);
            if(prod_ids!= null){
                for(Integer prodid : prod_ids){
//                    Product p = getProductOp.getProduct(prodid);
                    Product p = null;
                    for(Product prod: products){
                        if(prod.getID()==prodid)
                            p = prod;
                    }
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
        FilterStrategy nextOne = getNext();
        if(nextOne!=null){
            matchingProducts = nextOne.filter(matchingProducts);
        }
        return matchingProducts;
    }

    /**
     * set the categories to filter by
     * @param cat
     */
    public void setCategoriesToSearchBy(ArrayList<String> cat){
        categoriesToSearchBy = cat;
    }
    /**
     * set the categories to filter (ALL CATEGORIES)
     * @param cat ConcurrentHashMap<String,ArrayList<Integer>>
     */
    @Override
    public void setCategories(ConcurrentHashMap<String, List<Integer>> cat){
        this.categories=cat;
    }


}
