package com.prakhar2_mayank.questioningreader.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.speech.tts.UtteranceProgressListener;

import com.prakhar2_mayank.questioningreader.FlashCardItem;
import com.prakhar2_mayank.questioningreader.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakhar0409 on 01/08/17.
 */
public class DbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = Utility.DATABASE_VERSION;
    public static final String DATABASE_NAME = Utility.DATABASE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Called whenever the Database needs to be created, create all shit!!!
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FLASHCARDS_TABLE = "CREATE TABLE " + Utility.FLASHCARDS_TABLE + " (" +
                Utility.FLASHCARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Utility.FLASHCARD_TITLE + " TEXT NOT NULL, " +
                Utility.FLASHCARD_CONTENT + " TEXT NOT NULL, " +
                Utility.FLASHCARD_ANSWER + " TEXT NOT NULL, " +
                Utility.FLASHCARD_CRCT + " INTEGER NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_FLASHCARDS_TABLE);

        // Temporary inserts
//        String insert1 = "INSERT INTO " + Utility.FLASHCARDS_TABLE + " VALUES(1,\"Pro\",\"Who is pm?\",\"IDK\");";
//        String insert2 = "INSERT INTO " + Utility.FLASHCARDS_TABLE + " VALUES(2,\"Alice\",\"Where is wonderland?\",\"IDK\");";

//        db.execSQL(insert1);
//        db.execSQL(insert2);

    }


    // when app upgrades - simply drop and create :P
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utility.FLASHCARDS_TABLE);
        onCreate(db);
    }

    /**
     * Closes the Database Connection.
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
