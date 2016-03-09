package com.example.michellewang.brainstorm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
1) SET TIMER TO START NEW ACTIVITY
2) FIX TIMER NOT SHOWING RIGHT NUMBER
*/
/**
 * // * Created by Victor on 3/5/2016.
 * //
 */
/*
1) SET TIMER TO START NEW ACTIVITY
2) FIX TIMER NOT SHOWING RIGHT NUMBER
*/
/**
 * // * Created by Victor on 3/5/2016.
 * //
 */
public class Groups extends AppCompatActivity {
    EditText group_text, group_name;
    String groupName;
    String username;
    LinearLayout member_list;
    SearchView member_search;
    ArrayList<String> AllUsers = new ArrayList<>();
    Map<String, Boolean> userMap = new HashMap<>();
    CheckBox[] cbArray = new CheckBox[20];
    int cbArray_size = 0;

    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";
    public final static String UserMap_key = "com.example.jonathancheung.firstapp.map_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        group_name = (EditText) findViewById(R.id.group_text);
        member_list=(LinearLayout) findViewById(R.id.groupLayout);
        member_search=(SearchView) findViewById(R.id.member_search);

        final Firebase UsernameList = ref.child("users");
        UsernameList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                    AllUsers.add(child.getValue().toString());

                userMap = (HashMap<String, Boolean>)dataSnapshot.getValue();
                createUserCB();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        //go to new activity and create a new group object
        Button createGroup = (Button) findViewById(R.id.create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupName = group_name.getText().toString();
                if (groupName.length() == 0) {
                    group_name.setError("invalid group name");
                }
                else {
                    Firebase GroupRef = ref.child("groups").child(groupName).child("users");
                    Firebase UserRef;
                    Map<String, Boolean> selectedUserMap = new HashMap<>();
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
                    Intent newGroup = new Intent(Groups.this, New_Session.class);
                    newGroup.putExtra(UserMap_key, (Serializable) selectedUserMap);
                    newGroup.putExtra(groupName_key, groupName);
                    newGroup.putExtra("username", username);
                    startActivity(newGroup);
                }
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

    public void createUserCB()
    {
        LinearLayout groupLayout = (LinearLayout)findViewById(R.id.groupLayout);
        groupLayout.setOrientation(LinearLayout.VERTICAL);
        for (HashMap.Entry<String, Boolean> entry : userMap.entrySet())
        {
            cbArray[cbArray_size] = new CheckBox(this);
            CheckBox tempCB = cbArray[cbArray_size];
            tempCB.setText(entry.getKey());
            int cbID = View.generateViewId();
            tempCB.setId(cbID);
            groupLayout.addView(tempCB);
            cbArray_size++;
        }
    }

}