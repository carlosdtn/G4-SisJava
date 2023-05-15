package Modelo;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 *
 * @author carlo
 */
public class Utilidades {
    public static String encriptarPassword(String password) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return hashedPassword;
    }
}