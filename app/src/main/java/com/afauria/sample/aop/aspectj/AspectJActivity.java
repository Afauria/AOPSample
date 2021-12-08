package com.afauria.sample.aop.aspectj;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afauria.sample.aop.R;
import com.afauria.sample.aop.aspectj.bindview.BindView;
import com.afauria.sample.aop.aspectj.checkPermission.CheckPermissions;
import com.afauria.sample.aop.aspectj.checklogin.CheckLogin;
import com.afauria.sample.aop.aspectj.throttle.ThrottleClick;
import com.afauria.sample.aop.aspectj.trace.DebugTrace;

public class AspectJActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.delayBtn)
    private View delayBtn;

    @BindView(R.id.exceptionBtn)
    private View exceptionBtn;

    @BindView(R.id.throwBtn)
    private View throwBtn;

    @BindView(R.id.routeBtn)
    private View routeBtn;

    @BindView(R.id.permissionBtn)
    private View permissionBtn;

    @BindView(R.id.argsBtn)
    private View argsBtn;

    @BindView(R.id.throttleBtn)
    private View throttleBtn;

    @DebugTrace
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspectj);
        delayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: ");
                testDebugTrace();
            }
        });
        exceptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCatchException();
            }
        });
        throwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTriggerThrow();
            }
        });
        routeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCheckLogin();
            }
        });
        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPermission();
            }
        });
        argsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testArgs("Hello World");
                testArgs(null);
            }
        });
        throttleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testThrottleClick();
            }
        });
    }

    @ThrottleClick(duration = 1000)
    private void testThrottleClick() {
        Log.e(TAG, "testThrottleClick: ");
    }

    @CheckPermissions({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void testPermission() {
        Log.e(TAG, "testPermission: 有权限");
        Toast.makeText(this, "已获取到权限", Toast.LENGTH_SHORT).show();
    }

    private void testArgs(String args) {
        Log.e(TAG, "testArgs: " + args);
    }

    @CheckLogin
    private void testCheckLogin() {
        Log.e(TAG, "testCheckLogin: ");
    }

    //handler异常捕获
    private void testCatchException() {
        try {
            String s = null;
            s.length();
        } catch (NullPointerException e) {

        }
        try {
            String s = null;
            s.length();
        } catch (Exception e) {

        }
    }

    //AfterThrowing捕获
    private void testTriggerThrow() {
        String s = null;
        s.length();
    }

    //统计方法耗时
    @DebugTrace
    private void testDebugTrace() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}