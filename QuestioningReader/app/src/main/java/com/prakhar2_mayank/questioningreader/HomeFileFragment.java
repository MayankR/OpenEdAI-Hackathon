package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class HomeFileFragment extends Fragment implements View.OnClickListener {
    Button selectFileButton;

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
        return v;
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
}
