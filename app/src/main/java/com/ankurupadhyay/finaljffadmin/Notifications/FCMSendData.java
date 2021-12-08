package com.ankurupadhyay.finaljffadmin.Notifications;
import java.util.Map;

public class FCMSendData {

    private String to;
    private String priority;
    private Map<String,String> data;
    private Map<String,String> notification;

    public Map<String, String> getNotification() {
        return notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }

    public FCMSendData() {
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
