package com.example.billing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.billing.component.MyTitle;
import com.example.billing.component.MyToShow;
import com.example.billing.helper.HelperFile;
import com.example.billing.helper.HelperIO;
import com.example.billing.helper.MyApplication;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        MyTitle title=findViewById(R.id.title);
        title.hideToSetting();
        title.setText("设置");

        MyToShow ts1=findViewById(R.id.ts1);
        ts1.setText("标签管理");
        ts1.setOnClickListener(view -> {
            Intent intent=new Intent(this,signManage.class);
            startActivity(intent);
        });

//        MyToShow ts2=findViewById(R.id.ts2);
//        ts2.setText("导入帐单");
//        ts2.setOnClickListener(view -> IfIn());

//        MyToShow ts3=findViewById(R.id.ts3);
//        ts3.setText("导出帐单");
//        ts3.setOnClickListener(view -> IfOut());

        MyToShow ts4=findViewById(R.id.ts4);
        ts4.setText("清空数据");
        ts4.setOnClickListener(view -> IfClear());
    }

    private void in() {
    }

    private void out () {
    }

    private void IfIn () {
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("是否导入数据？")
                .setNegativeButton("否", null)
                .setPositiveButton("是", (dialogInterface, i) -> in())
                .create()
                .show();
    }

    private void IfOut () {
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("是否导出数据？")
                .setNegativeButton("否", null)
                .setPositiveButton("是", (dialogInterface, i) -> out())
                .create()
                .show();
    }

    private void IfClear() {
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("是否清空数据？（该过程不可逆）")
                .setNegativeButton("否", null)
                .setPositiveButton("是", (dialogInterface, i) -> clear())
                .create()
                .show();
    }

    private void clear() {
        HelperFile.DeleteFile(HelperFile.BILLING);
        HelperFile.DeleteFile(HelperFile.ID);
        HelperFile.DeleteFile(HelperFile.BUDGET);
        HelperIO.DeleteDatabase();

        MyApplication globe=(MyApplication) getApplication();

        globe.init();

        Toast.makeText(this, "清空成功！", Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this,Main.class);
        startActivity(intent);
    }
}