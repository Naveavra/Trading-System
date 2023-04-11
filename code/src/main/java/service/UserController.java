package service;

import domain.user.Guest;
import domain.user.Member;

import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    ConcurrentHashMap<Integer, Guest> guestList;
    ConcurrentHashMap<Integer, Member> memberList;



    public void login(int guestId, String email, String password){
        //Guest g = guestList.get(guestId);

    }
}
