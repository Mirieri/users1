package ke.co.eaglesafari.auth;


import ke.co.eaglesafari.constant.Constant;

public class LoginModel {

    public final static String KEY_TOKEN = "token";

    public class Url {
        public static final String URL_LOGIN = Constant.DOMAIN
                + "authenticate";
        public static final String URL_REGISTER = Constant.DOMAIN
                + "register";
    }
}
