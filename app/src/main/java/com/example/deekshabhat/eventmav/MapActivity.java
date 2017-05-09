//package com.example.deekshabhat.eventmav;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.example.deekshabhat.eventmav.R.id.map;
//
//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {
//
//    final int PERMISSION_REQUEST_LOCATION = 101;
//    GoogleMap gMap;
//    GoogleApiClient mGoogleApiClient;
//    LocationRequest mLocationRequest;
//    double destLat, destLong;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
//        mapFragment.getMapAsync(this);
//        //gMap.setMyLocationEnabled(true);
//
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        }
//    }
//
//    protected void onStart() {
//        mGoogleApiClient.connect();
//        super.onStart();
//    }
//
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    private void startLocationUpdates() {
//        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission( getBaseContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return  ;
//        }
//
//        gMap.setMyLocationEnabled(true);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//        switch (requestCode) {
//            case PERMISSION_REQUEST_LOCATION: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    startLocationUpdates();
//
//                } else {
//                    Toast.makeText(MapActivity.this, "EventMav will not locate your events", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission( getBaseContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            return  ;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//   }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission( getBaseContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return  ;
//        }
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission( getBaseContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return  ;
//        }
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        Point size = new Point();
//        WindowManager w = getWindowManager();
//        //1
//        w.getDefaultDisplay().getSize(size);
//        int measuredWidth = size.x;
//        int measuredHeight = size.y;
//
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        // TODO
//        String address = "Central Library, Arlington, TX 76013";
//        String eventname = "GN Lite";
//
//        List<Address> addresses = null;
//        Geocoder geo = new Geocoder(this);
//        try {
//            addresses = geo.getFromLocationName(address, 1);
//        } catch (IOException e) {
//            Log.e("Error","Something went wrong in getting addresses");
//        }
//
//        LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
//        builder.include(point);
//        gMap.addMarker(new MarkerOptions().position(point).title(eventname).snippet(address)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot));
//        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 400));
//        destLat = point.latitude;
//        destLong = point.longitude;
//
//        createLocationRequest();
//
//        /*try {
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (ContextCompat.checkSelfPermission(ContactMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                        Snackbar.make(findViewById(R.id.activity_contact_map), "MyContactList requires this permission to locate your contacts", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                ActivityCompat.requestPermissions(ContactMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
//                            }
//                        })
//                                .show();
//
//                    } else {
//                        ActivityCompat.requestPermissions(ContactMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
//                    }
//                } else {
//                    startLocationUpdates();
//                }
//            } else {
//                startLocationUpdates();
//            }
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
//        }*/
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Toast.makeText(getBaseContext(), "Lat: "+location.getLatitude()+ " Long: "+location.getLongitude()+" Accuracy:  "+ location.getAccuracy(), Toast.LENGTH_LONG).show();
//        double lat1 = location.getLatitude();
//        double long1 = location.getLongitude();
//        drawPrimaryLinePath(lat1,long1,destLat,destLong);
//    }
//
//    private void drawPrimaryLinePath(double lat1, double long1, double destLat, double destLong)
//    {
//        PolylineOptions options = new PolylineOptions();
//        options.color( Color.parseColor( "#CC0000FF" ) );
//        options.width( 5 );
//        options.visible( true );
//        options.add(new LatLng(lat1, long1));
//        options.add(new LatLng(destLat, destLong));
//        gMap.addPolyline( options );
//
//    }
//
//}
