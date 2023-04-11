package domain.user;

import domain.states.Buyer;
import domain.states.UserState;
import domain.store.Store;

import java.util.HashMap;
//TODO: change all the void functions to return a value in case of success/failure
public class Member {
    //private Guest guest; //the user's information he had as guest
    ShoppingCart cart;
    private int id;
    private String name;
    private String birthday;
    private String email;

    private HashMap<Integer, UserState> roles; //connection between registered to the shops
    private HashMap<Integer, Store> stores; //saves all the stores it has a job at
    private UserState currentState;

    private UserHistory userHistory;
    private int currentStoreId;


    public void changeState(int storeId){ // if the user is not a specific store(such as main page) storeId == -1
        if(roles.containsKey(storeId))
            currentState = roles.get(storeId);
        else
            currentState = new Buyer();
        currentStoreId = storeId;
    }

    public void changeRoleInStore(int storeId, UserState userState){
        roles.put(storeId, userState);
    }

    public void addActionToStore(int storeId){
    }

    public UserState getRole(int storeId){
        return roles.get(storeId);
    }


    public void getGuestInfo(Guest g){
        cart = g.getCart();

    }


}
