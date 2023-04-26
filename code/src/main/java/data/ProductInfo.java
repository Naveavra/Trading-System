package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductInfo {
    private int productId;
    private String name;
    private String description;
    private List<String> categories;
    private int price;
    private int quantity;

    public ProductInfo(String name, String description, List<String> categories, int price, int quantity) {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductInfo(utils.ProductInfo product) {
        this.productId = product.id;
        this.name = product.name;
        this.description = product.description;
        this.price = product.price;
        this.quantity = product.quantity;
        this.categories = new ArrayList<>();
        this.categories.addAll(product.getCategories());
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
