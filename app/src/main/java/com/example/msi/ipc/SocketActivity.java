package com.example.msi.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.msi.ipc.services.SocketService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 文 件 名: SocketActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 20:36
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class SocketActivity extends AppCompatActivity {
    private static final String TAG = "SocketActivity";
    private TextView tv_content;
    private Button btn_send;
    private EditText edit_msg;

    private Socket client = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private boolean stop = false;

    private final int MSG_SERVICE = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SERVICE: {
                    String service_msg = "server: " + msg.getData().getString("reply") + "\n";
                    tv_content.setText(tv_content.getText() + service_msg);
                }
                default: {
                    super.handleMessage(msg);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        initView();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edit_msg.getText().toString();
                if (msg != null && !TextUtils.isEmpty(msg)) {
                    sendMsg(msg);
                    edit_msg.setText("");
                    tv_content.setText(tv_content.getText().toString() + msg + "\n");
                }
            }
        });
        connectService();
    }

    private void connectService() {
        Intent intent = new Intent(SocketActivity.this, SocketService.class);
        startService(intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (client == null && !stop) {
                    try {
                        client = new Socket("localhost", 8688);
                        if (client != null) {
                            Log.i(TAG,"connect success!");
                            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                            if (in != null) {
                                receiveMsg(in);
                            }
                        }
                    } catch (IOException e) {
                        Log.i(TAG, "connect faild,retry...");
                        SystemClock.sleep(1000);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void receiveMsg(final BufferedReader in) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    try {
                        final String service_msg = in.readLine();
                        if (service_msg != null) {
                            Log.i(TAG, "service msg:" + service_msg);
                            Message msg = Message.obtain(null, MSG_SERVICE);
                            Bundle data = new Bundle();
                            data.putString("reply", service_msg);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void sendMsg(String msg) {
        if (out != null){
            out.println(msg);
        }
    }

    private void initView() {
        tv_content = (TextView) findViewById(R.id.tv_socket_ocntent);
        edit_msg = (EditText) findViewById(R.id.edit_socket_msg);
        btn_send = (Button) findViewById(R.id.btn_socket_send);
    }

    @Override
    protected void onDestroy() {
        stop = true;
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (client != null)
                client.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
