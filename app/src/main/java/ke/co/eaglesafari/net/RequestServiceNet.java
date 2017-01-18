package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ke.co.eaglesafari.db.CurrentRequestDb;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;

public class RequestServiceNet extends AsyncTask<String, String,ResponseModel>
{
	RequestItem loginItem;
	Context context;
	I_Response<Boolean,String> booleanI_response;
	Boolean can_connect=false;

	public RequestServiceNet(RequestItem loginItem, Context context, I_Response<Boolean,String> booleanI_response)
	{
		this.loginItem=loginItem;
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
		if(can_connect)
		{

			rep = usf.sendRequest(loginItem);
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
			case 201:
				JSONObject jobj=responseModel.getJson();
				CurrentRequestDb ldb= CurrentRequestDb.getInstance(context);
				ldb.getWritableDatabase();
				Gson gson=new Gson();
				if(jobj!=null)
				{
				RequestItem requestItem=gson.fromJson(jobj.toString(),RequestItem.class);
				try
				{
					ldb.add(requestItem);
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}
					booleanI_response.onTaskCompleted(true);
					booleanI_response.onTaskCommpletedMessage("Request Sent Successfully");
					booleanI_response.onData(jobj.toString(),true);}
				else
				{
					booleanI_response.onTaskCompleted(false);
					booleanI_response.onTaskCommpletedMessage("A response error occured");
				//	booleanI_response.onData(jobj.toString(),true);
				}
				//get the other dependencies


				break;
			case 401:
				booleanI_response.onTaskCompleted(false);
				booleanI_response.onTaskCommpletedMessage("Wrong username or password");

				break;

		}




	}
}

