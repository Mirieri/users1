package ke.co.eaglesafari.net;

import android.content.Context;
import android.os.AsyncTask;

import ke.co.eaglesafari.items.SignUpItem;
import ke.co.eaglesafari.net.network.I_netcheck;
import ke.co.eaglesafari.net.network.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterNet extends AsyncTask<String, String, JSONObject>
	{
		Context								context;
		I_netcheck<Boolean, String>	callback;
		SignUpItem							litem;

		public RegisterNet(Context context, I_netcheck<Boolean, String> callback,
				SignUpItem litem)
			{
				this.context = context;
				this.callback = callback;
				this.litem = litem;
			}

		@Override
		protected JSONObject doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				JSONObject json = null;
				UserFunctions usf = new UserFunctions(context);
				//json = usf.register(litem);
				return json;
			}

		@Override
		protected void onPostExecute(JSONObject json)
			{
				try
					{
						try
							{
								if (json.getString("success").equals("1"))
									{

										callback.onTaskCompleted(true);
										callback.onMessage(json.getString("message"));
									} else
									{
										callback.onTaskCompleted(false);
										callback.onMessage(json.getString("message"));
									}
							} catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} catch (NullPointerException e)
					{
						// TODO Auto-generated catch block
						callback.onTaskCompleted(false);
						callback.onMessage("A server error occured");
						e.printStackTrace();
					}

			}
	}
