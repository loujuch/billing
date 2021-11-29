package com.example.billing.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.billing.R;

public class MySelector extends RadioGroup {

    public MySelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.selector,this);
    }

    public void setText() {
        ((RadioButton)findViewById(R.id.add0)).setText("增加");
        ((RadioButton)findViewById(R.id.un_add)).setText("减少");
    }

    public void setText(String in, String out) {
        ((RadioButton)findViewById(R.id.add0)).setText(in);
        ((RadioButton)findViewById(R.id.un_add)).setText(out);
    }

    public boolean getCheck() {
        return ((RadioButton)findViewById(R.id.add0)).isChecked();
    }
}
