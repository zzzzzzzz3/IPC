package com.example.msi.ipc;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.msi.ipc.modle.BinderPool;
import com.example.msi.ipc.modle.BinderPoolImpl;

/**
 * 文 件 名: BinderPoolActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/18 23:58
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BinderPoolActivity extends AppCompatActivity {
    private static final String TAG = "BinderPoolActivity";
    private BinderPool binderPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binderpool);

        dowork();
    }

    private void dowork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //连接service和查询方法比较耗时需要在工作线程中开启
                binderPool = BinderPool.getINSTANCE(BinderPoolActivity.this);
                IBinder securityBinder = binderPool.queryBinder(BinderPoolImpl.SECURITY_CODE);
                ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
                String content = "android";
                String password = null;
                try {
                    password = securityCenter.encrypt(content);
                    Log.i(TAG,"password :"+password);
                    Log.i(TAG,"content :"+securityCenter.decrypt(password));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        unbindService(binderPool.getConnection());
        super.onDestroy();
    }
}
