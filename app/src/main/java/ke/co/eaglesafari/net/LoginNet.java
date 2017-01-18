package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import ke.co.eaglesafari.auth.LoginDb;
import ke.co.eaglesafari.auth.LoginModel;
import ke.co.eaglesafari.items.LoginItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;

public class LoginNet extends AsyncTask<String, String,ResponseModel>
{
	LoginItem loginItem;
	Context context;
	I_Response<Boolean,String> booleanI_response;
	Boolean can_connect=false;

	public LoginNet(LoginItem loginItem, Context context, I_Response<Boolean,String> booleanI_response)
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

			rep = usf.login(loginItem);
		}
		return rep;
	}
	@Override
	protected void onPostExecute(ResponseModel responseModel)
	{
		if(!can_connect)
		{
			booleanI_response.onTaskCompleted(false);
			booleanI_response.onTaskCommpletedMessage("Check your internet connection");
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
				booleanI_response.onTaskCommpletedMessage("Please contact eagle safari adminstrator");
				break;
			case 200:

				JSONObject jobj=responseModel.getJson();
				if(jobj!=null) {
					LoginDb ldb = LoginDb.getInstance(context);
					ldb.getWritableDatabase();
					ldb.resetTables();
					try {
						ldb.saveToken(jobj.getString(LoginModel.KEY_TOKEN));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//get the other dependencies

					booleanI_response.onTaskCompleted(true);
					booleanI_response.onTaskCommpletedMessage("You successfully logged in");
				}else
				{
					booleanI_response.onTaskCompleted(false);
					booleanI_response.onTaskCommpletedMessage("A connection error occured");
				}
				break;
			case 401:
				booleanI_response.onTaskCompleted(false);
				booleanI_response.onTaskCommpletedMessage("Wrong username or password");

				break;
			default:
				booleanI_response.onTaskCompleted(false);
				booleanI_response.onTaskCommpletedMessage("Server return unknown response");
				break;


		}




	}
}

