package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DisplayMessageActivity extends AppCompatActivity {

    private Map<String, Long> votes = new HashMap<String, Long>();
    public static boolean DESC = false;
    private int limit = 3;
    private String groupName = null; // Passed from MyActivity.java
    private String username = null; // Passed from MyActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        Bundle extras = getIntent().getExtras();
        groupName = extras.getString("groupName");
        username = extras.getString("username");
        Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        Firebase groupRef = ref.child("Brainstorms");
        setContentView(R.layout.activity_display_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayMessageActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(groupName)) {
                        for (DataSnapshot postSnapshot2 : postSnapshot.getChildren())
                        {
                            if (postSnapshot2.getKey().equals("CurrentIdeas"))
                            {
                                votes = (HashMap<String, Long>) postSnapshot2.getValue();
                                System.out.println("Final votes retrieved");
                                displayHighestVoted(votes);
                            }
                        }
                    }
                }
               /* for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //System.out.println(postSnapshot.getKey());
                    if (postSnapshot.getKey().equals("test1")) {
                        votes = (HashMap<String, Long>) postSnapshot.getValue();
                        System.out.println("Final votes retrieved");
                        displayHighestVoted(votes);
//                        votes.put(postSnapshot.getValue().getKey(), ((Long) postSnapshot.getValue()).intValue());
                        //System.out.println(votes);
                        //System.out.println(postSnapshot.getKey());
                        //System.out.println(post.getAuthor() + " - " + post.getTitle());
                    }
                }*/

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
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

        final ImageView animImageView = (ImageView) findViewById(R.id.ivAnimation);
        animImageView.setBackgroundResource(R.drawable.animate_idea_bulb);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });


        /*
        for (HashMap.Entry<String, Integer> entry : sortedMap.entrySet())
        {
            if (itemCounter >= limit){
                break;
            }
            s.append(entry.getKey());
            s.append("\n");
            TextView textView = new TextView(this);
            textView.setTextSize(40);
            textView.setText(entry.getKey());
            tasks.add(textView);
            itemCounter++;
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        */

    }

    public void displayHighestVoted(Map<String, Long> map)
    {
        int itemCounter = 0;
        StringBuilder s = new StringBuilder(100);
        String currHighest = "";
//        for (int i = 0; i < limit; i++)
//        {
            currHighest = findHighestVoted(map);
            System.out.println(currHighest);
            map.remove(currHighest);
            s.append(currHighest);
            s.append("\n");
//        }

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        String newMessage = s.toString();
//        System.out.println(newMessage);
        TextView textView = (TextView) findViewById(R.id.top_result);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/KOMIKAX.ttf");
        textView.setTypeface(tf);
        textView.setTextSize(48);
        textView.setText(newMessage);
    }


    public String findHighestVoted(Map<String, Long> map)
    {
        long currMaxVotes = 0;
        String currHighest = "";
        for (HashMap.Entry<String, Long> entry : map.entrySet())
        {
            //Integer i = (int) (long) entry.getValue();
            Long curr = (Long) entry.getValue();
            if (curr.compareTo(Long.valueOf(currMaxVotes)) > 0)
            {
                System.out.print("Current value: ");
                System.out.print(entry.getValue());
                System.out.print("; Current max: ");
                System.out.println(currMaxVotes);


                currMaxVotes = entry.getValue();
                currHighest = entry.getKey();
            }
        }

        return currHighest;
    }

}

