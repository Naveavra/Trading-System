package domain.user;

import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    ConcurrentHashMap<Integer, Guest> guestList;
    ConcurrentHashMap<Integer, Member> registeredList;

}
