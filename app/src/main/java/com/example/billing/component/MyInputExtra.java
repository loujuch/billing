package com.example.billing.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.billing.R;

public class MyInputExtra extends LinearLayout {
    public MyInputExtra(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.input_extra,this);
    }

    public String getExtra() {
        return ((EditText)findViewById(R.id.in_extra)).getText().toString();
    }

    public void setText(String s) {
        ((EditText)findViewById(R.id.in_extra)).setText(s);
    }
}
