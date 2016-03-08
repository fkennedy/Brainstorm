package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;

public class New_Session extends AppCompatActivity {

    EditText topic_text, spec_text,timer_text;
    ListView timer_list = null;

    public final static String timer_key = "com.example.michellewang.brainstorm.timer_key"; //MUST BE UNIQUE!
    public final static String topic_key = "com.example.michellewang.brainstorm.topic_key";
    public final static String spec_key = "com.example.michellewang.brainstorm.spec_key";
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";

    private String groupName = null;
    private String username = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__session);

        Bundle extras = getIntent().getExtras();
        groupName = extras.getString(groupName_key);
        username = extras.getString("username");

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
                submit.putExtra(groupName_key, groupName);
                submit.putExtra("username", username);

                database.child("Brainstorms").child(topic).child("details").child("topic").setValue(topic);
                database.child("Brainstorms").child(topic).child("details").child("timer").setValue(timer);
                database.child("Brainstorms").child(topic).child("details").child("spec").setValue(spec);
                startActivity(submit);
            }
        });
    }
}