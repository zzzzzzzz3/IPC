package com.example.msi.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.msi.ipc.modle.Book;
import com.example.msi.ipc.services.BookManagerService;

import java.util.List;

/**
 * 文 件 名: AidlActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 18:29
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class AidlActivity extends AppCompatActivity {
    private static final String TAG = "AidlActivity";
    private final int NEWBOOK_MSG = 0;
    private IBookManager bookManager = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == NEWBOOK_MSG){
                Log.i(TAG,"new book:"+((Book)msg.obj).name);
            }else {
                super.handleMessage(msg);
            }
        }
    };
    private OnNewBookArrivedListener listener = new OnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            //此方法运行在Binder线程池中需要用handler切换到ui线程
            handler.obtainMessage(NEWBOOK_MSG,book).sendToTarget();
        }
    };

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                bookManager.registerListener(listener);
                Book newbook = new Book(1,"android");
                bookManager.addBook(newbook);
                List<Book> books = bookManager.getBookList();
                Log.i(TAG,"book:"+books.size());

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
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(AidlActivity.this, BookManagerService.class);
        bindService(intent,con, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (bookManager != null && bookManager.asBinder().isBinderAlive()){
            try {
                bookManager.unregisterListener(listener);
                Log.i(TAG,"unregister listener success!");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(con);
        super.onDestroy();
    }
}
