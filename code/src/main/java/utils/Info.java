package utils;

import java.util.LinkedList;
import java.util.List;

public class Info {
    private int id;
    private String name;
    private String email;
    private String birthday;
    private int age;

    private List<Action> managerActions;

    public Info(int id, String name, String email, String birthday, int age){
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.age = age;
        managerActions = new LinkedList<>();
    }

    public void addManagerActions(List<Action> actions){
        managerActions.addAll(actions);
    }
}
