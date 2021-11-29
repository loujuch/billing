package com.example.billing;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyBottomButton;
import com.example.billing.component.MyShow;
import com.example.billing.component.MyTitle;
import com.example.billing.component.MyToShow;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((MyTitle)findViewById(R.id.title)).hideBack();

        ((MyShow)findViewById(R.id.show)).setText(Main.this);

        MyToShow to1=findViewById(R.id.to1);
        to1.setText("显示详情");
        to1.setOnClickListener(this,Detail.class);
        MyToShow to2=findViewById(R.id.to2);
        to2.setText("制定预算");
        to2.setOnClickListener(this,Budget.class);
        MyToShow to3=findViewById(R.id.to3);
        to3.setText("收支分析");
        to3.setOnClickListener(this,Analyze.class);

        MyBottomButton to=findViewById(R.id.to4);
        to.setText("+ 记账");
        to.setTo(this,Billing.class);

        Intent intent=new Intent(this,Billing.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((MyShow)findViewById(R.id.show)).setText(Main.this);
    }
}
