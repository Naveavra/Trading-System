package domain.store.discount;

import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscountCategoryPredicate extends DiscountPredicate{
    public PredicateTypes type;
    private String category;
    private int quantity;
    private GetCategoriesOperation getCategories;

    public DiscountCategoryPredicate(String category,int quantity,PredicateTypes type, GetCategoriesOperation op){
        this.category = category;
        this.quantity = quantity;
        this.type = type;
        this.getCategories = op;
    }
    @Override
    public boolean checkPredicate(Order order) {
        boolean answer = false;
        int count = 0;
        HashMap<Integer,Integer> productsInStore = order.getProductsInStores().get(storeId);
        for(Integer prodId : productsInStore.keySet()){
            ArrayList<String> categories = getCategories.getProductCategories(prodId);
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
        return this.quantity <= quantity;
    }

    @Override
    public boolean handleMin(double quantity) {
        return this.quantity >= quantity;
    }
}
