package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.net.URI;
import java.util.ArrayList;
import java.util.Set;


public class HomeFileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button selectFileButton;
    SQLiteDatabase myDatabase;
    ListView recentFilesLV;
    RecentFilesAdapter fileAdapter;
    static final String TAG = "HomeFileFragment";
    ArrayList<String> recentFileUriList;

    public HomeFileFragment() {
        // Required empty public constructor
    }


    public static HomeFileFragment newInstance() {
        HomeFileFragment fragment = new HomeFileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_file, container, false);

        selectFileButton = (Button) v.findViewById(R.id.select_file);
        selectFileButton.setOnClickListener(this);


        recentFilesLV = (ListView) v.findViewById(R.id.past_files_list);
        fileAdapter = new RecentFilesAdapter(getActivity(), getActivity().getLayoutInflater());

        SharedPreferences myPrefs = getActivity().getSharedPreferences("HollaMan", 0);
        Set<String> recentFileSet = myPrefs.getStringSet("recent_files", new ArraySet<String>());
        ArrayList<String> recentFileList = new ArrayList<String>(recentFileSet);
        fileAdapter.updateData(recentFileList);

        // Set the ListView to use the ArrayAdapter
        recentFilesLV.setAdapter(fileAdapter);
        recentFilesLV.setOnItemClickListener(this);



        return v;
    }

    @Override
    public void onResume() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences("HollaMan", 0);
        Set<String> recentFileSet = myPrefs.getStringSet("recent_files", new ArraySet<String>());
        ArrayList<String> recentFileList = new ArrayList<String>(recentFileSet);
        recentFileUriList = recentFileList;
        ArrayList<String> recentFileListName = new ArrayList<String>();
        for(int i=0;i<recentFileList.size();i++) {
            try {
                Uri fileUri = Uri.parse(recentFileList.get(i));
                String filePath = fileUri.getPath();
                recentFileListName.add(filePath.substring(filePath.lastIndexOf('/') + 1));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        fileAdapter.updateData(recentFileListName);
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.select_file:
                FileReader.getFile(getActivity());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String uriString = recentFileUriList.get(position);
        Uri fileUri = Uri.parse(uriString);
        ((HomeActivity)getActivity()).processFile(fileUri);

    }
}
