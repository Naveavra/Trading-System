package utils.Filter;


import domain.store.product.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductFilter implements FilterStrategy {
    public enum strategies{
        Name,Category,ProductRating,Keywords,PriceRangeMax,PriceRangeMin,StoreRating
    }
    private HashMap<strategies, FilterStrategy> mapper;

    public ArrayList<Product> filter(ArrayList<Product> products,strategies strategy){
        return mapper.get(strategy).filter(products);
    }
    public ArrayList<Product>filter(ArrayList<Product> products){

    }
}
