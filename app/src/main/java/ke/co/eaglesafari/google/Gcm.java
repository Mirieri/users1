package ke.co.eaglesafari.google;/*
package com.skv.google;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.skv.net.network.I_netcheck;
import com.skv.net.network.NetCheck;
import com.skv.views.PDialog;

public class Gcm
	{
		Activity								activity;
		public static final String		TAG										= "Login Serviteur";
		public static final String		EXTRA_MESSAGE							= "message";
		public static final String		PROPERTY_REG_ID						= "registration_id";
		private static final String	PROPERTY_APP_VERSION					= "appVersion";
		private final static int		PLAY_SERVICES_RESOLUTION_REQUEST	= 9000;
		public static final String		GOOGLE_SENDER_ID						= "152574251792";
		GoogleCloudMessaging				gcm;
		String								regid										= "";
		PDialog								pdia;

		public Gcm(Activity activity)
			{
				this.activity = activity;
				pdia = new PDialog(activity);
			}

		public void Toaster(String text)
			{
				Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
			}

		private boolean checkPlayServices()
			{
				int resultCode = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(activity);
				if (resultCode != ConnectionResult.SUCCESS)
					{
						if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
							{
								GooglePlayServicesUtil.getErrorDialog(resultCode,
										activity, PLAY_SERVICES_RESOLUTION_REQUEST)
										.show();
							} else
							{
								Log.i(TAG, "This device is not supported.");
								// finish();
							}
						return false;
					}
				return true;
			}

		public void register()
			{
				Log.i(TAG, "Registration not found.");
				if (checkPlayServices())
					{
						pdia.start("Connecting...");
						new NetCheck(activity, new CheckNet()).execute();
					} else
					{
						Toaster("install google play services");
					}
			}

		public String getRegistrationId()
			{
				final SharedPreferences prefs = getGCMPreferences(activity);
				String registrationId = prefs.getString(PROPERTY_REG_ID, "");

				int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
						Integer.MIN_VALUE);
				int currentVersion = getAppVersion(activity);
				if (registeredVersion != currentVersion)
					{
						Log.i(TAG, "App version changed.");
						return "";
					}

				return registrationId;
			}

		*/
/**
		 * @return Application's {@code SharedPreferences}.
		 *//*

		private SharedPreferences getGCMPreferences(Context context)
			{
				// This sample app persists the registration ID in shared
				// preferences, but
				// how you store the registration ID in your app is up to you.
				return activity.getSharedPreferences(activity.getClass()
						.getSimpleName(), Context.MODE_PRIVATE);
			}

		private static int getAppVersion(Context context)
			{
				try
					{
						PackageInfo packageInfo = context.getPackageManager()
								.getPackageInfo(context.getPackageName(), 0);
						return packageInfo.versionCode;
					} catch (NameNotFoundException e)
					{
						// should never happen
						throw new RuntimeException("Could not get package name: " + e);
					}
			}

		// get houses
		class RegisterGCM extends AsyncTask<String, String, String>
			{

				@Override
				protected void onPreExecute()
					{
						super.onPreExecute();
						pdia.update("Registering to google servers");

					}

				*/
/**
				 * getting All products from url
				 * *//*

				@Override
				protected String doInBackground(String... args)
					{

						String id = "";
						try
							{
								if (gcm == null)
									{
										gcm = GoogleCloudMessaging.getInstance(activity);
									}
								id = gcm.register(GOOGLE_SENDER_ID);
							} catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						return id;

					}

				*/
/**
				 * After completing background task Dismiss the progress dialog
				 * **//*

				@Override
				protected void onPostExecute(String id)
					{
						pdia.end();
						storeRegistrationId(activity, regid);
						if (id.isEmpty())
							{
								Toaster("Registration Failed");
								pdia.end();
							} else
							{
								regid = id;

								new NetCheck(activity, new CheckNet()).execute();
							}

					}

			}

		private void storeRegistrationId(Context context, String regId)
			{
				final SharedPreferences prefs = getGCMPreferences(context);
				int appVersion = getAppVersion(context);
				Log.i(TAG, "Saving regId on app version " + appVersion);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(PROPERTY_REG_ID, regId);
				editor.putInt(PROPERTY_APP_VERSION, appVersion);
				editor.commit();
			}

		class CheckNet implements I_netcheck<Boolean, String>
			{

				@Override
				public void onTaskCompleted(Boolean i)
					{
						if (i)
							{
								pdia.update("Securing connection...");
								new RegisterGCM().execute();

							} else
							{
								pdia.end();
								Toaster("Connection Failed.Check Internet");

							}

					}

				@Override
				public void onMessage(String x)
					{
						// TODO Auto-generated method stub

					}

			}

	}
*/
