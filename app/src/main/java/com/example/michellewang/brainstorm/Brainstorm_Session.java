package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Brainstorm_Session extends AppCompatActivity {

    public final static String timer_key = "com.example.jonathancheung.firstapp.timer_key"; //MUST BE UNIQUE!
    public final static String topic_key = "com.example.jonathancheung.firstapp.topic_key";
    public final static String spec_key = "com.example.jonathancheung.firstapp.spec_key";
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";

    TextView textViewTime;
    TextView textViewTopic;
    TextView textViewSpec;
    private ArrayList<String> BrainstormList;
    private ArrayAdapter<String>  BrainAdapter;
    private EditText txtInput;
    Map<String, Long> Ideas = new HashMap<>();
    Map<String, Long> UpdatedIdeas = new HashMap<>();
    String GroupName;
    String username;

    //Timer
    private MalibuCountDownTimer countDownTimer;
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

        //Get all necessary values from the previous activity
        Intent submit = getIntent();
        String timer_value = submit.getStringExtra(timer_key);
        final String topic_value = submit.getStringExtra(topic_key);
        final String spec_value = submit.getStringExtra(spec_key);
        GroupName = submit.getStringExtra(groupName_key);
        username = submit.getStringExtra("username");
        final Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/").child("Brainstorms").child(GroupName).child("CurrentIdeas");


        //set Topic/Spec string in Brainstorm Session
        textViewTopic = (TextView) findViewById(R.id.header_topic);
        textViewTopic.setText(topic_value);
        textViewSpec = (TextView) findViewById(R.id.header_spec);
        textViewSpec.setText(spec_value);

        //create the timer
        textViewTime = (TextView) findViewById(R.id.timer_text_view);
        int countdownTime = Integer.valueOf(timer_value);
        hours = countdownTime/3600;
        minutes = (countdownTime%3600)/60;
        seconds = countdownTime - hours*3600 - minutes*60;
        String FormattedTime = String.valueOf(hours) + ':' + String.valueOf(minutes) + ':' + String.valueOf(seconds);
        startTime = countdownTime*1000;
        textViewTime.setText(FormattedTime);

        //create brainstorm item list
        ListView IdeaList = (ListView) findViewById(R.id.current_idealist);
        String [] items= {};
        BrainstormList = new ArrayList<>(Arrays.asList(items));
        BrainAdapter = new ArrayAdapter<>(this,R.layout.brainstorm_list_item, R.id.brainstorm_item, BrainstormList);
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
                    Map tempMap = Ideas;
                    ref.updateChildren(tempMap);
                    System.out.println(Ideas);
                    BrainstormList.add(NewItem);
                    BrainAdapter.notifyDataSetChanged();
                    txtInput.setText("");
                }
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UpdatedIdeas = (HashMap<String, Long>) dataSnapshot.getValue();
                if (UpdatedIdeas != null) {
                    for (String NewIdea : UpdatedIdeas.keySet()) {
                        if (!BrainstormList.contains(NewIdea))
                            BrainstormList.add(NewIdea);
                    }
                    BrainAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //SET TIMER
        countDownTimer = new MalibuCountDownTimer(startTime, interval);
        countDownTimer.start();

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
            Intent myIntent = new Intent(Brainstorm_Session.this, MyActivity.class);
            myIntent.putExtra(groupName_key,GroupName);
            myIntent.putExtra("username", username);
            startActivity(myIntent);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
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
