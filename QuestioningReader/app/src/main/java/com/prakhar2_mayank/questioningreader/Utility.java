package com.prakhar2_mayank.questioningreader;

/**
 * Created by mayank on 30/07/17.
 */
public class Utility {
    public static String DOCUMENT_CONTENT_MESSAGE = "document.content.message.intent";

    public static String IP = "http://35.195.85.23";

    public static String SEARCH_URL = IP + "/search/keyword?query=";
    public static String QUESTION_URL = IP + "/question";
    public static String CONVERT_PDF_URL = IP + "/convertpdf/html";

    /**
     * Database Constants
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "p2m.db";


    /**
     * FLASHCARDS DATABASE TABLE
     */
    public static final String FLASHCARDS_TABLE = "flashcards";
    public static final String FLASHCARD_ID = "id"; // we need to use _ID because ID is already used by the system.
    public static final String FLASHCARD_TITLE = "title";
    public static final String FLASHCARD_CONTENT = "content";
    public static final String FLASHCARD_ANSWER = "answer";
}
