package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private String groupName = null;
    private String mUsername = null;
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";
    public final static String UserMap_key = "com.example.jonathancheung.firstapp.map_key";
    CheckBox[] cbArray = new CheckBox[20];
    int cbArray_size = 0;
    Map<String, Boolean> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        Bundle extras = getIntent().getExtras();
        mUsername = extras.getString("username");
        groupName = extras.getString(groupName_key);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Firebase GroupRef = ref.child("groups").child(groupName).child("users");
        Firebase UserRef;
        final Map<String, Boolean> selectedUserMap = new HashMap<>();
        for (int i = 0; i < cbArray_size; i++)
        {
            if (cbArray[i].isChecked())
            {
                selectedUserMap.put(cbArray[i].getText().toString(), Boolean.TRUE);
                UserRef = ref.child("users").child(cbArray[i].getText().toString());
                UserRef.child(groupName).setValue("none");
            }
        }
        //PUSH USER WHO MADE GROUP INTO USERMAP
        //UPDATE USER WHO MADE GROUP
        GroupRef.setValue(selectedUserMap);

        Button createGroup = (Button) findViewById(R.id.create_brainstorm);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newBrainstorm = new Intent(DetailActivity.this, New_Session.class);
                newBrainstorm.putExtra(groupName_key, groupName);
                newBrainstorm.putExtra("username", mUsername);
                newBrainstorm.putExtra(UserMap_key, (Serializable) selectedUserMap);
                startActivity(newBrainstorm);
            }
        });

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent= new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra("username", mUsername);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void createUserCB()
    {
        for (HashMap.Entry<String, Boolean> entry : userMap.entrySet())
        {
            cbArray_size++;
        }
    }

}
