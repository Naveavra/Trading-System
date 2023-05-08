package server;

import market.Admin;
import market.Market;

public class API {
    public Market market;
    public API(){
        Admin admin = new Admin(-1, "elibenshimol6@gmail.com", "123Aaa");
        market = new Market(admin);
    }


    //TODO: add from manager methods
}
