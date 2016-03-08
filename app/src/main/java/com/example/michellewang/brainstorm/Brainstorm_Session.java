package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Brainstorm_Session extends AppCompatActivity {

    public final static String timer_key = "com.example.michellewang.brainstorm.timer_key"; //MUST BE UNIQUE!
    public final static String topic_key = "com.example.michellewang.brainstorm.topic_key";
    public final static String spec_key = "com.example.michellewang.brainstorm.spec_key";
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";

    TextView textViewTime;
    TextView textViewTopic;
    TextView textViewSpec;
    private ArrayList<String> BrainstormList;
    private ArrayAdapter<String>  BrainAdapter;
    private EditText txtInput;
    Map<String, Long> Ideas = new HashMap<>();
    //Timer
    private String groupName = null; //MUST SET THIS
    private String username = null;
    private MalibuCountDownTimer countDownTimer;
    private long timeElapsed;
    private  long startTime = 10000 ;
    private final long interval = 1;
    int hours;
    int minutes;
    int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brainstorm__session);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");

        Bundle extras = getIntent().getExtras();
        groupName = extras.getString(groupName_key);
        username = extras.getString("username");

        //Get the timer value from the previous activity
        Intent submit = getIntent();
        String timer_value = submit.getStringExtra(timer_key);
        final String topic_value = submit.getStringExtra(topic_key);
        final String spec_value = submit.getStringExtra(spec_key);

        //set Topic/Spec string in Brainstorm Session
        textViewTopic = (TextView) findViewById(R.id.header_topic);
        textViewTopic.setText(topic_value);
        textViewSpec = (TextView) findViewById(R.id.header_spec);
        textViewSpec.setText(spec_value);

        //create the timer
        textViewTime = (TextView) findViewById(R.id.timer_text_view);
        int countdownTime = Integer.valueOf(timer_value);
        hours = countdownTime / 3600;
        minutes = (countdownTime % 3600) / 60;
        seconds = countdownTime - hours * 3600 - minutes * 60;
        String FormattedTime = String.valueOf(hours) + ':' + String.valueOf(minutes) + ':' + String.valueOf(seconds);
        startTime = countdownTime * 1000;
        textViewTime.setText(FormattedTime);

        //create brainstorm item list
        ListView IdeaList = (ListView) findViewById(R.id.current_idealist);
        String[] items = {};
        BrainstormList = new ArrayList<>(Arrays.asList(items));
        BrainAdapter = new ArrayAdapter<>(this, R.layout.brainstorm_list_item, R.id.brainstorm_item, BrainstormList);
        IdeaList.setAdapter(BrainAdapter);

        txtInput = (EditText) findViewById(R.id.addIdea_EditText);
        final Button addButton = (Button) findViewById(R.id.addIdea_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NewItem = txtInput.getText().toString();
                if (NewItem.length() == 0) {
                    txtInput.setError("Please enter a valid idea!");
                } else {
                    Ideas.put(NewItem, (long) 0);
                    ref.child("Brainstorms").child(groupName).child("CurrentIdeas").setValue(Ideas);
                    BrainstormList.add(NewItem);
                    BrainAdapter.notifyDataSetChanged();
                    txtInput.setText("");
                }
            }
        });

        //SET TIMER
        countDownTimer = new MalibuCountDownTimer(startTime, interval);
        countDownTimer.start();
    }

    // CountDownTimer class
    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            System.out.println("Reached intent creation");
            Intent myIntent = new Intent(Brainstorm_Session.this, MyActivity.class); //CHANGE THIS
            myIntent.putExtra(groupName_key, groupName);
            myIntent.putExtra("username", username);
            startActivity(myIntent);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            timeElapsed = startTime - millisUntilFinished;
            //FORMAT EVERYTHING
            long new_hours = millisUntilFinished/3600000;
            long new_minutes = (millisUntilFinished%3600000)/60000;
            long new_seconds = millisUntilFinished - new_hours*3600000 - new_minutes*60000;
            new_seconds= new_seconds/1000;
            String FormattedTime = String.valueOf(new_hours) + ':' + String.valueOf(new_minutes) + ':' + String.valueOf(new_seconds);
            textViewTime.setText(FormattedTime);
        }
    }
}