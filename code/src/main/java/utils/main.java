package utils;

import service.UserController;

public class main {

    public static void main(String[] args) throws Exception {
        UserController userController = new UserController();
        userController.register("elliben123@gmail.com", "aBc1234", "21/02/2002");
        userController.register("ellibend123@gmail.com", "aBc1234", "21/02/2002");
        System.out.println(userController.getAllUserNames());
        //userController.displayNotifications("ellibend123@gmail.com");


    }
}
