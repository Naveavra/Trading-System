package database.dtos;
import jakarta.persistence.*;

@Entity
@Table(name = "storeReviews")
public class StoreReviewDto {

    @Id
    private int reviewId;
    private int orderId;
    private int reviewerId;

    @Id
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId")
    private StoreDto storeDto;
    private String content;
    private int rating;
    private boolean seen;
    public StoreReviewDto(){}
    public StoreReviewDto(StoreDto storeDto, int reviewId, int reviewerId, String content, int rating,
                          int orderId, boolean seen){
        this.storeDto = storeDto;
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

    public StoreDto getStoreDto() {
        return storeDto;
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

    public void setStoreDto(StoreDto storeDto) {
        this.storeDto = storeDto;
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
