package ke.co.eaglesafari.net;

import android.content.Context;
import android.os.AsyncTask;

import ke.co.eaglesafari.items.ReceiptItem;
import ke.co.eaglesafari.items.RequestItemReceive;
import ke.co.eaglesafari.net.network.I_netcheck;
import ke.co.eaglesafari.net.network.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptNet extends AsyncTask<String, String, JSONObject>
	{

		I_netcheck<Boolean, ReceiptItem>	callback;
		Context									context;
		String									request_id;

		public ReceiptNet(I_netcheck<Boolean, ReceiptItem> callback,
				Context context, String request_id)
			{
				// TODO Auto-generated constructor stub
				this.callback = callback;
				this.context = context;
				this.request_id = request_id;
			}

		@Override
		protected JSONObject doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				JSONObject json = null;
				UserFunctions usf = new UserFunctions(context);
				//json = usf.getreceipt(request_id);
				return json;
			}

		@Override
		protected void onPostExecute(JSONObject json)
			{
				try
					{
						if (json.has("success"))
							{
								try
									{
										if (json.getString("success").equals("1"))
											{
												ReceiptItem repitem = new ReceiptItem();
												JSONObject j = json.getJSONObject("task");
												RequestItemReceive item = new RequestItemReceive();

												item.setCreated_at(j
														.getString((RequestItemReceive.Cons.KEY_CREATED_AT)));
												item.setDestination(j
														.getString((RequestItemReceive.Cons.KEY_DESTINATION)));
												item.setName(j
														.getString(RequestItemReceive.Cons.KEY_NAME));
												item.setPick_up(j
														.getString(RequestItemReceive.Cons.KEY_PICK_UP));
												item.setRequest_id(j
														.getString(RequestItemReceive.Cons.KEY_REQUEST_ID));
												item.setService(j
														.getString(RequestItemReceive.Cons.KEY_SERVICE));
												item.setTelephone(j
														.getString(RequestItemReceive.Cons.KEY_TELEPHONE));
												item.setAmount(j
														.getString(RequestItemReceive.Cons.KEY_AMOUNT));
												item.setStatus(j
														.getString(RequestItemReceive.Cons.KEY_STATUS));

												repitem.setItem(item);
												JSONArray jarray = json
														.getJSONArray("items");
												if (jarray.length() > 0)
													{
														JSONObject i1 = jarray
																.getJSONObject(0);
														repitem.setItem_1(i1
																.getString("item_name"));
														repitem.setNumber_1(i1
																.getString("number"));
														repitem.setPrice_1(i1
																.getString("cost_per_item"));
														int s1 = Integer.parseInt(i1
																.getString("number"))
																* Integer
																		.parseInt(i1
																				.getString("cost_per_item"));
														repitem.setSub_1(String.valueOf(s1));

														if (jarray.length() > 1)
															{
																JSONObject i2 = jarray
																		.getJSONObject(1);
																repitem.setItem_2(i2
																		.getString("item_name"));
																repitem.setNumber_2(i2
																		.getString("number"));
																repitem
																		.setPrice_2(i2
																				.getString("cost_per_item"));
																int s2 = Integer.parseInt(i2
																		.getString("number"))
																		* Integer
																				.parseInt(i2
																						.getString("cost_per_item"));
																repitem.setSub_2(String
																		.valueOf(s2));

															}

													}

												callback.onTaskCompleted(true);
												callback.onMessage(repitem);

											} else
											{
												callback.onTaskCompleted(false);

											}
									} catch (JSONException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							} else
							{
								callback.onTaskCompleted(false);
								// callback.onMessage("Error Occured");

							}
					} catch (NullPointerException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						callback.onTaskCompleted(false);
						// callback.onMessage("Error Occured");
					}

			}

	}
