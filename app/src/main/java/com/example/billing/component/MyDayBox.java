package com.example.billing.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.billing.R;
import com.example.billing.helper.HelperType;

public class MyDayBox extends LinearLayout {

    public MyDayBox(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.day_box,this);
    }

    @SuppressLint("DefaultLocale")
    public void setText(int whole, double price) {
        ((TextView)findViewById(R.id.t1)).setText(HelperType.getCalendar(whole,HelperType.DAY_SECOND));
        ((TextView)findViewById(R.id.t2)).setText(String.format("当日花费：%.2f",price));
    }
}
