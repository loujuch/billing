package com.example.billing.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.billing.R;

public class MyToShow extends LinearLayout {

    public MyToShow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.to_show,this);
    }

    public void setOnClickListener(Context context, Class<?> to) {
        findViewById(R.id.top).setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),to);
            context.startActivity(intent);
        });
    }

    public void setOnClickListener(@org.jetbrains.annotations.Nullable View.OnClickListener l) {
        findViewById(R.id.top).setOnClickListener(l);
    }

    public void setText(String s) {
        ((Button)findViewById(R.id.top)).setText(s);
    }
}
