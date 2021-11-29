package com.example.billing.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.billing.R;
import com.example.billing.Setting;

public class MyTitle extends LinearLayout {

    public MyTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);

        findViewById(R.id.back).setOnClickListener(view -> ((Activity)getContext()).finish());
        findViewById(R.id.to_setting).setOnClickListener(view -> {
            Intent intent=new Intent(getContext(), Setting.class);
            context.startActivity(intent);
        });
    }

    public void ChangeToSave() {
        Button button=findViewById(R.id.to_setting);
        button.setText("完成");
        button.setBackgroundColor(getResources().getColor(R.color.title));
    }

    public void ChangeToSave(String str) {
        Button button=findViewById(R.id.to_setting);
        button.setText(str);
        button.setBackgroundColor(getResources().getColor(R.color.title));
    }

    public void setOnClickListener(@org.jetbrains.annotations.Nullable View.OnClickListener l) {
        findViewById(R.id.to_setting).setOnClickListener(l);
    }

    public void hideBack() {
        findViewById(R.id.back).setVisibility(View.INVISIBLE);
    }

    public void hideToSetting() {
        findViewById(R.id.to_setting).setVisibility(View.INVISIBLE);
    }

    public void setText(String s) {
        ((TextView)findViewById(R.id.title_name)).setText(s);
    }
}
