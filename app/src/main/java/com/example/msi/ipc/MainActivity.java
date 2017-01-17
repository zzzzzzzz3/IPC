package com.example.msi.ipc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.msi.ipc.modle.User;
import com.example.msi.ipc.services.SocketService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_bundle;
    private Button btn_file;
    private Button btn_messenger;
    private Button btn_aidl;
    private Button btn_soket;
    private Button btn_contnetProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnClick();
    }

    private void initView() {
        btn_bundle = (Button) findViewById(R.id.btn_bundle);
        btn_file = (Button) findViewById(R.id.btn_file);
        btn_messenger = (Button) findViewById(R.id.btn_messenger);
        btn_aidl = (Button) findViewById(R.id.btn_aidl);
        btn_soket = (Button) findViewById(R.id.btn_socket);
        btn_contnetProvider = (Button) findViewById(R.id.btn_contentprovider);
    }

    private void setOnClick() {
        btn_bundle.setOnClickListener(this);
        btn_file.setOnClickListener(this);
        btn_messenger.setOnClickListener(this);
        btn_aidl.setOnClickListener(this);
        btn_soket.setOnClickListener(this);
        btn_contnetProvider.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_bundle){
            openBundleActivity();
        }else if(view == btn_file){
            openFileActivity();
        }else if(view == btn_messenger){
            openMessengerActivity();
        }else if(view == btn_aidl){
            openAidlActivity();
        }else if(view == btn_soket){
            openSocketActivity();
        }else if(view == btn_contnetProvider){
            openContentProviderActivity();
        }
    }

    private void openBundleActivity() {
        Intent intent = new Intent(MainActivity.this,BundleActivity.class);
        Bundle data = new Bundle();
        data.putString("bundle","welcome to bundleActivity");
        intent.putExtras(data);
        startActivity(intent);
    }

    private void openFileActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dirpath = getExternalFilesDir(null).getPath()+"/test";
                Log.i("MainActivity",dirpath);
                File dir = new File(dirpath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, "test.txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(file));
                    out.writeObject(new User("jack", 0, 1));
                    Log.i("MainActivity","save file success!");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Intent intent = new Intent(MainActivity.this,FileActivity.class);
        startActivity(intent);
    }

    private void openMessengerActivity() {
        Intent intent = new Intent(MainActivity.this,MessengerActivity.class);
        startActivity(intent);
    }

    private void openAidlActivity() {
        Intent intent = new Intent(MainActivity.this,AidlActivity.class);
        startActivity(intent);
    }

    private void openSocketActivity() {
        Intent intent = new Intent(MainActivity.this, SocketActivity.class);
        startActivity(intent);
    }

    private void openContentProviderActivity() {
        Intent intent = new Intent(MainActivity.this,ContentProviderActivity.class);
        startActivity(intent);
    }
}
