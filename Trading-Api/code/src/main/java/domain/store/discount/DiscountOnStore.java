package domain.store.discount;

import utils.orderRelated.Order;

import java.util.HashMap;

public class DiscountOnStore extends SimpleDiscount{

    public DiscountOnStore(int price, String discountedCategory) {
        super(price, discountedCategory);
    }

    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) {
        double newPrice = 0;
        HashMap<Integer,HashMap<Integer,Integer>> prices = order.getPrices();
//        order.setTotalPrice((int)(price * getPercentage()/100));
        for(Integer storeId : prices.keySet()){
            for(Integer prodId : prices.get(storeId).keySet()){
                int oldPrice = prices.get(storeId).get(prodId);
                newPrice += (oldPrice - (oldPrice * getPercentage()/100));
//                prices.get(storeId).put(prodId,(int));
            }
        }
        return newPrice;
    }
}
