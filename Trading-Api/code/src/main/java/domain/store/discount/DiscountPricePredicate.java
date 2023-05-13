package domain.store.discount;

import utils.orderRelated.Order;

public class DiscountPricePredicate extends DiscountPredicate {
    public PredicateTypes type;
    public int price;
    public DiscountPricePredicate(int price, PredicateTypes type,int storeId){
        this.price = price;
        this.type = type;
        this.storeId = storeId;
    }

    @Override
    public boolean checkPredicate(Order order) {
        double basketPrice = order.getTotalPrice();
        boolean answer = false;
        switch (type){
            case MaxPrice -> answer = handleMax(basketPrice);
            case MinPrice -> answer = handleMin(basketPrice);
        }
        return handleNext(order,answer);
    }



    public boolean handleMin(double basketPrice) {
        return basketPrice >= price;
    }

    public boolean handleMax( double basketPrice) {
        return basketPrice <= price;
    }
}
