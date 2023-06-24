package domain.store.purchase;

import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;

public class CategoryPolicy extends PurchasePolicy{
    public String category;
    public int amount;
    public limiters limiter;
    public CategoryPolicy(int policyID,int storeID,String category, int amount, limiters limiter){
        this.storeID = storeID;
        this.category = category;
        this.amount = amount;
        this.limiter = limiter;
        this.policyID = policyID;
    }
    @Override
    public boolean validate(Order order) throws Exception {
        int count=0;
        for (ProductInfo pI : order.getShoppingCart().getBasket(storeID).getContent()){
            if(pI.getCategories().contains(category)){
                count += pI.getQuantity(); //counts how much items that belongs to "category" appears in the order
            }
        }
        return handleNext(switch (limiter){
            case Max -> handleMax(count);
            case Min -> handleMin(count);
            case Exact -> count == amount;
        },order);
    }

    public boolean handleMax(int numFromCategory){
        return numFromCategory <= amount;
    }

    public boolean handleMin(int numFromCategory){
        return numFromCategory >= amount;
    }


}
