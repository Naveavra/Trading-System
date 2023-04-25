package utils;

import java.util.List;

public class PrivateInfo extends Info{

    private List<String> oldNames;
    private List<String> oldEmails;
    private List<String> oldPasswords;

    public PrivateInfo(int id, String name, String email, String birthday, int age){
        super(id, name, email, birthday, age);
    }

    public void addOldNames(List<String> oldNames){
        this.oldNames.addAll(oldNames);
    }

    public void addOldEmails(List<String> oldEmails){
        this.oldEmails.addAll(oldEmails);
    }

    public void addOldPasswords(List<String> oldPasswords){
        this.oldPasswords.addAll(oldPasswords);
    }
}
