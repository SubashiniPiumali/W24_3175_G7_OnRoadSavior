package com.example.w24_3175_g7_onroadsavior.Model;

public class Notification {
    private int id;
    private String createdDate;
    private String upodatedDate;
    private String message;
    private String status;
    private String receiverId;

    public Notification(String message, String updateDate){
        this.upodatedDate = updateDate;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpodatedDate() {
        return upodatedDate;
    }

    public void setUpodatedDate(String upodatedDate) {
        this.upodatedDate = upodatedDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
