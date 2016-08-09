package com.etongwl.androiddownmodule.download.listener;


import com.etongwl.androiddownmodule.download.DownloadException;

/**
 *连接任务接口--主要获取下载前新浪 文件信息
 */
public interface ConnectTask extends Runnable {

    public interface OnConnectListener {
        void onConnecting();

        void onConnected(long time, long length, boolean isAcceptRanges);

        void onConnectCanceled();

        void onConnectFailed(DownloadException de);
    }

    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
