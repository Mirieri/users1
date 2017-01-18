package ke.co.eaglesafari.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import ke.co.eaglesafari.items.UserItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;


public class UserPatchProcess extends AsyncTask<String, String, ResponseModel> {
    Context context;
    I_Response<Boolean, String> booleanI_response;
    Boolean can_connect = false;
    UserItem user;

    public UserPatchProcess(UserItem user, Context context, I_Response<Boolean, String> booleanI_response) {
        this.booleanI_response = booleanI_response;
        this.context = context;
        this.user = user;


    }

    @Override
    protected void onPreExecute() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            can_connect = true;
        }


    }

    @Override
    protected ResponseModel doInBackground(String... params) {
        UserFunctions usf = new UserFunctions(context);
        ResponseModel rep = null;
        if (can_connect) {

            rep = usf.patchUser(user);
        }
        return rep;
    }

    @Override
    protected void onPostExecute(ResponseModel responseModel) {
        Log.e("Response", responseModel.toString());
        if (!can_connect) {
            booleanI_response.onTaskCompleted(false);
            booleanI_response.onTaskCommpletedMessage("Cannot connect to server.Check your connection settings");

            return;
        }
        switch (responseModel.getStatus_code()) {
            case 404:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Server Not Found");
                break;
            case 500:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("A server error occurred.Please contact system administrator");
                break;
            case 204:

                booleanI_response.onTaskCompleted(true);
                booleanI_response.onTaskCommpletedMessage("Success");

                break;
            case 201:


                break;
            case 401:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Bad Request");

                break;
            case 409:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("409 Error");

                break;
            default:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Server return unknown response");

                break;


        }


    }
}

