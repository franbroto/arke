package org.tinnwork.arke.auth;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


public class UserService implements Serializable{


	private static SecureRandom random = new SecureRandom();

    private static Map<String, String> rememberedUsers = new HashMap<>();

    public static boolean isAuthenticUser(String username, String password) {
        return username.equals("admin") && password.equals("password") ||
                username.equals("fran") && password.equals("fran") ;
    }

    public static String rememberUser(String username) {
        String randomId = new BigInteger(130, random).toString(32);
        rememberedUsers.put(randomId, username);
        return randomId;
    }

    public static String getRememberedUser(String id) {
        return rememberedUsers.get(id);
    }

    public static void removeRememberedUser(String id) {
        rememberedUsers.remove(id);
    }

}
