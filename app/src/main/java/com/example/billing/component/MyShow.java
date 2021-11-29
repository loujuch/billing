package com.example.billing.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.billing.R;
import com.example.billing.helper.HelperType;
import com.example.billing.helper.MyApplication;

public class MyShow extends LinearLayout {
    
    private final String TAG="MyShow";
    
    public MyShow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.show,this);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void setText(Activity activity) {

        MyApplication globe=(MyApplication) activity.getApplication();

        double day_pay=0;
        double month_pay=0;
        double month_income=0;
        int week=globe.getWeek();
        int month=globe.getMonth();
        int day=globe.getDay();

        if(globe.getMonthIncomeId()>0) {
            month_income=globe.getMonthIncome();
        }

        if (globe.getMonthPayId()>0) {
            month_pay=globe.getMonthPay();
        }

        if (globe.getDayPayId()>0) {
            day_pay=globe.getDayPay();
        }

        ((TextView)findViewById(R.id.day_pay)).setText(Double.toString(day_pay));
        ((TextView)findViewById(R.id.month_pay)).setText(Double.toString(month_pay));
        ((TextView)findViewById(R.id.month_income)).setText(Double.toString(month_income));
        ((TextView)findViewById(R.id.time)).setText(String.format("%d月%d日",month,day));
        ((TextView)findViewById(R.id.week)).setText(HelperType.getWeekStr(week));

    }
}
