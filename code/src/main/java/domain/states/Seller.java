package domain.states;

import utils.Action;
import utils.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Seller extends UserState {

    public Seller(){
        role = Role.Seller;
        permission = new Permission();
        List<Action> actions = new LinkedList<>();
        /*
        actions.add(Action.buyProduct);
        actions.add(Action.createStore);
        actions.add(Action.getProductInformation);
        actions.add(Action.getStoreInformation);
        actions.add(Action.writeReview);
        actions.add(Action.rateProduct);
        actions.add(Action.rateStore);
        actions.add(Action.sendQuestion);
        actions.add(Action.sendComplaint);
        actions.add(Action.sellProduct);

         */
        permission.addActions(actions);
    }
}

