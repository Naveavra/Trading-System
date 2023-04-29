package utils.Filter;


import domain.store.product.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
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
     * @return
     */
    public FilterStrategy createStrategy(strategies strategy,String params){
        FilterStrategy strat = null;
        switch(strategy) {
            case ProductRating: // ProductRating <rating number>
                FilterProductByRating temp1 =  new FilterProductByRating();
                temp1.setRating(Integer.parseInt(params)); //expect only one number here
                strat = temp1;
            case PriceRangeMax:// PriceRangeMax <max number>
                FilterProductByPriceMax temp2 = new FilterProductByPriceMax();
                temp2.setMaxPrice(Integer.parseInt(params));
                strat = temp2;
            case PriceRangeMin:// PriceRangeMin <min number>
                FilterProductByPriceMin temp3 =  new FilterProductByPriceMin();
                temp3.setMinPrice(Integer.parseInt(params));
                strat = temp3;
            case StoreRating:// StoreRating <rating number>
                FilterProductByStoreRating temp4 = new FilterProductByStoreRating();
                temp4.setStoreRatingToFilterBy(Integer.parseInt(params));
                strat = temp4;
            case Categories:// Categories <categories divided by spaces>
                FilterProductByCategories temp5 =  new FilterProductByCategories();
                ArrayList<String> categoriesToSearchBy = new ArrayList<>(Arrays.asList(params.split(" ")));
                temp5.setCategoriesToSearchBy(categoriesToSearchBy);
                strat = temp5;
            case Keywords:// Keywords <divided by spaces>
                FilterProductByKeywords temp6 = new FilterProductByKeywords();
                ArrayList<String> keywordsToSearchBy = new ArrayList<>(Arrays.asList(params.split(" ")));
                temp6.setKeywords(keywordsToSearchBy);
                strat = temp6;
            default:
                return null;
        }
    }
    public boolean setOp(GetProductOperation op){
        return baseFilter == null ? false : baseFilter.setOp(op);
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
    private FilterStrategy getFilterStrategy(String name){
        FilterStrategy temp = baseFilter;
        while(temp!=null){
            if(Objects.equals(temp.getName(), name)){
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }
    public void setStoreRating(double storeRating){
        FilterStrategy strategy = getFilterStrategy("StoreRating");
        if(strategy!=null) {
            strategy.setStoreRating(storeRating);
        }
    }
//    public void setRating(int rating){
//        FilterStrategy strategy = getFilterStrategy("ProductRating");
//        if(strategy!=null) {
//            strategy.setRating(rating);
//        }
//    }
    public void setCategories(ConcurrentHashMap<String,ArrayList<Integer>> cat){
        FilterStrategy strategy = getFilterStrategy("Categories");
        if(strategy!=null) {
            strategy.setCategories(cat);
        }
    }
}
