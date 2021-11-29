package com.example.billing.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.billing.R;

public class MyShowBudget extends LinearLayout {
    public MyShowBudget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.show_sign_budget,this);
    }

    public void Show() {
        findViewById(R.id.all_show).setVisibility(View.VISIBLE);
    }

    public void unShow() {
        findViewById(R.id.all_show).setVisibility(View.INVISIBLE);
    }

    public void setText(String s) {
        ((TextView)findViewById(R.id.show1)).setText(s);
    }
}
