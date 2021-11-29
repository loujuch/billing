package com.example.billing;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.MyApplication;
import com.example.billing.helper.Sign;

public class signDelete extends AppCompatActivity {

    private Sign from;
    private Sign to;
    private boolean select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_delete);

        MyApplication globe = (MyApplication) getApplication();

        if (globe.getBilling() == null) globe.setBilling(new Sign(this, HelperFile.BILLING));
        if (globe.getBudget() == null) globe.setBudget(new Sign(this, HelperFile.BUDGET));
        Sign billing = globe.getBilling();
        Sign budget = globe.getBudget();
        Sign myBilling=new Sign(this,HelperFile.BILLING);
        Sign myBudget=new Sign(this,HelperFile.BUDGET);

        from = myBilling;
        to = billing;
        from.showMain(this, findViewById(R.id.first5), findViewById(R.id.first6));
        to.showMain(this,findViewById(R.id.first7), findViewById(R.id.first8));
        select = true;

        ((RadioGroup) findViewById(R.id.all3)).setOnCheckedChangeListener((radioGroup, i) -> {
            if (((RadioButton) findViewById(R.id.add3)).isChecked()) {
                from=myBilling;
                to = billing;
                select = true;
            } else {
                from = myBudget;
                to = budget;
                select = false;
            }
            from.showMain(signDelete.this, findViewById(R.id.first5), findViewById(R.id.first6));
            to.showMain(this,findViewById(R.id.first7), findViewById(R.id.first8));
        });

        MyTitle mt=findViewById(R.id.mt4);
        mt.setText("删除标签");
        mt.ChangeToSave("确定");
        mt.setOnClickListener(view -> delete(globe));
    }

    private void delete(MyApplication globe) {
        int fromMainValue=from.getMainValue();
        int fromSecondValue=from.getSecondValue();
        int toMainValue= to.getMainValue();
        int toSecondValue=to.getSecondValue();

        if(fromSecondValue==-1||toSecondValue==-1) {
            Toast.makeText(this, "请选择删除标签和接收标签！", Toast.LENGTH_SHORT).show();
            return;
        }

        if(fromMainValue==toMainValue&&fromSecondValue==toSecondValue) {
            Toast.makeText(this,"删除标签不可以和接收标签为同一标签！",Toast.LENGTH_SHORT).show();
            return;
        }

        to.setPrice(from.getPrice());

        int mv=from.getMain_value();
        int ms=from.getSecond_value();
        to.delete(mv,ms);

        HelperIO.UpdateDetailByLabel(fromMainValue,fromSecondValue,toMainValue,toSecondValue);

        if(select) {
            HelperFile.Save(this,to.getSignList(),HelperFile.BILLING);
            globe.setBilling(null);
        } else {
            HelperFile.Save(this,to.getSignList(),HelperFile.BUDGET);
            globe.setBudget(null);
        }
        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        finish();
    }
}