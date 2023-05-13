package domain.store.discount.discountFunctionalInterface;

import java.util.ArrayList;

public interface GetCategoriesOperation {
    ArrayList<String> getProductCategories(int productId);
}
