package ke.co.eaglesafari.activity_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import ke.co.eaglesafari.R;
import ke.co.eaglesafari.Receipt;
import ke.co.eaglesafari.auth.UserDetails;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.RequestItem;
import ke.co.eaglesafari.items.UserItem;
import ke.co.eaglesafari.maps.LatLngInterpolator;
import ke.co.eaglesafari.maps.SyncedMapFragment;
import ke.co.eaglesafari.net.RequestServiceNet;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.views.PDialog;
import ke.co.eaglesafari.views.PlaceAutoComp;
import ke.co.eaglesafari.views.Tos;
import ke.co.eaglesafari.volley.AppController;

public class RequestModel

{
    Activity activity;
    RequestItem item = new RequestItem();
    PDialog pdia;

    boolean FLAG_DRIVER_SYNC_RUN = true;
    /******************************************/
    private static final String TAG = "Fragment Autocomplete";
    private static final String LOG_TAG = "G P Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyA_scM4bfN4llfAYpufU70p5VTyBiYSz5w";
    ArrayList<UserItem> userItemArrayList = new ArrayList<>();
    public ConcurrentHashMap<String, Marker> markerHashMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, String> userMarkersHashMap = new ConcurrentHashMap<>();

    GoogleMap mGoogleMap;
    PlaceAutoComp autocompleteFragment;
    /****************************************/
    final GoogleMap
            googleMap;
    LatLng my_position;
    String token;
    SyncedMapFragment syncedMapFragment;

    public RequestModel(final Activity activity, final GoogleMap googleMap, SyncedMapFragment syncedMapFragment) {
        this.syncedMapFragment = syncedMapFragment;

        this.activity = activity;
        this.googleMap = googleMap;
        UserDetails userDetails = new UserDetails(activity);
        token = userDetails.getToken();
        pdia = new PDialog(activity);

        autocompleteFragment = (PlaceAutoComp)
                activity.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        PlaceAutoComp autocompleteFragment2 = (PlaceAutoComp)
                activity.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

/*
* The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
* set a filter returning only results with a precise address.
*/
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment2.setFilter(typeFilter);
        autocompleteFragment.start("Pick Point", "");
        autocompleteFragment2.start("Destination", "");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());//get place details here

