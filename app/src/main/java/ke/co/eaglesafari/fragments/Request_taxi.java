package ke.co.eaglesafari.fragments;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ke.co.eaglesafari.R;
import ke.co.eaglesafari.activity_model.RequestModel;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.UserItem;
import ke.co.eaglesafari.maps.SyncedMapFragment;
import ke.co.eaglesafari.volley.AppController;

public class Request_taxi extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap googleMap;

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    String lat, lon;
    private LatLng center;
    RequestModel requestModel;
    private static View view;
    boolean ran_volley = true;
    Timer timer;
    SyncedMapFragment mapFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_request_taxi, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SyncedMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SyncedMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapFragmentContainer, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        cancel_volley();
        try {
            timer.cancel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();
        requestQueue.cancelAll(Constant.KEY_VALUE);


    }


    @Override
    public void onMapReady(GoogleMap gmap) {

        googleMap = gmap;
        requestModel = new RequestModel(getActivity(), googleMap, mapFragment);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getActivity(), "Enable GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        googleMap.setMyLocationEnabled(true);


        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub
                center = googleMap.getCameraPosition().target;
                //markerText.setText(" Set your Location ");
                updateLocation(center);
                requestModel.setMy_position(center);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                String data = requestModel.userMarkersHashMap.get(arg0.getId());
                Gson gson = new Gson();
                final UserItem userItem = gson.fromJson(data, UserItem.class);

                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Select for " + userItem.getName() + " driving " + userItem.getCar())
                        .setContentText("Kindly confirm the driver for your Eagle Safari ride")
                        .setCancelText("No,cancel")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setConfirmClickListener
                                (new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        requestModel.setTaxi(userItem.getId());
                                        sDialog.cancel();
                                    }
                                })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });
        setRepeatingAsyncTask();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);// Update location every second

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());

        }
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void updateUI() {
        try {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 17));
            updateLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
        } catch (NullPointerException r) {
            r.printStackTrace();
        }


    }

    private void updateLocation(LatLng centerLatLng) {
        cancel_volley();

        requestModel.setMy_position(centerLatLng);


        if (centerLatLng != null) {
            Geocoder geocoder = new Geocoder(getActivity(),
                    Locale.getDefault());

            List<Address> addresses = new ArrayList<Address>();
            try {
                addresses = geocoder.getFromLocation(centerLatLng.latitude,
                        centerLatLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {

                String addressIndex0 = (addresses.get(0).getAddressLine(0) != null) ? addresses
                        .get(0).getAddressLine(0) : null;
                String addressIndex1 = (addresses.get(0).getAddressLine(1) != null) ? addresses
                        .get(0).getAddressLine(1) : null;
                String addressIndex2 = (addresses.get(0).getAddressLine(2) != null) ? addresses
                        .get(0).getAddressLine(2) : null;
                String addressIndex3 = (addresses.get(0).getAddressLine(3) != null) ? addresses
                        .get(0).getAddressLine(3) : null;

                String completeAddress = addressIndex0 + "," + addressIndex1;

                if (addressIndex2 != null) {
                    completeAddress += "," + addressIndex2;
                }
                if (addressIndex3 != null) {
                    completeAddress += "," + addressIndex3;
                }
                if (completeAddress != null) {
                    requestModel.updatelocation(completeAddress);
                    resume_volley();

                }
            }
        }
    }

    private void cancel_volley() {
        ran_volley = false;
    }

    private void resume_volley() {
        ran_volley = true;
    }

    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (ran_volley)
                                requestModel.get_location(lat, lon);

                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 1, 7 * 1000);  // interval of one minute

    }


}