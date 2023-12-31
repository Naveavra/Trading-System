package domain.store.purchase;

import utils.orderRelated.Order;
import utils.infoRelated.ProductInfo;
import static domain.user.StringChecks.curDay;

public class DateTimePolicy extends PurchasePolicy {
    public int[] dateLimit;
    public int[] timeLimit;
    public limiters limiter;
    public String category;

    /**
     * will allways return true for datelimit, timelimit null values.
     *
     * @param storeID
     * @param category
     * @param limiter   enum {Min,Max}
     * @param dateLimit should be int[] = [0,0,0] for null value || int[]=null
     * @param timeLimit should be int[] = [0,0,0] for null value
     */
    public DateTimePolicy(int policyID, int storeID, String category, limiters limiter, int[] dateLimit, int[] timeLimit) {
        this.policyID = policyID;
        this.storeID = storeID;
        this.category = category;
        this.limiter = limiter;
        this.dateLimit = dateLimit;
        this.timeLimit = timeLimit;
    }

    @Override
    public boolean validate(Order order) throws Exception {
        boolean contains = false;
        for (ProductInfo pI : order.getShoppingCart().getBasket(storeID).getContent()) {
            if (pI.getCategories().contains(category)) {
                contains = true;
                break;
            }
        }
        if (!contains)
            return handleNext(true,order);
        int[] curDay = curDay(); //first 3 are for date, next 3 are for time
        int[] curDate = {curDay[0], curDay[1], curDay[2]};
        int[] curTime = {curDay[3], curDay[4], curDay[5]};
        if (!checkLimitValues(dateLimit)) {
            return handleNext(switch (limiter) {
                case Min -> handleMin(curDate, dateLimit);
                case Max -> handleMax(curDate, dateLimit);
                case Exact -> !(handleExact(curDate,dateLimit));
            },order);
        }
        if (!checkLimitValues(timeLimit)) {
            return handleNext(switch (limiter) {
                case Min -> handleMin(curTime, timeLimit);
                case Max -> handleMax(curTime, timeLimit);
                case Exact -> !(handleExact(curTime,timeLimit));

            },order);
        }
        return handleNext(true,order);
    }

    private boolean handleMax(int[] curDay, int[] limit) {
        boolean res = true;
        res = curDay[2] <= limit[2] && (curDay[1] <= limit[1] && curDay[0] <= limit[0]);
        return res;
    }

    private boolean handleMin(int[] curDay, int[] limit) {
        return curDay[0] >= limit[0] && (curDay[1] >= limit[1] && (curDay[2] >= limit[2]));
    }

    private boolean handleExact(int[] cur,int[] limit){
        boolean res = cur[0] == limit[0] && cur[1] == limit[1]  && cur[2] == limit[2];
        return res;
    }
    public boolean checkLimitValues(int[] limit) {
        if(limit == null)
            return true;
        if (limit[0] == 0 && limit[1] == 0 && limit[2] == 0)
            return true;
        return false;
    }


}
