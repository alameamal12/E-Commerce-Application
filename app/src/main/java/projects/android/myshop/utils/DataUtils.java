package projects.android.myshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import kotlin.UninitializedPropertyAccessException;

public final class DataUtils {
    private static SharedPreferences preferences;

    private DataUtils() {
    }

    private static SharedPreferences.Editor editor() {
        if (preferences == null) {
            throw new UninitializedPropertyAccessException("Preferences must be initialized before accessing.");
        }
        return preferences.edit();
    }

    private static SharedPreferences preference() {
        if (preferences == null) {
            throw new UninitializedPropertyAccessException("Preferences must be initialized before accessing.");
        }
        return preferences;
    }


    public static void initialize(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(KEY.SHARED_PREF_FILE, Context.MODE_PRIVATE);
    }

    private static void setString(@NonNull String KEY, String value) {
        SharedPreferences.Editor editor = editor();
        editor.putString(KEY, value);
        editor.apply();
    }

    private static String getString(@NonNull String KEY, String defValue) {
        return preference().getString(KEY, defValue);
    }

    private static void setLong(@NonNull String KEY, long value) {
        SharedPreferences.Editor editor = editor();
        editor.putLong(KEY, value);
        editor.apply();
    }

    private static Long getLong(@NonNull String KEY, long defValue) {
        return preference().getLong(KEY, defValue);
    }


    private static void setBoolean(@NonNull String KEY, boolean value) {
        SharedPreferences.Editor editor = editor();
        editor.putBoolean(KEY, value);
        editor.apply();
    }

    private static boolean getBoolean(@NonNull String KEY, boolean defValue) {
        return preference().getBoolean(KEY, defValue);
    }

    public static void setIsAdmin(boolean isAdmin) {
        setBoolean(KEY.IS_ADMIN, isAdmin);
    }

    public static boolean isAdmin() {
        return getBoolean(KEY.IS_ADMIN, false);
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        setBoolean(KEY.IS_LOGGED_IN, isLoggedIn);
    }

    public static boolean isLoggedIn() {
        return getBoolean(KEY.IS_LOGGED_IN, false);
    }

    public static String getEmail() {
        return getString(KEY.EMAIL, null);
    }

    public static void setEmail(String email) {
        setString(KEY.EMAIL, email);
    }

    public static Long getUserId() {
        return getLong(KEY.USER_ID, -1);
    }

    public static void setUserId(long userId) {
        setLong(KEY.USER_ID, userId);
    }


    private static final class KEY {
        static final String SHARED_PREF_FILE = "my_shop_shared_pref_file";
        static final String IS_ADMIN = "is_admin";
        static final String EMAIL = "email";
        static final String USER_ID = "user_id";
        static final String IS_LOGGED_IN = "is_logged_in";

        private KEY() {
        }
    }

}
