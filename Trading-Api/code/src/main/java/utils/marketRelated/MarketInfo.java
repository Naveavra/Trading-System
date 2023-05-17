package utils.marketRelated;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class MarketInfo {
    private transient LocalTime startTime;
    private transient AtomicInteger countUserIn;
    private transient AtomicInteger countUserOut;
    private transient AtomicInteger purchaseCount;
    private transient AtomicInteger countRegister;

    private double averageUserIn;
    private double averageUserOut;
    private double averageRegistered;
    private double averagePurchase;

    public MarketInfo(){
        startTime = LocalTime.now();
        countUserIn = new AtomicInteger(0);
        countUserOut = new AtomicInteger(0);
        purchaseCount = new AtomicInteger(0);
        countRegister = new AtomicInteger(0);
        averageUserIn = 0;
        averageUserOut = 0;
        averageRegistered = 0;
        averagePurchase = 0;
    }


    public void addUserCount(){
        countUserIn.incrementAndGet();
    }

    public void reduceUserCount(){
        countUserOut.incrementAndGet();
    }

    public void addRegisteredCount(){
        countRegister.incrementAndGet();
    }

    public void addPurchaseCount(){
        purchaseCount.incrementAndGet();
    }

    public void calculateAverages(){
        LocalTime curTime = LocalTime.now();
        int startHour = startTime.getHour();
        int startMinute = startTime.getMinute();
        int hour = curTime.getHour();
        int minute = curTime.getMinute();

        double calcMinutes = (hour - startHour) * 60 + minute - startMinute;
        averagePurchase = purchaseCount.get() / calcMinutes;
        averageRegistered = countRegister.get() / calcMinutes;
        averageUserOut =countUserOut.get() / calcMinutes;
        averageUserIn = countUserIn.get() / calcMinutes;


    }

    public double getAverageUserIn(){
        return averageUserIn;
    }

    public double getAverageUserOut(){
        return averageUserOut;
    }

    public double getAverageRegistered(){
        return averageRegistered;
    }

    public double getAveragePurchase(){
        return averagePurchase;
    }
}
