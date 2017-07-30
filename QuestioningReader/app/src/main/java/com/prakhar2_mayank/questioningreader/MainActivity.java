package com.prakhar2_mayank.questioningreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "MainActivity";
    Button selectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFile = (Button) findViewById(R.id.select_file);
        selectFile.setOnClickListener(this);
    }

    void processFile(Uri uri) {
        try {
            String content = FileReader.readTextFromUri(this, uri);
            Intent it = new Intent(this, ReaderActivity.class);
            it.putExtra(Utility.DOCUMENT_CONTENT_MESSAGE, content);
            Log.d(TAG, content);
            startActivity(it);

        } catch(IOException e) {
            Toast.makeText(this, "Error reading file contents :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == FileReader.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Toast.makeText(this, "Uri: " + uri.toString(), Toast.LENGTH_SHORT).show();
                processFile(uri);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.select_file:
                FileReader.getFile(this);
                break;


        }
    }
}
