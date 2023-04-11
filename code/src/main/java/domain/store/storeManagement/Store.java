package domain.store.storeManagement;

import domain.store.storeManagement.AppHistory;
import utils.Role;

public class Store {
    private int id;
    private boolean isActive;
    private int creatorId;
    private String storeDescription;
    private AppHistory appHistory; //first one is always the store creator

    public Store(){
        appHistory = new AppHistory(null, creatorId, Role.Creator);
    }
}
