package com.etongwl.androiddownmodule.download.db;

import android.content.Context;

import com.etongwl.androiddownmodule.entity.ThreadInfo;

import java.util.List;

/**
 * 数据库管理
 */
public class DataBaseManager {
    private static DataBaseManager sDataBaseManager;//
    private final ThreadInfoDao mThreadInfoDao;//线程类管理

    //单例
    public static DataBaseManager getInstance(Context context) {
        if (sDataBaseManager == null) {
            sDataBaseManager = new DataBaseManager(context);
        }
        return sDataBaseManager;
    }

    //数据库管理构造方法
    private DataBaseManager(Context context) {
        mThreadInfoDao = new ThreadInfoDao(context);
    }

    //插入方法
    public synchronized void insert(ThreadInfo threadInfo) {
        mThreadInfoDao.insert(threadInfo);
    }

    //删除方法
    public synchronized void delete(String tag) {
        mThreadInfoDao.delete(tag);
    }

    //更新方法
    public synchronized void update(String tag, int threadId, long finished) {
        mThreadInfoDao.update(tag, threadId, finished);
    }

    //获取信息方法
    public List<ThreadInfo> getThreadInfos(String tag) {
        return mThreadInfoDao.getThreadInfos(tag);
    }

    //判断是否存在方法
    public boolean exists(String tag, int threadId) {
        return mThreadInfoDao.exists(tag, threadId);
    }
}
