package com.example.billing;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.component.MyToShow;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.HelperType;
import com.example.billing.helper.MyApplication;

public class Analyze extends AppCompatActivity {

    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze);

        MyApplication globe=(MyApplication) getApplication();

        startYear=endYear=globe.getYear();
        startMonth=endMonth=globe.getMonth();
        startDay=1;
        endDay=globe.getDay();

        ((MyTitle)findViewById(R.id.an_nt)).setText("收支分析");

        DatePickerDialog.OnDateSetListener listenerStart= (datePicker, i, i1, i2) -> {
            if(HelperType.getWhole(i,i1+1,i2)>HelperType.getWhole(endYear,endMonth,endDay)) {
                Toast.makeText(this, "请确保开始时间小于结束时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            startYear=i;
            startMonth=i1 + 1;
            startDay=i2;
            ((TextView)findViewById(R.id.start_time)).setText(
                    String.format("开始时间：%d年%d月%d日",startYear,startMonth,startDay));
        };

        DatePickerDialog.OnDateSetListener listenerEnd= (datePicker, i, i1, i2) -> {
            if(HelperType.getWhole(i,i1+1,i2)<
                    HelperType.getWhole(startYear,startMonth,startDay)) {
                Toast.makeText(this, "请确保开始时间小于结束时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            endYear=i;
            endMonth=i1 + 1;
            endDay=i2;
            ((TextView)findViewById(R.id.end_time)).setText(
                    String.format("结束时间：%d年%d月%d日",endYear,endMonth,endDay));
        };

        TextView start=findViewById(R.id.start_time);
        start.setText(String.format("开始时间：%d年%d月%d日",startYear,startMonth,startDay));
        start.setOnClickListener(view -> showDateSelector(listenerStart,startYear,startMonth,startDay));

        TextView end=findViewById(R.id.end_time);
        end.setText(String.format("结束时间：%d年%d月%d日",endYear,endMonth,endDay));
        end.setOnClickListener(view -> showDateSelector(listenerEnd,endYear,endMonth,endDay));

        MyToShow mts1=findViewById(R.id.all_inline);
        mts1.setText("通用分析");
        mts1.setOnClickListener(view -> startNext(AnalyzeComment.class));

        MyToShow mts2=findViewById(R.id.time_income);
        mts2.setText("时间-收入");
        mts2.setOnClickListener(view -> startNext(TimePrice.class,HelperIO.INCOME));

        MyToShow mts3=findViewById(R.id.time_pay);
        mts3.setText("时间-支出");
        mts3.setOnClickListener(view -> startNext(TimePrice.class,HelperIO.PAY));

        MyToShow mts4=findViewById(R.id.sign_income);
        mts4.setText("标签-收入");
        mts4.setOnClickListener(view -> startNext(SignPrice.class,HelperIO.INCOME));

        MyToShow mts5=findViewById(R.id.sign_pay);
        mts5.setText("标签-支出");
        mts5.setOnClickListener(view -> startNext(SignPrice.class,HelperIO.PAY));
    }

    private void showDateSelector(DatePickerDialog.OnDateSetListener listener,
                                  int year, int month, int day) {
        DatePickerDialog test=new DatePickerDialog(this,
                DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month-1,day);
        test.show();
    }

    @SuppressLint("DefaultLocale")
    private void startNext(Class<?>t, int io) {
        Intent intent=new Intent(this,t);
        intent.putExtra("startYear",String.format("%d",startYear));
        intent.putExtra("startMonth",String.format("%d",startMonth));
        intent.putExtra("startDay",String.format("%d",startDay));
        intent.putExtra("endYear",String.format("%d",endYear));
        intent.putExtra("endMonth",String.format("%d",endMonth));
        intent.putExtra("endDay",String.format("%d",endDay));
        intent.putExtra("io",String.format("%d",io));
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    private void startNext(Class<?>t) {
        Intent intent=new Intent(this,t);
        intent.putExtra("startYear",String.format("%d",startYear));
        intent.putExtra("startMonth",String.format("%d",startMonth));
        intent.putExtra("startDay",String.format("%d",startDay));
        intent.putExtra("endYear",String.format("%d",endYear));
        intent.putExtra("endMonth",String.format("%d",endMonth));
        intent.putExtra("endDay",String.format("%d",endDay));
        startActivity(intent);
    }
}