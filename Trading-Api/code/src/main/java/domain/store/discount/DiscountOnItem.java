package domain.store.discount;


import utils.orderRelated.Order;

import java.util.HashMap;

public class DiscountOnItem extends AbstractDiscount {


    public DiscountOnItem(int discountID, int storeId, double percentage, String discountedCategory) {
        super(discountID, storeId, percentage, discountedCategory);
    }

    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) {
        if(predicate!=null && !predicate.checkPredicate(order)){
            return 0;
        }
        double newPrice = 0;
        double discountedProdId = getDiscountedProductID();
        HashMap<Integer,HashMap<Integer,Integer>> prices = order.getPrices();
        if(discountedProdId != -1 && basket.containsKey(discountedProdId)){
////            Product p = getProductOp.getProduct(discountedProdId);
            int storeId = getStoreId();
            double oldPrice = prices.get(storeId).get(discountedProdId);
            newPrice = oldPrice - (oldPrice * 100/getPercentage());
//            prices.get(storeId).put(discountedProdId,newPrice);
////            order.setTotalPrice(oldTotalPrice);
        }
        return newPrice;
//
    }
}
