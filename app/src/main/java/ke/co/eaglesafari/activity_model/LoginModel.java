package ke.co.eaglesafari.activity_model;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ke.co.eaglesafari.MainActivity;
import ke.co.eaglesafari.R;
import ke.co.eaglesafari.items.LoginItem;
import ke.co.eaglesafari.net.LoginNet;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.views.PDialog;


public class LoginModel
	{
		Activity		activity;
		LoginItem litem;
		// Gcm gcm;
		PDialog pdia;

		public LoginItem getLitem()
			{
				return litem;
			}

		public void setLitem()
			{

				LoginItem litem = new LoginItem();
				litem.setEmail(getEdit_email().getText().toString());
				litem.setPassword(getEdit_password().getText().toString());
				//litem.setGcm(gcm.getRegistrationId());
				litem.setGcm("12345678");
				this.litem = litem;

			}

		public LoginModel(Activity activity)
			{
				this.activity = activity;
				setBtn_login();
				setBtn_register();
				setEdit_email();
				setEdit_password();
				// gcm = new Gcm(activity);
				pdia = new PDialog(activity);

			}

		Button	btn_login, btn_register;
		EditText	edit_email, edit_password;

		public Activity getActivity()
			{
				return activity;
			}

		public Button getBtn_login()
			{
				return btn_login;
			}

		public void setBtn_login()
			{
				btn_login = (Button) activity.findViewById(R.id.btnlogin);
			}

		public Button getBtn_register()
			{
				return btn_register;
			}

		public void setBtn_register()
			{
				btn_register = (Button) activity.findViewById(R.id.btn_register);
			}

		public EditText getEdit_email()
			{
				return edit_email;
			}

		public void setEdit_email()
			{
				edit_email = (EditText) activity.findViewById(R.id.txtEmail);
				edit_email.setText("h.murega@gmail.com");
			}

		public EditText getEdit_password()
			{
				return edit_password;
			}

		public void setEdit_password()
			{
				edit_password = (EditText) activity.findViewById(R.id.txtpassword);
				edit_password.setText("123456");
			}

		public boolean isready()
			{
				if (litem.getEmail().isEmpty())
					{
						return false;
					}
				return !litem.getPassword().isEmpty();

			}



		public void upload()
			{
				setLitem();

				if (isready())
					{

						pdia.start("Signing in...");
						new LoginNet(litem,activity, new LoginCOmplete())
								.execute();
					} else
					{
						Toast.makeText(activity, "Complete All Fields", Toast.LENGTH_SHORT).show();
						// gcm.register();
					}

			}

		public class LoginCOmplete implements I_Response<Boolean, String>
			{

				@Override
				public void onTaskCompleted(Boolean i)
					{
						if (i)
							{
								pdia.end();
								activity.startActivity(new Intent(activity, MainActivity.class));
								activity.finish();
							} else
							{
								pdia.end();

							}

					}

				@Override
				public void onTaskCommpletedMessage(String s) {
					Toaster(s);

				}

				@Override
				public void onData(String s, Boolean aBoolean) {

				}



			}

		public void Toaster(String text)
			{
				Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
			}

	}
