package com.nervelap.overchat;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    private ListView mGroupListView;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mGroupArrayList;
    private DatabaseReference mDatabaseReference;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupsFragment = inflater.inflate(R.layout.fragment_groups, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");
        mGroupArrayList = new ArrayList<>();
        mGroupListView = groupsFragment.findViewById(R.id.group_list_id);
        mArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mGroupArrayList);
        mGroupListView.setAdapter(mArrayAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> list = new ArrayList<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    list.add(((DataSnapshot) iterator.next()).getKey());
                }
                mGroupArrayList.clear();
                mGroupArrayList.addAll(list);
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return groupsFragment;
    }

}
