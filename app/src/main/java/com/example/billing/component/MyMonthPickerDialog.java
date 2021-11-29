package com.example.billing.component;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyMonthPickerDialog extends DatePickerDialog {

    @SuppressLint("DefaultLocale")
    public MyMonthPickerDialog(@NonNull Context context, int themeResId, @Nullable OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
        this.setTitle(String.format("%d年%d月",year,monthOfYear+1));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        this.setTitle(String.format("%d年%d月",year,month+1));
    }

    @Override
    public void show() {
        super.show();
        DatePicker dp = this.getDatePicker();
        NumberPicker view0 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0);
        NumberPicker view1 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1);
        NumberPicker view2 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2);

        int value0 = view0.getMaxValue();
        int value1 = view1.getMaxValue();
        int value2 = view2.getMaxValue();
        if(value0 >= 28 && value0 <= 31){
            view0.setVisibility(View.GONE);
        }else if(value1 >= 28 && value1 <= 31){
            view1.setVisibility(View.GONE);
        }else if(value2 >= 28 && value2 <= 31){
            view2.setVisibility(View.GONE);
        }
    }
}
