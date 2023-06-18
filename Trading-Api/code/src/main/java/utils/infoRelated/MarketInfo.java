package utils.infoRelated;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class MarketInfo extends Information{
    private transient LocalTime startTime;
    private transient AtomicInteger countGuestIn;
    private transient AtomicInteger countMemberIn;
    private transient AtomicInteger countUserOut;
    private transient AtomicInteger purchaseCount;
    private transient AtomicInteger countRegister;

    private double averageUserIn;
    private double averageUserOut;
    private double averageRegistered;
    private double averagePurchase;

    public MarketInfo(){
        startTime = LocalTime.now();
        countGuestIn = new AtomicInteger(0);
        countMemberIn = new AtomicInteger(0);
        countUserOut = new AtomicInteger(0);
        purchaseCount = new AtomicInteger(0);
        countRegister = new AtomicInteger(0);
        averageUserIn = 0;
        averageUserOut = 0;
        averageRegistered = 0;
        averagePurchase = 0;
    }

    public void addGuestIn(){
        countGuestIn.incrementAndGet();
    }

    public void addMemberIn(){
        countMemberIn.incrementAndGet();
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
        if(startHour == 0)
            startHour = 24;
        int startMinute = startTime.getMinute();
        int startSecond = startTime.getSecond();
        int hour = curTime.getHour();
        int minute = curTime.getMinute();
        double second = curTime.getSecond();
        if(hour == 0)
            hour = 24;
        double calcMinutes = (hour - startHour) * 60 + minute - startMinute + (second - startSecond)/60;
        averagePurchase = purchaseCount.get() / calcMinutes;
        averageRegistered = countRegister.get() / calcMinutes;
        averageUserOut =countUserOut.get() / calcMinutes;
        averageUserIn = (countGuestIn.get() + countMemberIn.get()) / calcMinutes;
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

    @Override
    public JSONObject toJson() {
        calculateAverages();
        DecimalFormat df=new DecimalFormat("0.000");
        JSONObject json = new JSONObject();
        json.put("guestCount", countGuestIn);
        json.put("memberCount", countMemberIn);
        json.put("averageUserIn", df.format(averageUserIn));
        json.put("averageUserOut", df.format(averageUserOut));
        json.put("registeredCount", countRegister);
        json.put("averageRegistered", df.format(averageRegistered));
        json.put("purchaseCount", purchaseCount);
        json.put("averagePurchase", df.format(averagePurchase));
        return json;
    }
}
