package com.isuru.mymovies.screens;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isuru.mymovies.R;
import com.isuru.mymovies.services.GetNearByPlacesAsync;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- This class handles the Google map functions and the google places API functions
 *
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 1000;
    GoogleApiClient client;
    LocationRequest locationRequest;
    LatLng latLng;
    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Executes when Map is initialized.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();
    }

    /**
     * Executes when location is changed.
     */
    @Override
    public void onLocationChanged(Location location) {

        if(location != null && isFirstTime) {   // Draw the marker of the current location in the very first time only
            isFirstTime = false;
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, getResources().getInteger(R.integer.map_zoom_level));  // Zooming the camera of the Map
            mMap.animateCamera(cameraUpdate);
            mMap.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.current_location_label)));

            findCinemas();
        }
    }

    /**
     * Setting up the parameters for the Google Location API.
     */
    public void findCinemas(){
        StringBuilder stringBuilder = new StringBuilder(getResources().getString(R.string.google_places_api));
        stringBuilder.append("&location="+ latLng.latitude + "," + latLng.longitude);
        stringBuilder.append("&radius="+ getResources().getInteger(R.integer.search_radius));   // Defining the search radius
        stringBuilder.append("&keyword="+"movies");
        stringBuilder.append("&key=" + getResources().getString(R.string.google_places_key));

        Object dataSend[] = new Object[2];
        dataSend[0] = mMap;
        dataSend[1] = stringBuilder.toString();

        // Passing URL to the Async class
        GetNearByPlacesAsync getNearByPlacesAsync = new GetNearByPlacesAsync();
        getNearByPlacesAsync.execute(dataSend);
    }

    /**
     * Fetching the current Geo Location.
     */
    @Override
    public void onConnected(Bundle bundle) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationRequest = new LocationRequest().create();
        locationRequest.setInterval(MIN_TIME);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
