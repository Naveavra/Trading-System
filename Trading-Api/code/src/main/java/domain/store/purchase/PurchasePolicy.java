package domain.store.purchase;
import utils.orderRelated.Order;

public interface PurchasePolicy {
    public enum limiters{Min,Max}
    public boolean validate(Order order) throws Exception;
    public String getContent();
    public int getId();
}
