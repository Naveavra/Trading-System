package domain.store.purchase;
import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;

public class BasketPolicy extends PurchasePolicy{

    public int productID;
    public int amount;
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
                return handleNext(true,order);
            }
            if(pI.id == productID){
                return handleNext(true,order);
            }
        }
        return handleNext(false,order);
    }

}
