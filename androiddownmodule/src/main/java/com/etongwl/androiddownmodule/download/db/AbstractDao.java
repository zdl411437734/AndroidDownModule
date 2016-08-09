package com.etongwl.androiddownmodule.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库Dao抽象类
 */
public abstract class AbstractDao<T> {
    private DBOpenHelper mHelper;

    public AbstractDao(Context context) {
        mHelper = new DBOpenHelper(context);
    }

    protected SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    public void close() {
        mHelper.close();
    }
}
