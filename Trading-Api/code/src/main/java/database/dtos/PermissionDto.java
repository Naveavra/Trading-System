package database.dtos;

import domain.states.Permission;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class PermissionDto {


    @Id
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId"),
            @JoinColumn(name = "userId", foreignKey = @ForeignKey, referencedColumnName = "userId")
    })
    private RoleDto roleDto;

    @Id
    private int permissionId;

    public PermissionDto(){
    }

    public PermissionDto(RoleDto roleDto, int permissionId){
        this.roleDto = roleDto;
        this.permissionId = permissionId;
    }

    public RoleDto getRoleDto() {
        return roleDto;
    }

    public void setRoleDto(RoleDto roleDto) {
        this.roleDto = roleDto;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}
