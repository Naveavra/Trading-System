package domain.store.discount;

import domain.store.product.Product;
import domain.user.Basket;
import utils.infoRelated.Information;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.HashMap;

public class DiscountOnCategory extends AbstractDiscount {
    public DiscountOnCategory(int discountID, int storeId, double percentage, String discountedCategory) {
        super(discountID, storeId, percentage, discountedCategory);
    }

    @Override
    public double handleDiscount(Basket basket, Order order) throws Exception {
        if(predicate!=null && !predicate.checkPredicate(order)){
            return 0;
        }
        double newPrice = 0;
        String discountedCategory = getDiscountedCategory();
        HashMap<Integer,HashMap<Integer,Integer>> prices = order.getPrices();
        for(ProductInfo product: basket.getContent()){
            Product p = getProductOp.getProduct(product.getId());
            if(p.belongsToCategory(discountedCategory)){
                int storeId = getStoreId();
                int oldPrice = prices.get(storeId).get(product.getId());
                newPrice += (oldPrice - (oldPrice * getPercentage()/100));
//                prices.get(storeId).put(prodId,newPrice);

            }
        }
        return newPrice;
    }

}
