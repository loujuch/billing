package com.example.billing.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.billing.R;
import com.example.billing.helper.HelperType;

public class MyDayBody extends LinearLayout {
    public MyDayBody(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.day_body,this);
    }

    @SuppressLint("DefaultLocale")
    public void setText(String label1, String label2, int time, double price) {
        ((TextView)findViewById(R.id.tv1)).setText(Html.fromHtml(label1));
        ((TextView)findViewById(R.id.tv2)).setText(Html.fromHtml(label2));
        ((TextView)findViewById(R.id.tv3)).setText(HelperType.getTime(time));
        ((TextView)findViewById(R.id.tv4)).setText(String.format("%.2f",price));
    }
}
