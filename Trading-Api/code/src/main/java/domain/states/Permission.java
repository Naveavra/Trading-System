package domain.states;


import database.DbEntity;
import database.daos.Dao;
import jakarta.persistence.*;
import utils.stateRelated.Action;

@Entity
@Table(name = "permissions")
public class Permission implements DbEntity {
    @Id
    private int userId;
    @Id
    private int storeId;
    @Id
    @Enumerated(EnumType.STRING)
    private Action permission;

    public Permission(){
    }

    public Permission(int userId, int storeId, Action permission){
        this.userId = userId;
        this.storeId = storeId;
        this.permission = permission;
        Dao.save(this);
    }


    public Action getPermission() {
        return permission;
    }

    public void setPermission(Action permission) {
        this.permission = permission;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public void initialParams() {}
}
