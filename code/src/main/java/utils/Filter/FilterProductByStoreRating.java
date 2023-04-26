package utils.Filter;

import java.util.ArrayList;

import domain.store.product.Product;
import domain.store.storeManagement.Store;

public class FilterProductByStoreRating extends FilterStrategy{
    private double storeRating;
    public FilterProductByStoreRating() {
        this.name = "StoreRating";
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        //TODO 
        throw new UnsupportedOperationException("Unimplemented method 'filter'");
    }
    @Override
    public void setStoreRating(double storeRating){
        this.storeRating = storeRating;
    }

    
}
