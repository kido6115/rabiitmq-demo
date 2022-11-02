package com.sungyeh.bean;

import java.io.Serializable;

public class OutputMessage implements Serializable {

    private static final long serialVersionUID = 361495983377109328L;
    private String dateStr;
    private Message message;

    public OutputMessage() {
    }

    public OutputMessage(String dateStr, Message message) {
        this.dateStr = dateStr;
        this.message = message;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
