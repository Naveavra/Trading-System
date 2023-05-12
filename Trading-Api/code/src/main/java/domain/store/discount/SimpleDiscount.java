package domain.store.discount;

import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.product.Inventory;
import utils.orderRelated.Order;

public abstract class SimpleDiscount implements Discount{
    //same for all discounts
    public int discountID;
    private int storeId;
    private double percentage;
    public DiscountPredicate predicate = null; //if not null it's a conditional discount.


    //    private int minBasketPrice;
    private String discountedCategory;
    private int discountedProductID = -1;

    //functional interfaces
    public GetProductOperation getProductOp;
    public GetCategoriesOperation getCategoriesOp;

    public SimpleDiscount(int price,String discountedCategory){
//        this.minBasketPrice = price;
        this.discountedCategory = discountedCategory;
    }



    // Setters
    public void setOperations(GetProductOperation getP,GetCategoriesOperation getCat){
        this.getProductOp = getP;
        this.getCategoriesOp = getCat;
    }

    @Override
    public void addPredicate(DiscountPredicate pred2Add){
        if(pred2Add!= null){
            DiscountPredicate temp = this.predicate;
            while (temp.next!=null){
                temp = temp.next;
            }
            temp.next = pred2Add;
            return;
        }
        this.predicate = pred2Add;
    }


    // Getters
    public int getStoreId() {
        return storeId;
    }
    public double getPercentage() {
        return percentage;
    }

    public void setDiscountedProductID(int discountedProductID) {
        this.discountedProductID = discountedProductID;
    }

    public int getDiscountedProductID() {
        return discountedProductID;
    }

    public String getDiscountedCategory() {
        return discountedCategory;
    }

//    public void setPercentage(double percentage) {
//        this.percentage = percentage;
//    }
}
