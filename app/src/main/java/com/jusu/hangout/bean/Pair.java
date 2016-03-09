package com.jusu.hangout.bean;

/**
 * Created by ayoko001 on 3/2/16.
 */
public class Pair {
    String first;
    String second;

    public Pair() {
        first = "";
        second = "";
    }

    public Pair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst( String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond (String second) {
        this.second = second;
    }
}
