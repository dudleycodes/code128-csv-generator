import java.util.Random;

public class Generators {
    public static String generateRandomHex(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be greater than 0.");
        }

        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        while (sb.length() < length) {
            sb.append(Integer.toHexString(rand.nextInt()).toUpperCase());
        }

        return sb.substring(0, length);
    }
}
