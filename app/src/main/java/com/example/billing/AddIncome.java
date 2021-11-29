package com.example.billing;

import android.annotation.SuppressLint;
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

public class AddIncome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income);

        MyApplication globe=(MyApplication) getApplication();

        if(globe.getBudget()==null)globe.setBudget(new Sign(this, HelperFile.BUDGET));
        Sign budget=globe.getBudget();
        budget.showMain(this,findViewById(R.id.first1),findViewById(R.id.second1));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        MyTitle title=findViewById(R.id.my_title_1);
        title.setText("收入");
        title.ChangeToSave();
        title.setOnClickListener(view -> addIncome(globe,budget));
    }

    @SuppressLint("DefaultLocale")
    private void addIncome(MyApplication globe, Sign sign) {

        MyInputDouble mid=findViewById(R.id.input_double_main1);
        MyInputExtra mie=findViewById(R.id.input_extra_main1);

        double price=mid.getAmount();
        String extra=mie.getExtra();
        int mainLabel=sign.getMainValue();
        int secondLabel=sign.getSecondValue();

        if(price==-1) {
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
            return;
        }

        globe.setAllUseAble(globe.getAllUseAble()+price);

        globe.Update(HelperIO.INCOME,price);

//        sign.setPrice(price, HelperIO.INCOME);

        DetailIO one=HelperIO.getOutputDetailIO(HelperIO.INCOME,HelperIO.TIME);
        HelperIO.Save(one,price,mainLabel,secondLabel,extra);

        HelperFile.Save(this,globe.getPayId(),globe.getIncomeId(),globe.getAllSaving(),globe.getAllUseAble());

        Toast.makeText(this, String.format("存储成功，当前可支配资金为：%.2f",globe.getAllUseAble()), Toast.LENGTH_SHORT).show();
        finish();
    }
}