package com.example.billing.component;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyXAxisFormatter implements IAxisValueFormatter {
    ArrayList<String>mid;

    @SuppressLint("DefaultLocale")
    public MyXAxisFormatter(int yearL, int monthL, int dayL, int yearR, int monthR, int dayR) {
        mid=new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start=sdf.parse(String.format("%04d-%02d-%02d",yearL,monthL,dayL));
            Date end=sdf.parse(String.format("%04d-%02d-%02d",yearR,monthR,dayR));
            Calendar tmp =Calendar.getInstance();
            assert start != null;
            tmp.setTime(start);
            while(true) {
                assert end != null;
                if (!(start.getTime()<=end.getTime())) break;
                start=tmp.getTime();
                int month=tmp.get(Calendar.MONTH)+1;
                int day=tmp.get(Calendar.DATE);
                mid.add(String.format("%02d月%02d日",month,day));
                tmp.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        int x=(int)v;
        if(x<0||x> mid.size()) return null;
        return mid.get(x);
    }
}
