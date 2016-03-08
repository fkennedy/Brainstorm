package com.example.michellewang.brainstorm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Intent;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public final static String groupName_key = "com.example.michellewang.brainstorm.group_key";
    private ArrayAdapter<String> mGroupAdapter;
    private List<String> groupArray = new ArrayList<String>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        MainActivity activity = (MainActivity) getActivity();
//        final String username = activity.getUsername();
        Intent intent = getActivity().getIntent();
        final String username = intent.getStringExtra("username");

        Firebase ref = new Firebase("https://csm117-brainstorm.firebaseio.com/");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("groups")) {
                        for (DataSnapshot postpostSnapshot : postSnapshot.getChildren()) {
                            groupArray.add(postpostSnapshot.getKey());
                            //System.out.println(postpostSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        mGroupAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_group,
                        R.id.list_item_group_textview,
                        groupArray);

        ListView listView = (ListView) rootView.findViewById(
                R.id.listview_group);
        listView.setAdapter(mGroupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String group = mGroupAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, group);
                intent.putExtra("username", username);
                intent.putExtra(groupName_key, group);
                startActivity(intent);
                Toast.makeText(getActivity(), group, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}