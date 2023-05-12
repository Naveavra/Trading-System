package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.Discount;

import java.util.ArrayList;

public abstract class AbstractDiscountComposite implements Discount {
    enum logical {And,Or,Xor};
    enum numeric {Max,Addition};
    private ArrayList<Discount> discounts;
    public AbstractDiscountComposite(){
        discounts = new ArrayList<>();
    }
}
