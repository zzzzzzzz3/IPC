package com.example.msi.ipc.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.msi.ipc.IBookManager;
import com.example.msi.ipc.OnNewBookArrivedListener;
import com.example.msi.ipc.modle.Book;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 文 件 名: BookManagerService
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 18:43
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";

    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<Book>();
    //使用此list可以在反序列化时取到相同的listener对象
    private RemoteCallbackList<OnNewBookArrivedListener> listeners = new RemoteCallbackList<OnNewBookArrivedListener>();
    //工作线程的开关
    private boolean stop = false;
    private IBinder bookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return books;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (!books.contains(book)) {
                books.add(book);
                Log.i(TAG, "add book success!");
                onNewBookArrived(book);
            } else {
                Log.i(TAG, "this book exists:" + book.name);
            }
        }

        @Override
        public void registerListener(OnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unregisterListener(OnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServiceSWork()).start();
    }

    @Override
    public void onDestroy() {
        stop = true;
        super.onDestroy();
    }

    private class ServiceSWork implements Runnable {

        @Override
        public void run() {
            while (!stop) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = new Random().nextInt(books.size() + 1);
                Book book = new Book(i, "newbook");
                books.add(book);
                Log.i(TAG,"new book");
                onNewBookArrived(book);
            }
        }
    }

    private void onNewBookArrived(Book book) {
        listeners.beginBroadcast();
        for (int i = 0; i < listeners.getRegisteredCallbackCount(); i++) {
            try {
                listeners.getBroadcastItem(i).onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        listeners.finishBroadcast();
    }
}
