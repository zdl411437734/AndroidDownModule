package com.etongwl.androiddownmodule;

import com.etongwl.androiddownmodule.download.CallBack;
import com.etongwl.androiddownmodule.download.DownloadException;

import java.io.File;

/**
 * 控制器监听器
 * 实现回调监听
 */
public abstract class DownLoadControllerListener implements CallBack{
    @Override
    public void onCompleted() {

    }

    /**
     * 完成回调文件路径
     * @param filePath
     */
    public void onCompleted(String filePath){

    }

    /**
     *完成回调文件
     * @param file
     */
    public  void onCompleted(File file){

    }

    @Override
    public void onConnected(long total, boolean isRangeSupport) {

    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onDownloadCanceled() {

    }

    @Override
    public void onDownloadPaused() {

    }

    @Override
    public void onFailed(DownloadException e) {

    }

    @Override
    public void onProgress(long finished, long total, int progress) {

    }

    @Override
    public void onStarted() {

    }


}
