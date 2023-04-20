package domain.states;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import utils.Action;
import utils.Role;

public class Buyer extends UserState {

    public Buyer(){
        role = Role.Buyer;
        permission = new Permission();
        List<Action> actions = new LinkedList<>();
        actions.add(Action.buyProduct);
        actions.add(Action.createStore);
        actions.add(Action.getProductInformation);
        actions.add(Action.getStoreInformation);
        actions.add(Action.writeReview);
        actions.add(Action.rateProduct);
        actions.add(Action.rateStore);
        actions.add(Action.sendQuestion);
        actions.add(Action.sendComplaint);
        permission.addActions(actions);
    }
}
