package domain.store.discount.predicates;

import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.user.Basket;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * amount from category required in basket
 */
public class DiscountCategoryPredicate extends DiscountPredicate {
    public PredicateTypes type;
    private String category;
    private int quantity;
    private GetCategoriesOperation getCategories;

    public DiscountCategoryPredicate(String category,int quantity,PredicateTypes type, GetCategoriesOperation op, int storeId){
        this.category = category;
        this.quantity = quantity;
        this.type = type;
        this.getCategories = op;
        this.storeId = storeId;
    }
    @Override
    public boolean checkPredicate(Order order) {
        boolean answer = false;
        int count = 0;
        Basket productsInStore = order.getShoppingCart().getBasket(storeId);
        for(ProductInfo product : productsInStore.getContent()){
            ArrayList<String> categories = getCategories.getProductCategories(product.getId());
            if(categories.contains(category))
                count++;
        }
        switch (type){
            case MinNumFromCategory -> answer = handleMin(count);
            case MaxNumFromCategory -> answer = handleMax(count);
        }

        return handleNext(order,answer);
    }

    @Override
    public boolean handleMax(double quantity) {
        return this.quantity >= quantity;
    }

    @Override
    public boolean handleMin(double quantity) {
        return this.quantity <= quantity;
    }
}
