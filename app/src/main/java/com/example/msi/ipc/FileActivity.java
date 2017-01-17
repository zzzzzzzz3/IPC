package com.example.msi.ipc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.msi.ipc.modle.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 文 件 名: FileActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 16:40
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class FileActivity extends AppCompatActivity {
    private static final String TAG = "FileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFileContent();
            }
        }).start();
    }

    private void getFileContent() {
        //此目录为根目录，使用其他目录会报错
        File file = new File(getExternalFilesDir(null).getPath()+"/test/test.txt");
        if (file.exists()){
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(file));
                User user = (User) in.readObject();
                Log.i(TAG,"user:"+user.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Log.i(TAG,"not found file");
        }
    }
}
