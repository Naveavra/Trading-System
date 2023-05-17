package utils.Filter;

import java.util.ArrayList;

import domain.store.product.Product;
import domain.store.storeManagement.Store;

public class FilterProductByStoreRating extends FilterStrategy{
    private double storeRating;
    private double filterBy;
    public FilterProductByStoreRating() {
        this.name = "StoreRating";
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        if(storeRating>=filterBy){
            FilterStrategy nextOne = getNext();
            if(nextOne!=null){
                products = nextOne.filter(products);
            }
            return products;
        }
        return new ArrayList<>();
    }
    @Override
    public void setStoreRating(double storeRating){
        this.storeRating = storeRating;
    }


    public void setStoreRatingToFilterBy(int parseInt) {
        filterBy = parseInt;
    }
}
