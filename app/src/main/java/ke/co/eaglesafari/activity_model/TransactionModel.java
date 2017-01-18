package ke.co.eaglesafari.activity_model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import ke.co.eaglesafari.R;
import ke.co.eaglesafari.Receipt;
import ke.co.eaglesafari.adapters.RequestAdapter;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.db.CurrentRequestDb;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.net.GetHistoryTask;
import ke.co.eaglesafari.net.network.I_Response;

import java.util.ArrayList;

public class TransactionModel
	{
		SwipyRefreshLayout swipeRefreshLayout;

		public TransactionModel(Activity acti,ListView listView)
			{
				this.activity = acti;
				setList_view(listView);


				getList_view().setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						Gson gson= new Gson();
						Intent intent =new Intent(activity, Receipt.class);
						intent.putExtra(Constant.KEY_VALUE,gson.toJson(ritem.get(position)));
						activity.startActivity(intent);
					}
				});

				 swipeRefreshLayout = (SwipyRefreshLayout)  getActivity().findViewById(R.id.swipyrefreshlayout);
				swipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
				swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh(SwipyRefreshLayoutDirection direction) {

						if(direction == SwipyRefreshLayoutDirection.TOP)
						{
							new GetHistoryTask(activity, new Tc(),true).execute();
						}
						else
						{
							new GetHistoryTask(activity, new Tc(),false).execute();
						}
						Log.d("MainActivity", "Refresh triggered at "
								+ (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
					}});
				swipeRefreshLayout.setColorSchemeResources(R.color.red_btn_bg_color, R.color.gray_btn_bg_color);

				this.adapter = new RequestAdapter(activity, ritem, "2");
				list_view.setAdapter(adapter);
				adapter.recycle(list_view);


				check();




			}

		Activity								activity;
		RequestAdapter						adapter;
		ArrayList<RequestItem>	ritem = new ArrayList<>();
		ListView								list_view;

		public Activity getActivity()
			{
				return activity;
			}

		public RequestAdapter getAdapter()
			{
				return adapter;
			}

		private void notifyDataset()
			{
				CurrentRequestDb db = CurrentRequestDb.getInstance(activity);
				db.getWritableDatabase();
				ritem.clear();
				ritem = db.get();

				adapter.updateList(ritem);
			}

		public ArrayList<RequestItem> getRitem()
			{
				return ritem;
			}








		public ListView getList_view()
			{
				return list_view;
			}

		public void setList_view(ListView list_view)
			{
				this.list_view = list_view;
			}


		public void check()
			{
				CurrentRequestDb ldb= CurrentRequestDb.getInstance(activity);
				ldb.getWritableDatabase();
				if(ldb.count()==0) {


					swipeRefreshLayout.setRefreshing(true);
					new GetHistoryTask(activity, new Tc(),true).execute();
				}
				else
				{
					notifyDataset();
				}
			}


		public class Tc implements I_Response<Boolean, String>
			{

				@Override
				public void onTaskCompleted(Boolean i)
					{
						swipeRefreshLayout.setRefreshing(false);
						if (i)
							{
								notifyDataset();
							} else
							{
							}

					}

				@Override
				public void onTaskCommpletedMessage(String s) {

				}

				@Override
				public void onData(String s, Boolean aBoolean) {

				}


			}

	}
