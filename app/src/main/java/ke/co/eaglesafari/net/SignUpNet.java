package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import ke.co.eaglesafari.items.SignUpItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;

import org.json.JSONException;


public class SignUpNet extends AsyncTask<String, String, ResponseModel>
{
    Context								context;
    I_Response<Boolean, String> booleanI_response;
    boolean can_connect=false;
    SignUpItem signUpItem;

    public SignUpNet(Context context, 		I_Response<Boolean, String>	booleanI_response,
                      SignUpItem signUpItem)
    {
        this.context = context;
        this.booleanI_response = booleanI_response;
        this.signUpItem=signUpItem;


    }
    @Override
    protected  void onPreExecute()
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            can_connect=true;
        }


    }

    @Override
    protected ResponseModel doInBackground(String... params)
    {

        ResponseModel json = null;
        UserFunctions usf = new UserFunctions(context);
        if(can_connect)
            json = usf.signUp(signUpItem);
        return json;
    }

    @Override
    protected void onPostExecute(ResponseModel responseModel)
    {
        if(!can_connect)
        {
            booleanI_response.onTaskCompleted(false);
            booleanI_response.onTaskCommpletedMessage("Cannot connect to server.Check your connection settings");
            return;
        }
        switch (responseModel.getStatus_code())
        {
            case 404:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Server Not Found");
                break;
            case 400:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Authentication Error");
                break;
            case 500:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("A server error occurred.Please contact system administrator");
                break;
            case 201:

                booleanI_response.onTaskCompleted(true);
                booleanI_response.onTaskCommpletedMessage("Successfully registered!");
                break;
            case 401:

                booleanI_response.onTaskCompleted(false);
                try {
                    booleanI_response.onTaskCommpletedMessage(responseModel.getJson().getString("message"));
                } catch (JSONException e) {
                    booleanI_response.onTaskCommpletedMessage("An error occurred");
                    e.printStackTrace();
                }

                break;

        }
    }
}
