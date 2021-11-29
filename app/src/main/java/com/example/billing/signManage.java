package com.example.billing;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.component.MyToShow;

public class signManage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_manage);

        MyTitle title=findViewById(R.id.mt1);
        title.setText("管理标签");
        title.hideToSetting();

        MyToShow addSign=findViewById(R.id.mts1);
        addSign.setText("新建标签");
        addSign.setOnClickListener(view -> {
            Intent intent=new Intent(this,signAdd.class);
            startActivity(intent);
        });

        MyToShow deleteSign=findViewById(R.id.mts2);
        deleteSign.setText("删除标签");
        deleteSign.setOnClickListener(view -> {
            Intent intent=new Intent(this,signDelete.class);
            startActivity(intent);
        });

        MyToShow changeSign=findViewById(R.id.mts3);
        changeSign.setText("修改标签");
        changeSign.setOnClickListener(view -> {
            Intent intent=new Intent(this,signChange.class);
            startActivity(intent);
        });

        MyToShow hideSign=findViewById(R.id.mts4);
        hideSign.setText("隐藏标签");
        hideSign.setOnClickListener(view -> {
            Intent intent=new Intent(this,signHide.class);
            startActivity(intent);
        });

        MyToShow unHideSign=findViewById(R.id.mts5);
        unHideSign.setText("解隐藏标签");
        unHideSign.setOnClickListener(view -> {
            Intent intent=new Intent(this,signUnHide.class);
            startActivity(intent);
        });
    }
}