package projects.android.myshop.data;

import projects.android.myshop.data.result.LoginResult;


// Utility class for admin
public final class Admin {
    // admin emails
    private static final String[] emails = {"KwesiJames@gmail.com"};
    // admin passwords
    private static final String[] passwords = {"admin"};

    private Admin() {
    }

    // checking admin email and password are correct or not
    public static LoginResult checkAdminLogin(String email, String password) {
        for (String e : emails) {
            if (e.equals(email)) {
                for (String p : passwords) {
                    if (p.equals(password)) {
                        return LoginResult.SUCCESS;
                    }
                }
                return LoginResult.WRONG_PASSWORD;
            }
        }
        return LoginResult.NOT_EXIST;
    }

}
