import org.junit.jupiter.api.Test;
import service.security.UserAuth;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthTest {

    private UserAuth userAuth = new UserAuth();


    @Test
    void generateToken() {
        String token = userAuth.generateToken(1);
        String token2 = userAuth.generateToken(2);
        System.out.println(token);
        System.out.println(token2);
        assertTrue(token.contains("1"));
        assertTrue(token2.contains("2"));
        try {
            userAuth.checkUser(1, token);
        }catch (Exception e){
            assert false;
        }
        try {
            userAuth.checkUser(1, token2);
            assert false;
        }catch (Exception e){
        }
        assert true;
    }

    @Test
    void hashPassword(){
        String hash = userAuth.hashPassword("elibew@gmail.com", "123Aaa", true);
        String hash2 = userAuth.hashPassword("eli2@gmail.com", "123Aaa", true);
        System.out.println(hash);
        System.out.println(hash2);
        try {
            userAuth.checkPassword("elibew@gmail.com", "123Aaa");
        }catch (Exception e){
            assert false;
        }
        try {
            userAuth.checkPassword("eli@gmail.com", "123Aaa");
            assert false;
        }catch (Exception e){
        }
        assert true;
    }
}