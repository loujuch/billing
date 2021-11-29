package com.example.billing.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.billing.R;
import com.example.billing.helper.HelperIO;
import com.example.billing.model.DetailIO;

import java.util.ArrayList;
import java.util.List;

public class MyDetailShow extends LinearLayout {


    public MyDetailShow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.detail_show,this);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public ArrayList<ArrayList<DetailIO>> show(int year, int month) {
        List<DetailIO> tmp=HelperIO.getInputDetailIOs(year,month);
        if(tmp==null||tmp.isEmpty()) {
            ((TextView)findViewById(R.id.cal)).setText(String.format("%d年%d月",year,month));
            ((TextView)findViewById(R.id.showMonthIncome)).setText("0.00");
            ((TextView)findViewById(R.id.showMonthPay)).setText("0.00");
            return null;
        }
        ArrayList<ArrayList<DetailIO>>out=new ArrayList<>();
        int site=-1;
        int len=tmp.size();
        for(int i=0;i<len;++i) {
            if(i==0||tmp.get(i).getCalendar()!=tmp.get(i-1).getCalendar()) {
                out.add(new ArrayList<>());
                ++site;
                DetailIO mid=new DetailIO();
                if(tmp.get(i).getIo()==HelperIO.PAY)mid.setPrice(tmp.get(i).getPrice());
                else mid.setPrice(0);
                mid.setCalendar(tmp.get(i).getCalendar());
                out.get(site).add(mid);
                out.get(site).add(tmp.get(i));
            } else {
                out.get(site).add(tmp.get(i));
                if(tmp.get(i).getIo()==HelperIO.PAY)out.get(site).get(0).setPrice(out.get(site).get(0).getPrice()+tmp.get(i).getPrice());
            }
        }
        ((TextView)findViewById(R.id.cal)).setText(String.format("%d年%d月",year,month));
        DetailIO monthIncome=HelperIO.getInputDetailIO(HelperIO.INCOME,year,month);
        DetailIO monthPay=HelperIO.getInputDetailIO(HelperIO.PAY,year,month);
        ((TextView)findViewById(R.id.showMonthIncome)).setText(String.format("%.2f",monthIncome==null?0:monthIncome.getPrice()));
        ((TextView)findViewById(R.id.showMonthPay)).setText(String.format("%.2f",monthPay==null?0:monthPay.getPrice()));
        return out;
    }
}
