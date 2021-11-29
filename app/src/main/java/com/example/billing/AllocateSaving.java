package com.example.billing;

import android.annotation.SuppressLint;
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

public class AllocateSaving extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocate_saving);

        MyApplication globe=(MyApplication) getApplication();

        ((TextView)findViewById(R.id.show3)).setText(String.format("%.2f",globe.getAllSaving()));
        ((TextView)findViewById(R.id.show4)).setText(String.format("%.2f",globe.getAllUseAble()));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        MyTitle title=findViewById(R.id.title3);
        title.setText("储蓄");
        title.ChangeToSave();
        title.setOnClickListener(view -> changeSaving(globe));
    }

    private void changeSaving(MyApplication globe) {
        double price=((MyInputDouble)findViewById(R.id.input_double_main3)).getAmount();

        if(price==-1) {
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
            return;
        }

        ((MySelector)findViewById(R.id.selector1)).setText();
        if(!((MySelector)findViewById(R.id.selector1)).getCheck())price*=-1;
        double tmp=globe.getAllSaving()+price;
        double mid=globe.getAllUseAble()-price;
        if (tmp<0) {
            Toast.makeText(this, "储蓄不足！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mid<0) {
            Toast.makeText(this, "空闲资金不足！", Toast.LENGTH_SHORT).show();
            return;
        }
        globe.setAllSaving(tmp);
        globe.setAllUseAble(mid);
        HelperFile.Save(this,globe.getPayId(),globe.getIncomeId(),globe.getAllSaving(),globe.getAllUseAble());
        finish();
    }
}