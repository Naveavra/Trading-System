package domain.states;

import utils.Action;
import utils.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class StoreCreator extends UserState {

    public StoreCreator() {
        role = Role.Creator;
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
        actions.add(Action.sellProduct);

        actions.add(Action.appointManager);
        actions.add(Action.changeStoreDescription);
        actions.add(Action.changePurchasePolicy);
        actions.add(Action.changeDiscountPolicy);
        actions.add(Action.addPurchaseConstraint);
        actions.add(Action.fireManager);
        actions.add(Action.checkWorkersStatus);
        actions.add(Action.viewQuestions);
        actions.add(Action.answerQuestions);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.addProduct);

        actions.add(Action.addOwner);
        actions.add(Action.fireOwner);
        actions.add(Action.changeManagerPermission);

        actions.add(Action.closeStore);
        actions.add(Action.reopenStore);

        permission.addActions(actions);
    }
}