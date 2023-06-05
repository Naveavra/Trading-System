package domain.store.purchase;
import utils.orderRelated.Order;

public abstract class PurchasePolicy {
    public enum limiters{Min,Max}
    public enum policyTypes{Basket,Category,DateTime,Item,User};
    public enum policyComposeTypes{PolicyAnd,PolicyOr,PolicyConditioning}

    public int policyID;
    public String content;
    public int storeID;
    public PurchasePolicy next;
    public policyComposeTypes composeType;

    public abstract boolean validate(Order order) throws Exception;
    public String getContent(){
        return this.content;
    }
    public int getId(){
        return this.policyID;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setNext(PurchasePolicy next,policyComposeTypes type){
        this.next = next;
        this.composeType = type;
    }
    public boolean handleNext(boolean res,Order order) throws Exception {
        if(composeType==null){
            return res;
        }
        return switch (composeType){
            case PolicyOr -> res || next.validate(order);
            case PolicyAnd ->  res && next.validate(order);
            case PolicyConditioning -> next.validate(order);
        };
    }
}
