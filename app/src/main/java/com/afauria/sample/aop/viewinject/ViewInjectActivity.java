package com.afauria.sample.aop.viewinject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afauria.sample.aop.R;

public class ViewInjectActivity extends AppCompatActivity {
    @ViewInject(R.id.viewInjectBtn1)
    private Button mBtn1;

    @ViewInject(R.id.viewInjectBtn2)
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inject);
        //运行时反射解析注解，给变量赋值
        ViewInjectUtil.injectViews(this);
        mBtn1.setOnClickListener(v -> Toast.makeText(ViewInjectActivity.this, "按钮1", Toast.LENGTH_SHORT).show());
        mBtn2.setOnClickListener(v -> Toast.makeText(ViewInjectActivity.this, "按钮2", Toast.LENGTH_SHORT).show());
    }
}