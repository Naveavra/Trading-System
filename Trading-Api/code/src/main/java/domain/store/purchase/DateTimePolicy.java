package main.java.domain.store.purchase;

import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;
import static domain.user.StringChecks.curDay;

public class DateTimePolicy implements PurchasePolicy{
    public int storeID;
    public int[] dateLimit;
    public int[] timeLimit;
    public limiters limiter;
    public String category;

    /**
     * will allways return true for datelimit, timelimit null values.
     * @param storeID
     * @param category
     * @param limiter enum {Min,Max}
     * @param dateLimit should be int[] = [0,0,0] for null value
     * @param timeLimit should be int[] = [0,0,0] for null value
     */
    public DateTimePolicy(int storeID,String category, limiters limiter,int[] dateLimit,int[]timeLimit){
        this.storeID = storeID;
        this.category = category;
        this.limiter = limiter;
        this.dateLimit = dateLimit;
        this.timeLimit = timeLimit;
    }
    @Override
    public boolean validate(Order order) throws Exception {
        boolean contains = false;
        for(ProductInfo pI : order.getShoppingCart().getBasket(storeID)){
            if(pI.getCategories().contains(category)){
                contains = true;
                break;
            }
        }
        if(!contains)
            return true;
        int[] curDay = curDay(); //first 3 are for date, next 3 are for time
        int[] curDate = {curDay[0],curDay[1],curDay[2]};
        int[] curTime = {curDay[3],curDay[4], curDay[5]};
        if(!checkLimitValues(dateLimit)){
            return switch (limiter){
                case Min -> handleMin(curDate,dateLimit);
                case Max -> handleMax(curDate,dateLimit);
            };
        }
        if(!checkLimitValues(timeLimit)){
            return switch (limiter){
                case Min -> handleMin(curTime,timeLimit);
                case Max -> handleMax(curTime,timeLimit);
            };
        }
        return true;
    }

    private boolean handleMax(int[] curDay, int[] limit) {
        return curDay[0] <= limit[0] || (curDay[1] <= limit[1] || (curDay[2] <= limit[2]));
    }

    private boolean handleMin(int[] curDay, int[] limit) {
        return curDay[0] >= limit[0] || (curDay[1] >= limit[1] || (curDay[2] >= limit[2]));
    }

    public boolean checkLimitValues(int[] limit){
        if(limit[0] == 0 && limit[1] == 0 && limit[2] == 0)
            return true;
        return false;
    }
}
