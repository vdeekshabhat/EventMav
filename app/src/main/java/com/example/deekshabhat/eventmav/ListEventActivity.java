package com.example.deekshabhat.eventmav;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class ListEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ListView mListView;
    private ArrayList<String> areventName= new ArrayList<>();
    private ArrayList<String> arKey= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");



        Intent intent=getIntent();
        if(intent!=null) {
            String eventCategory=getIntent().getStringExtra("category");
            openCategory(eventCategory);
        }
        else
            Log.d("status", "no intent");
            mListView = (ListView) findViewById(R.id.mListView);


    }
        public void openCategory(String category) {
            com.google.firebase.database.Query query = mDatabase.orderByChild("eventCategory").equalTo(category);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap: dataSnapshot.getChildren()) {
                           // String eventName = (String) ((java.util.HashMap)snap.getValue()).entrySet().toArray()[4];
                            String eventName = snap.child("eventName").getValue(String.class);
                            String key=snap.getKey();
                            arKey.add(key);
                            if (!areventName.contains(eventName))
                                areventName.add(eventName);
                        }
                        LoadListView();
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    private void LoadListView() {

        //Create Array Adapter and pass ArrayOfVales to it
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, areventName);

        //BindAdapter with our Actual ListView
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getBaseContext(), EventDetailsActivity.class);
                    intent.putExtra("eventID", arKey.get(position) );
                    startActivity(intent);


                // ID = eventIDArrayList[position]
                // pass ID to new activity
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
        getMenuInflater().inflate(R.menu.list_event, menu);
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
