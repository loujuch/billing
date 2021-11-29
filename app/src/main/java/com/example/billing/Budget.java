package com.example.billing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.billing.component.MyBottomButton;
import com.example.billing.component.MyShow;
import com.example.billing.component.MyTitle;
import com.example.billing.component.MyToShow;

public class Budget extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget);

        ((MyTitle)findViewById(R.id.title1)).setText("制定预算");

        ((MyShow)findViewById(R.id.show2)).setText(Budget.this);

        MyToShow to5=findViewById(R.id.to5);
        to5.setText("增加收入");
        to5.setOnClickListener(this,AddIncome.class);

        MyToShow to6=findViewById(R.id.to6);
        to6.setText("分配预算");
        to6.setOnClickListener(this,AllocateBudget.class);

        MyToShow to8=findViewById(R.id.to8);
        to8.setText("分配储蓄");
        to8.setOnClickListener(this,AllocateSaving.class);

        MyBottomButton to7=findViewById(R.id.to7);
        to7.setText("+ 记账");
        to7.setTo(this,Billing.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((MyShow)findViewById(R.id.show2)).setText(Budget.this);
    }
}