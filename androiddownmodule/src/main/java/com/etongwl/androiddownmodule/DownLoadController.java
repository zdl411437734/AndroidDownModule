package com.etongwl.androiddownmodule;

import android.content.Context;

import com.etongwl.androiddownmodule.download.CallBack;
import com.etongwl.androiddownmodule.download.DownloadConfiguration;
import com.etongwl.androiddownmodule.download.DownloadException;
import com.etongwl.androiddownmodule.download.DownloadManager;
import com.etongwl.androiddownmodule.download.DownloadRequest;

import java.io.File;

/**
 * 下载控制器
 */
public class DownLoadController {

    private static Context mContext;

    private DownLoadController(Context mContext) {
        initDownloader(mContext);
    }

    /**
     * 实现了线程安全，又避免了同步带来的性能影响
     */
    private static class LazyHolder {
        private static final DownLoadController INSTANCE = new DownLoadController(mContext);
    }

    /**
     * 下载控制器单例
     *
     * @param mContext 建议传递Application的Context
     * @return
     */
    public DownLoadController getInstance(Context mContext) {
        this.mContext = mContext;
        return LazyHolder.INSTANCE;
    }

    private void initDownloader(Context mContext) {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(mContext, configuration);
    }


    /**
     * 配置下载配置
     *
     * @param configuration
     */
    public void setDownloadConfig(DownloadConfiguration configuration) {
        DownloadManager.getInstance().init(mContext, configuration);
    }

    /**
     * 下载请求构建
     *
     * @param fileName 文件名称带上后缀哦例如qxt.apk,myhead.png
     * @param url      下载路径
     * @param dirs     保存的文件夹
     * @return
     */
    public DownloadRequest downloadRequest(String fileName, String url, File dirs) {
        boolean result = false;
        if (!(dirs.exists() && dirs.isDirectory())) {
            result = dirs.mkdirs();
        } else {
            result = true;
        }
        if (result) {
            DownloadRequest request = new DownloadRequest.Builder()
                    .setTitle(fileName)
                    .setUri(url)
                    .setFolder(dirs)
                    .build();
            return request;
        } else {
            return null;
        }
    }

    /**
     * 下载请求构建
     *
     * @param fileName 文件名称带上后缀哦例如qxt.apk,myhead.png
     * @param url      下载路径
     * @param dir      保存的文件夹路径
     * @return
     */
    public DownloadRequest downloadRequest(String fileName, String url, String dir) {
        File dirs = new File(dir);
        boolean result = false;
        if (!(dirs.exists() && dirs.isDirectory())) {
            result = dirs.mkdirs();
        } else {
            result = true;
        }
        if (result) {
            DownloadRequest request = new DownloadRequest.Builder()
                    .setTitle(fileName)
                    .setUri(url)
                    .setFolder(dirs)
                    .build();
            return request;
        } else {
            return null;
        }

    }

    /**
     * 下载
     *
     * @param request
     * @param tag
     */
    public void download(final DownloadRequest request, String tag, final DownLoadControllerListener listener) {
        DownloadManager.getInstance().download(request, tag, new CallBack() {
            @Override
            public void onStarted() {
                if (listener!=null){
                    listener.onStarted();
                }
            }

            @Override
            public void onConnecting() {
                if (listener!=null){
                    listener.onConnecting();
                }
            }

            @Override
            public void onConnected(long total, boolean isRangeSupport) {
                if (listener!=null){
                    listener.onConnected(total,isRangeSupport);
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
                if (listener!=null){
                    listener.onProgress(finished,total,progress);
                }
            }

            @Override
            public void onCompleted() {
                if (listener!=null){
                    listener.onCompleted();
                    File file = new File(request.getFolder(),request.getTitle().toString());
                    if (file.exists()){
                        listener.onCompleted(file);
                        listener.onCompleted(file.getAbsolutePath());
                    }

                }
            }

            @Override
            public void onDownloadPaused() {
                if (listener!=null){
                    listener.onDownloadPaused();
                }
            }

            @Override
            public void onDownloadCanceled() {
                if (listener!=null){
                    listener.onDownloadCanceled();
                }
            }

            @Override
            public void onFailed(DownloadException e) {
                if (listener!=null){
                    listener.onFailed(e);
                }
            }
        });
    }

    /**
     * 下载--返回自己处理的Callback
     *
     * @param request
     * @param tag
     * @param callBack
     */
    public void download(DownloadRequest request, String tag, CallBack callBack) {
        DownloadManager.getInstance().download(request, tag, callBack);
    }

    public void pause(String tag) {
        //pause
        DownloadManager.getInstance().pause(tag);
    }


    public void pauseAll() {
        //pause all
        DownloadManager.getInstance().pauseAll();
    }


    public void cancel(String tag) {
        //cancel
        DownloadManager.getInstance().cancel(tag);
    }

    public void cancelAll() {
        //cancel all
        DownloadManager.getInstance().cancelAll();
    }

}
