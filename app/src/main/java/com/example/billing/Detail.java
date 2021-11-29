package com.example.billing;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyDayBody;
import com.example.billing.component.MyDayBox;
import com.example.billing.component.MyDetailShow;
import com.example.billing.component.MyMonthPickerDialog;
import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;
import com.example.billing.model.DetailIO;

import java.util.ArrayList;

public class Detail extends AppCompatActivity {

    private int year;
    private int month;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        MyApplication globe=(MyApplication)getApplication();

        year= globe.getYear();
        month= globe.getMonth();

        ((MyTitle)findViewById(R.id.title2)).setText("显示细节");

        DatePickerDialog.OnDateSetListener listener= (datePicker, i, i1, i2) -> {
            year=i;
            month=i1 + 1;
            show(year, month);};

        TextView tv=findViewById(R.id.tv);
        tv.setText(String.format("%d年%d月",globe.getYear(),globe.getMonth()));
        tv.setOnClickListener(view -> showDateSelector(listener, year, month));
        show(year, month);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onStart() {
        super.onStart();
        DatePickerDialog.OnDateSetListener listener= (datePicker, i, i1, i2) -> {
            year=i;
            month=i1 + 1;
            show(year, month);};

        TextView tv=findViewById(R.id.tv);
        tv.setText(String.format("%d年%d月",year,month));
        tv.setOnClickListener(view -> showDateSelector(listener, year, month));
        show(year, month);
    }

    private void showDateSelector(DatePickerDialog.OnDateSetListener listener, int year, int month) {
        MyMonthPickerDialog test=new MyMonthPickerDialog(this,
                DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month-1,1);
        test.show();
    }

    @SuppressLint("DefaultLocale")
    private void show(int year, int month) {
        ((TextView)findViewById(R.id.tv)).setText(String.format("%d年%d月",year,month));
        MyApplication globe=(MyApplication)getApplication();
        if(globe.getBilling()==null)globe.setBilling(new Sign(this, HelperFile.BILLING));
        if(globe.getBudget()==null)globe.setBudget(new Sign(this,HelperFile.BUDGET));
        Sign billing=globe.getBilling();
        Sign budget=globe.getBudget();
        LinearLayout sv=findViewById(R.id.scroll);
        sv.removeAllViews();
        MyDetailShow mds=new MyDetailShow(this);
        ArrayList<ArrayList<DetailIO>>mid= mds.show(year,month);
        if(mid==null) {
            Toast.makeText(this,"该月无任何收支记录！",Toast.LENGTH_SHORT).show();
            return;
        }
        for(ArrayList<DetailIO> tmp:mid) {
            int len=tmp.size();
            MyDayBox mdb=new MyDayBox(this);
            mdb.setText(tmp.get(0).getCalendar(),tmp.get(0).getPrice());
            sv.addView(mdb);
            for(int i=1;i<len;++i) {
                MyDayBody mdb2=new MyDayBody(this);
                if(tmp.get(i).getIo()== HelperIO.PAY)mdb2.setText(billing.getMainLabel(tmp.get(i).getMainLabel()),billing.getSecondLabel(tmp.get(i).getSecondLabel()),tmp.get(i).getTime(),-tmp.get(i).getPrice());
                else mdb2.setText(budget.getMainLabel(tmp.get(i).getMainLabel()),budget.getSecondLabel(tmp.get(i).getSecondLabel()),tmp.get(i).getTime(),tmp.get(i).getPrice());
                int finalI = i;
                mdb2.setOnClickListener(view -> {
                    Intent intent=new Intent(this,oneDayDetail.class);
                    intent.putExtra("id", String.format("%d",tmp.get(finalI).getId()));
                    startActivity(intent);
                });
                sv.addView(mdb2);
            }
        }
    }
}