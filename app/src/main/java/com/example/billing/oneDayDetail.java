package com.example.billing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyBottomButton;
import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.HelperType;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;
import com.example.billing.model.DetailIO;

public class oneDayDetail extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_day_detail);

        if(getIntent()==null) {
            Toast.makeText(this, "发生错误！", Toast.LENGTH_SHORT).show();
            finish();
        }

        DetailIO my=HelperIO.getInputDetailIOById(HelperType.StoI(getIntent().getStringExtra("id")));

        if(my==null) {
            Toast.makeText(this, "MY is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        int cal=my.getCalendar();
        int time=my.getTime();

        Sign sign;
        MyApplication globe=(MyApplication) getApplication();

        if(my.getIo()== HelperIO.PAY) {
            if(globe.getBilling()==null)globe.setBilling(new Sign(this, HelperFile.BILLING));
            sign=globe.getBilling();
        } else {
            if(globe.getBudget()==null)globe.setBudget(new Sign(this, HelperFile.BUDGET));
            sign=globe.getBudget();
        }

        ((TextView)findViewById(R.id.timer)).setText(String.format("%d年%d月%d日  %d:%d:%d",
                HelperType.getPart(cal,HelperType.YEAR_HOUR),
                HelperType.getPart(cal,HelperType.MONTH_MINTER),
                HelperType.getPart(cal,HelperType.DAY_SECOND),
                HelperType.getPart(time,HelperType.YEAR_HOUR),
                HelperType.getPart(time,HelperType.MONTH_MINTER),
                HelperType.getPart(time,HelperType.DAY_SECOND)));

        ((TextView)findViewById(R.id.price)).setText(String.format("%.2f",my.getPrice()));

        ((TextView)findViewById(R.id.tv5)).setText(Html.fromHtml(sign.getMainLabel(my.getMainLabel())));
        ((TextView)findViewById(R.id.tv6)).setText(Html.fromHtml(sign.getSecondLabel(my.getSecondLabel())));

        String tmp="该标签无备注";
        if(my.getExtra()!=null&&!my.getExtra().equals(""))tmp=my.getExtra();
        ((TextView)findViewById(R.id.text3)).setText(tmp);

        MyTitle myt=findViewById(R.id.myT);
        myt.setText("显示细节");
        myt.ChangeToSave("更改");
        myt.setOnClickListener(view -> {
            Intent intent=new Intent(this,ChangeSign.class);
            intent.putExtra("id",String.format("%d",my.getId()));
            startActivityForResult(intent,2);
        });

        MyBottomButton myb=findViewById((R.id.bb));
        myb.setText("删除记录");
        myb.setOnClickListener(view -> IfDelete(my));
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onStart() {
        super.onStart();
        DetailIO my=HelperIO.getInputDetailIOById(
                HelperType.StoI(getIntent().getStringExtra("id")));

        if(my==null) {
            Toast.makeText(this, "MY is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        int cal=my.getCalendar();
        int time=my.getTime();

        Sign sign;
        MyApplication globe=(MyApplication) getApplication();

        if(my.getIo()== HelperIO.PAY) {
            if(globe.getBilling()==null)globe.setBilling(new Sign(this, HelperFile.BILLING));
            sign=globe.getBilling();
        } else {
            if(globe.getBudget()==null)globe.setBudget(new Sign(this, HelperFile.BUDGET));
            sign=globe.getBudget();
        }

        ((TextView)findViewById(R.id.timer)).setText(String.format("%d年%d月%d日  %d:%d:%d",
                HelperType.getPart(cal,HelperType.YEAR_HOUR),
                HelperType.getPart(cal,HelperType.MONTH_MINTER),
                HelperType.getPart(cal,HelperType.DAY_SECOND),
                HelperType.getPart(time,HelperType.YEAR_HOUR),
                HelperType.getPart(time,HelperType.MONTH_MINTER),
                HelperType.getPart(time,HelperType.DAY_SECOND)));

        ((TextView)findViewById(R.id.price)).setText(String.format("%.2f",my.getPrice()));

        ((TextView)findViewById(R.id.tv5)).setText(
                Html.fromHtml(sign.getMainLabel(my.getMainLabel())));
        ((TextView)findViewById(R.id.tv6)).setText(
                Html.fromHtml(sign.getSecondLabel(my.getSecondLabel())));

        String tmp="该标签无备注";
        if(my.getExtra()!=null&&!my.getExtra().equals(""))tmp=my.getExtra();
        ((TextView)findViewById(R.id.text3)).setText(tmp);

        MyTitle myt=findViewById(R.id.myT);
        myt.setText("显示细节");
        myt.ChangeToSave("更改");
        myt.setOnClickListener(view -> {
            Intent intent=new Intent(this,ChangeSign.class);
            intent.putExtra("id",String.format("%d",my.getId()));
            startActivity(intent);
        });

        MyBottomButton myb=findViewById((R.id.bb));
        myb.setText("删除记录");
        myb.setOnClickListener(view -> IfDelete(my));
    }

    private void IfDelete(DetailIO dio) {
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("是否删除该记录？")
                .setNegativeButton("否", null)
                .setPositiveButton("是", (dialogInterface, i) -> MyDelete(dio))
                .create()
                .show();
    }

    private void MyDelete(DetailIO dio) {
        MyApplication globe=(MyApplication) getApplication();
        double price=dio.getPrice();
        if(dio.getIo()==HelperIO.INCOME) {
            price*=-1;
            if(globe.getAllUseAble()+price<0) {
                Toast.makeText(this,"余额不足！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        globe.Update(dio.getIo(),price,dio.getCalendar());
        HelperIO.RemoveDetailById(dio.getId());
        HelperIO.UpdateDetailByCalendar(dio.getIo(),price,dio.getCalendar());
        finish();
    }
}