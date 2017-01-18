package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ke.co.eaglesafari.constant.ModelConstants;
import ke.co.eaglesafari.db.CurrentRequestDb;
import ke.co.eaglesafari.db.PaginationDb;
import ke.co.eaglesafari.items.Pagination;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;


public class GetHistoryTask extends AsyncTask<String, String,ResponseModel> {

    Context context;
    I_Response<Boolean,String> booleanI_response;
    Boolean can_connect=false;
    Boolean next;

    public GetHistoryTask(Context context, I_Response<Boolean,String> booleanI_response,Boolean next)
    {
        this.booleanI_response = booleanI_response;
        this.context=context;
        this.next=next;


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
        if(can_connect)
        {

            rep = usf.get(next);
        }
        return rep;
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
            case 200:



                Gson gson= new Gson();
                JSONObject jsonObject=responseModel.getJson();

                try {

                    Pagination pagination=gson.fromJson(jsonObject.toString(),Pagination.class);
                    PaginationDb paginationDb= PaginationDb.getInstance(context);
                    paginationDb.getWritableDatabase();
                    if(paginationDb.isExist(ModelConstants.History))
                    {
                        paginationDb.update(ModelConstants.History,pagination);

                    }
                    else
                    {
                        paginationDb.addPagination(pagination,ModelConstants.History);
                    }





                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                CurrentRequestDb ldb= CurrentRequestDb.getInstance(context);
                ldb.getWritableDatabase();
                ldb.resetTables();

                    if(jsonArray!=null)
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            RequestItem requestItem=gson.fromJson(jsonArray.getJSONObject(i).toString(),RequestItem.class);
                            ldb.add(requestItem);

                        }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (NullPointerException f)
                {
                    f.printStackTrace();
                }
                //get the other dependencies

                booleanI_response.onTaskCompleted(true);
                booleanI_response.onTaskCommpletedMessage("You successfully retrieved requests");
                break;
            case 401:
                booleanI_response.onTaskCompleted(false);
                booleanI_response.onTaskCommpletedMessage("Wrong username or password");

                break;

        }




    }
}

