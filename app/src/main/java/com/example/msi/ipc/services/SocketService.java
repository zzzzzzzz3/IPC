package com.example.msi.ipc.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 文 件 名: SocketService
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 20:39
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class SocketService extends Service {
    private static final String TAG = "SocketService";

    private ServerSocket serverSocket = null;
    private boolean stop = false;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServiceWork()).start();
    }

    private class ServiceWork implements Runnable {

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8688);
                while (!stop) {
                    Socket client = serverSocket.accept();
                    if (client != null) {
                        Log.i(TAG, "a client connect");
                        reponsedClient(client);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reponsedClient(final Socket client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader in = null;
                PrintWriter out = null;
                try {
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                    out.println("welcome!");
                    while (!stop) {
                        String client_msg = in.readLine();
                        if (client_msg == null) {
                            break;
                        }
                        Log.i(TAG, "client msg:" + client_msg);
                        out.println("hello!");
                    }
                    Log.i(TAG, "a client quit...");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null)
                            in.close();
                        if (out != null)
                            out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        stop = true;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
