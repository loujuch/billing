package com.example.billing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTable;
import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.HelperType;
import com.example.billing.model.DetailIO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AnalyzeComment extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze_comment);

        Intent intent=getIntent();

        if(intent==null) {
            Toast.makeText(this, "这里发生了一些错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        ((MyTitle)findViewById(R.id.comment_title)).setText("通用分析");

        int startYear= HelperType.StoI(intent.getStringExtra("startYear"));
        int startMonth=HelperType.StoI(intent.getStringExtra("startMonth"));
        int startDay=HelperType.StoI(intent.getStringExtra("startDay"));
        int endYear=HelperType.StoI(intent.getStringExtra("endYear"));
        int endMonth=HelperType.StoI(intent.getStringExtra("endMonth"));
        int endDay=HelperType.StoI(intent.getStringExtra("endDay"));

        List<DetailIO> tmp= HelperIO.getInputDetailIOs(
                startYear,startMonth,startDay,endYear,endMonth,endDay);

        double allPay=0.0;
        double allIncome=0.0;
        int allDay=getDayNum(startYear,startMonth,startDay,endYear,endMonth,endDay);

        if(allDay<0||tmp==null) {
            Toast.makeText(this,"一些错误发生了！",Toast.LENGTH_SHORT).show();
            return;
        }

        for(DetailIO i:tmp) {
            if(i.getIo()==HelperIO.PAY)allPay+=i.getPrice();
            else allIncome+=i.getPrice();
        }

        MyTable tab_mt1=findViewById(R.id.tab_mt1);
        tab_mt1.setFirstText("开始时间");
        tab_mt1.setSecondText(String.format("%d年%d月%d日",startYear,startMonth,startDay));

        MyTable tab_mt2=findViewById(R.id.tab_mt2);
        tab_mt2.setFirstText("结束时间");
        tab_mt2.setSecondText(String.format("%d年%d月%d日",endYear,endMonth,endDay));

        MyTable tab_mt3=findViewById(R.id.tab_mt3);
        tab_mt3.setFirstText("统计天数");
        tab_mt3.setSecondText(String.format("%d", allDay));

        MyTable tab_mt4=findViewById(R.id.tab_mt4);
        tab_mt4.setFirstText("总收入");
        tab_mt4.setSecondText(String.format("%.5f",allIncome));

        MyTable tab_mt5=findViewById(R.id.tab_mt5);
        tab_mt5.setFirstText("总支出");
        tab_mt5.setSecondText(String.format("%.5f",allPay));

        MyTable tab_mt6=findViewById(R.id.tab_mt6);
        tab_mt6.setFirstText("总净收入");
        tab_mt6.setSecondText(String.format("%.5f",allIncome-allPay));

        MyTable tab_mt7=findViewById(R.id.tab_mt7);
        tab_mt7.setFirstText("日均收入");
        tab_mt7.setSecondText(String.format("%.5f",allIncome/allDay));

        MyTable tab_mt8=findViewById(R.id.tab_mt8);
        tab_mt8.setFirstText("日均支出");
        tab_mt8.setSecondText(String.format("%.5f",allPay/allDay));

        MyTable tab_mt9=findViewById(R.id.tab_mt9);
        tab_mt9.setFirstText("日均净收入");
        tab_mt9.setSecondText(String.format("%.5f",(allIncome-allPay)/allDay));
    }

    private int getDayNum(int startYear, int startMonth, int startDay,
                          int endYear, int endMonth, int endDay) {
        @SuppressLint("SimpleDateFormat") DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            @SuppressLint("DefaultLocale") Date start=
                    dft.parse(String.format("%04d-%02d-%02d",startYear,startMonth,startDay));
            @SuppressLint("DefaultLocale") Date end=
                    dft.parse(String.format("%04d-%02d-%02d",endYear,endMonth,endDay));
            assert end != null;
            assert start != null;
            return (int)((end.getTime()-start.getTime())/24/60/60/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}