package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class to generate SHA-256 hashes from strings.
 * This class uses Java's {@link MessageDigest} to perform the hashing.
 */
public class HashGenerator {

    /**
     * Generates a SHA-256 hash for the given input string.
     *
     * @param input the String to hash.
     * @return the SHA-256 hash of the input as a hex string.
     * @throws RuntimeException if SHA-256 digest algorithm is not available.
     */
    public static String generateSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
