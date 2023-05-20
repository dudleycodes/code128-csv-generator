import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Escapers {
    public static String csvQuotes(String myString) {
        if (myString == null) {
            return null;
        }

        return myString.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("\"\""));
    }
}
