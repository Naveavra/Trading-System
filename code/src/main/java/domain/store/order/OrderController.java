package domain.store.order;

import domain.store.order.Order;
import utils.Status;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderController {

    private ConcurrentHashMap<Integer, Order> orders;
    private AtomicInteger orderID;
    
    public OrderController(){
        orders = new ConcurrentHashMap<Integer,Order>();
        orderID = new AtomicInteger();
    }
    
    public synchronized Order createNewOrder(int userID,HashMap<Integer,HashMap<Integer,Integer>> products){
        int id = orderID.getAndIncrement();
        Order or =  new Order(id,userID,products);
        orders.put(id, or);
        return or;
    }
    
    public synchronized Order getOrder(int order_ID){
        if(orders.containsKey(order_ID)){
            return orders.get(order_ID);
        }
        return null;
    }

    //only a specific user will be the cause of calling this function, so no need to synchronize
    public void replaceProductsInOrder(int order_ID,int store_ID,HashMap<Integer,Integer> products){
        Order ord;
        if((ord = getOrder(order_ID)) != null){
            ord.replaceProductsInOrder(store_ID, products);
        }
    }
    //only a specific user will be the cause of calling this function, so no need to synchronize
    public void addProductsToOrder(int order_ID,int store_ID,HashMap<Integer,Integer> products){
        Order ord;
        if((ord = getOrder(order_ID)) != null){
            ord.addProductsToOrder(store_ID, products);
        }
    }
    
    /**
     * need to check whether the return value is null.
     * @param order_ID
     * @return order Status or Null
     */
    public Status getOrderStatus(int order_ID){
        Order ord;
        if((ord = getOrder(order_ID)) != null){
            return ord.getStatus();
        }
        return null;
    }

    public void changeStatus(int order_ID,Status status){
        Order ord;
        if((ord = getOrder(order_ID)) != null){
            ord.setStatus(status);
        }
    }


}
