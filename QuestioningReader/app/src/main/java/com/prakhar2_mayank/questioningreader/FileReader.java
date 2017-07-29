package com.prakhar2_mayank.questioningreader;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mayank on 29/07/17.
 */
public class FileReader {
    static final int READ_REQUEST_CODE = 42;

    private static void loadFilePicker(Activity act) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/pdf", "text/plain"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

        act.startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public static String getFile(Activity act) {
        String content = "";

        loadFilePicker(act);
        return content;
    }

    public static String readTextFromUri(ContextWrapper cw, Uri uri) throws IOException {
        InputStream inputStream = cw.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
//        parcelFileDescriptor.close();
        return stringBuilder.toString();
    }

}
