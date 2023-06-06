package database.daos;

import database.dtos.ComplaintDto;
import database.dtos.MemberDto;

public class ComplaintDao {

    public ComplaintDao() {

    }
    public void saveComplaint(ComplaintDto c){
        DaoTemplate.save(c);
    }

    public void updateComplaint(ComplaintDto c){
        DaoTemplate.update(c);
    }

    public ComplaintDto getComplaintById(int id){
        return (ComplaintDto) DaoTemplate.getById(ComplaintDto.class, id);
    }
}
