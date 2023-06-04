package domain.store.purchase;
import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;

public class BasketPolicy implements PurchasePolicy{
    public int policyID;
    public int storeID;
    public int productID;
    public int amount;
    public String content;
    public BasketPolicy(int policyID,int storeID,int productID,int amount){
        this.storeID = storeID;
        this.productID = productID;
        this.amount = amount;
        this.policyID = policyID;
    }

    @Override
    public boolean validate(Order order) throws Exception {
        for(ProductInfo pI : order.getShoppingCart().getBasket(storeID).getContent()){
            if(pI.id == productID && pI.quantity == amount){
                return true;
            }
        }
        return false;
    }
    @Override
    public String getContent(){
        return this.content;
    }
    @Override
    public int getId(){
        return this.policyID;
    }
    public void setContent(String content){
        this.content = content;
    }
}
