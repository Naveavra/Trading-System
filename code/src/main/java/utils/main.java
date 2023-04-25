package utils;

import com.google.gson.Gson;
import domain.user.Member;
import service.UserController;

import java.util.concurrent.ConcurrentHashMap;

public class main {

    public static void main(String[] args) throws Exception {
        Gson json = new Gson();
        UserController userController = new UserController();
        userController.register("elliben123@gmail.com", "aBc1234", "21/02/2002");
        userController.register("ellibend123@gmail.com", "aBc1234", "21/02/2002");
        ConcurrentHashMap<String, Member> memberList = new ConcurrentHashMap<>();
        memberList = json.fromJson(userController.getUsersInfo(), memberList.getClass());
        System.out.println(memberList);




    }
}
