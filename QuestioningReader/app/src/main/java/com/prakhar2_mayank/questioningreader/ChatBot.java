package com.prakhar2_mayank.questioningreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

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

    ChatBot(final AppCompatActivity parentActivity) {

        this.parentActivity = parentActivity;
        nextAns = null;

        questionQueue = new ArrayDeque<>(10000);
        try {
            addQuestionsToQueue(new JSONObject("{\"sucess\":true,\"message\":\"generate questions API\",\"questions\":[{\"question\":\"What is your name?\",\"answer\":[\"Tumhein matlab\",\"main nahin bataunga\",\"Tom Cruise\",\"Kya karega jaan ke\"]}]}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ListView messageListView = ((ListView) parentActivity.findViewById(R.id.message_list));
        messagesAdapter = MessagesAdapter.getMessagesAdapter(parentActivity);
        messageListView.setAdapter(messagesAdapter);


        parentActivity.findViewById(R.id.send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) parentActivity.findViewById(R.id.new_message);
                String userAnswer = editText.getText().toString();
                if (userAnswer.length() > 0) {
                    MessagesAdapter.addChatMessage(new UserChatMessage(userAnswer));
                    editText.setText("");

                    if (messageListView.getMaxScrollAmount() == 1) {
                        messageListView.scrollListBy(1);
                    }

                    verifyAnswer(userAnswer);
                }
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
        }
    }

    public void addQuestionsToQueue(JSONObject responseFromQuestionApi) {
        try {
            if (responseFromQuestionApi.getBoolean("sucess")) {
                JSONArray questions = responseFromQuestionApi.getJSONArray("questions");
                for (int i = 0; i < questions.length(); i++) {
                    questionQueue.add(questions.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBotMessage(JSONObject message) {

        final BotChatMessage botChatMessage;

        try {
            botChatMessage = new BotChatMessage(message.getString("question"));

            nextAns = new HashSet<>();
            JSONArray answerList = message.getJSONArray("answer");

            for (int i = 0; i < answerList.length(); i++) {
                nextAns.add(answerList.getString(i));
            }
            addDelayedChatBotMessage(botChatMessage, 3000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addDelayedChatBotMessage(final BotChatMessage botChatMessage, final long delay) {
        MessagesAdapter.addChatMessage(botChatMessage);

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
                    }
                });
            }
        }).start();
    }

}
