package com.etongwl.androiddownmodule.download.listener;

/**
 * 下载状态回调
 */
public interface DownloadStatusDelivery {

    void post(DownloadStatus status);

}
