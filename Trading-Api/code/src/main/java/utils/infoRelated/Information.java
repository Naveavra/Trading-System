package utils.infoRelated;


import org.json.JSONObject;
import utils.stateRelated.Action;

import java.util.*;

public abstract class Information {

    public abstract JSONObject toJson();
    public static List<String> fromActionToString(List<Action> actions){
        List<String> ans = new LinkedList<>();
        for(Action a : actions){
            String as = a.toString();
            for(int i = 0; i<as.length(); i++) {
                if (40 < as.charAt(i) && as.charAt(i) < 91) {
                    char add = (char)(as.charAt(i) + 32);
                    as = as.substring(0, i) + " " + add + as.substring(i + 1);
                }
            }
            ans.add(as);
        }
        return ans;
    }

    public static List<String> fromStringToActionString(List<String> actions){
        List<String> ans = new LinkedList<>();
        for(String a : actions){
            for(int i = 0; i<a.length(); i++) {
                if (a.charAt(i) == ' ') {
                    char add = (char)(a.charAt(i+1) - 32);
                    a = a.substring(0, i)+ add + a.substring(i + 2);
                }
            }
            ans.add(a);
        }
        return ans;
    }

    public static List<JSONObject> hashMapToJson(HashMap<Integer, ? extends Information> hashMap, String key, String value){
        List<JSONObject> jsonList = new ArrayList();
        if(hashMap != null) {
            for (Map.Entry<Integer, ? extends Information> entry : hashMap.entrySet()) {
                JSONObject jsonO = new JSONObject();
                jsonO.put(key, entry.getKey());
                jsonO.put(value, entry.getValue().toJson());
                jsonList.add(jsonO);
            }
        }
        return jsonList;
    }

    public static List<JSONObject> infosToJson(List<? extends Information> infos){
        List<JSONObject> ans = new LinkedList<>();
        for(Information info : infos)
            ans.add(info.toJson());
        return ans;
    }
}
