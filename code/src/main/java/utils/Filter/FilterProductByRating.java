package utils.Filter;

import java.util.ArrayList;

import domain.store.product.Product;

public class FilterProductByRating extends FilterStrategy{
    private int rating;

    public FilterProductByRating() {
        this.name = "ProductRating";
    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filter'");
    }
    @Override
    public void setRating(int rating){
        this.rating=rating;
    }

}
