package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private static ArrayList<ChatMessage> chatMessages;
    private static MessagesAdapter messagesAdapter;
    final private AppCompatActivity context;
    int num = -1;

    private MessagesAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = (AppCompatActivity) context;
        chatMessages = new ArrayList<>();
    }

    public static MessagesAdapter getMessagesAdapter(Context context) {
        if (messagesAdapter == null) {
            messagesAdapter = new MessagesAdapter(context);
        }
        return messagesAdapter;
    }


    public static void clearChat() {
        chatMessages = new ArrayList<ChatMessage>();
        messagesAdapter.notifyDataSetChanged();
    }

    public static void addChatMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
        messagesAdapter.notifyDataSetChanged();
    }

    public static void changeLastChatMessage(ChatMessage chatMessage) {
        ChatMessage lastChatMessage = chatMessages.get(chatMessages.size() - 1);
        chatMessages.remove(chatMessages.size() - 1);
        chatMessages.add(chatMessage);
        chatMessages.add(lastChatMessage);
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatMessages.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            return inflater.inflate(R.layout.chat_blank_message, null);
        }
        View vi;
        ChatMessage chatMessage = chatMessages.get(position);
        if (chatMessage.isUser()) {
            vi = inflater.inflate(R.layout.chat_user_message, null);
        } else {
            if (((BotChatMessage) (chatMessage)).isTyping()) {
                return getTypingView();
            }
            vi = inflater.inflate(R.layout.chat_bot_message, null);
        }

        TextView text = (TextView) vi.findViewById(R.id.message);
        text.setText((CharSequence) chatMessages.get(position).getMessage());
        return vi;
    }

    private View getTypingView() {
        View typingView = inflater.inflate(R.layout.typing_indicator, null);

        ImageView dot10 = (ImageView) typingView.findViewById(R.id.typing_dot_10);
        ImageView dot20 = (ImageView) typingView.findViewById(R.id.typing_dot_20);
        ImageView dot30 = (ImageView) typingView.findViewById(R.id.typing_dot_30);
        ImageView dot11 = (ImageView) typingView.findViewById(R.id.typing_dot_11);
        ImageView dot21 = (ImageView) typingView.findViewById(R.id.typing_dot_21);
        ImageView dot31 = (ImageView) typingView.findViewById(R.id.typing_dot_31);

        num++;
        switch (num % 12) {
            case 0:
                dot10.setVisibility(View.INVISIBLE);
                dot20.setVisibility(View.INVISIBLE);
                dot30.setVisibility(View.INVISIBLE);
                dot11.setVisibility(View.VISIBLE);
                dot21.setVisibility(View.VISIBLE);
                dot31.setVisibility(View.VISIBLE);
                break;
            case 1:
                dot10.setVisibility(View.VISIBLE);
                dot20.setVisibility(View.INVISIBLE);
                dot30.setVisibility(View.INVISIBLE);
                dot11.setVisibility(View.INVISIBLE);
                dot21.setVisibility(View.VISIBLE);
                dot31.setVisibility(View.VISIBLE);
                break;
            case 2:
                dot10.setVisibility(View.VISIBLE);
                dot20.setVisibility(View.VISIBLE);
                dot30.setVisibility(View.INVISIBLE);
                dot11.setVisibility(View.INVISIBLE);
                dot21.setVisibility(View.INVISIBLE);
                dot31.setVisibility(View.VISIBLE);
                break;
            case 3:
            case 4:
                dot10.setVisibility(View.VISIBLE);
                dot20.setVisibility(View.VISIBLE);
                dot30.setVisibility(View.VISIBLE);
                dot11.setVisibility(View.INVISIBLE);
                dot21.setVisibility(View.INVISIBLE);
                dot31.setVisibility(View.INVISIBLE);
                break;
            case 5:
            case 6:
                dot10.setVisibility(View.INVISIBLE);
                dot20.setVisibility(View.VISIBLE);
                dot30.setVisibility(View.VISIBLE);
                dot11.setVisibility(View.VISIBLE);
                dot21.setVisibility(View.INVISIBLE);
                dot31.setVisibility(View.INVISIBLE);
                break;
            case 7:
            case 8:
                dot10.setVisibility(View.INVISIBLE);
                dot20.setVisibility(View.INVISIBLE);
                dot30.setVisibility(View.VISIBLE);
                dot11.setVisibility(View.VISIBLE);
                dot21.setVisibility(View.VISIBLE);
                dot31.setVisibility(View.INVISIBLE);
                break;
            case 9:
            case 10:
            case 11:
                dot10.setVisibility(View.INVISIBLE);
                dot20.setVisibility(View.INVISIBLE);
                dot30.setVisibility(View.INVISIBLE);
                dot11.setVisibility(View.VISIBLE);
                dot21.setVisibility(View.VISIBLE);
                dot31.setVisibility(View.VISIBLE);
                break;
        }

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }.start();
        return typingView;
    }
}
