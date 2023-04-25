package market;

import domain.user.Member;

public class Admin {

    Market market;
    private int adminId;

    private String emailAdmin;
    private String passwordAdmin;

    private Member adminsAccount;

    public Admin(){

    }
    public void closeStorePermanetly(int storeID)
    {
        market.closeStorePermanetly(this.adminId, storeID);
    }


    public void holdMarket(Market market) {
    }
}
