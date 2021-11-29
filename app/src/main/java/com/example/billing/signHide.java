package com.example.billing;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;

public class signHide extends AppCompatActivity {

    boolean select;
    Sign sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_hide);

        MyApplication globe=(MyApplication) getApplication();

        if(globe.getBilling()==null)globe.setBilling(new Sign(this, HelperFile.BILLING));
        if(globe.getBudget()==null)globe.setBudget(new Sign(this,HelperFile.BUDGET));
        Sign billing=globe.getBilling();
        Sign budget=globe.getBudget();

        sign=billing;
        billing.showMain(this,findViewById(R.id.first3),findViewById(R.id.first4));
        select=true;

        ((RadioGroup)findViewById(R.id.all1)).setOnCheckedChangeListener((radioGroup, i) -> {
            if(((RadioButton)findViewById(R.id.add1)).isChecked()) {
                sign=billing;
                select=true;
            } else {
                sign=budget;
                select=false;
            }
            sign.showMain(signHide.this,findViewById(R.id.first3),findViewById(R.id.first4));
        });

        MyTitle mt=findViewById(R.id.mt2);
        mt.setText("隐藏标签");
        mt.ChangeToSave("确定");
        mt.setOnClickListener(view -> hide(globe));
    }

    private void hide(MyApplication globe) {
        if(sign.changeType()) {
            if(select) {
                HelperFile.Save(this,sign.getSignList(),HelperFile.BILLING);
                globe.setBilling(null);
            }
            else {
                HelperFile.Save(this,sign.getSignList(),HelperFile.BUDGET);
                globe.setBudget(null);
            }
            Toast.makeText(this,"隐藏成功",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "请选择一个二级标签！", Toast.LENGTH_SHORT).show();
        }
    }
}