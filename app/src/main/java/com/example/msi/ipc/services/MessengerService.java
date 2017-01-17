package com.example.msi.ipc.services;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.msi.ipc.modle.MyContent;

/**
 * 文 件 名: MessengerService
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 17:48
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class MessengerService extends Service {
    private static final String TAG = "MessengerService";

    private Handler serverHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyContent.MSG_from_client: {
                    Log.i(TAG, "receive msg:" + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message message = Message.obtain(null,MyContent.MSG_from_server);
                    Bundle data = new Bundle();
                    data.putString("reply","收到！");
                    message.setData(data);
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default: {
                    super.handleMessage(msg);
                }
            }

        }
    };

    private Messenger messenger = new Messenger(serverHandler);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