                item.setPick_point(place.getName().toString());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
                setMy_position(place.getLatLng());


            }


            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                item.setDestination(place.getName().toString());
                Log.i(TAG, "Place: " + place.getName());//get place details here
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        setBtn_request();


        getBtn_request().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                send();
            }
        });

    }

    ImageButton btn_request;


    public Activity getActivity() {
        return activity;
    }


    public void updatelocation(String address) {
        autocompleteFragment.start("Pick Location", address);

        item.setPick_point(address);


    }

    public ImageButton getBtn_request() {
        return btn_request;
    }

    public void setBtn_request() {
        this.btn_request = (ImageButton) activity.findViewById(R.id.request_btn);
    }


    public void send() {

        if (!isEmpty()) {
            pdia.start("Checking connection");
            pdia.update("Submitting Request...");
            new RequestServiceNet(item, activity, new Pchk()
            ).execute();
        }

    }

    public void setTaxi(String taxi) {
        FLAG_DRIVER_SYNC_RUN = false;
        item.setEmployee_id(taxi);
    }

    public boolean isEmpty() {
        if (item.getEmployee_id().isEmpty()) {
            new Tos(activity.getApplicationContext())
                    .s("Select  a taxi from the map");
            return true;
        }
        if (item.getDestination().isEmpty()) {
            new Tos(activity.getApplicationContext())
                    .s("Enter destination name");
            return true;
        }

        if (item.getPick_point().isEmpty()) {
            new Tos(activity.getApplicationContext())
                    .s("Select a valid pick point");
            return true;
        }


        return false;
    }


    public class Pchk implements I_Response<Boolean, String> {

        @Override
        public void onTaskCompleted(Boolean i) {
            // TODO Auto-generated method stub
            pdia.end();
            if (i) {
                                /*

								// show dialog
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(
										activity);

								// Setting Dialog Title
								alertDialog.setTitle("Pay for Service");

								// Setting Dialog Message
								alertDialog.setMessage("Do you want to pay?"
										+ x.getMessage());

								// On pressing Settings button
								alertDialog.setPositiveButton("Pay",
										new DialogInterface.OnClickListener()
											{
												@Override
												public void onClick(DialogInterface dialog,
														int which)
													{
														pdia.start("Connecting...");
														new NetCheck(activity, new Chk_Pay())
																.execute();
													}
											});

								// on pressing cancel button
								alertDialog.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener()
											{
												@Override
												public void onClick(DialogInterface dialog,
														int which)
													{
														dialog.cancel();
													}
											});

								// Showing Alert Message
								alertDialog.show();

							} else
							{
								// reset();
								new AlertDia(activity).show("Response", x.getMessage(),
										"ok");
							}
								 */

            }

        }

        @Override
        public void onTaskCommpletedMessage(String s) {
            new Tos(activity).s(s);

        }

        @Override
        public void onData(String s, Boolean aBoolean) {


            if (aBoolean) {
                Intent intent = new Intent(activity, Receipt.class);
                intent.putExtra(Constant.KEY_VALUE, s);
                activity.startActivity(intent);
                //activity.finish();
            } else {
                // Toast.makeText(activity,"Something happened and we could not process your request",Toast.LENGTH_SHORT).show();
            }
        }


    }


    public void reset() {
        //getEdit_pick().setText("");
        //getEdit_destination().setText("");
    }

    /***********************************************/


    public void setMy_position(LatLng latLng) {
        my_position = latLng;
    }


    public void get_location(String latitude, String longitude) {
        if (!isNetworkAvailable())
            return;
        String Url = Constant.DOMAIN + "location?token=" + token + "&latitude=" + latitude + "&longitude=" + longitude;
        Log.e("Url", Url);
        RequestQueue queue = AppController.getInstance().getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        userItemArrayList.clear();

                        LatLngInterpolator mLatLngInterpolator = new LatLngInterpolator.Linear();
                        Log.e("Response", response.toString());
                        try {
                            Gson gson = new Gson();
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    UserItem userItem1 = gson.fromJson(response.getJSONObject(i).toString(), UserItem.class);
                                    if (userItem1.getCar() != null)
                                        userItemArrayList.add(userItem1);

                                } catch (JSONException h) {
                                    h.printStackTrace();
                                }
                            }
                            for (int i = 0; i < userItemArrayList.size(); i++) {
                                UserItem userItem = userItemArrayList.get(i);
                                if (markerHashMap.get(userItem.getId()) != null) {

                                    Marker marker = markerHashMap.get(userItem.getId());
                                    float d = computeAngleBetween(marker.getPosition(), new LatLng(Double.parseDouble(userItem.getLatitude()), Double.parseDouble(userItem.getLongitude())));
                                    Log.e("Rotation", d + "");


                                    marker.setAnchor(0, 0);
                                    marker.setRotation(d);

                                    animateMarkerToGB(marker, new LatLng(Double.parseDouble(userItem.getLatitude()), Double.parseDouble(userItem.getLongitude())), mLatLngInterpolator);
                                    //	animateMarker(marker,new LatLng(Double.parseDouble(userItem.getLatitude()),Double.parseDouble(userItem.getLongitude())),false);
                                    //  syncedMapFragment.animateMarkerToGB(marker,Double.parseDouble(userItem.getLatitude()),Double.parseDouble(userItem.getLongitude()),mLatLngInterpolator,5000);
                                } else {
                                    if (userItem.getCar() != null) {
                                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(userItem.getLatitude()), Double.parseDouble(userItem.getLongitude())))
                                                .title(userItem.getName()).snippet(userItem.getCar().getRegistration())
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_red))
                                                .draggable(false)
                                                .anchor(0, 0)
                                                .rotation(90));
                                        markerHashMap.put(userItem.getId(), marker);
                                        userMarkersHashMap.put(marker.getId(), gson.toJson(userItem));
                                    }
                                }


                            }

                            check_obsolete();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("yog", error.toString());
                    }
                });

        //Creating request queue
        queue.start();
        jsonArrayRequest.setTag(Constant.KEY_VALUE);
        queue.add(jsonArrayRequest);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean isKeyExist(String id) {
        for (int i = 0; i < userItemArrayList.size(); i++) {
            UserItem userItem = userItemArrayList.get(i);
            if (id.equals(userItem.getId())) {
                return true;
            }
        }
        return false;
    }

    public void check_obsolete() {

        for (String key : markerHashMap.keySet()) {
            if (!isKeyExist(key)) {
                Marker marker = markerHashMap.get(key);
                userMarkersHashMap.remove(marker.getId());
                marker.remove();
                markerHashMap.remove(key);

            }
        }

        //check if the marker exists in the above


    }

    static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


    public float computeAngleBetween(LatLng start, LatLng end) {
        Location beginLocation = convertLatLngToLocation(start);
        Location endLocation = convertLatLngToLocation(end);
        return beginLocation.bearingTo(endLocation);
    }

    private Location convertLatLngToLocation(LatLng latLng) {
        Location location = new Location("someLoc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }
}
