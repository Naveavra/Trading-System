package domain.user;

import domain.store.Product;

import java.util.LinkedList;
import java.util.List;

public class Basket {
    private List<Product> productList; //a list of all the product related to a specific shop


    public Basket(){
        productList = new LinkedList<>();
    }
}
