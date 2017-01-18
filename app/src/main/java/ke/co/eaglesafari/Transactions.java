package ke.co.eaglesafari;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class Transactions extends AppCompatActivity
	{
		@Override
		protected void onCreate(Bundle b)
			{
				super.onCreate(b);
				setContentView(R.layout.activity_transactions);
				getSupportActionBar().show();
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setTitle("Select a request");
			//	TransactionModel tmodel = new TransactionModel(this);

			}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int itemId = item.getItemId();
			if(itemId == android.R.id.home){
				finish();
			}
			return true;
		}
	}
