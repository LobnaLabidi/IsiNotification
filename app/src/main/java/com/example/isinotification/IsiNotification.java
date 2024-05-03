package com.example.isinotification;

import java.io.Serializable;

public class IsiNotification implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String category;
    private String title;
    private String body;
    private String priority;
    private String imageUrl;

    public IsiNotification(){}
    public IsiNotification(String category, String title, String body, String priority, String imageUrl) {
        this.category = category;
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.imageUrl = imageUrl;
    }
}
