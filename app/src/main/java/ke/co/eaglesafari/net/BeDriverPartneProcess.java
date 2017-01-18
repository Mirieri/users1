package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


import ke.co.eaglesafari.items.ApplicationItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;


public class BeDriverPartneProcess extends AsyncTask<String, String, ResponseModel>
{
    Context								context;
    I_Response<Boolean, String> booleanI_response;
    boolean can_connect=false;
    ApplicationItem requestItem;

    public BeDriverPartneProcess(Context context, 		I_Response<Boolean, String>	booleanI_response,
                      ApplicationItem requestItem)
    {
        this.context = context;
        this.booleanI_response = booleanI_response;
        this.requestItem=requestItem;


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
            json = usf.apply(requestItem);
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
            case 500:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("A server error occurred.Please contact system administrator");
                break;
            case 201:


               booleanI_response.onTaskCompleted(true);
                booleanI_response.onTaskCommpletedMessage("Successfully sent!");
                break;
            case 401:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Wrong username or password");

                break;

        }
    }
}
