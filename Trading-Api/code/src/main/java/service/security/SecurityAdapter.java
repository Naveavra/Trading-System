package service.security;

import java.security.NoSuchAlgorithmException;

//TODO: make payment and supplier the same way as security
public interface SecurityAdapter {

    public void checkUser(int userId, String token) throws Exception;

    public String generateToken(int userId);

    public void checkPassword(String username, String password) throws Exception;

    public String hashPassword(String username, String password, boolean add);

}
