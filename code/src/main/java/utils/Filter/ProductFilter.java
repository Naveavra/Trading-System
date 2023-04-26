package utils.Filter;


import domain.store.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is the base class for filtering products.
 * You should'nt create the FilterStrategy anywhere else but here, you can get the strategies
 * by calling getStrategy(Enum strategies), types are listed below.                   
 * Enums: ProductRating, PriceRangeMax, PriceRangeMin,StoreRating
 */
public class ProductFilter{
    public enum strategies{
        ProductRating,PriceRangeMax,PriceRangeMin,StoreRating,Categories,Keywords
    }
    private ArrayList<String> filterNames;
    private ConcurrentHashMap<Integer,Product> products;
    private FilterStrategy baseFilter;
    public ProductFilter(){
        filterNames = new ArrayList<>();
        for(strategies st : strategies.values()){
            filterNames.add(st.name());
        }
    }

    /**
     * @return a list of all filter options
     */
    public ArrayList<String> getNames(){
        return filterNames;
    }

    /**
     * @param name of filter option (String)
     * @return the enum represented by name
     */
    public strategies getStrategy(String name){
        for(String s: filterNames){
            if(s.equalsIgnoreCase(name)){
                return strategies.valueOf(s);
            }
        }
        return null;
    }

    /**
     * factory function for FilterStrategies
     * @param strategy the value which we got from getStrategy
     * @param op getProduct from inventory
     * @return
     */
    public FilterStrategy createStrategy(strategies strategy){
        switch(strategy) {
            case ProductRating: // ProductRating <rating number>
                return new FilterProductByRating();
            case PriceRangeMax:// PriceRangeMax <max number>
                return new FilterProductByPriceMax();
            case PriceRangeMin:// PriceRangeMin <min number>
                return new FilterProductByPriceMin();
            case StoreRating:// StoreRating <rating number>
                return new FilterProductByStoreRating();
            case Categories:// Categories <categories divided by spaces>
                return new FilterProductByCategories();
            case Keywords:// Keywords <divided by spaces>
                return new FilterProductByKeywords();
            default:
                return null;
        }
    }
    /**
     * This is the base class for filter, you should use the addStrategy method before activating this function for it
     * @param products
     * @return
     */
    public ArrayList<Product> filter(ArrayList<Product> products){
        return baseFilter==null ? null : baseFilter.filter(products);
    }

    /**
     * add a FilterStrategy to the chain of responsibility.
     * @param next FilterStrategy
     */
    public void addStrategy(FilterStrategy next){
        if(baseFilter == null){
            baseFilter = next;
        }
        else{
            FilterStrategy temp = baseFilter;
            while((temp = temp.getNext()) != null){
            }
            temp.setNext(next);
        }
    }
}
