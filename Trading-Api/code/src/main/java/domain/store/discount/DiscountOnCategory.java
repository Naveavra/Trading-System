package domain.store.discount;

import domain.store.product.Product;
import utils.orderRelated.Order;

import java.util.HashMap;

public class DiscountOnCategory extends AbstractDiscount {
    public DiscountOnCategory(int discountID, int storeId, double percentage, String discountedCategory) {
        super(discountID, storeId, percentage, discountedCategory);
    }

    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) throws Exception {
        if(predicate!=null && !predicate.checkPredicate(order)){
            return 0;
        }
        double newPrice = 0;
        String discountedCategory = getDiscountedCategory();
        HashMap<Integer,HashMap<Integer,Integer>> prices = order.getPrices();
        for(Integer prodId: basket.keySet()){
            Product p = getProductOp.getProduct(prodId);
            if(p.belongsToCategory(discountedCategory)){
                int storeId = getStoreId();
                int oldPrice = prices.get(storeId).get(prodId);
                newPrice += (oldPrice - (oldPrice * 100/getPercentage()));
//                prices.get(storeId).put(prodId,newPrice);

            }
        }
        return newPrice;
    }
}
