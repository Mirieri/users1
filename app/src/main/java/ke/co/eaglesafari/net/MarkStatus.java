package ke.co.eaglesafari.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;


import org.json.JSONObject;

import ke.co.eaglesafari.db.CurrentRequestDb;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.net.network.ResponseModel;
import ke.co.eaglesafari.net.network.UserFunctions;


public class MarkStatus extends AsyncTask<String, String, ResponseModel>
	{
		Context								context;
		I_Response<Boolean, String> booleanI_response;
		boolean can_connect=false;
		RequestItem requestItem;

		public MarkStatus(Context context, 		I_Response<Boolean, String>	booleanI_response,
						  RequestItem requestItem)
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
				// TODO Auto-generated method stub
				int state=Integer.parseInt(requestItem.getStatus());
			//	if(Integer.parseInt(requestItem.getStatus())!=4)
				 state=Integer.parseInt(requestItem.getStatus())+1;


				requestItem.setStatus(String.valueOf(state));
				ResponseModel json = null;
				UserFunctions usf = new UserFunctions(context);
				if(can_connect)
					json = usf.mark_finshed(requestItem);
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
					case 200:
						JSONObject jobj=responseModel.getJson();


						CurrentRequestDb ldb= CurrentRequestDb.getInstance(context);
						ldb.getWritableDatabase();
						Gson gson= new Gson();

						ldb.update(gson.fromJson(jobj.toString(),RequestItem.class),requestItem.getDb_id());








						booleanI_response.onTaskCompleted(true);
						booleanI_response.onTaskCommpletedMessage("Successfully updated!");
						break;
					case 401:
						booleanI_response.onTaskCompleted(false);
						booleanI_response.onTaskCommpletedMessage("Wrong username or password");

						break;

				}
			}
	}
