package com.example.msi.ipc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 文 件 名: BundleActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 16:21
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BundleActivity extends AppCompatActivity {
    private static final String TAG = "BundleActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle);
        String msg = getIntent().getStringExtra("bundle");
        Log.i(TAG,msg);
    }
}
