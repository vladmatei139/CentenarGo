package echipa_8.centenargo_app.utilities;

import android.content.SharedPreferences;

/**
 * Created by sando on 4/21/2018.
 */

public class SharedPreferencesUtility {

    private static SharedPreferences sharedPref;
    public final static String sharedPreferencesName = "ApplicationPreferences";
    private static String tokenKey = "Token";

    public static void setSharedPreferences(SharedPreferences pref) {
        sharedPref = pref;
    }

    public static void putUserDetails(String token, String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(tokenKey, token);
        editor.commit();
    }

    public static String getToken() {
        return sharedPref.getString(tokenKey, "");
    }
}
