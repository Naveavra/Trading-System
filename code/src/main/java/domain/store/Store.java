package domain.store;

import domain.user.AppHistory;
import utils.Action;
import utils.Role;

public class Store {
    private int id;
    private boolean isActive;
    private int creatorId;
    private String storeDescription;
    private AppHistory appHistory; //first one is always the store creator

    public Store(){
        appHistory = new AppHistory(creatorId, Role.Creator);
    }
}
