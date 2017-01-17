package com.example.msi.ipc;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 文 件 名: ContentProviderActivity
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 22:26
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class ContentProviderActivity extends AppCompatActivity {
    private static final String TAG = "ContentProviderActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentprovider);
        Uri uri = Uri.parse("content://com.msi.example.ipc/book");
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","android");
        getContentResolver().insert(uri,values);
        Cursor cursor = getContentResolver().query(uri,new String[]{"_id","name"},null,null,null);
        while (cursor.moveToNext()){
            Log.i(TAG,"query book:"+cursor.getString(1));
        }
        cursor.close();
    }
}
