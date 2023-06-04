package database.dtos;

import domain.states.Permission;
import jakarta.persistence.*;
import utils.stateRelated.Action;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleDto {

    @Id
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId")
    private StoreDto storeDto;

    @Id
    private int userId;
    @Id
    private String role;
    private String userName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="roleDto")
    private List<PermissionDto> permissions;


    public RoleDto(){
    }

    public RoleDto(StoreDto storeDto, int userId, String role, String userName){
        this.storeDto = storeDto;
        this.userId = userId;
        this.role = role;
        this.userName = userName;
    }

    public StoreDto getStoreDto() {
        return storeDto;
    }

    public void setStoreDto(StoreDto storeDto) {
        this.storeDto = storeDto;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Action> actions) {
        List<PermissionDto> permissionDtos = new ArrayList<>();
        for(Action a : actions)
            permissionDtos.add(new PermissionDto(this, Permission.getActionsMap().get(a)));
        permissions = permissionDtos;
    }
}
