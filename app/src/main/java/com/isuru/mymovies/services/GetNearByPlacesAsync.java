package com.isuru.mymovies.services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Isuru Senanayake on 17/03/2019.
 *
 * -- This Async task Fetching the data from Google Places API and process
 */

public class GetNearByPlacesAsync extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... params) {

        mMap = (GoogleMap) params[0];
        url = (String) params[1];   // Taking the URL form the params

        try {
            URL placesUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) placesUrl.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            stringBuilder = new StringBuilder();

            // Reading the result
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            LatLng latLng = null;

            /*
             * Processing the JSON result object.
             */
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject jsonLocation = jsonObject1.getJSONObject("geometry").getJSONObject("location");

                String lati = jsonLocation.getString("lat");
                String lon = jsonLocation.getString("lng");

                JSONObject nameObj = jsonArray.getJSONObject(i);
                String address = nameObj.getString("name");

                latLng = new LatLng(Double.parseDouble(lati), Double.parseDouble(lon));

                // Marking the Places in the Google map
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(address);
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(markerOptions);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex){
            Log.e("AsyncTaskError", ex.getMessage());
        }

        super.onPostExecute(s);
    }
}
