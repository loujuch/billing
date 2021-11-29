package com.example.billing;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;

public class signChange extends AppCompatActivity {

    private Sign sign;
    private boolean select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_change);

        MyApplication globe = (MyApplication) getApplication();

        if (globe.getBilling() == null) globe.setBilling(new Sign(this, HelperFile.BILLING));
        if (globe.getBudget() == null) globe.setBudget(new Sign(this, HelperFile.BUDGET));
        Sign billing = globe.getBilling();
        Sign budget = globe.getBudget();

        sign = billing;
        billing.showMain(this, findViewById(R.id.first5), findViewById(R.id.first6));
        select = true;

        ((RadioGroup) findViewById(R.id.all3)).setOnCheckedChangeListener((radioGroup, i) -> {
            if (((RadioButton) findViewById(R.id.add3)).isChecked()) {
                sign = billing;
                select = true;
            } else {
                sign = budget;
                select = false;
            }
            sign.showMain(signChange.this, findViewById(R.id.first5), findViewById(R.id.first6));
        });


        MyTitle mt=findViewById(R.id.mt3);
        mt.setText("更改标签");
        mt.ChangeToSave("确定");
        mt.setOnClickListener(view -> change(globe));
    }

    private void change(MyApplication globe) {
        String mid=((EditText)findViewById(R.id.et3)).getText().toString();

        if(mid.equals("")) {
            Toast.makeText(this, "请填入新名字", Toast.LENGTH_SHORT).show();
            return;
        }

        int len=mid.length();
        if(len>4) {
            Toast.makeText(this, "新名字应该在四字符内", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0;i<len;++i) {
            char c=mid.charAt(i);
            if(c=='<'||c=='>'||c=='\\'||c=='/') {
                Toast.makeText(this, "新名字不应有<,>,/和\\", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(len>2)mid=mid.substring(0,2)+"<br/>"+mid.substring(2);

        if(sign.setText(mid)) {
            if(select) {
                HelperFile.Save(this,sign.getSignList(),HelperFile.BILLING);
                globe.setBilling(null);
            }
            else {
                HelperFile.Save(this,sign.getSignList(),HelperFile.BUDGET);
                globe.setBudget(null);
            }
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "请选择一个二级标签！", Toast.LENGTH_SHORT).show();
        }
    }
}