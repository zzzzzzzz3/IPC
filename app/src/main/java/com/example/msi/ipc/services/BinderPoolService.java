package com.example.msi.ipc.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.msi.ipc.modle.BinderPoolImpl;

/**
 * 文 件 名: BinderPoolService
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/19 00:13
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BinderPoolService extends Service {
    private static final String TAG = "BinderPoolService";
    private IBinder binder = new BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
