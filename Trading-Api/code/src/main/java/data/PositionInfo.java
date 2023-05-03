package data;

import utils.stateRelated.Action;
import utils.userInfoRelated.Info;

import java.util.List;

public class PositionInfo {

    private int id;
    private String name;
    private String email;
    private String birthday;
    private int age;

    private List<Action> managerActions;

    public PositionInfo(Info info){
        this.id = info.getId();
        this.name = info.getName();
        this.email = info.getEmail();
        this.birthday = info.getBirthday();
        this.age = info.getAge();
        this.managerActions = info.getManagerActions();
    }
}
