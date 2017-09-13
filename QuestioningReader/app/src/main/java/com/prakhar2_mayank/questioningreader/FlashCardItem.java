package com.prakhar2_mayank.questioningreader;

/**
 * Created by prakhar0409 on 01/08/17.
 */
public class FlashCardItem {

    private String title; // All Items must have a title so we can display on a list
    private long id; // Each card will also have an ID so we can find it inside our SQLite DB

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private int color;
    private String content, answer;

}
