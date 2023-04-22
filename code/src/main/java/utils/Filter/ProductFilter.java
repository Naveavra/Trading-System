package utils.Filter;


import domain.store.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class is the base class for filtering products.
 * You should'nt create the FilterStrategy anywhere else but here, you can get the strategies
 * by calling getStrategy(Enum strategies), types are listed below.                   
 * Enums: ProductRating, PriceRangeMax, PriceRangeMin,StoreRating
 */
public class ProductFilter{
    public enum strategies{
        ProductRating,PriceRangeMax,PriceRangeMin,StoreRating
    }
    public ProductFilter(){

    }
    public FilterStrategy getStrategy(strategies strategy){
        switch(strategy) {
            case ProductRating:
                return new FilterProductByRating();
            case PriceRangeMax:
                return new FilterProductByPriceMax();
            case PriceRangeMin:
                return new FilterProductByPriceMin();
            case StoreRating:
                return new FilterProductByStoreRating();
            default:
                return null;
        }
    }
    /**
     * This is the base class for filter, you should create the linked strategies first and send it as
     * strategy parameter
     * @param products
     * @param strategy type: FilterStrategy
     * @return
     */
    public ArrayList<Product> filter(ArrayList<Product> products,FilterStrategy strategy){
        return strategy.filter(products);
    }
    
}
