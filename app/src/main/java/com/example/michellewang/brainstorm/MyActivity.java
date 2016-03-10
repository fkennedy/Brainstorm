package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.michellewang.brainstorm.MESSAGE";
    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";
    public int selectedMax = 3;
    public int selected = 0;
    private Map<String, Long> votes = new HashMap<String, Long>();
    CheckBox[] cbArray = new CheckBox[20];
    int cbArray_size = 0;
    boolean mapInit_flag = false;
    private MalibuCountDownTimer countDownTimer;
    private long timeElapsed;
    private final long startTime = 15000;
    private final long interval = 1;
    private String groupName = null; // PASSED FROM CHEUNG'S
    private String username = null; // PASSED FROM CHEUNG'S
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        //Firebase ideaRef = ref.child("test1");
        ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        Firebase groupRef = ref.child("Brainstorms");

        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        groupName = intent.getStringExtra(groupName_key);

        username = extras.getString("username");


        /*
        Map ideaMap = new HashMap<String, Long>();
        ideaMap.put("idea 1", 1);
        ideaMap.put("idea 2", 2);
        ideaRef.setValue(ideaMap);
        Map ideaMap2 = new HashMap<String, Long>();
        ideaMap2.put("idea 11", 11);
        ideaRef.updateChildren(ideaMap2);
*/

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {/*
                votes = (HashMap<String, Long>) snapshot.getValue();
                if (mapInit_flag == false) {
                    create_page();
                    mapInit_flag = true;
                }
                */
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(groupName)) {
                        for (DataSnapshot postSnapshot2 : postSnapshot.getChildren())
                        {
                            if (postSnapshot2.getKey().equals("CurrentIdeas"))
                            {
                                votes = (HashMap<String, Long>) postSnapshot2.getValue();
                                System.out.println(votes);
                                if (mapInit_flag == false) {
                                    create_page();
                                    mapInit_flag = true;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void create_page()
    {
        //ScrollView sv = new ScrollView(this);
        //ScrollView sv = (ScrollView)findViewById(R.id.votingSV);
        //LinearLayout optionsLayout = new LinearLayout(this);
        //optionsLayout.setOrientation(LinearLayout.VERTICAL);
        //sv.addView(optionsLayout);
        LinearLayout optionsLayout = (LinearLayout)findViewById(R.id.optionsLayout);

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/FiraSans-Regular.ttf");
        for (HashMap.Entry<String, Long> entry : votes.entrySet())
        {
            cbArray[cbArray_size] = new CheckBox(this);
            cbArray[cbArray_size].setText(entry.getKey());
            cbArray[cbArray_size].setTypeface(tf1);
            cbArray[cbArray_size].setTextSize(16);
            int cbID = View.generateViewId();
            cbArray[cbArray_size].setId(cbID);
            cbArray[cbArray_size].setOnClickListener(cbListener);
            cbArray[cbArray_size].setTextColor(Color.parseColor("#000000"));
            optionsLayout.addView(cbArray[cbArray_size]);
            cbArray_size++;
            //System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        TextView selectionsDisplay = (TextView)findViewById(R.id.selectionsDisplay);
        StringBuilder selectMsgBuilder = new StringBuilder(30);
        selectMsgBuilder.append("Please pick your top 3 choices: ");
        String selectMsg = selectMsgBuilder.toString();
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/ChampagneLimousines.ttf");
        selectionsDisplay.setTypeface(tf2);
        selectionsDisplay.setGravity(Gravity.CENTER_VERTICAL);
        selectionsDisplay.setText(selectMsg);
        selectionsDisplay.setTextSize(24);
        selectionsDisplay.setBottom(5);


        //this.setContentView(sv);
        //this.addContentView(sv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        //        ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    //called when the user clicks Send button
    /*
    public void sendMessage(View view) {
        //Do something in response
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


        Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        for (Map.Entry<String, Integer> entry : votes.entrySet())
        {
            ref.child(entry.getKey()).setValue(entry.getValue());
        }


*/
        /*ref.runTransaction(new Transaction.Handler(){
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if(currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                //This method will be called once with the results of the transaction.
            }
        });*/
//    }


    //defines action when checkbox is clicked
    View.OnClickListener cbListener = new View.OnClickListener() {

        public void onClick(View view) {
            //find ID of checkbox being selected
            int currId = view.getId();
            CheckBox currBox = (CheckBox) findViewById(currId);
            //if checkbox was deselected, decrement counter
            if (!currBox.isChecked()) {
                selected--;
            }
            //if checkbox selection is attempted and the max number of selections has been made,
            //deny the selection
            else if (selected >= selectedMax) {
                currBox.setChecked(false);
            }

            //if checkbox selection was successful, increment the selected counter
            if (currBox.isChecked()) {
                selected++;
            }

            return;
        }
    };

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
            Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com");
            Firebase groupRef = ref.child("Brainstorms").child(groupName).child("CurrentIdeas");
            groupRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData currentData) {
                    if(currentData.getValue() == null) {
                        currentData.setValue(1);
                    } else {
                        Map<String,Long> tempMap = (Map<String, Long>)currentData.getValue();
                        updateMap(tempMap);
                        currentData.setValue(tempMap);
                        //currentData.setValue((Long) currentData.getValue() + 1);
                    }
                    return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
                }
                @Override
                public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                    //This method will be called once with the results of the transaction.
                }
            });

            System.out.println("Reached intent creation");
            Intent myIntent = new Intent(MyActivity.this, DisplayMessageActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            String message = "item1"; //Name of group
            myIntent.putExtra("groupName", groupName);
            myIntent.putExtra("username", username);
            startActivity(myIntent);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            timeElapsed = startTime - millisUntilFinished;
        }
    }

    public void updateMap(Map<String,Long> map)
    {
        for (int i = 0; i < cbArray_size; i++)
        {
            if(cbArray[i].isChecked())
            {
                String currText = (String)cbArray[i].getText();
                Long currValue = map.get(currText);
                currValue++;
                map.put(currText, currValue);
            }
        }
    }
}

