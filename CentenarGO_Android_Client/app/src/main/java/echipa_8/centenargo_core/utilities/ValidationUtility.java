package echipa_8.centenargo_core.utilities;

import java.util.regex.Pattern;

/**
 * Created by sando on 4/21/2018.
 */

public class ValidationUtility {

    public static final Pattern emailAddressPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    public static Boolean isValidEmail(String emailAddress){
        return emailAddressPattern.matcher(emailAddress).matches();
    }
}
