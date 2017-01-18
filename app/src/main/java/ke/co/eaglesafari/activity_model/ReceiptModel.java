package ke.co.eaglesafari.activity_model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import ke.co.eaglesafari.R;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.db.CurrentRequestDb;
import ke.co.eaglesafari.gcm2.NotificationUtils;
import ke.co.eaglesafari.items.ChargeItem;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.net.MarkStatus;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.views.PDialog;
import ke.co.eaglesafari.views.Tos;
import ke.co.eaglesafari.volley.AppController;
import ke.co.eaglesafari.volley.RoundedCornerNetworkImageView;


public class ReceiptModel
	{
		Activity	activity;

		RequestItem requestItem;
		PDialog pdia;
		TextView review;
		RatingBar ratingBar;
		private ImageLoader imageLoader;


        /******* to be fixed************/
		TextView txt_person_name;
		TextView txt_brand,txt_number_plate;

		NetworkImageView img_car;
		RoundedCornerNetworkImageView img_person;

		public ReceiptModel(final Activity activi)
			{
				this.activity = activi;
				Gson gson=new Gson();
				requestItem= gson.fromJson(activity.getIntent().getStringExtra(
						Constant.KEY_VALUE),RequestItem.class);

				String type=activity.getIntent().getStringExtra(Constant.KEY_VALUE);
				if(type!=null && type=="1")
				{
					CurrentRequestDb currentRequestDb=CurrentRequestDb.getInstance(activity);
					currentRequestDb.getWritableDatabase();
					currentRequestDb.add(requestItem);

					NotificationUtils.clearNotifications();

				}

				/**************to be fixed**********/
				txt_person_name=(TextView)activity.findViewById(R.id.txt_name1);
				txt_brand=(TextView)activity.findViewById(R.id.txt_service1);
				txt_number_plate=(TextView)activity.findViewById(R.id.txt_amount1);

				img_car=(NetworkImageView)activity.findViewById(R.id.lite_listrow_map);
				img_person=(RoundedCornerNetworkImageView)activity.findViewById(R.id.img_user1);


				imageLoader = AppController.getInstance().getImageLoader();
				//Image URL - This can point to any image file supported by Android
				try {
					final String url = Constant.DOMAIN_IMAGES_CARS + requestItem.getEmployee().getCar().getImg1();
					img_car.setImageUrl(url, imageLoader);
					img_person.setImageUrl(Constant.DOMAIN_IMAGES_PROFILES + requestItem.getEmployee().getProfile_image(), imageLoader);

					txt_person_name.setText(requestItem.getEmployee().getName());
					txt_brand.setText(requestItem.getEmployee().getCar().getBrand());
					txt_number_plate.setText(requestItem.getEmployee().getCar().getRegistration());

				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}

				/**********************/

				review=(TextView)activity.findViewById(R.id.txt_review);
				ratingBar=(RatingBar)activity.findViewById(R.id.rating_bar);
				if(requestItem.getRating().equals("")||requestItem.getRating().isEmpty()||requestItem.getRating()==null)
					ratingBar.setVisibility(View.GONE);

				LinearLayout list = (LinearLayout)activity.findViewById(R.id.list);
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				for (int i=0; i<requestItem.getCharges().size(); i++) {
					ChargeItem chargeItem = requestItem.getCharges().get(i);
					View vi = inflater.inflate(R.layout.charges_list, null);

					ViewHolder holder = new ViewHolder();
					holder.txt_item = (TextView) vi.findViewById(R.id.txt_item);
					holder.txt_number = (TextView) vi.findViewById(R.id.txt_number);
					holder.txt_price = (TextView) vi.findViewById(R.id.txt_price);
					holder.txt_sub = (TextView) vi.findViewById(R.id.txt_sub_total);

					holder.txt_sub.setText(String.valueOf(Double.parseDouble(chargeItem.getQuantity()) * Double.parseDouble(chargeItem.getAmount())));
					holder.txt_price.setText(chargeItem.getAmount());
					holder.txt_number.setText(chargeItem.getQuantity()+" "+chargeItem.getUnit());
					holder.txt_item.setText(chargeItem.getName());

					list.addView(vi);
				}

			/*	ListView listView=(ListView)activity.findViewById(R.id.list);
				ReceiptAdapter receiptAdapter= new ReceiptAdapter(activity,requestItem.getCharges());
				listView.setAdapter(receiptAdapter);*/

				setTxt_name();


				setTxt_receipt_no();

				setTxt_total();
				setTxt_main();
				setTxt_more_info();
				pdia = new PDialog(activity);
			//	pdia.start("Connecting...");
			//	new NetCheck(activity, new Chk()).execute();



				txt_name.setText(requestItem.getUser().getName());


				try {

				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}

				txt_more_info
						.setText("Payment is for "
								+ requestItem.getService().getName()
								+ ".Service once requested can only be refunded before the status changes to  'In Progress'");


				Button btnconfirm=(Button) activity.findViewById(R.id.btn_confirm);

				if(!requestItem.getReview().isEmpty())
				{
					review.setVisibility(View.VISIBLE);
					review.setText(requestItem.getReview());
					btnconfirm.setVisibility(View.GONE);

				}
				else
				{
					btnconfirm.setVisibility(View.VISIBLE);
				}
				if(!requestItem.getRating().isEmpty()|| requestItem.getRating()!=null) {
					try {
						ratingBar.setRating(Float.parseFloat(requestItem.getRating()));
					}
					catch (NumberFormatException e)
					{

					}
					btnconfirm.setVisibility(View.GONE);
				}
				else
				{
					btnconfirm.setVisibility(View.VISIBLE);
				}


				if(requestItem.getStatus().equals("1"))
					btnconfirm.setVisibility(View.VISIBLE);

				if(requestItem.getStatus().equals("1"))
				{
					btnconfirm.setText("Confirm");
					btnconfirm.setOnClickListener(new View.OnClickListener()
												  {
													  @Override
													  public  void onClick(View v)
													  {

														  pdia.start("Processing...");
														  new MarkStatus(activity,new MarkC(),requestItem).execute();
													  }
												  }
					);
				}

				if(requestItem.getStatus().equals("4"))
				{
					btnconfirm.setText("Review");
					btnconfirm.setVisibility(View.VISIBLE);
					btnconfirm.setOnClickListener(new View.OnClickListener()
												  {
													  @Override
													  public  void onClick(View v)
													  {
														 showInputDialog();
													  }
												  }
					);
				}





				try
				{
					if (requestItem.getStatus().equals("1"))
					{
						txt_main.setText("Estimated Charges");
						txt_receipt_no.setText("Request No:"
								+ requestItem.getId());

						txt_total.setText("Ksh." + requestItem.getAmount());

					}
					if (requestItem.getStatus().equals("2"))
					{
						txt_main.setText("INVOICE");
						txt_receipt_no.setText("Invoice No:"
								+ requestItem.getId());
						txt_total.setText("Ksh." + requestItem.getAmount());

					}
					if (requestItem.getStatus().equals("4"))
					{
						txt_main.setText("RECEIPT");
						txt_receipt_no.setText("Receipt No:"
								+ requestItem.getId());
						txt_total.setText("Ksh." + requestItem.getAmount());

					}
					if (requestItem.getStatus().equals("5"))
					{
						txt_main.setText("RECEIPT");
						txt_receipt_no.setText("Receipt No:"
								+ requestItem.getId());
						txt_total.setText("Ksh." + requestItem.getAmount());
						btnconfirm.setVisibility(View.GONE);
					}

				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		TextView	txt_receipt_no, txt_name,
				 txt_total,
				 txt_main, txt_more_info;

		public TextView getTxt_more_info()
			{
				return txt_more_info;
			}

		public void setTxt_more_info()
			{
				this.txt_more_info = (TextView) activity
						.findViewById(R.id.txt_more_info);
			}

		public TextView getTxt_main()
			{
				return txt_main;
			}

		public void setTxt_main()
			{
				this.txt_main = (TextView) activity.findViewById(R.id.txt_main);
			}
		/**
		 * @param
		 * @return the txt_receipt_no
		 */
		public TextView getTxt_receipt_no()
			{
				return txt_receipt_no;
			}

		/**
		 *           the txt_receipt_no to set
		 */
		public void setTxt_receipt_no()
			{
				this.txt_receipt_no = (TextView) activity
						.findViewById(R.id.txt_receipt_no);
			}

		/**
		 * @return the txt_name
		 */
		public TextView getTxt_name()
			{
				return txt_name;
			}


		public void setTxt_name()
			{
				this.txt_name = (TextView) activity.findViewById(R.id.txt_name);
			}

		/**
		 * @return the txt_item_1
		 */


		/**
		 * @return the txt_number_1
		 */





		/**
		 * @return the txt_total
		 */
		public TextView getTxt_total()
			{
				return txt_total;
			}


		public void setTxt_total()
			{
				this.txt_total = (TextView) activity.findViewById(R.id.txt_total);
			}


		private class MarkC implements I_Response<Boolean, String>
		{

			@Override
			public void onTaskCompleted(Boolean i)
			{

				pdia.end();
if(i)
{

	activity.finish();
}

				// delete from the list

			}

			@Override
			public void onTaskCommpletedMessage(String s) {
				new Tos(activity).s(s);
			}

			@Override
			public void onData(String s, Boolean aBoolean) {

			}



		}

		protected void showInputDialog() {

			// get prompts.xml view
			LayoutInflater layoutInflater = LayoutInflater.from(activity);
			View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
			alertDialogBuilder.setView(promptView);

			final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
			final RatingBar ratingBar = (RatingBar) promptView.findViewById(R.id.rating);
			// setup a dialog window
			alertDialogBuilder.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if(editText.getText().toString().isEmpty() && ratingBar.getRating()==0)
							{
								Toast.makeText(activity,"Please enter some text or click a star",Toast.LENGTH_SHORT).show();
								showInputDialog();
								return;
							}
							requestItem.setRating(String.valueOf(ratingBar.getRating()));
							requestItem.setReview(editText.getText().toString());
							pdia.start("Processing...");
							new MarkStatus(activity,new MarkC(),requestItem).execute();
							//start rating
						}
					})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

			// create an alert dialog
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
		}
		class ViewHolder {

			TextView txt_item,txt_number,txt_price,txt_sub;

		}

	}
