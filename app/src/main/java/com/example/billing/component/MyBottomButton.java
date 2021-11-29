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

public class MyBottomButton extends LinearLayout {
    public MyBottomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_button,this);
    }

    public void setText(String s) {
        ((Button)findViewById(R.id.to)).setText(s);
    }

    public void setTo(Context context, Class<?> to) {
        findViewById(R.id.to).setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),to);
            context.startActivity(intent);
        });
    }

    public void setOnClickListener(@org.jetbrains.annotations.Nullable View.OnClickListener l) {
        findViewById(R.id.to).setOnClickListener(l);
    }
}
