package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import ke.co.eaglesafari.auth.LoginDb;
import ke.co.eaglesafari.auth.LoginModel;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;


public class AccountNet extends AsyncTask<String, String,ResponseModel>
{
	Context context;
	I_Response<Boolean,String> booleanI_response;
	Boolean can_connect=false;

	public AccountNet( Context context, I_Response<Boolean,String> booleanI_response)
	{
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

			rep = usf.checkBalance();
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
				JSONObject jobj=responseModel.getJson();
				LoginDb ldb= LoginDb.getInstance(context);
				ldb.getWritableDatabase();
				ldb.resetTables();
				try
				{
					ldb.saveToken(jobj.getString(LoginModel.KEY_TOKEN));
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				//get the other dependencies

				booleanI_response.onTaskCompleted(true);
				booleanI_response.onTaskCommpletedMessage("You successfully logged in");
				break;
			case 401:
				booleanI_response.onTaskCompleted(false);
				booleanI_response.onTaskCommpletedMessage("Wrong username or password");

				break;

		}




	}
}

