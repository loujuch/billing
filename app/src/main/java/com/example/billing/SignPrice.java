package com.example.billing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.component.MyXAxisFormatterBar;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.HelperType;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class SignPrice extends AppCompatActivity {

    private int io;
    private Sign sign;
    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int id;
    private List<PieEntry>tmp;
    private ArrayList<Integer> color;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_price);

        Intent intent=getIntent();

        if(intent==null) {
            Toast.makeText(this, "这里发生了一些错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        io=HelperType.StoI(intent.getStringExtra("io"));
        startYear= HelperType.StoI(intent.getStringExtra("startYear"));
        startMonth=HelperType.StoI(intent.getStringExtra("startMonth"));
        startDay=HelperType.StoI(intent.getStringExtra("startDay"));
        endYear=HelperType.StoI(intent.getStringExtra("endYear"));
        endMonth=HelperType.StoI(intent.getStringExtra("endMonth"));
        endDay=HelperType.StoI(intent.getStringExtra("endDay"));
        id=HelperType.StoI(intent.getStringExtra("id"));

        MyApplication globe=(MyApplication) getApplication();

        if (io==HelperIO.PAY) {
            ((MyTitle)findViewById(R.id.an_mt)).setText("标签—支出");
            if (globe.getBilling()==null)globe.setBilling(new Sign(this,HelperFile.BILLING));
            sign=globe.getBilling();
        } else {
            ((MyTitle)findViewById(R.id.an_mt)).setText("标签—收入");
            if(globe.getBudget()==null)globe.setBudget(new Sign(this, HelperFile.BUDGET));
            sign=globe.getBudget();
        }

        if(id==-1) {
            tmp= HelperIO.getInputDetailIOSignMainSum(sign.getMain(),io,
                    HelperType.getWhole(startYear,startMonth,startDay),
                    HelperType.getWhole(endYear,endMonth,endDay));
        } else {
            tmp= HelperIO.getInputDetailIOSignSecondSum(sign.getSecond(id),io,
                    HelperType.getWhole(startYear,startMonth,startDay),
                    HelperType.getWhole(endYear,endMonth,endDay),id);
        }

        if(tmp==null) {
            Toast.makeText(this,"该标签没有二级标签！",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int colorAll=HelperType.getWhole(255,255,255);
        color=new ArrayList<>();
        int colorOffset=colorAll/(tmp.size()+4);
        for(int i=1;i<=tmp.size();++i) {
            int nowColor=colorOffset*i;
            color.add(Color.rgb(nowColor>>16,(nowColor>>8)&0xff,nowColor&0xff));
        }

        TextView start=findViewById(R.id.start_time);
        start.setText(String.format("开始时间：%d年%d月%d日",startYear,startMonth,startDay));

        TextView end=findViewById(R.id.end_time);
        end.setText(String.format("结束时间：%d年%d月%d日",endYear,endMonth,endDay));

        showPie();

        ((RadioGroup)findViewById(R.id.all3)).setOnCheckedChangeListener((radioGroup, i) ->
        {
            if (((RadioButton) findViewById(R.id.add3)).isChecked()) {
                showPie();
            } else {
                showBar();
            }
        });
    }

    private void showBar() {
        findViewById(R.id.pic_chart).setVisibility(View.GONE);
        BarChart bar =findViewById(R.id.bar_chart);
        bar.setVisibility(View.VISIBLE);

        bar.setDrawBorders(true);
        bar.setTouchEnabled(true);
        bar.setDragDecelerationFrictionCoef(0.9f);
        bar.setDragEnabled(true);
        bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bar.setDrawBarShadow(false);
        bar.setHighlightFullBarEnabled(false);

        Description description = new Description();
        description.setEnabled(false);
        bar.setDescription(description);

        MyXAxisFormatterBar custom = new MyXAxisFormatterBar(tmp);
        XAxis leftAxis = bar.getXAxis();
        leftAxis.setValueFormatter(custom);

        YAxis leftYAxis=bar.getAxisLeft();
        YAxis rightYAxis=bar.getAxisRight();
        rightYAxis.setEnabled(false);
        leftYAxis.setAxisMinimum(0);

        List<BarEntry>data=new ArrayList<>();

        int len= tmp.size();
        for(int i=0;i<len;++i) {
            data.add(new BarEntry(i,tmp.get(i).getValue()));
        }

        float ratio = 1f;
        if (data.size() >= 6){
            ratio = (float) data.size()/(float) 6;
            bar.moveViewToX(data.size()-1);
        }
        bar.zoom(ratio, 0f, 0, 0);

        BarDataSet barDataSet=new BarDataSet(data,"");
        barDataSet.setDrawValues(true);
        barDataSet.setColor(Color.rgb(100,120,120));
        barDataSet.setBarBorderWidth(0f);
        barDataSet.setValueTextSize(17f);

        BarData barData=new BarData(barDataSet);

        if(id==-1) {
            bar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @SuppressLint({"DefaultLocale", "SetTextI18n"})
                @Override
                public void onValueSelected(Entry entry, Highlight highlight) {
                    String s=tmp.get((int)entry.getX()).getLabel();
                    int myId=sign.getMainId(s);
                    TextView to=findViewById(R.id.an_az);
                    to.setVisibility(View.VISIBLE);
                    to.setText("详细情况："+s);
                    to.setOnClickListener(view -> {
                        Intent intent = new Intent(SignPrice.this, SignPrice.class);
                        intent.putExtra("startYear", String.format("%d", startYear));
                        intent.putExtra("startMonth", String.format("%d", startMonth));
                        intent.putExtra("startDay", String.format("%d", startDay));
                        intent.putExtra("endYear", String.format("%d", endYear));
                        intent.putExtra("endMonth", String.format("%d", endMonth));
                        intent.putExtra("endDay", String.format("%d", endDay));
                        intent.putExtra("id", String.format("%d", myId));
                        intent.putExtra("io", String.format("%d", io));
                        startActivity(intent);
                    });
                }

                @Override
                public void onNothingSelected() {
                    findViewById(R.id.an_az).setVisibility(View.INVISIBLE);
                }
            });
        }

        bar.setData(barData);
        bar.invalidate();
    }

    private void showPie() {
        findViewById(R.id.bar_chart).setVisibility(View.GONE);
        PieChart pie=findViewById(R.id.pic_chart);
        pie.setVisibility(View.VISIBLE);

        pie.setUsePercentValues(true);
        pie.getDescription().setEnabled(false);
        pie.setRotationEnabled(false);
        pie.setHighlightPerTapEnabled(true);
        pie.setDrawCenterText(true);
        pie.setDrawEntryLabels(true);
        pie.setDrawHoleEnabled(true);
        pie.setEntryLabelColor(Color.BLACK);
        pie.setEntryLabelTextSize(20f);

        Description description = new Description();
        description.setEnabled(false);
        pie.setDescription(description);

        PieDataSet pieDataSet=new PieDataSet(tmp,"");
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setSliceSpace(9f);
        pieDataSet.setColors(color);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.rgb(0,0,0));
        pieData.setValueTextSize(20f);
        pieData.setValueFormatter(new PercentFormatter());

        if(id==-1) {
            pie.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @SuppressLint({"DefaultLocale", "SetTextI18n"})
                @Override
                public void onValueSelected(Entry entry, Highlight highlight) {
                    PieEntry pieEntry = (PieEntry) entry;
                    String s=pieEntry.getLabel();
                    int myId=sign.getMainId(s);
                    TextView to=findViewById(R.id.an_az);
                    to.setVisibility(View.VISIBLE);
                    to.setText("详细情况："+s);
                    to.setOnClickListener(view -> {
                        Intent intent=new Intent(SignPrice.this,SignPrice.class);
                        intent.putExtra("startYear",String.format("%d",startYear));
                        intent.putExtra("startMonth",String.format("%d",startMonth));
                        intent.putExtra("startDay",String.format("%d",startDay));
                        intent.putExtra("endYear",String.format("%d",endYear));
                        intent.putExtra("endMonth",String.format("%d",endMonth));
                        intent.putExtra("endDay",String.format("%d",endDay));
                        intent.putExtra("id",String.format("%d",myId));
                        intent.putExtra("io",String.format("%d",io));
                        startActivity(intent);
                    });
                }

                @Override
                public void onNothingSelected() {
                    findViewById(R.id.an_az).setVisibility(View.INVISIBLE);
                }
            });
        }

        pie.setData(pieData);
        pie.highlightValues(null);
        pie.invalidate();
    }
}