package com.example.localisermonenfant_enfant.activity.Contacts.SMS;

public class Sms {
        private String id;
        private String message;
        private String createdAt;
        private String sender;
        private String number;
        private String type;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
                "_id='" + id + '\'' +
                ", _msg='" + message + '\'' +
                ", _date='" + createdAt + '\'' +
                ", _name='" + sender + '\'' +
                ", _number='" + number + '\'' +
                '}';
    }
}
