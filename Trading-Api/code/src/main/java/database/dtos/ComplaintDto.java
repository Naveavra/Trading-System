package database.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "complaints")
public class ComplaintDto {

    @Id
    private int complaintId;
    private int orderId;
    private int senderId;
    private String content;
    private boolean gotFeedback;
    private boolean seen;

    public ComplaintDto() {
    }

    public ComplaintDto(int complaintId, int orderId, int senderId, String content, boolean gotFeedback, boolean seen) {
        this.complaintId = complaintId;
        this.orderId = orderId;
        this.senderId = senderId;
        this.content = content;
        this.gotFeedback = gotFeedback;
        this.seen = seen;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int reviewerId) {
        this.senderId = reviewerId;
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
