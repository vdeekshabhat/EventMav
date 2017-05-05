package com.example.deekshabhat.eventmav;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mEvent;
    private TextView tvDetailsEventName;
    private TextView tvDetailsEventDate;
    private TextView tvDetailsEventLocation;
    private TextView tvDetailsEventDescription;
    private TextView tvDetailsEventCount;
    private String eventID = null;
    private Button buDetailsRegister,budetailsNavigate, buDetailsShare;
    private String userID;
    private int count;
    private String eventName;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvDetailsEventName = (TextView) findViewById(R.id.tvDetailsEventName);
        tvDetailsEventDate = (TextView) findViewById(R.id.tvDetailsEventDate);
        tvDetailsEventLocation = (TextView) findViewById(R.id.tvDetailsEventLocation);
        tvDetailsEventDescription = (TextView) findViewById(R.id.tvDetailsEventDescription);
        tvDetailsEventCount = (TextView) findViewById(R.id.tvDetailsEventCount);
        buDetailsRegister = (Button) findViewById(R.id.buDetailsRegister);
        budetailsNavigate = (Button) findViewById(R.id.budetailsNavigate);

        buDetailsShare = (Button) findViewById(R.id.buDetailsShare);
        budetailsNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermission()) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(EventDetailsActivity.this, EventPathActivity.class);
                        intent.putExtra("eventAddress", tvDetailsEventLocation.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });

        buDetailsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send the message
                String eventname = tvDetailsEventName.getText().toString();
                String subject = "Hey! Check this out - "+eventname;
                String message = "Hello, Your friend has invited you to attend "+eventname;
                shareEvent(message);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        Intent intent=getIntent();
        if(intent!=null) {
            eventID = getIntent().getStringExtra("eventID");
            displayEventInfo();
        }

        buDetailsRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                mDatabase.child("Events").child(eventID).child("eventCount").setValue(String.valueOf(count));
                mDatabase.child("Registration").child(userID).child(eventID).setValue(eventName);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                buDetailsRegister.setVisibility(View.GONE);
                Log.d("updatedcount", String.valueOf(count));
                //tvDetailsEventCount.setText(count);
            }
        });

        checkRegistration();

//        buDetailsRemind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("We need your permission to access the device current location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(EventDetailsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventDetailsActivity.this, EventPathActivity.class);
                        intent.putExtra("eventAddress", tvDetailsEventLocation.getText().toString());
                        startActivity(intent);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "You can't access show map without location permission", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

    private void shareEvent(String message) {
        /*Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, );
        startActivity(intent);*/
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("sms_body",message);
        startActivity(smsIntent);
    }

    private void checkRegistration(){
        mDatabase.child("Registration").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(eventID))
                    buDetailsRegister.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayEventInfo() {
        mDatabase.child("Events").child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("eventdata", dataSnapshot.getKey());
                tvDetailsEventName.setText(dataSnapshot.child("eventName").getValue(String.class));
                tvDetailsEventDate.setText(dataSnapshot.child("eventDate").getValue(String.class));
                tvDetailsEventLocation.setText(dataSnapshot.child("evenLocation").getValue(String.class));
                tvDetailsEventDescription.setText(dataSnapshot.child("evenDescription").getValue(String.class));
                tvDetailsEventCount.setText(dataSnapshot.child("eventCount").getValue(String.class));
                eventName=(dataSnapshot.child("eventName").getValue(String.class)).toString();
                count = Integer.parseInt(dataSnapshot.child("eventCount").getValue().toString());
                if (count == 0){
                    // Remove display button
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        } else if (id == R.id.nav_camera) {
            startActivity(new Intent(getBaseContext(),ProfileActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(getBaseContext(),AddEventActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(getBaseContext(),MyEventActivity.class));

        } else if (id == R.id.nav_manage) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getBaseContext(),LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
