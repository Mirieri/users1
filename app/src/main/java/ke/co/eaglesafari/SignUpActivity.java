package ke.co.eaglesafari;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ke.co.eaglesafari.items.SignUpItem;
import ke.co.eaglesafari.net.SignUpNet;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.views.DatePickerProcessor;
import ke.co.eaglesafari.views.PDialog;


public class SignUpActivity extends Activity
	{
		SignUpItem signUpItem;
		ViewHolder viewHolder;
		PDialog pDialog;

		@Override
		protected void onCreate(Bundle savedState)
			{
				super.onCreate(savedState);
				setContentView(R.layout.activity_sign_up);
				pDialog= new PDialog(this);
				viewHolder= new ViewHolder();
				viewHolder.btn_login=(Button) findViewById(R.id.btn_login);
				viewHolder.btn_submit=(Button)findViewById(R.id.btnregister);
				viewHolder.dob=(DatePicker)findViewById(R.id.dt_dob);
				viewHolder.txt_confirm_password=(EditText)findViewById(R.id.txt_confirmpassword);
				viewHolder.txt_password=(EditText)findViewById(R.id.txt_password);
				viewHolder.txt_name=(EditText)findViewById(R.id.txt_name);
				viewHolder.txt_email=(EditText)findViewById(R.id.txt_email);
				viewHolder.txt_phone=(EditText)findViewById(R.id.txt_telephone);
				viewHolder.txt_error=(TextView)findViewById(R.id.txt_error);

				viewHolder.btn_login.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
						finish();
					}

				});
				viewHolder.btn_submit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						signUpItem= new SignUpItem();
						signUpItem.setConfirm_password(viewHolder.txt_confirm_password.getText().toString());
						signUpItem.setPhone(viewHolder.txt_phone.getText().toString());
						signUpItem.setEmail(viewHolder.txt_email.getText().toString());
						signUpItem.setName(viewHolder.txt_name.getText().toString());
						signUpItem.setPassword(viewHolder.txt_password.getText().toString());
						DatePickerProcessor datePickerProcessor= new DatePickerProcessor(viewHolder.dob);
						signUpItem.setDob(datePickerProcessor.getDate());

						if (!signUpItem.isEmpty().getaBoolean())
						{
							viewHolder.txt_error.setVisibility(View.VISIBLE);
							viewHolder.txt_error.setText(signUpItem.isEmpty().getMessage());
							return;
						}

	pDialog.start("Registering Eagle Safari.....");
						new SignUpNet(SignUpActivity.this,new SignUpComplete(),signUpItem).execute();

					}

				});
			}

		public void Toaster(String text)
			{
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			}
		class ViewHolder
		{
			EditText txt_name,txt_phone,txt_email,txt_password,txt_confirm_password;
			DatePicker dob;
			TextView txt_error;
			Button btn_login,btn_submit;
		}

		class SignUpComplete implements I_Response<Boolean,String>
		{


			@Override
			public void onTaskCompleted(Boolean i) {
				pDialog.end();

				if(i)
				{
					Toaster("You registered successfully!");
					startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
					finish();
				}
				else
				{
					viewHolder.txt_error.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onTaskCommpletedMessage(String s) {
				viewHolder.txt_error.setText(s);

			}

			@Override
			public void onData(String s, Boolean aBoolean) {

			}
		}
	}
