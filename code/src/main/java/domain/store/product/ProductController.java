package domain.store.product;

import domain.store.product.Product;

import java.util.concurrent.ConcurrentHashMap;

public class ProductController {

    ConcurrentHashMap<Integer, Product> productList;
}
