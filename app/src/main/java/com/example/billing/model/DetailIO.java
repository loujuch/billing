package com.example.billing.model;

import org.litepal.crud.LitePalSupport;

public class DetailIO extends LitePalSupport {
    int id;
    int io;
    int mainLabel;
    int secondLabel;
    double price;
    int calendar;
    int week;
    int time;
    String extra;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIo() {
        return io;
    }

    public void setIo(int io) {
        this.io = io;
    }

    public int getMainLabel() {
        return mainLabel;
    }

    public void setMainLabel(int mainLabel) {
        this.mainLabel = mainLabel;
    }

    public int getSecondLabel() {
        return secondLabel;
    }

    public void setSecondLabel(int secondLabel) {
        this.secondLabel = secondLabel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCalendar() {
        return calendar;
    }

    public void setCalendar(int calendar) {
        this.calendar = calendar;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

}
