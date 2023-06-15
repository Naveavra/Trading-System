package data;

import utils.messageRelated.ProductReview;

import java.util.ArrayList;
import java.util.List;

public class ProductInfo {
    private int productId;
    private String name;
    private String description;
    private List<String> categories;
    private int price;
    private int quantity;
    private String img;

    public ProductInfo(String name, String description, List<String> categories, int price, int quantity, String img) {
        this.name = name;
        this.description = description;
        this.categories = new ArrayList<>();
        this.categories.addAll(categories);
        this.price = price;
        this.quantity = quantity;
        this.img = img;
    }

    public ProductInfo(utils.infoRelated.ProductInfo product) {
        this.productId = product.id;
        this.name = product.name;
        this.description = product.description;
        this.price = product.price;
        this.quantity = product.quantity;
        this.categories = new ArrayList<>();
        this.categories.addAll(product.getCategories());
        this.img = product.img;
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

    public String getImg(){return img;}

    private boolean compare(List<String> cat1, List<String> cat2)
    {
        if(cat1 == null || cat2 == null || cat1.size() != cat2.size()){
            return false;
        }
        return cat1.containsAll(cat2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ProductInfo))
            return false;
        ProductInfo other = (ProductInfo)obj;
        return this.productId == other.productId && this.name.equals(other.name) &&
                this.description.equals(other.description) && this.img.equals(other.img) &&
                this.price == other.price && this.quantity == other.quantity
                && compare(this.categories, other.categories);
    }
}
