package database.dtos;

import jakarta.persistence.*;


@Entity
@Table(name = "storeQuestions")
public class QuestionDto {

    @Id
    private int questionId;
    @Id
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId")
    private StoreDto storeDto;
    private int senderId;
    private String content;
    private boolean gotFeedback;
    private boolean seen;

    public QuestionDto() {
    }

    public QuestionDto(StoreDto storeDto, int questionId, int senderId, String content, boolean gotFeedback, boolean seen) {
        this.questionId = questionId;
        this.storeDto = storeDto;
        this.senderId = senderId;
        this.content = content;
        this.gotFeedback = gotFeedback;
        this.seen = seen;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public StoreDto getStoreDto() {
        return storeDto;
    }

    public void setStoreDto(StoreDto storeDto) {
        this.storeDto = storeDto;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isGotFeedback() {
        return gotFeedback;
    }

    public void setGotFeedback(boolean gotFeedback) {
        this.gotFeedback = gotFeedback;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
