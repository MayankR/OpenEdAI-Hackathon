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
 * Helper class for file reading
 */
public class FileReader {
    static final int READ_REQUEST_CODE = 42;

    /**
     * Ask the android system to present the file picker menu
     * @param act The activity that requested the menu
     */
    private static void loadFilePicker(Activity act) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        //We want to read only pdf and txt files
        String[] mimetypes = {"application/pdf", "text/plain"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

        act.startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Wrapper function for asking the android system to present the file picker menu
     * @param act The activity that requested the menu
     */
    public static String getFile(Activity act) {
        String content = "";

        loadFilePicker(act);
        return content;
    }

    /**
     * Read the text from the file given its uri.
     */
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

        return stringBuilder.toString();
    }

}
