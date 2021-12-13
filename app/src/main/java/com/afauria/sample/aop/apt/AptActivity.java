package com.afauria.sample.aop.apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.afauria.sample.aop.R;
import com.afauria.sample.apt_annotation.AptBindString;
import com.afauria.sample.apt_annotation.AptBindView;
import com.afauria.sample.apt_annotation.AptOnClick;
import com.afauria.sample.apt_library.AptBinder;

public class AptActivity extends AppCompatActivity {

    @AptBindString(R.string.app_name)
    String text1;

    @AptBindView(R.id.aptBtn1)
    Button btn1;

    @AptOnClick(R.id.aptBtn1)
    void onBtn1Click() {
        Log.e("AptActivity", "onBtn1Click: " + text1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt);
        //方式一：调用生成的类完成视图绑定、资源查找、事件绑定等功能
//        new AptActivity_ViewBinding(this);
        //方式二：封装API接口，供调用方使用
        AptBinder.bind(this);
    }
}