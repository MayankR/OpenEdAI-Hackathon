package com.prakhar2_mayank.questioningreader;

/**
 * Created by user on 30/7/17.
 */
public class BotChatMessage extends ChatMessage {

    private boolean typing;

    public BotChatMessage(String message) {
        super(message, false);
        typing = true;
    }

    public boolean isTyping() {
        return typing;
    }

    public BotChatMessage setTyping(boolean typing) {
        this.typing = typing;
        return this;
    }
}
