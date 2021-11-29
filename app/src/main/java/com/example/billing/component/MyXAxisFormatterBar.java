package com.example.billing.component;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class MyXAxisFormatterBar implements IAxisValueFormatter {

    List<String> tmp;

    public MyXAxisFormatterBar(List<PieEntry>mid) {
        tmp=new ArrayList<>();
        for(PieEntry i:mid){
            tmp.add(i.getLabel());
        }
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        int n=(int)v;
        if(n<0||n>tmp.size())return "";
        return tmp.get(n);
    }
}
