package com.prakhar2_mayank.questioningreader;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.prakhar2_mayank.questioningreader.Helpers.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class ChatBot {

    private Set<String> nextAns;
    private Queue<JSONObject> questionQueue;
    private MessagesAdapter messagesAdapter;
    private AppCompatActivity parentActivity;
    private ListView messageListView;
    String curQues;
    boolean newQues;

    ChatBot(final AppCompatActivity parentActivity) {

        this.parentActivity = parentActivity;
        nextAns = null;

        questionQueue = new ArrayDeque<>(10000);
//        try {
//            addQuestionsToQueue(new JSONObject("{\"api\":\"get Question\",\"message\":\"successful\",\"text\":[{\"answer\":\"speculation\",\"question\":\"After all the __________ about whether we would have the fight, the last few weeks have seen much name-calling and animosity on both sides, as the rivalry intensifies ahead of the big day.\",\"similar_words\":[\"adverse opinion\",\"guess\",\"side\"],\"title\":\"mytopic\"}]}"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        messageListView = ((ListView) parentActivity.findViewById(R.id.message_list));
        messagesAdapter = MessagesAdapter.getMessagesAdapter(parentActivity);
        MessagesAdapter.clearChat();
        messageListView.setAdapter(messagesAdapter);


        parentActivity.findViewById(R.id.send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) parentActivity.findViewById(R.id.new_message);
                String userAnswer = editText.getText().toString();
                if (userAnswer.length() > 0) {
                    MessagesAdapter.addChatMessage(new UserChatMessage(userAnswer));
                    editText.setText("");

                    verifyAnswer(userAnswer);
                    //messageListView.scrollTo(0, messageListView.getHeight());
                    //messageListView.scrollBy(0,60);
                }
            }
        });
    }

    private void scrollMyListViewToBottom() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                messageListView.setSelection(messagesAdapter.getCount() - 1);
            }
        });
    }

    private void verifyAnswer(String userAnswer) {
        if (nextAns == null) {
            MessagesAdapter.addChatMessage((new BotChatMessage("WelCome")).setTyping(false));
            if (questionQueue.isEmpty()) {
                addDelayedChatBotMessage(new BotChatMessage("Sorry, No Questions For Now"), 1000);
            } else {
                addBotMessage(questionQueue.remove());
            }
        } else if (nextAns.contains(userAnswer)) {
            MessagesAdapter.addChatMessage((new BotChatMessage("Correct")).setTyping(false));
            if (questionQueue.isEmpty()) {
                addDelayedChatBotMessage(new BotChatMessage("Sorry, No More Questions For Now"), 1000);
            } else {
                addBotMessage(questionQueue.remove());
            }
        } else {
            MessagesAdapter.addChatMessage((new BotChatMessage("Wrong Answer")).setTyping(false));
            MessagesAdapter.addChatMessage((new BotChatMessage("Try Again")).setTyping(false));
            scrollMyListViewToBottom();

            if(newQues) {
                DbHelper dbHelper = new DbHelper(parentActivity);

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String insert1 = "INSERT INTO " + Utility.FLASHCARDS_TABLE + " (" + Utility.FLASHCARD_TITLE + ", " + Utility.FLASHCARD_CONTENT + ", " + Utility.FLASHCARD_ANSWER + ", " + Utility.FLASHCARD_CRCT + ") VALUES(\"Book\",\"" + curQues + "\",\"" + nextAns.toArray()[0] + "\", 0);";

                db.execSQL(insert1);
                newQues = false;
            }
        }
    }

    public int addQuestionsToQueue(JSONObject responseFromQuestionApi) {
        int numQues = 0;
        try {
            if (responseFromQuestionApi.getString("message").equals("successful")) {
                JSONArray questions = responseFromQuestionApi.getJSONArray("text");
                numQues = questions.length();
                for (int i = 0; i < questions.length(); i++) {
                    questionQueue.add(questions.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return numQues;
    }

    private void addBotMessage(JSONObject message) {

        final BotChatMessage botChatMessage;

        try {
            botChatMessage = new BotChatMessage(message.getString("question"));
            curQues = message.getString("question");

            newQues = true;

            nextAns = new HashSet<>();
            nextAns.add(message.getString("answer"));

            JSONArray answerList = message.getJSONArray("similar_words");
            for (int i = 0; i < answerList.length(); i++) {
                nextAns.add(answerList.getString(i));
            }
            addDelayedChatBotMessage(botChatMessage, 3000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addDelayedChatBotMessage(final BotChatMessage botChatMessage, final long delay) {
        scrollMyListViewToBottom();
        MessagesAdapter.addChatMessage(botChatMessage);
        final ImageView botImage = (ImageView) parentActivity.findViewById(R.id.bot_image);
        botImage.setImageResource(R.drawable.typing_bot);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                botChatMessage.setTyping(false);

                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messagesAdapter.notifyDataSetChanged();
                        botImage.setImageResource(R.drawable.default_bot);
                        scrollMyListViewToBottom();
                    }
                });
            }
        }).start();
    }

}
