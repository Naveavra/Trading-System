package domain.store.discount.discountFunctionalInterface;

import domain.store.product.Product;

public interface GetProductOperation {
    Product getProduct(int prodId) throws Exception;
}
