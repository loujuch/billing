package com.example.billing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyMarkerView;
import com.example.billing.component.MyTitle;
import com.example.billing.component.MyXAxisFormatter;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.HelperType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

public class TimePrice extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_pay);

        Intent intent=getIntent();

        if(intent==null) {
            Toast.makeText(this, "这里发生了一些错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        int startYear= HelperType.StoI(intent.getStringExtra("startYear"));
        int startMonth=HelperType.StoI(intent.getStringExtra("startMonth"));
        int startDay=HelperType.StoI(intent.getStringExtra("startDay"));
        int endYear=HelperType.StoI(intent.getStringExtra("endYear"));
        int endMonth=HelperType.StoI(intent.getStringExtra("endMonth"));
        int endDay=HelperType.StoI(intent.getStringExtra("endDay"));
        int io=HelperType.StoI(intent.getStringExtra("io"));

        if(io==HelperIO.PAY)((MyTitle)findViewById(R.id.an_mt)).setText("时间—支出");
        else ((MyTitle)findViewById(R.id.an_mt)).setText("时间—收入");

        TextView start=findViewById(R.id.start_time);
        start.setText(String.format("开始时间：%d年%d月%d日",startYear,startMonth,startDay));

        TextView end=findViewById(R.id.end_time);
        end.setText(String.format("结束时间：%d年%d月%d日",endYear,endMonth,endDay));

        LineChart chart=findViewById(R.id.chart);

        chart.setDrawBorders(true);
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.setDragEnabled(true);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        MyXAxisFormatter custom = new MyXAxisFormatter(startYear,startMonth,startDay,endYear,endMonth,endDay);
        XAxis leftAxis = chart.getXAxis();
        leftAxis.setValueFormatter(custom);

        YAxis leftYAxis=chart.getAxisLeft();
        YAxis rightYAxis=chart.getAxisRight();
        rightYAxis.setEnabled(false);
        leftYAxis.setAxisMinimum(0);

        List<Entry> tmp= HelperIO.getInputDetailIOSum
                (startYear,startMonth,startDay,endYear,endMonth,endDay,io);

        float ratio = 1f;
        if (tmp.size() >= 6){
            ratio = (float) tmp.size()/(float) 6;
            chart.moveViewToX(tmp.size()-1);
        }
        chart.zoom(ratio, 0f, 0, 0);

        LineDataSet lineDataSet=new LineDataSet(tmp,"金额");
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);

        MyMarkerView detailsMarkerView = new MyMarkerView(TimePrice.this,R.layout.marker);
        detailsMarkerView.setTime(startYear,startMonth,startDay);
        detailsMarkerView.setChartView(chart);
        chart.setMarker(detailsMarkerView);

        LineData data = new LineData(lineDataSet);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                int x=(int) entry.getX();
                TextView to=findViewById(R.id.detail);
                to.setVisibility(View.VISIBLE);
                to.setText("显示详情："+
                        HelperType.getTimeString(startYear,startMonth,startDay,x));
                to.setOnClickListener(view -> {
                    int year=HelperType.getTime(startYear,startMonth,startDay,x,HelperType.YEAR_HOUR);
                    int month=HelperType.getTime(startYear,startMonth,startDay,x,HelperType.MONTH_MINTER);
                    int day=HelperType.getTime(startYear,startMonth,startDay,x,HelperType.DAY_SECOND);
                    Intent intent = new Intent(TimePrice.this, SignPrice.class);
                    intent.putExtra("startYear", String.format("%d", year));
                    intent.putExtra("startMonth", String.format("%d", month));
                    intent.putExtra("startDay", String.format("%d", day));
                    intent.putExtra("endYear", String.format("%d", year));
                    intent.putExtra("endMonth", String.format("%d", month));
                    intent.putExtra("endDay", String.format("%d", day));
                    intent.putExtra("io", String.format("%d", io));
                    startActivity(intent);
                });
            }

            @Override
            public void onNothingSelected() {
                ((TextView)findViewById(R.id.detail)).setVisibility(View.INVISIBLE);
            }
        });

        chart.setData(data);
        chart.invalidate();
    }
}