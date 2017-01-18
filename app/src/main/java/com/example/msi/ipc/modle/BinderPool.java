package com.example.msi.ipc.modle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.msi.ipc.IBinderPool;
import com.example.msi.ipc.services.BinderPoolService;

import java.util.concurrent.CountDownLatch;

/**
 * 文 件 名: BinderPool
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/19 00:15
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BinderPool {
    private static final String TAG = "BinderPool";

    private static BinderPool INSTANCE = null;
    private Context context;
    private IBinderPool binderPool = null;
    //count用于将连接service的方法变成同步方法
    private CountDownLatch count = null;

    private BinderPool(Context context){
        this.context = context;
        connectService();
    }
    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binderPool = IBinderPool.Stub.asInterface(iBinder);
            //设置死亡代理
            try {
                binderPool.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            count.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (binderPool != null){
                binderPool.asBinder().unlinkToDeath(deathRecipient,0);
                binderPool = null;
            }
            connectService();
        }
    };
    public ServiceConnection getConnection(){
        return con;
    }

    private void connectService() {
        count = new CountDownLatch(1);
        Intent intent = new Intent(context, BinderPoolService.class);
        context.bindService(intent,con,Context.BIND_AUTO_CREATE);
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static BinderPool getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (BinderPool.class){
                if (INSTANCE == null){
                    INSTANCE = new BinderPool(context);
                }
            }
        }
        return INSTANCE;
    }
    public IBinder queryBinder(int code){
        IBinder binder = null;
        try {
            binder = binderPool.queryBinder(code);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
}
