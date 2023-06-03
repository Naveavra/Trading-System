package database.dtos;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "userHistory")
public class UserHistoryDto {
    @ManyToOne
    @Transient
    @Id
    private MemberDto memberDto;
    @Id
    private int receiptId;
    private double totalPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiptId")
    private List<ReceiptDto> receiptDtos;

    public UserHistoryDto(){
    }

    public UserHistoryDto(MemberDto memberDto, int receiptId, double totalPrice){
        this.memberDto = memberDto;
        this.receiptId = receiptId;
        this.totalPrice = totalPrice;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public void setMemberDto(MemberDto memberDto) {
        this.memberDto = memberDto;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ReceiptDto> getReceiptDtos() {
        return receiptDtos;
    }

    public void setReceiptDtos(List<ReceiptDto> receiptDtos) {
        this.receiptDtos = receiptDtos;
    }
}
