package service;

import domain.user.Guest;
import domain.user.Member;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class UserController {

    ConcurrentHashMap<Integer, Guest> guestList;
    int guestIds;
    ConcurrentHashMap<String, Member> memberList;
    ConcurrentHashMap<Integer, String> idToEmail;
    int memberIds;


    public UserController(){
        guestList = new ConcurrentHashMap<>();
        guestIds = 0;
        memberList = new ConcurrentHashMap<>();
        idToEmail = new ConcurrentHashMap<>();
        memberIds = 1;
    }



    public synchronized int enterGuest(){
        Guest g = new Guest(guestIds);
        guestList.put(guestIds, g);
        guestIds+=2;
        return g.getId();
    }

    public synchronized void exitGuest(int id){
        guestList.remove(id);
    }

    public synchronized void register(String name, String email, String password, String birthday) throws RuntimeException{
        if(!checkEmail(email))
            throw new RuntimeException("invalid email");
        for (Member m : memberList.values())
            if(m.getEmail().equals(email))
                throw new RuntimeException("the email is already taken");
        if(!checkPassword(password))
            throw new RuntimeException("password not meeting requirements");
        if(!checkBirthday(birthday))
            throw new RuntimeException("birthday not legal");

        Member m = new Member(memberIds, name, email, password, birthday);
        memberList.put(email, m);
        idToEmail.put(memberIds, email);
        memberIds+=2;
    }


    public synchronized int login(String email, String password) throws RuntimeException{
        if(!checkEmail(email))
            throw new RuntimeException("invalid email");
        if(!checkPassword(password))
            throw new RuntimeException("invalid password");
        Member m = memberList.get(email);
        if(m == null)
            throw new RuntimeException("no such email");
        try {
            if(m.login(password))
                return m.getId();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return -1;
    }

    //when logging out returns to main menu as guest
    public synchronized int logout(int memberId) throws RuntimeException{
        String email = idToEmail.get(memberId);
        if(email != null){
            return logout(email);
        }
        else
            throw new RuntimeException("no such id for a member");
    }

    public synchronized int logout(String email) throws RuntimeException{
        Member m = memberList.get(email);
        if (m != null) {
            if (m.getIsConnected()) {
                m.disconnect();
                return enterGuest();
            }
            else
                throw new RuntimeException("member is disconnected already");
        }
        else
            throw new RuntimeException("member not found");

    }

    //adding the productId to the user's cart with the given quantity
    public synchronized void addProductToCart(int userId, int storeId, int productId, int quantity) throws RuntimeException{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            g.addProductToCart(storeId, productId, quantity);
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                addProductToCart(email, storeId, productId, quantity);
            else
                throw new RuntimeException("no such member exists");
        }

    }

    public synchronized void addProductToCart(String email, int storeId, int productId, int quantity) throws RuntimeException{
        Member m = memberList.get(email);
        if(m != null)
            m.addProductToCart(storeId, productId, quantity);
        else
            throw new RuntimeException("no such member exists");
    }

    //removing the productId from the user's cart
    public synchronized void removeProductFromCart(int userId, int storeId, int productId) throws RuntimeException{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            if(g != null)
                g.removeProductFromCart(storeId, productId);
            else
                throw new RuntimeException("no such guest exists");
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                removeProductFromCart(email, storeId, productId);
            else
                throw new RuntimeException("no such member exists");
        }
    }

    public synchronized void removeProductFromCart(String email, int storeId, int productId) throws RuntimeException{
        Member m = memberList.get(email);
        if(m != null)
            m.removeProductFromCart(storeId, productId);
        else
            throw new RuntimeException("no such member exists");
    }


    //adding the change quantity to the product's quantity in the user's cart
    public void changeQuantityInCart(int userId, int storeId, int productId, int change) throws RuntimeException{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            if(g != null)
                g.changeQuantityInCart(storeId, productId, change);
            else
                throw new RuntimeException("no such guest exists");
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                changeQuantityInCart(email, storeId, productId, change);
            else
                throw new RuntimeException("no such member exists");
        }
    }
    public synchronized void changeQuantityInCart(String email, int storeId, int productId, int change) throws RuntimeException{
        Member m = memberList.get(email);
        if(m != null)
            m.changeQuantityInCart(storeId, productId, change);
        else
            throw new RuntimeException("no such member exists");
    }

    //the return of the function is a hashmap between storeId to hashmap of productId to quantity. meaning that it displays the
    //product and quantity for each store.
    public synchronized HashMap<Integer, HashMap<Integer, Integer>> getUserCart(int userId) throws RuntimeException{
        if(userId % 2 == 0) {
            Guest g = guestList.get(userId);
            if(g != null)
                return  g.getCartContent();
            else
                throw new RuntimeException("no such guest exists");
        }
        else {
            String email = idToEmail.get(userId);
            if (email != null)
                return getUserCart(email);
            else
                throw new RuntimeException("no such member exists");
        }
    }

    public synchronized HashMap<Integer, HashMap<Integer, Integer>> getUserCart(String email) throws RuntimeException{
        Member m = memberList.get(email);
        if(m != null)
            return m.getCartContent();
        else
            throw new RuntimeException("no such member exists");
    }




    //check that the format user@domain.com exists
    public boolean checkEmail(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z0-9-]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    //the password length is between 6 and 20, need 1 small letter, 1 big letter and 1 number at least.
    // all other characters are not allowed
    public boolean checkPassword(String password){
        int countSmall = 0;
        int countBig = 0;
        int countNum = 0;
        if(password.length()<6 || password.length() > 20)
            return false;
        for(int i = 0; i<password.length(); i++){
            if(password.charAt(i)>=48 && password.charAt(i)<=57)
                countNum++;
            else if(password.charAt(i)>=65 && password.charAt(i)<=90)
                countBig++;
            else if(password.charAt(i)>=97 && password.charAt(i)<=122)
                countSmall++;
            else
                return false;
        }
        return countNum != 0 && countBig != 0 && countSmall != 0;
    }

    //birthdays are written in the format: dd/mm/yyyy. only integers and '/'
    //check for 1<=month<=12 and that the day is good accordingly
    //checks for an age that makes sense (0<=age<=120).
    public boolean checkBirthday(String birthday){
        int[] validDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for(int i = 0; i<birthday.length(); i++)
            if(birthday.charAt(i) < 47 || birthday.charAt(i) > 57)
                return false;

        String[] splitBDay = birthday.split("/");
        int[] bDay = {Integer.parseInt(splitBDay[0]), Integer.parseInt(splitBDay[1]), Integer.parseInt(splitBDay[2])};
        if(bDay[1] <1 || bDay[1] > 12)
            return false;
        if(validDay[bDay[1] - 1] < bDay[0] || bDay[0] < 1)
            return false;

        String currentDate = String.valueOf(java.time.LocalDate.now());
        String[] splitCurrentDay = currentDate.split("-");
        int[] curDay = {Integer.parseInt(splitCurrentDay[0]), Integer.parseInt(splitCurrentDay[1]),
                Integer.parseInt(splitCurrentDay[2])};
        int tmp = curDay[0];
        curDay[0] = curDay[2];
        curDay[2] = tmp;

        if(bDay[2] > curDay[2] || (bDay[2] == curDay[2] && bDay[1] > curDay[1]) ||
                (bDay[2] == curDay[2] && bDay[1] == curDay[1] && bDay[0] > curDay[0]))
                return false;
        if(bDay[2] < (curDay[2] - 120))
            return false;

        return true;
    }
}
