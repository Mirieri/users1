package ke.co.eaglesafari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ke.co.eaglesafari.auth.LoginDb;
import ke.co.eaglesafari.constant.Config;
import ke.co.eaglesafari.gcm2.GcmIntentService;
import ke.co.eaglesafari.items.LoginItem;
import ke.co.eaglesafari.items.UserItem;
import ke.co.eaglesafari.net.LoginNet;
import ke.co.eaglesafari.net.UserPatchProcess;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.volley.AppController;

public class LoginActivity extends AppCompatActivity {
    // widgets
    Button btn_login, btn_register;
    EditText edit_email, edit_password;
    TextView txt_forgot, txt_error;
    // Strings
    String domain;
    String email, password;
    JSONArray houses = null;
    SharedPreferences prefshouse_id;
    SweetAlertDialog sweetAlertDialog;
    LoginItem loginItem;
    /******************
     * Gcm
     ******************/
    private String TAG = LoginActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**************************gcm***************/

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {

                    String token = intent.getStringExtra("token");

                    UserItem user = new UserItem();
                    user.setGcm(token);
                    new UserPatchProcess(user, getApplicationContext(), new UserPatchComplete()).execute();
                }
            }
        };

        if (checkPlayServices()) {

        }
        /*****************************end gcm**************/

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        // check if logged in
        LoginDb ldb = new LoginDb(getApplicationContext());
        ldb.getWritableDatabase();
        if (ldb.getRowCount() > 0) {
            startActivity(new Intent(LoginActivity.this,
                    MainActivity.class));
            finish();

        }
        btn_login = (Button) findViewById(R.id.btnlogin);
        edit_email = (EditText) findViewById(R.id.txtEmail);
        edit_password = (EditText) findViewById(R.id.txtpassword);
        txt_error = (TextView) findViewById(R.id.txt_error);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        SignUpActivity.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edit_email.getText().toString().equals("")
                        || edit_password.getText().toString().equals("")) {
                    Toaster("Email and Password required.");
                } else {
                    email = edit_email.getText().toString();
                    password = edit_password.getText().toString();
                    if (sweetAlertDialog == null)
                        sweetAlertDialog = new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    sweetAlertDialog.setTitleText("Logging in");
                    sweetAlertDialog.setCancelable(true);
                    sweetAlertDialog.show();
                    sweetAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            sweetAlertDialog.dismiss();
                            AppController appController = AppController.getInstance();
                            //appController.cancelPendingRequests(RequestTags.REQUEST_POST);
                        }
                    });
                    Login(email, password);
                }
            }
        });
    }

    public void Login(String email, String password) {

        loginItem = new LoginItem().setEmail(email).setPassword(password);
        new LoginNet(loginItem, LoginActivity.this, new LoginComplete()).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    class LoginComplete implements I_Response<Boolean, String> {


        @Override
        public void onTaskCompleted(Boolean i) {
            if (i) {
                registerGCM();
            } else {
                sweetAlertDialog.dismiss();
                txt_error.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTaskCommpletedMessage(String s) {
            txt_error.setText(s);
        }

        @Override
        public void onData(String s, Boolean aBoolean) {

        }
    }

    public void Toaster(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
                .show();
    }

    /***********************
     * gcm
     *******************/
    private void registerGCM() {
        //	Toaster("Registering gcm");
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /*************************
     * end gcm
     ********************/
    class UserPatchComplete implements I_Response<Boolean, String> {


        @Override
        public void onTaskCompleted(Boolean i) {
            if (i) {
                sweetAlertDialog.dismiss();
                Intent intents = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intents);
                finish();
            } else {

            }
        }

        @Override
        public void onTaskCommpletedMessage(String s) {

        }

        @Override
        public void onData(String s, Boolean aBoolean) {

        }
    }

}