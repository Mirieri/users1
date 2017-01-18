package ke.co.eaglesafari.net;

        import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ke.co.eaglesafari.items.UserItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;


public class TaxiPositionsNet extends AsyncTask<String, String,ResponseModel>
{
LatLng latLng;    Context context;
    I_Response<Boolean,String> booleanI_response;
    Boolean can_connect=false;

    public TaxiPositionsNet(LatLng latLng, Context context, I_Response<Boolean,String> booleanI_response)
    {
        this.latLng=latLng;
        this.booleanI_response = booleanI_response;
        this.context=context;


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
        UserFunctions usf=new UserFunctions(context);
        ResponseModel rep=null;
        if(can_connect&& latLng!=null)
        {

            rep = usf.getDriverPositions(latLng);
        }
        return rep;
    }
    @Override
    protected void onPostExecute(ResponseModel responseModel)
    {
        if(!can_connect)
        {
            booleanI_response.onTaskCompleted(false);
            booleanI_response.onTaskCommpletedMessage("Check for internet connection");
            return;
        }
        switch (responseModel.getStatus_code())
        {

            case 400:
                JSONObject jsonObject=responseModel.getJson();
                booleanI_response.onTaskCompleted(false);
                try {
                    booleanI_response.onTaskCommpletedMessage(jsonObject.getString("message"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                break;

            case 404:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Server Not Found");
                break;
            case 500:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("A server error occurred.Please contact system administrator");
                break;
            case 200:
                ArrayList<UserItem> userItemArrayList=new ArrayList<>();
                JSONArray jsonArray=responseModel.getJarray();
                Gson gson=new Gson();
                for(int i=0;i<jsonArray.length();i++)
                {
                    try {
                        UserItem userItem = gson.fromJson(jsonArray.getJSONObject(i).toString(), UserItem.class);
                        userItemArrayList.add(userItem);
                    }
                    catch (JSONException h)
                    {
                        h.printStackTrace();
                    }
                }

                //get the other dependencies
                booleanI_response.onTaskCompleted(true);
                booleanI_response.onTaskCommpletedMessage("Drivers Retrieved Successfully");
                booleanI_response.onData(gson.toJson(userItemArrayList),true);


                break;
            case 401:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Wrong username or password");

                break;

        }




    }
}

