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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mGroupAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] groupArray = {
                "Group 1",
                "Group 2",
                "Group 3"
        };

        List<String> listGroup = new ArrayList<String>(
                Arrays.asList(groupArray));

        mGroupAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_group,
                        R.id.list_item_group_textview,
                        listGroup);

        ListView listView = (ListView) rootView.findViewById(
                R.id.listview_group);
        listView.setAdapter(mGroupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String group = mGroupAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, group);
                startActivity(intent);
                Toast.makeText(getActivity(), group, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
