package com.jusu.hangout.bean;

/**
 * Created by ayoko001 on 3/1/16.
 */
public class chatBean {
    String sender;
    String message;
    String time;

    public chatBean () {
        this.sender="";
        this.message="";
        this.time="";
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return "Sender: " + this.sender + "\n"
                + "Time: " + this.time + "\n"
                + "Message: " + this.message + "\n";

    }
}
