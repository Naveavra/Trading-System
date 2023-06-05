package database.dtos;

import jakarta.persistence.*;


@Entity
@Table(name = "productReviews")
public class ProductReviewDto {

    @Id
    private int reviewId;
    private int orderId;
    private int reviewerId;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId"),
            @JoinColumn(name = "productId", foreignKey = @ForeignKey, referencedColumnName = "productId")
    })
    private InventoryDto inventoryDto;

    private String content;
    private int rating;
    private boolean seen;
    public ProductReviewDto(){}
    public ProductReviewDto(InventoryDto inventoryDto, int reviewId, int reviewerId, String content, int rating,
                          int orderId, boolean seen){
        this.inventoryDto = inventoryDto;
        this.reviewId = reviewId;
        this.reviewerId = reviewerId;
        this.content = content;
        this.rating = rating;
        this.orderId = orderId;
        this.seen = seen;
    }

    public String getContent() {
        return content;
    }

    public InventoryDto getInventoryDto() {
        return inventoryDto;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getRating() {
        return rating;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setInventoryDto(InventoryDto inventoryDto) {
        this.inventoryDto = inventoryDto;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

}
