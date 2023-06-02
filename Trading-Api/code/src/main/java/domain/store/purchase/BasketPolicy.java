package domain.store.purchase;
import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;

public class BasketPolicy implements PurchasePolicy{
    public int storeID;
    public int productID;
    public int amount;
    public BasketPolicy(int storeID,int productID,int amount){
        this.storeID = storeID;
        this.productID = productID;
        this.amount = amount;
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
}
