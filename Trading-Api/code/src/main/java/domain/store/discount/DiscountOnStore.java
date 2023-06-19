package domain.store.discount;

import domain.user.Basket;
import utils.infoRelated.Information;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscountOnStore extends AbstractDiscount {
    public DiscountOnStore(int discountID, int storeId, double percentage, String discountedCategory) {
        super(discountID, storeId, percentage, discountedCategory);
    }

    @Override
    public double handleDiscount(Basket basket, Order order) {
        if(predicate!=null && !predicate.checkPredicate(order)){
            return 0;
        }
        double newPrice = 0;
        HashMap<Integer,HashMap<Integer,Integer>> prices = order.getPrices();
//        order.setTotalPrice((int)(price * getPercentage()/100));
        for(Integer prodId : prices.get(getStoreId()).keySet()){
            int oldPrice = prices.get(getStoreId()).get(prodId);
            newPrice += (oldPrice - (oldPrice * getPercentage()/100));
//                prices.get(storeId).put(prodId,(int));
        }

        return newPrice;
    }

}
