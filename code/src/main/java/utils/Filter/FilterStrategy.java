package utils.Filter;

import domain.store.product.Product;

import java.util.ArrayList;


public abstract class FilterStrategy {
    private FilterStrategy next;
    public void setNext(FilterStrategy strategy){
        next = strategy;
    }
    public FilterStrategy getNext(){
        return next;
    }
    public abstract ArrayList<Product>filter(ArrayList<Product> products);
}
