package data;

import java.util.HashMap;
import java.util.Map;

public class CartInfo {
    HashMap<Integer, HashMap<Integer, Integer>> cart;

    public CartInfo(HashMap<Integer, HashMap<Integer, Integer>> cart) {
        this.cart = new HashMap<>();
        for (Integer key : cart.keySet()) {
            HashMap<Integer, Integer> innerCopiedMap = new HashMap<>();
            for (Integer innerKey : cart.get(key).keySet()) {
                innerCopiedMap.put(innerKey, cart.get(key).get(innerKey));
            }
            this.cart .put(key, innerCopiedMap);
        }
    }

    public int getCountOfProduct()
    {
        int size = 0;
        for (Map.Entry<Integer, HashMap<Integer, Integer>> basket: cart.entrySet()) {
            size += basket.getValue().size();
        }
        return size;
    }
}
