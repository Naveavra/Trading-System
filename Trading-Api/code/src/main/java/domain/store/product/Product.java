package domain.store.product;

import database.daos.Dao;
import database.DbEntity;
import database.daos.StoreDao;
import jakarta.persistence.*;
import org.hibernate.Session;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.ProductReview;

import java.text.DecimalFormat;
import java.util.*;


@Entity
@Table(name = "products")
public class Product implements DbEntity{

    @Id
    public int productId;
    @Id
    private int storeId;

    public String name;
    public String description;
    public int price; //for one product
    public int quantity;
    private String imgUrl;
    @Transient
    private List<ProductReview> reviews;
    @Transient
    private List<String> categories;

    public Product(){
    }
    /**
     * Quantity, Price and Categories are set manually.
     * @param prod_id Unique
     * @param _name String
     * @param desc String
     */
    public Product(int storeId, int prod_id, String _name, String desc, int price, int quantity){
        this.storeId = storeId;
        productId = prod_id;
        name = _name;
        description = desc;
        this.price = price;
        this.quantity = quantity;
        categories = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public Product(int storeId, int prod_id, String _name, String desc, String imgUrl, int price, int quantity){
        this.storeId = storeId;
        productId = prod_id;
        name = _name;
        description = desc;
        this.price = price;
        this.quantity = quantity;
        categories = new ArrayList<>();
        this.imgUrl = imgUrl;
        reviews = new ArrayList<>();
    }

    public void changeImg(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getName(){return name;}
    public int getQuantity(){
        return quantity;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    /**
     * adds the quantity given (amount) to the current quantity.
     * @param amount int, to add
     */
    public void setQuantity(int amount, Session session) throws Exception{
        if(quantity + amount>=0){
            quantity += amount;
            StoreDao.saveProduct(this, session);
        }else {
            throw new Exception("Invalid Quantity: New quantity for product <= 0.");
        }
    }
    public void replaceQuantity(int newQuantity) throws Exception{
        if(newQuantity>0){
            quantity = newQuantity;
        }else {
            throw new Exception("Invalid Quantity: New quantity for product <= 0.");
        }
    }
    public void setPrice(int newPrice) throws Exception {
        if (newPrice > 0) {
            price = newPrice;
        } else {
            throw new Exception("Invalid Price: New price for product <= 0.");
        }
    }

    /**
     * This function returns a deep copy of the product,
     * should be used when inserting products to the store Inventory.
     * @return new deep copy of the product.
     */
    public synchronized Product clone() {
        Product clone = new Product(storeId, productId,name,description, price, quantity);
        try {
            clone.setPrice(price);
        }catch (Exception e){
            return null;
        }
//        clone.setQuantity(quantity);
        return clone;
    }

    public int getID(){
        return this.productId;
    }

    public String getDescription() { return description;
    }

    public int getPrice() { return price;}
    public String getImgUrl(){return imgUrl;}

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getCategories(){
        return categories;
    }

    public double getRating() {
        if(reviews == null || reviews.size() == 0)
            return 5;
        else{
            double sum = 0;
            for(ProductReview review : reviews)
                sum+=review.getRating();
            double avg = sum / reviews.size();
            DecimalFormat df = new DecimalFormat("#.#");
            avg = Double.parseDouble(df.format(avg));
            return avg;
        }
    }

    public void addCategory(String category) {
        if(!categories.contains(category))
            categories.add(category);
    }

    public boolean belongsToCategory(String discountedCategory) {
        return getCategories().contains(discountedCategory);
    }

    public ProductInfo getProductInfo(){
        return new ProductInfo(storeId, productId, categories, name, description,price
        ,quantity, getRating(), reviews, imgUrl);
    }

    public void addReview(ProductReview m) {
        reviews.add(m);
    }

    public List<ProductReview> getReviews(){return reviews;}

    @Override
    public void initialParams() throws Exception {
        getReviewsFromDb();
        getCategoriesFromDb();
    }

    private void getReviewsFromDb() throws Exception{
        if(reviews == null){
            reviews = new ArrayList<>();
            List<? extends DbEntity> productReviewsDto = Dao.getListByCompositeKey(ProductReview.class, storeId, productId,
                    "StoreReview", "storeId", "productId");
            reviews.addAll((List<ProductReview>) productReviewsDto);
        }
    }

    private void getCategoriesFromDb(){
        if(categories == null){
            categories = new ArrayList<>();
        }
    }
}
