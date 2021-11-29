package com.example.billing.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.billing.R;
import com.example.billing.helper.HelperType;

public class MyInputDouble extends LinearLayout {

    public MyInputDouble(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.input_double,this);

        EditText in=findViewById(R.id.in);
        in.setFocusable(true);
        in.setFocusableInTouchMode(true);
        in.requestFocus();
    }

    public double getAmount() {
        String s=((EditText)findViewById(R.id.in)).getText().toString();
        if(s.equals(""))return -1;
        else return HelperType.StoD(s);
    }

    public void setText(String s) {
        ((EditText)findViewById(R.id.in)).setText(s);
    }
}
