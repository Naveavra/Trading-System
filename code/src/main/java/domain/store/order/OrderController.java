package domain.store.order;

import domain.store.order.Order;

import java.util.concurrent.ConcurrentHashMap;

public class OrderController {

    private ConcurrentHashMap<Integer, Order> orders;
}
