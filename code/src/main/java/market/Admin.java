package market;

import domain.user.Member;
import utils.Message;

import java.util.ArrayList;
import java.util.List;

public class Admin {

    Market market;
    private int adminId;

    private String emailAdmin;
    private String passwordAdmin;

    private Member adminsAccount;
    private List<Message> complaints;
    public Admin(){
    complaints = new ArrayList<>();
    }
    public void closeStorePermanetly(int storeID)
    {
        market.closeStorePermanetly(this.adminId, storeID);
    }
    public void addComplaint(Message m){
        complaints.add(m);
    }


    public void holdMarket(Market market) {
    }
}
