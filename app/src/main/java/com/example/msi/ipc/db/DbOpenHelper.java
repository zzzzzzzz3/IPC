package com.example.msi.ipc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 文 件 名: DbOpenHelper
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 22:39
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book.db";
    private static final int DB_VERSION = 1;

    public static final String BOOK_TABLE = "book";
    private String CREATE_TABLE_BOOK = "CREATE TABLE IF NOT EXISTS "+BOOK_TABLE+"(_id INTEGER PRIMARY KEY,name TEXT)";

    public DbOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
