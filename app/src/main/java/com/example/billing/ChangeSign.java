package com.example.billing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyInputDouble;
import com.example.billing.component.MyInputExtra;
import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.HelperType;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;
import com.example.billing.model.DetailIO;

public class ChangeSign extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing);

        if (getIntent() == null) {
            Toast.makeText(this, "发生错误！", Toast.LENGTH_SHORT).show();
            finish();
        }
        DetailIO my = HelperIO.getInputDetailIOById(HelperType.StoI(getIntent().getStringExtra("id")));

        if (my == null) {
            Toast.makeText(this, "发生错误！", Toast.LENGTH_SHORT).show();
            finish();
        }

        Sign sign;
        MyApplication globe = (MyApplication) getApplication();

        assert my != null;
        if (my.getIo() == HelperIO.PAY) {
            if (globe.getBilling() == null) globe.setBilling(new Sign(this, HelperFile.BILLING));
            sign = globe.getBilling();
        } else {
            if (globe.getBudget() == null) globe.setBudget(new Sign(this, HelperFile.BUDGET));
            sign = globe.getBudget();
        }

        ((MyInputDouble) findViewById(R.id.input_double_main)).setText(String.format("%.2f", my.getPrice()));
        ((MyInputExtra) findViewById(R.id.input_extra_main)).setText(my.getExtra());

        sign.setLabel(this, my.getMainLabel(), my.getSecondLabel(),
                findViewById(R.id.first), findViewById(R.id.second),
                findViewById(R.id.show10), findViewById(R.id.show11));
//Toast.makeText(this,String.format("%d %d",my.getMainLabel(),my.getSecondLabel()),Toast.LENGTH_SHORT).show();
        MyTitle mt = findViewById(R.id.title_button);
        mt.setText("更改记录");
        mt.ChangeToSave();
        mt.setOnClickListener(view -> update((MyApplication) getApplication(), my, sign));
    }

    private void update(MyApplication globe, DetailIO my, Sign sign) {
        MyInputDouble mid = findViewById(R.id.input_double_main);

        double offset;
        double price=mid.getAmount();

        if(Math.abs(mid.getAmount()-my.getPrice())<1e-5) {
            Toast.makeText(this,"存储成功！",Toast.LENGTH_SHORT).show();
            return;
        }

        if(price==-1) {
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
            return;
        }

        if(my.getIo()==HelperIO.PAY) {
            offset=-mid.getAmount()+my.getPrice();

            int tmp=sign.setPrice(offset,HelperIO.INCOME);

            if (tmp==1){
                Toast.makeText(this, "该二级标签预算不足，已使用上一级标签预算支付差额！",
                        Toast.LENGTH_SHORT).show();
                change(globe,my,offset,sign);
            } else if(tmp==-1) {
                double var=globe.getAllUseAble()+sign.getDiff(offset,HelperIO.INCOME);
                if(var<0) {
                    Toast.makeText(this,"余额不足！",Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(this)
                        .setTitle("提示：")
                        .setMessage("预算不足，是否使用空闲资金填补差值？")
                        .setNegativeButton("否", null)
                        .setPositiveButton("是", (dialogInterface, i) -> {
                                    globe.setAllUseAble(var);
                                    change(globe,my,offset,sign);
                                })
                        .create()
                        .show();
            } else if(tmp==-2) {
                double var;
                var=globe.getAllUseAble()+offset;
                if(var<0) {
                    Toast.makeText(this,"空闲资金不足！",Toast.LENGTH_SHORT).show();
                    return;
                }
                globe.setAllUseAble(var);
                change(globe,my,offset,sign);
            } else if(tmp==0) {
                change(globe,my,offset,sign);
            }
        } else {
            offset=mid.getAmount()-my.getPrice();
            if (offset+globe.getAllUseAble()<0) {
                Toast.makeText(this,"余额不足！",Toast.LENGTH_SHORT).show();
                return;
            }
            globe.setAllUseAble(globe.getAllUseAble()+offset);
            change(globe,my,offset,sign);
        }
    }

    private void change(MyApplication globe, DetailIO my, double offset, Sign sign) {
        String extra = ((MyInputExtra)findViewById(R.id.input_extra_main)).getExtra();
        int mainLabel=sign.getMainValue();
        int secondLabel=sign.getSecondValue();
        int io=my.getIo();
        int cal=my.getCalendar();

        if(my.getIo()==HelperIO.PAY)offset*=-1;

        globe.Update(io,offset,cal);

        HelperIO.UpdateDetail(my.getId(),offset+my.getPrice(),mainLabel,secondLabel,extra);

        HelperFile.Save(this,globe.getPayId(),globe.getIncomeId(),
                globe.getAllSaving(), globe.getAllUseAble());

        if(io==HelperIO.PAY) {
            HelperFile.Save(this,sign.getSignList(),HelperFile.BILLING);
        } else {
            HelperFile.Save(this,sign.getSignList(),HelperFile.BUDGET);
        }

        Toast.makeText(this, "存储成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
}