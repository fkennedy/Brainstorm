package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity {

    private String groupName = null;
    private String mUsername = null;
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        mUsername = extras.getString("username");
        groupName = extras.getString(groupName_key);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button createGroup = (Button) findViewById(R.id.create_brainstorm);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newBrainstorm = new Intent(DetailActivity.this, New_Session.class);
                newBrainstorm.putExtra(groupName_key, groupName);
                newBrainstorm.putExtra("username", mUsername);
                startActivity(newBrainstorm);
            }
        });
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

//    public String getUsername() {
//        return mUsername;
//    }

}
