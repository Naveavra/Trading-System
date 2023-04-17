package domain.store.storeManagement;

import datastructres.Pair;
import utils.Role;

public class Store {
    private int id;
    private boolean isActive;
    private int creatorId;
    private String storeDescription;
    private AppHistory appHistory; //first one is always the store creator

    public Store(){
        Pair<Integer, Role > creatorNode = new Pair<>(creatorId, Role.Creator);
        appHistory = new AppHistory(creatorNode);
    }
}
