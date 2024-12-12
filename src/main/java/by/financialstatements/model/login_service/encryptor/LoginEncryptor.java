package by.financialstatements.model.login_service.encryptor;

import java.util.Base64;
import java.util.Random;
import java.util.stream.Collectors;
/**
 * The LoginEncryptor class provides functionality for encrypting and decrypting strings used to
 * store or transmit user data (logins).
 * Encryption is performed using Base64, on the basis of which random characters are additionally added,
 * which complicates decryption of data without special methods.
 */
public class LoginEncryptor {
    /**
     * The encrypt method encrypts the input string using Base64 encoding
     * and adds a random "salt" to the beginning of the encoded string for increased security.
     *
     * @param input The input string to be encrypted.
     * @return The encrypted string with the "salt" added.
     */
    public static String encrypt(String input) {
        String encryptedString = Base64.getEncoder().encodeToString(input.getBytes());
        return addSalt(encryptedString);
    }
    /**
     * The decrypt method decrypts a string that was previously encrypted with the encrypt method.
     * Before decryption, random characters added during the encryption step are removed.
     *
     * @param input The encrypted string.
     * @return The decrypted string.
     */
    public static String decrypt(String input) {
        // Remove first 150 characters and decode Base64
        byte[] bytes = Base64.getDecoder().decode(input.substring(150));
        return new String(bytes);
    }
    /**
     * The addSalt method generates a random string of 150 characters long, consisting of letters and numbers,
     * which is added to the beginning of the input string.
     *
     * @param input The input string to which random characters will be added.
     * @return The string with the characters added.
     */
    private static String addSalt(String input) {
        // Set of characters to generate
        String symbols = "abcdefghijklmnopqrstuvwxyz0123456789";
        // Generate a random string of 150 characters
        String salt = new Random()
                .ints(150, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        // Add before encrypted string
        return salt + input;
    }
}
