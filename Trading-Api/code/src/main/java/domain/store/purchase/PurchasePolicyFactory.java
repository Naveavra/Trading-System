package domain.store.purchase;

import java.util.Objects;

public class PurchasePolicyFactory {

    public PurchasePolicyFactory(){

    }
    public PurchasePolicy createPolicy() throws Exception{
        throw new Exception("Place Holder for the real function create policy");
    }
    public PurchasePolicy createPolicy(PurchasePolicyDataObject policyData) throws Exception {
        PurchasePolicy policy = switch (policyData.type) {
            case Category -> createCategoryPolicy(policyData);
            case Item -> createItemPolicy(policyData);
            case User -> createUserPolicy(policyData);
            case Basket -> createBasketPolicy(policyData);
            case DateTime -> createDateTimePolicy(policyData);
        };
        if(policyData.next!=null){
            policy.setNext(createPolicy(policyData.next),policyData.composure);
        }
        return policy;
    }

    private PurchasePolicy createDateTimePolicy(PurchasePolicyDataObject policyData) throws Exception {
        if((policyData.dateLimit.length ==0 && policyData.timeLimit.length == 0 ) || policyData.limiter == null || policyData.category == null){
            throw new Exception("Some information is missing in creating dateTime policy, please check all required fields:\n dateLimit, timeLimit, limiter, category");
        }
        return new DateTimePolicy(policyData.policyID, policyData.storeID, policyData.category, policyData.limiter, policyData.dateLimit, policyData.timeLimit);
    }

    private PurchasePolicy createBasketPolicy(PurchasePolicyDataObject policyData) throws Exception {
        if(policyData.productID < 0 || policyData.amount ==0){
            throw new Exception("Some information is missing in creating basket purchase policy, please check all required fields: \n productID, amount");
        }
        return new BasketPolicy(policyData.policyID, policyData.storeID, policyData.productID, policyData.amount);
    }

    private PurchasePolicy createUserPolicy(PurchasePolicyDataObject policyData) throws Exception {
        if(policyData.ageLimit>=0 && policyData.productID >=0 && policyData.limiter!=null){
            return new UserPolicy(policyData.policyID, policyData.storeID, policyData.ageLimit, policyData.productID,policyData.limiter);
        }
        if(policyData.ageLimit>=0 && !Objects.equals(policyData.category,"") && policyData.limiter!=null){
            return new UserPolicy(policyData.policyID, policyData.storeID, policyData.ageLimit, policyData.category,policyData.limiter);
        }
        throw new Exception("Some information is missing in creating user purchase policy, please check all required fields:\n productID or Category, ageLimit,limiter");
    }

    private PurchasePolicy createItemPolicy(PurchasePolicyDataObject policyData) throws Exception {
        if(policyData.productID < 0 || policyData.limiter ==null || policyData.amount == 0){
            throw new Exception("Some Information is missing in creating item purchase policy, please check all required fields:\n productID,amount,limiter");
        }
        return new ItemPolicy(policyData.policyID, policyData.storeID, policyData.productID, policyData.amount, policyData.limiter);
    }

    private PurchasePolicy createCategoryPolicy(PurchasePolicyDataObject policyData) throws Exception {
        if(Objects.equals(policyData.category, "") || policyData.amount == 0 || policyData.limiter == null){
            throw new Exception("Some Information is missing in creating category purchase policy, please check all required fields:\n category,amount,limiter");
        }
        return new CategoryPolicy(policyData.policyID,policyData.storeID, policyData.category, policyData.amount, policyData.limiter);
    }

}
