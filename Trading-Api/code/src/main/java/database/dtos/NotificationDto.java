package database.dtos;

import jakarta.persistence.*;
import utils.messageRelated.NotificationOpcode;

@Entity
@Table(name = "notifications")
public class NotificationDto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @Transient
    private MemberDto memberDto;
    private String content;
    private String opcode;

    public NotificationDto(){
    }
    public NotificationDto(MemberDto memberDto, String content, String opcode){
        this.memberDto = memberDto;
        this.content = content;
        this.opcode = opcode;

    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public void setMemberDto(MemberDto memberDto) {
        this.memberDto = memberDto;
    }
}
