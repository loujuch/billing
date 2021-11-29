package com.example.billing.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.billing.R;

public class MyTable extends LinearLayout {

    public MyTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.table,this);
    }

    public void setFirstText(String s) {
        ((TextView)findViewById(R.id.start1)).setText(s);
    }

    public void setSecondText(String s) {
        ((TextView)findViewById(R.id.end1)).setText(s);
    }
}
