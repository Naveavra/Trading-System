package utils.Filter;

import java.util.ArrayList;

import domain.store.product.Product;

public class FilterProductByPriceMax extends FilterStrategy{
    int maxPrice = 0;
    public FilterProductByPriceMax(){


    }
    public void setMaxPrice(int price){
        if(price>=0){
            maxPrice = price;
        }
    }
    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        ArrayList<Product> res = new ArrayList<>();
        for(Product p : products){
            if(p.getPrice()<= maxPrice) res.add(p);
        }
        if(getNext()!=null){
            res = getNext().filter(res);
        }
        return res;
    }
}
