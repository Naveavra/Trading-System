package database.dtos;

import domain.store.storeManagement.AppHistory;
import jakarta.persistence.*;
import utils.Pair;
import utils.infoRelated.Info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "stores")
public class StoreDto {

    @Id
    private int storeId;
    private String name;
    private boolean isActive;
    private String image;

    private String description;
    @ManyToOne
    @JoinColumn(name = "creatorId", foreignKey = @ForeignKey(name = "creatorId"), referencedColumnName = "id")
    private MemberDto memberDto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<RoleDto> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="storeDto")
    private List<AppointmentDto> appointments;

    public StoreDto(){

    }
    public StoreDto(MemberDto memberDto,int storeId, String name,String description,  String image){
        this.storeId = storeId;
        this.name = name;
        this.isActive = true;
        this.image = image;
        this.memberDto = memberDto;
        this.description = description;
    }

    public int getId(){return storeId;}

    public void  setId(int storeId){this.storeId = storeId;}

    public String getName(){return this.name;}

    public void setName(String name){this.name = name;}

    public boolean isActive(){return this.isActive;}
    public void setActive(boolean state){this.isActive = state;}

    public String getImage(){return this.image;}

    public void setImage(String image){this.image = image;}

    public MemberDto getCreatorId(){return memberDto;}

    public String getDescription(){return this.description;}

    public void setDescription(String desc){this.description = desc;}

    public List<RoleDto> getRoles() {
        return roles;
    }

    public List<AppointmentDto> getAppointments() {
        return appointments;
    }
    public void setRoles(AppHistory appHistory) {
        List<RoleDto> roleDtos = new ArrayList<>();
        Set<Integer> checkDup = new HashSet<>();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for(Pair<Info, List<Info>> p : appHistory.getAppHistory()){
            if(!checkDup.contains(p.getFirst().getId())) {
                RoleDto roleDto = new RoleDto(this, p.getFirst().getId(), p.getFirst().getRole().toString(), p.getFirst().getEmail());
                roleDtos.add(roleDto);
                roleDto.setPermissions(p.getFirst().getActions());
                checkDup.add(p.getFirst().getId());
            }
            for(Info info : p.getSecond()) {
                if(!checkDup.contains(info.getId())) {
                    RoleDto roleDto = new RoleDto(this, info.getId(), info.getRole().toString(), info.getEmail());
                    roleDtos.add(roleDto);
                    roleDto.setPermissions(info.getActions());
                    checkDup.add(info.getId());
                }
                appointmentDtos.add(new AppointmentDto(this, p.getFirst().getId(), info.getId()));
            }
        }
        roles = roleDtos;
        appointments = appointmentDtos;
    }
}
