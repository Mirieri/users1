package ke.co.eaglesafari.net;

import android.content.Context;
import android.os.AsyncTask;

import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.items.ResponseItem;
import ke.co.eaglesafari.net.network.I_netcheck;
import ke.co.eaglesafari.net.network.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class PayServiceNet extends AsyncTask<String, String, JSONObject>
	{

		I_netcheck<Boolean, ResponseItem>	callback;
		RequestItem									sitem;
		Context										context;

		public PayServiceNet(RequestItem sitem,
				I_netcheck<Boolean, ResponseItem> callback, Context context)
			{
				// TODO Auto-generated constructor stub
				this.sitem = sitem;
				this.callback = callback;
				this.context = context;
			}

		@Override
		protected JSONObject doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				JSONObject json = null;
				UserFunctions usf = new UserFunctions(context);
				//json = usf.sendPay(sitem);
				return json;
			}

		@Override
		protected void onPostExecute(JSONObject json)
			{
				if (json.has("success"))
					{
						try
							{
								switch (json.getString("success"))
									{
										case "1":
											callback.onTaskCompleted(true);
											ResponseItem ritem = new ResponseItem();
											ritem.setSuccess(json.getString("message"));
											ritem.setExtra("1");
											callback.onMessage(ritem);
											break;
										case "2":
											callback.onTaskCompleted(false);
											ResponseItem ritem2 = new ResponseItem();
											ritem2.setSuccess(json.getString("message"));
											ritem2.setExtra("2");
											callback.onMessage(ritem2);
											break;
										case "3":

											callback.onTaskCompleted(false);
											ResponseItem ritem3 = new ResponseItem();
											ritem3.setSuccess(json.getString("message"));
											ritem3.setExtra("3");
											callback.onMessage(ritem3);
											break;
										default:
											break;
									}

							} catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} else
					{
						callback.onTaskCompleted(false);
						ResponseItem ritem = new ResponseItem();
						ritem.setSuccess("Bad response");
						callback.onMessage(ritem);

					}

			}
	}
