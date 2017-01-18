package ke.co.eaglesafari.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDia
	{

		Context	context;

		public AlertDia(Context context)
			{
				this.context = context;
			}

		// show dialog
		public void show(String title, String message, String positive)
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

				// Setting Dialog Title
				alertDialog.setTitle(title);

				// Setting Dialog Message
				alertDialog.setMessage(message);

				// On pressing Settings button
				alertDialog.setPositiveButton(positive,
						new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
									{
										// new NetCheck(activity, new
										// Chk_Pay()).execute();
									}
							});

				// on pressing cancel button
				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
									{
										dialog.cancel();
									}
							});

				// Showing Alert Message
				alertDialog.show();
			}
	}
