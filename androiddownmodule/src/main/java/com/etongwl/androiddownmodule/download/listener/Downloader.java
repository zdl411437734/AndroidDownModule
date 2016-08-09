package com.etongwl.androiddownmodule.download.listener;

/**
 * 下载
 */
public interface Downloader {

    /**
     * 文件破坏监听
     */
    public interface OnDownloaderDestroyedListener {
        //破坏
        void onDestroyed(String key, Downloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();

}
