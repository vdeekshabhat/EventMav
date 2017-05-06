package com.example.deekshabhat.eventmav;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class EventPathActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener gpsLocationListener;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_path);

        final WebView myWebView = (WebView) findViewById(R.id.webview);
        final LatLng destination = getDestination();
        final String eventAddress = getIntent().getExtras().get("eventAddress").toString();
        eventAddress.replaceAll(" ", "+");

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        try {
            final LocationManager locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String source = location.getLatitude()+","+location.getLongitude();
                    String dest = "@"+destination.latitude+","+destination.longitude+"z";
                    String mapsurl = "https://www.google.com/maps/dir/" + source + "/" + eventAddress + "/" + dest;
                    Log.d("mapsurl",mapsurl);
                    myWebView.loadUrl(mapsurl);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            }, null);
        } catch (SecurityException se){

        }
//        myWebView.loadUrl("32.7296731,-97.1130213/1002+Greek+Row+Dr,+Arlington,+TX+76013,+USA/@32.7302534,-97.1213396,16z");
    }

    private LatLng getDestination() {
        String address = "Central Library, Arlington, TX 76013";
        String eventname = "GN Lite";

        List<Address> addresses = null;
        Geocoder geo = new Geocoder(this);
        try {
            addresses = geo.getFromLocationName(address, 1);
        } catch (IOException e) {
            Log.e("Error", "Something went wrong in getting addresses");
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        builder.include(point);
        return point;
    }
}
