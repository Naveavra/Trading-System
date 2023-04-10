package domain.store;


import utils.Status;

import java.util.HashMap;
import java.util.List;

public class Order {
    private int orderId;
    private Status status;
    private int userId;
    private HashMap<Integer, List<Integer>> productsInStores;

}
