package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";
    public final static String timer_key = "com.example.jonathancheung.firstapp.timer_key"; //MUST BE UNIQUE!
    public final static String topic_key = "com.example.jonathancheung.firstapp.topic_key";
    public final static String spec_key = "com.example.jonathancheung.firstapp.spec_key";


    NavigationView navigationView = null;
    Toolbar toolbar = null;

    private String mUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        Bundle extras = getIntent().getExtras();
        final String username = extras.getString("username");
        mUsername = username;

        // Initial Fragment to display
        MainActivityFragment fragment = new MainActivityFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Groups.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String toDisplay = "Logged in as: ";
        toDisplay = toDisplay.concat(username);
        View navHeaderView = navigationView.getHeaderView(0);
        TextView textViewUsername = (TextView) navHeaderView.findViewById(R.id.textView_username);
        textViewUsername.setText(toDisplay);

        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.app_bar_main_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("brainblast");
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/antipasto.regular.otf");
        textviewTitle.setTypeface(tf);
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);

        //Firebase ideaRef = ref.child("test1");
        ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        Firebase usersRef = ref.child("users").child(username);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.getValue().equals("none"))
                        break;
                    //System.out.println(postSnapshot.getValue());
                    Map<String, String> specMap = new HashMap<String, String>();
                    specMap = (HashMap<String, String>)postSnapshot.getValue();
                    System.out.println(specMap);
                    if(!specMap.containsKey("Active"))
                        break;
                    if (specMap.get("Active").equals("1")) {
                        System.out.println("User is now in an active brainstorm session");
                        Intent intent = new Intent(MainActivity.this, Brainstorm_Session.class);
                        intent.putExtra("username", username);
                        intent.putExtra(spec_key, specMap.get("spec"));
                        intent.putExtra(timer_key, specMap.get("timer"));
                        intent.putExtra(topic_key, specMap.get("topic"));
                        intent.putExtra(groupName_key, postSnapshot.getKey());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ref.unauth();
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_groupchats)
        {
            MainActivityFragment fragment = new MainActivityFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_friendslist)
        {
            FriendsListFragment fragment = new FriendsListFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_settings)
        {
            SettingsFragment fragment = new SettingsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getUsername() {
        return mUsername;
    }
}