package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



public class HomeWebFragment extends Fragment implements View.OnClickListener {
    EditText searchQueryET;
    Button searchButton;

    public HomeWebFragment() {
        // Required empty public constructor
    }

    public static HomeWebFragment newInstance() {
        HomeWebFragment fragment = new HomeWebFragment();
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
        View v = inflater.inflate(R.layout.fragment_home_web, container, false);
        searchQueryET = (EditText) v.findViewById(R.id.web_search_query);
        searchButton = (Button) v.findViewById(R.id.web_search_button);
        searchButton.setOnClickListener(this);

        return v;
    }

    void doSearch() {
        String query = searchQueryET.getText().toString();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.web_search_button:
                doSearch();
                break;
        }
    }
}
