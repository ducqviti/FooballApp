package ducqv.com.fooball.unility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mrtran on 4/17/17.
 */

public class PreferenceHelper {
    private SharedPreferences sharedPreferences;
    public static final String PREFERENCE_FILE = "preference_file.com.ducqv";

    // user_id
    public static final String USER_NAME = "user_name";
    public static final String USE_PASS = "use_pass";
    public static final String LUU = "use_luu";
    public static final String LOGIN = "use_login";

    public PreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("ducqv", 0);
    }

    public String getUsePass() {
        return this.sharedPreferences.getString(USE_PASS, "");
    }

    public String getUserName() {
        return this.sharedPreferences.getString(USER_NAME, "");
    }

    public void setUsePass(String usePass) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putString(USE_PASS, usePass);
        localEditor.commit();
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putString(USER_NAME, userName);
        localEditor.commit();
    }

    public void isLuu(Boolean status) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putBoolean(LUU, status);
        localEditor.commit();
        if (!status) clear();

    }

    public boolean isLuu() {
        return this.sharedPreferences.getBoolean(LUU, false);
    }

    public void isLogin(Boolean status) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putBoolean(LOGIN, status);
        localEditor.commit();
    }

    public boolean isLogin() {
        return this.sharedPreferences.getBoolean(LOGIN, false);
    }

    public void clear() {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.clear();
        localEditor.commit();
    }
}
