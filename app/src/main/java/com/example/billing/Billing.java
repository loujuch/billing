package com.example.billing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyInputDouble;
import com.example.billing.component.MyInputExtra;
import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;
import com.example.billing.model.DetailIO;

public class Billing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing);

        MyApplication globe=(MyApplication) getApplication();

        if(globe.getBilling()==null)globe.setBilling(new Sign(this,HelperFile.BILLING));
        Sign sign=globe.getBilling();
        sign.showMain(this, findViewById(R.id.first), findViewById(R.id.second),findViewById(R.id.show10),findViewById(R.id.show11));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        MyTitle title=findViewById(R.id.title_button);
        title.setText("记账");
        title.ChangeToSave();
        title.setOnClickListener(view -> oneBilling(globe,sign));
    }

    private void oneBilling(MyApplication globe, Sign sign) {
        MyInputDouble mid=findViewById(R.id.input_double_main);
        MyInputExtra mie=findViewById(R.id.input_extra_main);

        double price=mid.getAmount();
        String extra=mie.getExtra();
        int mainLabel=sign.getMainValue();
        int secondLabel=sign.getSecondValue();

        if(price==-1) {
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
            return;
        }

        globe.Update();

        int tmp= sign.setPrice(price, HelperIO.PAY);

        if (tmp==1) {
            Toast.makeText(this, "该二级标签预算不足，已使用上一级标签预算支付差额！", Toast.LENGTH_SHORT).show();
            end(globe, price, mainLabel, secondLabel, extra);
        } else if(tmp==-1) {
            double var=globe.getAllUseAble()+sign.getDiff(price,HelperIO.PAY);
            if(var<0) {
                Toast.makeText(this,"资金不足！",Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("提示：")
                    .setMessage("预算不足，是否使用空闲资金填补差值？")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", (dialogInterface, i) -> mid(sign,globe,price,mainLabel,secondLabel,extra))
                    .create()
                    .show();
        } else if(tmp==-2) {
            double var=globe.getAllUseAble()-price;
            if(var<0) {
                Toast.makeText(this,"空闲资金不足！",Toast.LENGTH_SHORT).show();
                return;
            }
            globe.setAllUseAble(var);
            end(globe,price,mainLabel,secondLabel,extra);
        } else if(tmp==0) {
            end(globe,price,mainLabel,secondLabel,extra);
        }
    }

    private void mid(Sign sign, MyApplication globe, double price, int mainLabel, int secondLabel, String extra) {
        double var = globe.getAllUseAble()+sign.getDiff(price,HelperIO.PAY);
        if (var < 0) {
            Toast.makeText(this, "空闲资金不足！", Toast.LENGTH_SHORT).show();
        } else {
            globe.setAllUseAble(var);
            sign.clear();
            end(globe,price,mainLabel,secondLabel,extra);
        }
    }

    private void end(MyApplication globe, double price, int mainLabel, int secondLabel, String extra) {

        DetailIO one = HelperIO.getOutputDetailIO(HelperIO.PAY, HelperIO.TIME);
        HelperIO.Save(one, price, mainLabel, secondLabel, extra);

        globe.Update(HelperIO.PAY,price);

        HelperFile.Save(this,globe.getPayId(),globe.getIncomeId(),globe.getAllSaving(),globe.getAllUseAble());
        HelperFile.Save(this,globe.getBilling().getSignList(),HelperFile.BILLING);

        Toast.makeText(this, "存储成功！", Toast.LENGTH_SHORT).show();
        reStart();
    }

    private void reStart() {
        Intent intent=getIntent();
        finish();
        startActivity(intent);
    }
}