package utils.Filter;

import domain.store.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public abstract class FilterStrategy {
    private FilterStrategy next;
    public GetProductOperation getProductOp;
    public FilterStrategy(){};
    public void setOp(GetProductOperation op){
        getProductOp = op;
        if(getNext()!= null){
            next.setOp(op);
        }

    }
    public void setNext(FilterStrategy strategy){
        next = strategy;
    }
    public FilterStrategy getNext(){
        return next;
    }
    public abstract ArrayList<Product>filter(ArrayList<Product> products);

}
