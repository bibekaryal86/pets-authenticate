package pets.authenticate.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogMasker {

    private static final String LOG_MASKER_PATTERN = "(.*?)";

    public static String maskDetails(String body) {
        if (body == null) {
            return null;
        } else {
            Matcher matcher = compile(quote("\"password\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
            if (matcher.find()) {
                body = matcher.replaceAll("\"password\":\"****\"");
            }

            matcher = compile(quote("\"email\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
            if (matcher.find()) {
                body = matcher.replaceAll("\"email\":\"****@****.***\"");
            }

            matcher = compile(quote("\"phone\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
            if (matcher.find()) {
                body = matcher.replaceAll("\"phone\":\"**********\"");
            }

            return body;
        }
    }
}
