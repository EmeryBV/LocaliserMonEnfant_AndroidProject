package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

public class Sms {
        private String id;
        private String message;
        private String createdAt;
        private String sender;
        private String type;


    public Sms() {
    }

    public Sms(String id, String message, String createdAt, String sender, String type) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.sender = sender;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
