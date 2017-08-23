package com.prakhar2_mayank.questioningreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;


public class HomeFileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    EditText searchQueryET;
    FloatingActionButton selectFileButton;
    Button searchButton;
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

        searchQueryET = (EditText) v.findViewById(R.id.web_search_query);
        searchQueryET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });



        selectFileButton = (FloatingActionButton) v.findViewById(R.id.select_file);
        selectFileButton.setOnClickListener(this);

        searchButton = (Button) v.findViewById(R.id.web_search_button);
        searchButton.setOnClickListener(this);

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
            case R.id.web_search_button:
                doSearch();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String uriString = recentFileUriList.get(position);
        Uri fileUri = Uri.parse(uriString);
        ((HomeActivity)getActivity()).processFile(fileUri);

    }

    void showReader(String content) {
        Intent it = new Intent(getActivity(), ReaderActivity.class);
        it.putExtra(Utility.DOCUMENT_CONTENT_MESSAGE, content);
        Log.d(TAG, content);
        startActivity(it);
        ReaderActivity.resetChatBot();
    }

    void getArticle(String q) {
        RequestParams params = new RequestParams();

        String url = Utility.SEARCH_URL + "error";
        try {
            url = Utility.SEARCH_URL + URLEncoder.encode(q, "utf-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("SA", "Hiting search URL: " + url);
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Finding content...", "Please wait...", false, false);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                loading.dismiss();
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    String docText = obj.getString("text");
                    showReader(docText);
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
                loading.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(getContext(), "404 - Not Found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getContext(), "500 - Internal Server Error!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getContext(), "403!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void doSearch() {
        String query = searchQueryET.getText().toString();
        getArticle(query);
    }



}
