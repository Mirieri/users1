package ke.co.eaglesafari.volley;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import ke.co.eaglesafari.gcm2.Config;
import ke.co.eaglesafari.sharedprefs.MyPreferenceManager;


public class AppController extends Application
{

	public static final String		TAG							= AppController.class
			.getSimpleName();

	private RequestQueue				mRequestQueue;
	private ImageLoader				mImageLoader;

	private static AppController	mInstance;
	private final int					MAX_ATTEMPTS				= 5;
	private final int					BACKOFF_MILLI_SECONDS	= 2000;
	private final Random				random						= new Random();
	private MyPreferenceManager pref;
	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
	}

	public static synchronized AppController getInstance()
	{
		return mInstance;
	}

	public RequestQueue getRequestQueue()
	{
		if (mRequestQueue == null)
		{
			mRequestQueue = Volley
					.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader()
	{
		getRequestQueue();
		if (mImageLoader == null)
		{
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());

		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req)
	{
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag)
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}

	private static void post(String endpoint, Map<String, String> params)
			throws IOException
	{

		URL url;
		try
		{

			url = new URL(endpoint);

		} catch (MalformedURLException e)
		{
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet()
				.iterator();

		// constructs the POST body using the parameters
		while (iterator.hasNext())
		{
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext())
			{
				bodyBuilder.append('&');
			}
		}

		String body = bodyBuilder.toString();

		Log.v("TAG", "Posting '" + body + "' to " + url);

		byte[] bytes = body.getBytes();

		HttpURLConnection conn = null;
		try
		{

			Log.e("URL", "> " + url);

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();

			// handle the response
			int status = conn.getResponseCode();

			// If response is not success
			if (status != 200)
			{

				throw new IOException("Post failed with error code "
						+ status);
			}
		} finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
		}
	}

	// Checking for all possible internet providers
	public boolean isConnectingToInternet()
	{

		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}

	// Notifies UI to display a message.
	public void displayMessageOnScreen(Context context, String message)
	{

		Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Config.EXTRA_MESSAGE, message);

		// Send Broadcast to Broadcast receiver with message
		context.sendBroadcast(intent);

	}

	// Function to display simple Alert Dialog
	public void showAlertDialog(Context context, String title,
								String message, Boolean status)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Set Dialog Title
		alertDialog.setTitle(title);

		// Set Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Set alert dialog icon
			//alertDialog.setIcon((status) ? R.drawable.yes : R.drawable.no);

			// Set OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});

		// Show Alert Message
		alertDialog.show();
	}

	private PowerManager.WakeLock	wakeLock;

	public void acquireWakeLock(Context context)
	{
		if (wakeLock != null)
			wakeLock.release();

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");

		wakeLock.acquire();
	}

	public void releaseWakeLock()
	{
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;
	}
	public MyPreferenceManager getPrefManager() {
		if (pref == null) {
			pref = new MyPreferenceManager(this);
		}

		return pref;
	}

}