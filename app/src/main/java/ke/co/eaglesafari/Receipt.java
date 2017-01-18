package ke.co.eaglesafari;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ke.co.eaglesafari.activity_model.ReceiptModel;


public class Receipt extends AppCompatActivity
	{
		@Override
		protected void onCreate(Bundle b)
			{
				super.onCreate(b);
				setContentView(R.layout.activity_receipt);
				new ReceiptModel(this);
			}
	}
