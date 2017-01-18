package ke.co.eaglesafari.net.network;


import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import ke.co.eaglesafari.auth.LoginModel;
import ke.co.eaglesafari.auth.UserDetails;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.AccountItem;
import ke.co.eaglesafari.items.ApplicationItem;
import ke.co.eaglesafari.items.FeedBackItem;
import ke.co.eaglesafari.items.LoginItem;
import ke.co.eaglesafari.items.ReceiptItem;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.items.SignUpItem;
import ke.co.eaglesafari.items.UserItem;
import ke.co.eaglesafari.utilities.NameValuePair;
import ke.co.eaglesafari.utilities.UrlPreproccessor;

public class UserFunctions
	{

		private JSONParserV2	jsonParser;

		Context					context;
		UserDetails				usd;
		String token_string;
		UrlPreproccessor urlPreproccessor;

		// constructor
		public UserFunctions(Context context)
			{
				jsonParser = new JSONParserV2();
				this.context = context;
				this.usd = new UserDetails(context);
				token_string="?token="+usd.getToken();
				urlPreproccessor= new UrlPreproccessor(context);

			}

		public ResponseModel mark_finshed(RequestItem requestItem)
		{
			Gson gson=new Gson();

			ResponseModel json = jsonParser.requestPATCHJSON(
					Constant.DOMAIN+"request/"+requestItem.getId()+token_string, gson.toJson(requestItem));
			return json;

		}
		public ResponseModel apply(ApplicationItem requestItem)
		{
			Gson gson=new Gson();

			ResponseModel json = jsonParser.requestPOSTJSON(
					Constant.DOMAIN+"application"+token_string, gson.toJson(requestItem));
			return json;

		}
		public ResponseModel patchUser(UserItem user)
		{
			Gson gson= new Gson();




			ResponseModel json = jsonParser.requestPATCHJSON(
					Constant.DOMAIN+"user/0"+"?token="+usd.getToken(),gson.toJson(user));
			return json;
		}


		public ResponseModel login(LoginItem litem)
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new NameValuePair("email", litem.getEmail()));
				params.add(new NameValuePair("password", litem.getPassword()));

				ResponseModel json = jsonParser.requestPOST(
						LoginModel.Url.URL_LOGIN, params,usd.getToken());
				return json;
			}

		public ResponseModel send_feedback(FeedBackItem litem)
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new NameValuePair(FeedBackItem.KEY_FEED_TEXT, litem
						.getFeed_text()));
				params.add(new NameValuePair(FeedBackItem.KEY_REQUEST_ID,
						litem.getRequest_id()));
				params.add(new NameValuePair(FeedBackItem.KEY_STAR, litem
						.getStar()));

				ResponseModel json = jsonParser.requestPOST(
						FeedBackItem.URL_FEEDBACK, params,"");
				return json;
			}

		public ResponseModel get(Boolean next)
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


				ResponseModel json = jsonParser.requestGETObject(
						urlPreproccessor.getHistoryUrl(next), params);
				return json;


			}



		public ResponseModel getDriverPositions(LatLng latLng)
		{
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new NameValuePair("latitude",String.valueOf(latLng.latitude)));
			params.add(new NameValuePair("longitude",String.valueOf(latLng.longitude)));
			params.add(new NameValuePair("token",usd.getToken()));
			ResponseModel json = jsonParser.requestGET(
					Constant.DOMAIN+"location", params);
			return json;
		}


		public ResponseModel sendRequest(RequestItem sitem)
			{
				Gson gson= new Gson();

				ResponseModel json = jsonParser.requestPOSTJSON(
						Constant.DOMAIN+"request"+token_string,gson.toJson(sitem) );
				return json;
			}
		public ResponseModel signUp(SignUpItem sitem)
		{
			Gson gson= new Gson();

			ResponseModel json = jsonParser.requestPOSTJSON(
					Constant.DOMAIN+"user"+token_string,gson.toJson(sitem) );
			return json;
		}

		public ResponseModel sendPay(RequestItem sitem)
			{
				Gson gson= new Gson();

				ResponseModel json = jsonParser.requestPOSTJSON(
						Constant.DOMAIN+"request"+token_string,gson.toJson(sitem) );
				return json;
			}

		public ResponseModel checkBalance()
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();



				ResponseModel json = jsonParser.requestPOST(
						AccountItem.URL.URL_BALANCE, params,"");
				return json;
			}

		public ResponseModel getreceipt(String id)
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


				params.add(new NameValuePair("request_id", id));

				ResponseModel json = jsonParser.requestPOST(
						ReceiptItem.URL_RECEIPT, params,"");
				return json;
			}

		public ResponseModel redeemPoints(String points)
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


				params.add(new NameValuePair("points", points));

				ResponseModel json = jsonParser.requestPOST(
						AccountItem.URL.URL_REDEEM_POINTS, params,"");
				return json;
			}

	}
