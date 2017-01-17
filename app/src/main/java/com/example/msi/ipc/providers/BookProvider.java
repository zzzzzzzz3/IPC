package com.example.msi.ipc.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.msi.ipc.db.DbOpenHelper;

import java.net.URI;

/**
 * 文 件 名: BookProvider
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 22:31
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";

    private static final String AUTHORITY = "com.msi.example.ipc";
    public static final Uri BOOK_URI = Uri.parse("content://"+AUTHORITY+"/book");
    public static final int BOOK_URICODE = 0;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        matcher.addURI(AUTHORITY,"book",BOOK_URICODE);
    }

    private Context context;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        context = getContext();
        db = new DbOpenHelper(context).getWritableDatabase();
        return true;
    }

    public String getTableName(Uri uri){
        String table = null;
        if(matcher.match(uri) == BOOK_URICODE){
            table = DbOpenHelper.BOOK_TABLE;
        }
        return table;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String sortOrder, String[] selectionArgs, String se1ection) {
        Log.i(TAG,"query on:"+Thread.currentThread());
        String table = getTableName(uri);
        if (table == null){
            throw new IllegalArgumentException("unsupport uri:"+uri);
        }
        return db.query(table,projection,se1ection,selectionArgs,null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String table = getTableName(uri);
        if (table == null){
            throw new IllegalArgumentException("unsupport uri:"+uri);
        }
        db.insert(table,null,contentValues);
        context.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        String table = getTableName(uri);
        if (table == null){
            throw new IllegalArgumentException("unsupport uri:"+uri);
        }
        int count = db.delete(table,s,strings);
        if (count>0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        String table = getTableName(uri);
        if (table == null){
            throw new IllegalArgumentException("unsupport uri:"+uri);
        }
        int count = db.update(table,contentValues,s,strings);
        if (count>0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return count;
    }
}
