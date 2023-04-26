package data;

import java.util.HashMap;

public class PurchaseInfo {
    HashMap<Integer, HashMap<Integer, Integer>> purchases;

    public PurchaseInfo(HashMap<Integer, HashMap<Integer, Integer>> purchases) {
        this.purchases = purchases;
    }
}
