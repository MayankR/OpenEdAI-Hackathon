package com.prakhar2_mayank.questioningreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class ReaderActivity extends AppCompatActivity {
    WebView contentWV;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent it = getIntent();
        content = it.getStringExtra(Utility.DOCUMENT_CONTENT_MESSAGE);

        contentWV = (WebView) findViewById(R.id.document_content);
        contentWV.loadData(content, "text/html; charset=utf-8", "utf-8");
    }
}
