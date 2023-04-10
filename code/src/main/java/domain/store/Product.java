package domain.store;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Product {

    private ConcurrentLinkedDeque<String> categories;
    private String name;
    private int quantity;
    private int price;
    private double discount;

}
