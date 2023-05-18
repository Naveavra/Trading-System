package utils.infoRelated;

import org.json.JSONObject;
import utils.infoRelated.Info;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PrivateInfo extends Info{

    private List<String> oldNames;
    private List<String> oldEmails;
    private List<String> oldPasswords;
    private HashMap<String, String> securityQuestions;

    public PrivateInfo(Info info, List<String> oldNames, List<String> oldEmails,
                       List<String> oldPasswords, HashMap<String, String> securityQuestions){
        super(info.getId(), info.getName(), info.getEmail(), info.getBirthday(), info.getAge());
        this.oldNames = oldNames;
        this.oldEmails = oldEmails;
        this.oldPasswords = oldPasswords;
        this.securityQuestions = securityQuestions;
    }

    @Override
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", getId());
        json.put("userName", getName());
        json.put("email", getEmail());
        json.put("birthday", getBirthday());
        json.put("age", getAge());
        json.put("managerPermissions", fromActionToString(getManagerActions()));
        json.put("oldNames", oldNames);
        json.put("oldEmails", oldEmails);
        json.put("oldPasswords", oldPasswords);
        json.put("securityQuestions", securityQuestions);
        return json;
    }
}
