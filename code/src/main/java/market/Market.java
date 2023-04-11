package market;

import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: can remove admins but at least one has to be in the system.
public class Market implements MarketInterface{
    private ConcurrentLinkedDeque<Admin> admins;
    public Market (Admin admin){
        admins = new ConcurrentLinkedDeque<>();
        admins.add(admin);
    }
}
