package domain.states;


import jakarta.persistence.*;
import utils.stateRelated.Action;

@Entity
@Table(name = "permissions")
public class Permission {


    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId"),
            @JoinColumn(name = "userId", foreignKey = @ForeignKey, referencedColumnName = "userId")
    })
    private UserState state;


    @Id
    @Enumerated(EnumType.STRING)
    private Action permission;

    public Permission(){
    }

    public Permission(UserState state, Action permission){
        this.state = state;
        this.permission = permission;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Action getPermission() {
        return permission;
    }

    public void setPermission(Action permission) {
        this.permission = permission;
    }
}
