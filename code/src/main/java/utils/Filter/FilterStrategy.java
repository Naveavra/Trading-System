package utils.Filter;

import domain.store.product.Product;

import java.util.ArrayList;


public interface FilterStrategy {

    public ArrayList<Product> filter(ArrayList<Product> products, ProductFilter.strategies strategy);
    public ArrayList<Product>filter(ArrayList<Product> products);
}
