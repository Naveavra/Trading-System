package utils.userInfoRelated;

import utils.userInfoRelated.Info;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PrivateInfo extends Info {

    private List<String> oldNames;
    private List<String> oldEmails;
    private List<String> oldPasswords;
    private HashMap<String, String> securityQuestions;

    public PrivateInfo(int id, String name, String email, String birthday, int age){
        super(id, name, email, birthday, age);
        oldNames = new LinkedList<>();
        oldEmails = new LinkedList<>();
        oldPasswords = new LinkedList<>();
        securityQuestions = new HashMap<>();
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
    public void addSecurityQuestion(HashMap<String, String> secQuestions){this.securityQuestions.putAll(secQuestions);}
}
