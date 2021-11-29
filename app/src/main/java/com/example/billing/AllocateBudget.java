package com.example.billing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyInputDouble;
import com.example.billing.component.MySelector;
import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;

public class AllocateBudget extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocate_budget);


        MyApplication globe=(MyApplication) getApplication();
        if(globe.getBilling()==null)globe.setBilling(new Sign(this, HelperFile.BILLING));
        Sign sign=globe.getBilling();
        sign.showMain(this,findViewById(R.id.first2),findViewById(R.id.second2),findViewById(R.id.show12),findViewById(R.id.show13));

        ((TextView)findViewById(R.id.show6)).setText(String.format("%.2f",globe.getAllUseAble()));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        MyTitle title=findViewById(R.id.title_button2);
        title.setText("分配预算");
        title.ChangeToSave();
        title.setOnClickListener(view -> changeBudget(globe,sign));
    }

    private void changeBudget(MyApplication globe, Sign sign) {
        MyInputDouble mid=findViewById(R.id.input_double_main2);
        double price=mid.getAmount();

        if(price==-1) {
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
            return;
        }

        ((MySelector)findViewById(R.id.selector0)).setText();
        if(!(((MySelector)findViewById(R.id.selector0)).getCheck()))price*=-1;
        double now=globe.getAllUseAble()-price;
        if(now<0) {
            Toast.makeText(this, "空余资金不足！", Toast.LENGTH_SHORT).show();
            return;
        }
        int tmp=sign.setPrice(price);
        if(tmp==-2) {
            Toast.makeText(this, "请选择要分配的标签！", Toast.LENGTH_SHORT).show();
            return;
        } else if(tmp==-1) {
            Toast.makeText(this, "该标签原预算不足！", Toast.LENGTH_SHORT).show();
            return;
        }
        globe.setAllUseAble(now);
        HelperFile.Save(this,globe.getPayId(),globe.getIncomeId(),globe.getAllSaving(),
                globe.getAllUseAble());
        HelperFile.Save(this,sign.getSignList(),HelperFile.BILLING);
        Toast.makeText(this,"分配成功！",Toast.LENGTH_SHORT).show();
        reStart();
    }

    private void reStart() {
        Intent intent=getIntent();
        finish();
        startActivity(intent);
    }
}