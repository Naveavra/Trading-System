package domain.store.order;

import domain.user.User;
import utils.infoRelated.ProductInfo;
import domain.user.ShoppingCart;
import utils.orderRelated.CalculatePriceOp;
import utils.orderRelated.Order;
import utils.orderRelated.SetPricesOp;
import utils.orderRelated.Status;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderController {

    private ConcurrentHashMap<Integer, Order> orders;
    private AtomicInteger orderID;
    
    public OrderController(){
        orders = new ConcurrentHashMap<Integer,Order>();
        orderID = new AtomicInteger();
    }
    
    public synchronized Order createNewOrder(User user, ShoppingCart products, double totalPrice) throws Exception {
        int id = orderID.getAndIncrement();
        Order or =  new Order(id,user,products);
        orders.put(id, or);
        or.setTotalPrice(totalPrice);
//        setPricesOp.setPrices(or); //sets the initial price values
        return or;
    }
    
    public synchronized Order getOrder(int order_ID){
        if(orders.containsKey(order_ID)){
            return orders.get(order_ID);
        }
        return null;
    }

    //only a specific user will be the cause of calling this function, so no need to synchronize
    public void replaceProductsInOrder(int order_ID,int store_ID,List<ProductInfo> products) throws Exception{
        Order ord;
        if((ord = getOrder(order_ID)) != null){
            ord.replaceProductsInOrder(store_ID, products);
        }
    }
    //only a specific user will be the cause of calling this function, so no need to synchronize
    
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
