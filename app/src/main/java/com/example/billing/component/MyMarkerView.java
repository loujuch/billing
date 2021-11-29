package com.example.billing.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.example.billing.R;
import com.example.billing.helper.HelperType;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class MyMarkerView extends MarkerView {

    private int year;
    private int month;
    private int day;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    public void setTime(int y, int m, int d) {
        year=y;
        month=m;
        day=d;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        @SuppressLint("DefaultLocale") String s= HelperType.getTimeString(year,month,day,(int)e.getX())+
                "<br/>金额："+String.format("%.2f",e.getY());
        ((TextView)findViewById(R.id.tvContent)).setText(Html.fromHtml(s));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
