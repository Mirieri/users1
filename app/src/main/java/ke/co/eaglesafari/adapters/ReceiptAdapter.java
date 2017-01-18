package ke.co.eaglesafari.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ke.co.eaglesafari.R;
import ke.co.eaglesafari.items.ChargeItem;
import ke.co.eaglesafari.views.PDialog;
import ke.co.eaglesafari.volley.AppController;


public class ReceiptAdapter extends BaseAdapter {
    private Activity activity;
    private List<ChargeItem> data;
    private LayoutInflater inflater = null;
    String id;
    PDialog pdia;
    ImageLoader imageLoader;
    NumberFormat format;
    Gson gson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    private final HashSet<MapView> mMaps = new HashSet<MapView>();
    public ReceiptAdapter(Activity a, List<ChargeItem> d) {
        activity = a;
        data = d;
        imageLoader = AppController.getInstance().getImageLoader();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Ksh.");
        dfs.setGroupingSeparator(',');
        dfs.setMonetaryDecimalSeparator('.');
        ((DecimalFormat) format).setDecimalFormatSymbols(dfs);
        gson= new Gson();
        pdia = new PDialog(a);

    }
    public void recycle(ListView listView)
    {
        AbsListView lv = listView;
        lv.setRecyclerListener(mRecycleListener);
    }
    public void updateList(ArrayList<ChargeItem> d){
        data.clear();
        data=d;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        // Check if a view can be reused, otherwise inflate a layout and set up the view holder
        if (row == null) {
            // Inflate view from layout file
            row= inflater.inflate(R.layout.charges_list, null);

            // Set up holder and assign it to the View
            holder = new ViewHolder();
            holder.txt_item = (TextView) row.findViewById(R.id.txt_item);
            holder.txt_number = (TextView) row.findViewById(R.id.txt_number);
            holder.txt_price = (TextView) row.findViewById(R.id.txt_price);
            holder.txt_sub = (TextView) row.findViewById(R.id.txt_sub_total);
            // Set holder as tag for row for more efficient access.
            row.setTag(holder);

            // Initialise the MapView


            // Keep track of MapView
        } else {
            // View has already been initialised, get its holder
            holder = (ViewHolder) row.getTag();
        }
        final ChargeItem chargeItem = data.get(position);
        holder.txt_sub.setText(String.valueOf(Double.parseDouble(chargeItem.getQuantity()) * Double.parseDouble(chargeItem.getAmount())));
        holder.txt_price.setText(chargeItem.getAmount()+" "+chargeItem.getUnit());
        holder.txt_number.setText(chargeItem.getQuantity());
        holder.txt_item.setText(chargeItem.getName());



        return row;
    }

    /**
     * Retuns the set of all initialised {@link MapView} objects.
     *
     * @return All MapViews that have been initialised programmatically by this adapter
     */
    public HashSet<MapView> getMaps() {
        return mMaps;
    }




















    private static void setMapLocation(GoogleMap map, NamedLocation data) {
        // Add a marker for this item and set the camera

        map.setPadding(100,100,100,100);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();




        builder.include(data.locationstart);
        builder.include(data.locationend);
        LatLngBounds bounds = builder.build();
        int padding = 0;

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        PolylineOptions rectOptions = new PolylineOptions()
                .add(data.locationstart)
                .add(data.locationend);  // North of the previous point, but at the same longitude
        // Closes the polyline.

// Get back the mutable Polyline
        Polyline polyline = map.addPolyline(rectOptions);
        map.addMarker(new MarkerOptions().position(data.locationstart).icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        map.addMarker(new MarkerOptions().position(data.locationend).icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
        // Set the map type back to normal.
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(cu);
    }
    private static class NamedLocation {

        public final String start;

        public final LatLng locationstart;
        public final String end;

        public final LatLng locationend;

        NamedLocation(String start, LatLng locationstart,String end, LatLng locationend) {
            this.start = start;
            this.locationstart = locationstart;
            this.end=end;
            this.locationend=locationend;
        }
    }
    private AbsListView.RecyclerListener mRecycleListener = new AbsListView.RecyclerListener() {

        @Override
        public void onMovedToScrapHeap(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();


        }
    };
    class ViewHolder {

        TextView txt_item,txt_number,txt_price,txt_sub;

    }

}
