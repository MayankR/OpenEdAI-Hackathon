package com.prakhar2_mayank.questioningreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class HomeWebFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeWebFragment";
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

        searchButton = (Button) v.findViewById(R.id.web_search_button);
        searchButton.setOnClickListener(this);

        return v;
    }

    void showReader(String content) {
        Intent it = new Intent(getActivity(), ReaderActivity.class);
        it.putExtra(Utility.DOCUMENT_CONTENT_MESSAGE, content);
        Log.d(TAG, content);
        startActivity(it);
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
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Updating Cart...", "Please wait...", false, false);
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
