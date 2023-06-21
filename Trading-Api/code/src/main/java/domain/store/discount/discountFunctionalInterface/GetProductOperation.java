package domain.store.discount.discountFunctionalInterface;

import domain.store.product.Product;
import org.hibernate.Session;

public interface GetProductOperation {
    Product getProduct(int prodId) throws Exception;
}
