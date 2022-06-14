package com.cos.mediumclone.model;


import androidx.annotation.NonNull;

public class Post {

    private int id;
    private String title;
    private String content;
    private User user;
    private String created;
    private String updated;

    public Post() {

    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "id : " + id + '\n' +
                "title : " + title + '\n' +
                "content : " + content + '\n' +
                "user : " + user + '\n' +
                "created : " + created + '\n' +
                "updated : " + updated + '\n';
    }
}
