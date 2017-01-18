package ke.co.eaglesafari.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import ke.co.eaglesafari.R;
import ke.co.eaglesafari.Receipt;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.views.PDialog;
import ke.co.eaglesafari.volley.AppController;
import ke.co.eaglesafari.volley.RoundedCornerNetworkImageView;


public class RequestAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<RequestItem> data;
    private LayoutInflater inflater = null;
    String id;
    String status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private final HashSet<MapView> mMaps = new HashSet<MapView>();

    public RequestAdapter(Activity a, ArrayList<RequestItem> d,
                          String status) {
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
        gson = new Gson();
        this.status = status;
        pdia = new PDialog(a);
        if (data.size() == 0) {

        }
    }

    public void recycle(ListView listView) {
        AbsListView lv = listView;
        lv.setRecyclerListener(mRecycleListener);
    }

    public void updateList(ArrayList<RequestItem> d) {
        data.clear();
        data = d;
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
            row = inflater.inflate(R.layout.history, null);

            // Set up holder and assign it to the View
            holder = new ViewHolder();
            holder.mapView = (MapView) row.findViewById(R.id.lite_listrow_map);
            holder.txt_amount = (TextView) row.findViewById(R.id.txt_amount);
            holder.txt_name = (TextView) row.findViewById(R.id.txt_name);
            holder.txt_service = (TextView) row.findViewById(R.id.txt_service);
            holder.img = (RoundedCornerNetworkImageView) row.findViewById(R.id.img_user);
            holder.btn_proceed = (Button) row.findViewById(R.id.btn_proceed);
            // Set holder as tag for row for more efficient access.
            row.setTag(holder);

            // Initialise the MapView
            holder.initializeMapView();


            // Keep track of MapView
            mMaps.add(holder.mapView);
        } else {
            // View has already been initialised, get its holder
            holder = (ViewHolder) row.getTag();
        }
        final RequestItem requestItem = data.get(position);
        holder.btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Receipt.class);
                intent.putExtra(Constant.KEY_VALUE, gson.toJson(requestItem));
                activity.startActivity(intent);
            }
        });


        try {
            if (!requestItem.getPickup_lat().isEmpty() && !requestItem.getPickup_long().isEmpty() && !requestItem.getDestination_lat().isEmpty() && !requestItem.getDestination_long().isEmpty()) {
                NamedLocation item = new NamedLocation(requestItem.getPick_point(), new LatLng(Double.parseDouble(requestItem.getPickup_lat()), Double.parseDouble(requestItem.getPickup_long())),
                        requestItem.getDestination(), new LatLng(Double.parseDouble(requestItem.getDestination_lat()), Double.parseDouble(requestItem.getDestination_long())));
                holder.mapView.setTag(item);
                if (holder.map != null) {
                    // The map is already ready to be used
                    setMapLocation(holder.map, item);
                }

            } else {
                NamedLocation item = new NamedLocation(requestItem.getPick_point(), new LatLng(0, 0), requestItem.getDestination(), new LatLng(0, 0));
                holder.mapView.setTag(item);
                if (holder.map != null) {
                    // The map is already ready to be used
                    setMapLocation(holder.map, item);
                }
            }
        } catch (NullPointerException p) {


            p.printStackTrace();
            NamedLocation item = new NamedLocation(requestItem.getPick_point(), new LatLng(0, 0), requestItem.getDestination(), new LatLng(0, 0));
            holder.mapView.setTag(item);
            if (holder.map != null) {
                // The map is already ready to be used
                setMapLocation(holder.map, item);
            }
        }
        // Get the NamedLocation for this item and attach it to the MapView


        // Ensure the map has been initialised by the on map ready callback in ViewHolder.
        // If it is not ready yet, it will be initialised with the NamedLocation set as its tag
        // when the callback is received.


        switch (requestItem.getStatus()) {
            case "1":
                holder.txt_service.setText(requestItem.getService().getName() + "(Inquiry)");
                break;
            case "2":
                holder.txt_service.setText(requestItem.getService().getName() + "(In Progress)");
                holder.btn_proceed.setVisibility(View.GONE);
                break;
            case "3":
                holder.txt_service.setText(requestItem.getService().getName() + "(In Progress");
                holder.btn_proceed.setVisibility(View.GONE);
                break;
            case "4":
                holder.txt_service.setText(requestItem.getService().getName() + "(Completed)");
                holder.btn_proceed.setVisibility(View.GONE);
                break;
            default:
                holder.txt_service.setText(requestItem.getService().getName());
                break;
        }


        try {
            holder.txt_amount.setText(format.format(Double.parseDouble(requestItem.getAmount())));
        } catch (NumberFormatException e) {

        }


        try {
            if (requestItem.getEmployee() != null)
                holder.txt_name.setText(requestItem.getEmployee().getName());
            else
                holder.txt_name.setText("Un confirmed");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            holder.img.setImageUrl(Constant.DOMAIN_IMAGES_PROFILES + requestItem.getEmployee().getProfile_image(), imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        map.setPadding(100, 100, 100, 100);
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

        NamedLocation(String start, LatLng locationstart, String end, LatLng locationend) {
            this.start = start;
            this.locationstart = locationstart;
            this.end = end;
            this.locationend = locationend;
        }
    }

    private AbsListView.RecyclerListener mRecycleListener = new AbsListView.RecyclerListener() {

        @Override
        public void onMovedToScrapHeap(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.map != null) {
                // Clear the map and free up resources by changing the map type to none
                holder.map.clear();
                holder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
            }

        }
    };

    class ViewHolder implements OnMapReadyCallback {

        MapView mapView;

        TextView txt_name, txt_service, txt_amount;
        RoundedCornerNetworkImageView img;
        Button btn_proceed;

        GoogleMap map;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(activity);
            map = googleMap;
            NamedLocation data = (NamedLocation) mapView.getTag();
            if (data != null) {
                setMapLocation(map, data);
            }
        }

        /**
         * Initialises the MapView by calling its lifecycle methods.
         */
        public void initializeMapView() {
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
        }

    }

}
