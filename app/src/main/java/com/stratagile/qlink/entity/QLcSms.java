package com.stratagile.qlink.entity;

public class QLcSms {
    private String number;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QLcSms{" +
                "number='" + number + '\'' +
                ", body='" + body + '\'' +
                ", id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }

    public QLcSms(String number, String body, int id, long timestamp) {
        this.number = number;
        this.body = body;
        this.id = id;
        this.timestamp = timestamp;
    }

    public QLcSms(String number, String body, long timestamp) {
        this.number = number;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getNumber() {
        return number;
    }

    public QLcSms(String number, String body) {
        this.number = number;
        this.body = body;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String body;
    private int id;
    private long timestamp;
}
