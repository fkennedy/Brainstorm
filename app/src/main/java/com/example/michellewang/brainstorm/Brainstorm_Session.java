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
import java.util.concurrent.TimeUnit;

public class Brainstorm_Session extends AppCompatActivity {

    public final static String timer_key = "com.example.jonathancheung.firstapp.timer_key"; //MUST BE UNIQUE!
    public final static String topic_key = "com.example.jonathancheung.firstapp.topic_key";
    public final static String spec_key = "com.example.jonathancheung.firstapp.spec_key";

    TextView textViewTime;
    TextView textViewTopic;
    TextView textViewSpec;
    private ArrayList<String> BrainstormList;
    private ArrayAdapter<String>  BrainAdapter;
    private EditText txtInput;
    Map<String, Integer> Ideas = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brainstorm__session);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");

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
        textViewTime.setText(timer_value);

        final Counter timer = new Counter (300000, 1000);
        timer.start();

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
                }
                else {
                    Ideas.put(NewItem, 0);
                    ref.child("Brainstorms").child(topic_value).child("CurrentIdeas").setValue(Ideas);
                    BrainstormList.add(NewItem);
                    BrainAdapter.notifyDataSetChanged();
                    txtInput.setText("");
                }
            }
        });
    }

    public class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.printf(hms);
            textViewTime.setText(hms);
        }

        @Override
        public void onFinish() {
            textViewTime.setText("BRAINSTORM SESSION OVER!");
            //Intent done = new Intent(Brainstorm_Session.this, NEW ACTIVITY);
            //startActivity(done);
        }
    }
}
