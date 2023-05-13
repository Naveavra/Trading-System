package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.Discount;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import utils.orderRelated.Order;

import java.util.HashMap;

public class NumericDiscountComposite extends AbstractDiscountComposite{
    enum numeric {Max,Addition};
    private numeric type;
    public NumericDiscountComposite(int discountID, int storeId, GetCategoriesOperation op, numeric type) {
        super(discountID, storeId, op, type);
        this.type = type;
    }


    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) {
        double discountValue = 0;
        switch (type){
            case Max -> {
                for(Discount dis : getDiscounts()){
                    double temp = dis.handleDiscount(basket,order);
                    if(discountValue<temp)
                        discountValue = temp;
                }
            }
            case Addition -> {
                for(Discount dis : getDiscounts()){
                    discountValue += dis.handleDiscount(basket,order);
                }
            }
        }
        return discountValue;
    }


}