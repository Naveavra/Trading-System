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
//                switch (limiter) {
//                    case Max -> {
//                        if (age > ageLimit) {
//                            throw new Exception("User is too old to purchase this product.");
//                        }
//                        break;
//                    }
//                    case Min -> {
//                        if (age < ageLimit) {
//                            throw new Exception("User is too young to purchase this product.");
//                        }
//                        break;
//                    }
//                    case Exact -> {
//                        if (age != ageLimit) {
//                            throw new Exception("User is not the right age to purchase this product.");
//                        }
//                        break;
//                    }
//                    default -> throw new Exception("Invalid limiter.");
//                }
                return handleNext(
                        switch (limiter){
                    case Max -> age <= ageLimit ;
                    case Min -> age >= ageLimit;
                    case Exact -> age == ageLimit;
                },order);
                //return true;
            }
        }
        return handleNext(false,order);
    }


}
