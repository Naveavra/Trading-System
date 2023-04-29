package utils.Filter;

import domain.store.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public abstract class FilterStrategy {
    private FilterStrategy next;
    public GetProductOperation getProductOp;
    public String name;
    public FilterStrategy(){};
    public boolean setOp(GetProductOperation op){
        getProductOp = op;
        if(getNext()!= null){
            next.setOp(op);
        }
        return true;

    }
    public abstract String getName();
    public void setNext(FilterStrategy strategy){
        next = strategy;
    }
    public FilterStrategy getNext(){
        return next;
    }
    public abstract ArrayList<Product>filter(ArrayList<Product> products);

    /**
     * default for all filter classes, only real instances that require the rating will override this function
     * @param storeRating int
     */
    public void setStoreRating(double storeRating){
    }
    public void setRating(int rating){
    }
    public void setCategories(ConcurrentHashMap<String,ArrayList<Integer>> cat){
    }

}
