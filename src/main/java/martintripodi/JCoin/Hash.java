package martintripodi.JCoin;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hash {
    public static String calculateHash(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1){
                    string.append('0');
                }
                string.append(hex);
            }
            return string.toString();
        }catch(Exception noDigest){
            return "error creating hash";
        }
    }
}
