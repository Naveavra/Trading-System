package service.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

public class UserAuth implements SecurityAdapter{

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // 2048 bit keys should be secure until 2030 - https://web.archive.org/web/20170417095741/https://www.emc.com/emc-plus/rsa-labs/historical/twirl-and-rsa-key-size.htm
    private static final int SECURE_TOKEN_LENGTH = 256;

    private static final SecureRandom random = new SecureRandom();

    private static final char[] symbols = CHARACTERS.toCharArray();

    private static final char[] buf = new char[SECURE_TOKEN_LENGTH];
    private ConcurrentHashMap<Integer, String> tokens;
    private ConcurrentHashMap<String, String> hashPass;

    public UserAuth(){
        tokens = new ConcurrentHashMap<>();
        hashPass = new ConcurrentHashMap<>();
    }
    @Override
    public void checkUser(int userId, String token) throws Exception{
        if(token.equals(tokens.get(0)))
            return;
        if(!tokens.containsKey(userId))
            throw new Exception("the userId given does not belong to any user in the system");
        if(tokens.get(userId).equals(token))
            return;
        throw new Exception("the token given does not match the one reserved for the userId");
    }

    @Override
    public String generateToken(int userId) {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        String token = new String(buf);
        token = userId + token;
        tokens.put(userId, token);
        return token;
    }

    @Override
    public void checkPassword(String username, String password) throws Exception{
        String check = hashPassword(username, password, false);
        if(!hashPass.containsKey(username))
            throw new Exception("the email given does not belong to any member in the system");
        if(hashPass.get(username).equals(check))
            return;
        throw new Exception("the password given for the username does not match the one reserved for him");
    }

    @Override
    public String hashPassword(String username, String password, boolean add){
        byte[] salt = username.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (Exception e){
            return null;
        }
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        if(add)
            hashPass.put(username, new String(hashedPassword));
        return new String(hashedPassword);
    }
}
