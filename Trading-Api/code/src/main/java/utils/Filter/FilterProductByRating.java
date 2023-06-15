package utils.Filter;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
        products= products.stream()
                .filter(product -> product.getRating() > rating)
                .collect(Collectors.toCollection(ArrayList::new));
        FilterStrategy nextOne = getNext();
        if(nextOne!=null){
            products = nextOne.filter(products);
        }
        return products;
    }

    @Override
    public void setRating(int rating){
        this.rating=rating;
    }

}
