package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class New_Session extends AppCompatActivity {

    EditText topic_text, spec_text,timer_text;
    public String GroupName;
    public String username;
    public final static String timer_key = "com.example.jonathancheung.firstapp.timer_key"; //MUST BE UNIQUE!
    public final static String topic_key = "com.example.jonathancheung.firstapp.topic_key";
    public final static String spec_key = "com.example.jonathancheung.firstapp.spec_key";
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";
    public final static String UserMap_key = "com.example.jonathancheung.firstapp.map_key";
    Map <String, Boolean> UsersToUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__session);
        Intent previous = getIntent();
        GroupName = previous.getStringExtra(groupName_key);
        username = previous.getStringExtra("username");
        UsersToUpdate = (Map <String, Boolean>) previous.getSerializableExtra(UserMap_key);
        Firebase.setAndroidContext(this);
        final Firebase database = new Firebase("https://csm117-brainstorm.firebaseio.com/");


        Button submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the text from the user input
                topic_text = (EditText) findViewById(R.id.topic_text);
                spec_text = (EditText) findViewById(R.id.spec_text);
                timer_text = (EditText) findViewById(R.id.timer_text);

                String topic =  topic_text.getText().toString(); // do we wanna store this in firebase"
                String timer = timer_text.getText().toString();
                String spec = spec_text.getText().toString();

                Intent submit = new Intent(New_Session.this,Brainstorm_Session.class);
                submit.putExtra(timer_key, timer);
                submit.putExtra(topic_key, topic);
                submit.putExtra(spec_key, spec);
                submit.putExtra(groupName_key, GroupName);
                submit.putExtra("username", username);

                //add the specs for the brain-session into each user's group node
                Map<String, String> Details = new HashMap<>();
                Details.put("topic", topic);
                Details.put("timer", timer);
                Details.put("spec", spec);
                database.child("Brainstorms").child(GroupName).child("details").setValue(Details);

                for (String user : UsersToUpdate.keySet()) {
                    database.child("users").child(user).child(GroupName).setValue(Details);
                    database.child("users").child(user).child(GroupName).child("Active").setValue("1");
                }
                startActivity(submit);
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
}