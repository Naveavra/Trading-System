package utils.Filter;

import java.util.ArrayList;

import domain.store.product.Product;

public class FilterProductByPriceMin extends FilterStrategy{
    int minPrice = 0;
    public FilterProductByPriceMin(){

    }
    public void setMinPrice(int price){
        if(price>=0){
            minPrice = price;
        }
    }
    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        ArrayList<Product> res = new ArrayList<>();
        for(Product p : products){
            if(p.getPrice()>= minPrice) res.add(p);
        }
        if(getNext()!=null){
            res = getNext().filter(res);
        }
        return res;
    }
}
