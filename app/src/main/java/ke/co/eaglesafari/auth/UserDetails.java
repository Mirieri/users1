package ke.co.eaglesafari.auth;

import android.content.Context;

public class UserDetails {
    Context context;
    String token;

    public UserDetails(Context context) {
        this.context = context;
        LoginDb ldb = LoginDb.getInstance(context);
        ldb.getWritableDatabase();
        this.token = ldb.getToken();
    }

    public String getToken() {
        return token;
    }

    public boolean isLoggedin() {
        return !token.isEmpty();
    }

}