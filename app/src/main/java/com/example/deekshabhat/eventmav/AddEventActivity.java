package com.example.deekshabhat.eventmav;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mDatabaseRef;
    private EditText etEventName, etEventDate, etEventLocation, etDescription, etEventCount, etEventTime ;
    private Button btSubmitEvent, btCancelEvent, btMarkMap;
    private Spinner spEventCategory;
    private String eCategory;
    private String userID;
    EventContainer ev;
    String EventName;
    boolean isValidForm=true;
    String databaseEventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef=FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        etEventName = (EditText) findViewById(R.id.etEventName);
        etEventDate = (EditText) findViewById(R.id.etEventDate);
        etEventLocation = (EditText) findViewById(R.id.etEventLocation);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etEventTime = (EditText) findViewById(R.id.etEventTime);
        etEventCount = (EditText) findViewById(R.id.etEventNumber);
        btSubmitEvent = (Button) findViewById(R.id.btSubmitEvent);
        btCancelEvent = (Button) findViewById(R.id.btCancelEvent);
        spEventCategory = (Spinner) findViewById(R.id.eventCategory);
        ev  = new EventContainer();

        spEventCategory=(Spinner) findViewById(R.id.eventCategory);
        String [] category= new String[] {"Music","Science & Tech", "Business", "Sports", "Community", "Health", "Others"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,category);
        spEventCategory.setAdapter(adapter);

        spEventCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eCategory=spEventCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btSubmitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID=mAuth.getCurrentUser().getUid();
                storeOnFirebase();

            }
        });




    }

    public void storeOnFirebase(){
        EventName = etEventName.getText().toString().trim();
        String EventDate = etEventDate.getText().toString().trim();
        String EventLocation = etEventLocation.getText().toString().trim();
        String EventDescription = etDescription.getText().toString().trim();
        String EventCount = etEventCount.getText().toString().trim();
        String EventTime=etEventTime.getText().toString().trim();

        userID=mAuth.getCurrentUser().getUid();
        ev.setEventTime(EventTime);
        ev.setEventName(EventName);
        ev.setEvenDescription(EventDescription);
        ev.setEvenLocation(EventLocation);
        ev.setEventCategory(eCategory);
        ev.setEventCount(EventCount);
        ev.setEventDate(EventDate);
        ev.setUserID(userID);
        if(EventName.isEmpty()){
            etEventName.setError("Enter Event Name");
            isValidForm = false;
        }
        if(EventDescription.isEmpty()){
            etDescription.setError("Enter Event Description");
            isValidForm = false;
        }
        if(EventLocation.isEmpty()){
            etEventLocation.setError("Enter Event Location");
            isValidForm = false;
        }
        if(EventDate.isEmpty()){
            etEventDate.setError("Enter Event Date in MM/DD/YYYY format");
            isValidForm = false;
        }
        if(EventCount.isEmpty()){
            etEventCount.setError("Enter Number Of Seats Available");
            isValidForm = false;
        }
        if(EventTime.isEmpty()){
            etEventTime.setError("Enter Number Of Seats Available");
            isValidForm = false;
        }
       if(isValidForm==true) {
           mDatabase.push().setValue(ev);
           mDatabaseRef.child("MyEvent").child(userID).child(databaseEventID).setValue(EventName);
           startActivity(new Intent(getBaseContext(), HomeActivity.class));
           Toast.makeText(AddEventActivity.this, "Success", Toast.LENGTH_LONG).show();
       }
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
        getMenuInflater().inflate(R.menu.add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }
        else if (id == R.id.nav_camera) {
            startActivity(new Intent(getBaseContext(),ProfileActivity.class));
        } else if (id == R.id.nav_gallery) {

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
