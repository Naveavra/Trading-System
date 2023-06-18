package domain.store.purchase;

import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;


import static domain.store.purchase.PurchasePolicy.limiters.*;

/**
 * a policy for an item, means that the cart needs to have less/more than a required amount.
 */
public class ItemPolicy extends PurchasePolicy{
    public int productId;
    public int amount;
    public limiters limiter;
    public ItemPolicy(int policyID,int storeID,int prodID,int amount, limiters limiter){
        this.policyID = policyID;
        this.storeID = storeID;
        this.productId = prodID;
        this.limiter = limiter;
        this.amount = amount;
    }
    @Override
    public boolean validate(Order order) throws Exception {
        for(ProductInfo productInfo : order.getShoppingCart().getBasket(storeID).getContent()){
            if(productInfo.id == productId){
                return handleNext(switch (limiter){
                    case Max ->  handleMax(productInfo);
                    case Min ->  handleMin(productInfo);
                    case Exact -> productInfo.quantity == amount;
                },order);
            }
        }
        return handleNext(false,order);
    }

    private boolean handleMin(ProductInfo prod){
        return prod.quantity >= amount;
    }
    private boolean handleMax(ProductInfo prod){
        return prod.quantity <= amount;
    }


}
