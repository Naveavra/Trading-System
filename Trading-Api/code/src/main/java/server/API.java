package server;

import market.Admin;
import market.Market;
import org.json.JSONObject;
import utils.Pair;
import utils.marketRelated.Response;

public class API {
    public Market market;
    public API(){
        Admin admin = new Admin(-1, "elibenshimol6@gmail.com", "123Aaa");
        market = new Market(admin);
    }

    public void enterGuest(spark.Response res){
        Response<Integer> r = market.enterGuest();
        JSONObject json = new JSONObject();
        if(r.getErrorMessage()==null) {
            res.status(200);
            json.put("guestId", r.getValue());
            //connected.put(r.getValue(),true);
            res.body(json.toString());
        }
        else{
            res.status(400);
            json.put("errorMsg", r.getValue());
            res.body(json.toString());
        }
    }

    //TODO: add from manager methods
}
