package com.etongwl.androiddownmodule.download.core;


import com.etongwl.androiddownmodule.download.DownloadInfo;
import com.etongwl.androiddownmodule.entity.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * 单任务下载
 */
public class SingleDownloadTask extends DownloadTaskImpl {

    public SingleDownloadTask(DownloadInfo mDownloadInfo, ThreadInfo mThreadInfo, OnDownloadListener mOnDownloadListener) {
        super(mDownloadInfo, mThreadInfo, mOnDownloadListener);
    }

    @Override
    protected void insertIntoDB(ThreadInfo info) {
        // don't support
    }

    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected void updateDB(ThreadInfo info) {
        // needn't Override this
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        // simply return null
        return null;
    }

    @Override
    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(0);
        return raf;
    }

    @Override
    protected String getTag() {
        return this.getClass().getSimpleName();
    }
}

