package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    private String mUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        final String username = extras.getString("username");
        mUsername = username;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(DetailActivity.this, MainActivity.class);
        back.putExtra("username",mUsername);
        startActivity(back);
    }

    public String getUsername() {
        return mUsername;
    }

}
