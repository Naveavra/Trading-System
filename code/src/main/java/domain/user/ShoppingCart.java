package domain.user;

import domain.user.Basket;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    private ConcurrentHashMap <Integer, Basket> baskets; // saves the connection between a shop and its basket;

}
