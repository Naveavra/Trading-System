package database.daos;


import database.HibernateUtil;
import database.dtos.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mockito.internal.matchers.Not;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class MemberDao {

    public MemberDao() {

    }
    public void saveMember(MemberDto m){
        DaoTemplate.save(m);
    }

    public void updateMember(MemberDto m){
        DaoTemplate.update(m);
    }

    public MemberDto getMemberById(int id){
        return (MemberDto) DaoTemplate.getById(MemberDto.class, id);
    }

    public List<NotificationDto> getMemberNotifications(int id){
        List<?> tmp = DaoTemplate.getListById(NotificationDto.class, id, "NotificationDto", "memberDto.id");
        return (List<NotificationDto>) tmp;
    }

    public List<CartDto> getMemberCart(int id){
        List<?> tmp = DaoTemplate.getListById(CartDto.class, id, "CartDto", "memberDto.id");
        return (List<CartDto>) tmp;
    }
    public List<UserHistoryDto> getMemberHistory(int id){
        List<?> tmp = DaoTemplate.getListById(UserHistoryDto.class, id, "UserHistoryDto", "memberDto.id");
        List<UserHistoryDto> history = (List<UserHistoryDto>) tmp;
        for(UserHistoryDto us : history) {
            tmp = DaoTemplate.getListById(ReceiptDto.class, us.getReceiptId(), "ReceiptDto", "userHistoryDto.receiptId");
            List<ReceiptDto> receipts = (List<ReceiptDto>) tmp;
            us.setReceiptDtos(receipts);
        }
        return history;
    }
}
