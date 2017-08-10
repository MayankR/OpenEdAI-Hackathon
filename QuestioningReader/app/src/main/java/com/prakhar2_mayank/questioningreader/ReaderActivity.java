package com.prakhar2_mayank.questioningreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ReaderActivity extends AppCompatActivity implements View.OnScrollChangeListener, View.OnClickListener {
    WebView contentWV;
    ScrollView contentSV;
    String content;
    private static String TAG = "ReaderActivity";
    boolean qMode = false;
    FloatingActionButton questionFab;
    String contentRead = "";
    int startScroll, curScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reader);
        qMode = false;
        Intent it = getIntent();
        content = it.getStringExtra(Utility.DOCUMENT_CONTENT_MESSAGE);

        contentWV = (WebView) findViewById(R.id.document_content);
        contentWV.loadData(content, "text/html; charset=utf-8", "utf-8");

        contentSV = (ScrollView) findViewById(R.id.content_scroll);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(this, "Scroll listener enabled", Toast.LENGTH_SHORT).show();
            contentSV.setOnScrollChangeListener(this);
        }
        else {
            Toast.makeText(this, "Scroll listener disabled", Toast.LENGTH_SHORT).show();
        }

        questionFab = (FloatingActionButton) findViewById(R.id.fab);
        questionFab.setOnClickListener(this);

        startReading();
    }

    //TODO: Gupta work from here!
    void handleQuestionResponse(JSONObject response) {

    }

    void startReading() {
        Log.d(TAG, "Start Reading in qMode!");
        startScroll = curScroll;
        qMode = true;
    }

    void getQuestions(String text) {
        RequestParams params = new RequestParams();
        params.add("text", text);

        String url = Utility.QUESTION_URL;

        Log.d("SA", "Hiting question URL: " + url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    handleQuestionResponse(obj);
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
//                loading.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(getApplication(), "404 - Not Found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplication(), "500 - Internal Server Error!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getApplication(), "403!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), throwable.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_q_toggle:
                if(qMode){
                    item.setTitle("Q Mode off");
                    qMode = false;
                } else {
                    item.setTitle("Q Mode on");
                    qMode = true;
                    startReading();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Log.d(TAG, "scrollx: " + scrollX + " oldscrollx: " + oldScrollX);
        Log.d(TAG, "scrolly: " + scrollY + " oldscrolly: " + oldScrollY);
        curScroll = scrollY;

        Log.d(TAG, "getting read content " + qMode);
        if(qMode) {
            int totalHeight = contentSV.getChildAt(0).getHeight();
            int totalContentLength = content.length();
            Log.d(TAG, "Content length: " + totalContentLength);

            double startPct = (double) startScroll / (double) totalHeight;
            startPct += 0.05;
            double endPct = (double) curScroll / (double) totalHeight;
            endPct -= 0.05;

            if(startPct >= 1) startPct = 0.96;
            if(endPct < startPct) endPct = startPct;

            contentRead = content.substring((int) (startPct * totalContentLength),
                    (int) (endPct * totalContentLength));

            Log.d(TAG, "Content Read: " + contentRead);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab:
                Intent it = new Intent(this, ChatBotActivity.class);
                startActivity(it);
                break;
        }
    }
}
