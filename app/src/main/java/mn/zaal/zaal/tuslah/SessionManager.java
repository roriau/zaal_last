package mn.zaal.zaal.tuslah;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

public class SessionManager extends Config {
    private final SharedPreferences prefs;

    public SessionManager(@NonNull Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static SessionManager create(@NonNull Context context) {
        if (context == null) {
            throw new NullPointerException("Context is Null!");
        }
        return new SessionManager(context);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public SessionManager setIsPermAccepted(boolean value) {
        prefs.edit().putBoolean("is_perm_accepted", value).apply();
        return this;
    }

    public boolean isPermAccepted() {
        return prefs.getBoolean("is_perm_accepted", isPermAccepted);
    }

    public boolean isPermAccepted(boolean defaultValue) {
        return prefs.getBoolean("is_perm_accepted", defaultValue);
    }

    public boolean hasIsPermAccepted() {
        return prefs.contains("is_perm_accepted");
    }

    public SessionManager removeIsPermAccepted() {
        prefs.edit().remove("is_perm_accepted").apply();
        return this;
    }

    public SessionManager setIsFirstTime(boolean value) {
        prefs.edit().putBoolean("is_first_time", value).apply();
        return this;
    }

    public boolean isFirstTime() {
        return prefs.getBoolean("is_first_time", isFirstTime);
    }

    public boolean isFirstTime(boolean defaultValue) {
        return prefs.getBoolean("is_first_time", defaultValue);
    }

    public boolean hasIsFirstTime() {
        return prefs.contains("is_first_time");
    }

    public SessionManager removeIsFirstTime() {
        prefs.edit().remove("is_first_time").apply();
        return this;
    }

    public SessionManager setUserId(int value) {
        prefs.edit().putInt("user_id", value).apply();
        return this;
    }

    public int getUserId() {
        return prefs.getInt("user_id", userId);
    }

    public int getUserId(int defaultValue) {
        return prefs.getInt("user_id", defaultValue);
    }

    public boolean hasUserId() {
        return prefs.contains("user_id");
    }

    public SessionManager removeUserId() {
        prefs.edit().remove("user_id").apply();
        return this;
    }

    public SessionManager setPhoneNumber(String value) {
        prefs.edit().putString("phone_number", value).apply();
        return this;
    }

    public String getPhoneNumber() {
        return prefs.getString("phone_number", phoneNumber);
    }

    public String getPhoneNumber(String defaultValue) {
        return prefs.getString("phone_number", defaultValue);
    }

    public boolean hasPhoneNumber() {
        return prefs.contains("phone_number");
    }

    public SessionManager removePhoneNumber() {
        prefs.edit().remove("phone_number").apply();
        return this;
    }

    public SessionManager setIsLoggedin(boolean value) {
        prefs.edit().putBoolean("is_loggedin", value).apply();
        return this;
    }

    public boolean isLoggedin() {
        return prefs.getBoolean("is_loggedin", isLoggedin);
    }

    public boolean isLoggedin(boolean defaultValue) {
        return prefs.getBoolean("is_loggedin", defaultValue);
    }

    public boolean hasIsLoggedin() {
        return prefs.contains("is_loggedin");
    }

    public SessionManager removeIsLoggedin() {
        prefs.edit().remove("is_loggedin").apply();
        return this;
    }
}
