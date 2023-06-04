package domain.store.purchase;

import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

public class UserPolicy extends PurchasePolicy{

    public int ageLimit;
    public limiters limiter;
    public int productID;
    public String category;
    public UserPolicy(int policyID,int storeID,int ageLimit,int productID,limiters limiter){
        this.storeID = storeID;
        this.policyID = policyID;
        this.ageLimit = ageLimit;
        this.limiter = limiter;
        this.productID = productID;
        this.category = "";
    }
    public UserPolicy(int policyID,int storeID,int ageLimit,String category,limiters limiter){
        this.storeID = storeID;
        this.policyID = policyID;
        this.ageLimit = ageLimit;
        this.limiter = limiter;
        this.productID = -1;
        this.category = category;
    }
    @Override
    public boolean validate(Order order) throws Exception {
        int age = order.getUser().getAge();
        if(age == -1){
            throw new Exception("A Guest cannot purchase age limiter products.");
        }
        for(ProductInfo pI : order.getShoppingCart().getBasket(storeID).getContent()){
            if(pI.id == productID || pI.getCategories().contains(category)){
                return handleNext(switch (limiter){
                    case Max -> age <= ageLimit;
                    case Min -> age >= ageLimit;
                },order);
            }
        }
        return handleNext(false,order);
    }


}
