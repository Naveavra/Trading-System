package domain.user;

import java.util.regex.Pattern;

public class StringChecks {


    public void checkRegisterInfo(String email, String pass, String birthday) throws Exception{
        if(!checkEmail(email))
            throw new Exception("invalid email");
        checkPassword(pass);
        if(!checkBirthday(birthday))
            throw new Exception("birthday not legal");
    }
    public boolean checkEmail(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z0-9-]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }


    //the password length is between 6 and 20, need 1 small letter, 1 big letter and 1 number at least.
    // all other characters are not allowed
    public void checkPassword(String password) throws Exception{
        int countSmall = 0;
        int countBig = 0;
        int countNum = 0;
        if(password.length()<6 || password.length() > 20)
            throw new Exception("the password length needs to be between 6 and 20");
        for(int i = 0; i<password.length(); i++){
            if(password.charAt(i)>=48 && password.charAt(i)<=57)
                countNum++;
            else if(password.charAt(i)>=65 && password.charAt(i)<=90)
                countBig++;
            else if(password.charAt(i)>=97 && password.charAt(i)<=122)
                countSmall++;
            else
                throw new Exception("the password can only include letters and numbers");
        }
        if(countNum == 0 || countBig == 0 || countSmall == 0)
            throw new Exception("the password must include at least 1 small letter, 1 big letter and 1 number");
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

        int[]curDay = curDay();
        if(bDay[2] > curDay[2] || (bDay[2] == curDay[2] && bDay[1] > curDay[1]) ||
                (bDay[2] == curDay[2] && bDay[1] == curDay[1] && bDay[0] > curDay[0]))
            return false;
        if(bDay[2] < (curDay[2] - 120))
            return false;

        return true;
    }

    public static int[] curDay(){
        String currentDate = String.valueOf(java.time.LocalDate.now());
        String[] splitCurrentDay = currentDate.split("-");
        int[] curDay = {Integer.parseInt(splitCurrentDay[0]), Integer.parseInt(splitCurrentDay[1]),
                Integer.parseInt(splitCurrentDay[2])};
        int tmp = curDay[0];
        curDay[0] = curDay[2];
        curDay[2] = tmp;
        return curDay;
    }

    public static int calculateAge(String birthday) {
        String[] splitBDay = birthday.split("/");
        int[] bDay = {Integer.parseInt(splitBDay[0]), Integer.parseInt(splitBDay[1]), Integer.parseInt(splitBDay[2])};
        int[] curDay = curDay();
        int ans = 0;
        ans = ans + curDay[2] - bDay[2];
        if(curDay[1] > bDay[1] || (curDay[1] == bDay[1] && curDay[0] > bDay[0]))
            ans = ans + 1;
        return ans;
    }
}
