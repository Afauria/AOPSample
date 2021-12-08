package com.afauria.sample.aop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afauria.sample.aop.aspectj.AspectJActivity;
import com.afauria.sample.aop.viewinject.ViewInjectActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAspectJClick(View view) {
        startActivity(new Intent(this, AspectJActivity.class));
    }

    public void onViewInjectClick(View view) {
        startActivity(new Intent(this, ViewInjectActivity.class));
    }
}