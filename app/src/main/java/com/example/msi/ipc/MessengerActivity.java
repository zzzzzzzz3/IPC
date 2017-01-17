package com.example.msi.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.msi.ipc.modle.MyContent;
import com.example.msi.ipc.services.MessengerService;

/**
 * 文 件 名: MessengerActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 17:42
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class MessengerActivity extends AppCompatActivity {
    private static final String TAG = "MessengerActivity";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyContent.MSG_from_server:{
                    Log.i(TAG,"server msg:"+msg.getData().getString("reply"));
                    break;
                }
                default:{
                    super.handleMessage(msg);
                }
            }
        }
    };
    //client使用service传来的messenger给service发送消息
    private Messenger messenger ;
    //service使用client的messenger给client发送消息
    private Messenger replyMessenger = new Messenger(handler);

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            Message msg = Message.obtain(null,MyContent.MSG_from_client);
            Bundle data = new Bundle();
            data.putString("msg","hello");
            msg.setData(data);
            //client将自己的messenger传给service
            msg.replyTo = replyMessenger;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(MessengerActivity.this, MessengerService.class);
        bindService(intent,con, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(con);
        super.onDestroy();
    }
}
