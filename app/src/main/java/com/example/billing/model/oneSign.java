package com.example.billing.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.billing.helper.HelperType;

public class oneSign {
    private int type;
    private int id;
    private String name;
    private double able;

    public oneSign(int i, String s) {
        type=1;
        id=i;
        name=s;
        able=0;
    }

    public oneSign(String s) {
        if(s.length()<31) {
            type=1;
            id=0;
            name="未定<br/>义";
            able=0;
        }
        id= HelperType.StoI(s.substring(0,10));
        able=HelperType.StoD(s.substring(10,30));
        type= HelperType.StoI(s.substring(30,31));
        name=s.substring(31);
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("%10d%20.5f%1d%s",id,able,type,name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getAble() {
        return able;
    }

    public void setAble(double able) {
        this.able = able;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
