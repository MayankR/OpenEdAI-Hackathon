package com.prakhar2_mayank.questioningreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReaderActivity extends AppCompatActivity {
    TextView contentTV;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Intent it = getIntent();
        content = it.getStringExtra(Utility.DOCUMENT_CONTENT_MESSAGE);

        contentTV = (TextView) findViewById(R.id.document_content);
        contentTV.setText(content);
    }
}
