package com.etongwl.androiddownmodule.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.etongwl.androiddownmodule.DownLoadController;
import com.etongwl.androiddownmodule.DownLoadControllerListener;
import com.etongwl.androiddownmodule.L;
import com.etongwl.androiddownmodule.download.CallBack;
import com.etongwl.androiddownmodule.download.DownloadException;
import com.etongwl.androiddownmodule.download.DownloadRequest;

import java.io.File;

/**
 * 下载服务
 */
public class DownloadServiceControler extends Service {

    private static final String TAG = DownloadServiceControler.class.getSimpleName();

    public static final String ACTION_DOWNLOAD_BROAD_CAST = "com.etongwl.androiddownmodule:action_download_broad_cast";

    public static final String ACTION_DOWNLOAD = "com.etongwl.androiddownmodule:action_download";

    public static final String ACTION_PAUSE = "com.etongwl.androiddownmodule:action_pause";

    public static final String ACTION_CANCEL = "com.etongwl.androiddownmodule:action_cancel";

    public static final String ACTION_PAUSE_ALL = "com.etongwl.androiddownmodule:action_pause_all";

    public static final String ACTION_CANCEL_ALL = "com.etongwl.androiddownmodule:action_cancel_all";

    public static final String EXTRA_POSITION = "extra_position";

    public static final String EXTRA_TAG = "extra_tag";

    public static final String EXTRA_NAME = "extra_name";

    public static final String EXTRA_URL = "extra_url";

    public static final String EXTRA_STATUS = "extra_status";//状态


    /**
     *下载监听
     */
    public static DownLoadControllerListener downLoadControllerListener;

    /**
     * Dir: /Download
     */
    private File mDownloadDir;

    private DownLoadController mDownloadManager;


    public static void intentDownload(Context context, int position, String tag, String name,String url) {
        Intent intent = new Intent(context, DownloadServiceControler.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_URL, url);
        context.startService(intent);
    }

    public static void setDownLoadControllerListener(DownLoadControllerListener listener){
        downLoadControllerListener = listener;
    }

    public static void intentPause(Context context, String tag) {
        Intent intent = new Intent(context, DownloadServiceControler.class);
        intent.setAction(ACTION_PAUSE);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadServiceControler.class);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            String name = intent.getStringExtra(EXTRA_NAME);
            String url = intent.getStringExtra(EXTRA_URL);
            String tag = intent.getStringExtra(EXTRA_TAG);
            switch (action) {
                case ACTION_DOWNLOAD:
                    download(name,url, tag);
                    break;
                case ACTION_PAUSE:
                    pause(tag);
                    break;
                case ACTION_CANCEL:
                    cancel(tag);
                    break;
                case ACTION_PAUSE_ALL:
                    pauseAll();
                    break;
                case ACTION_CANCEL_ALL:
                    cancelAll();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download( String name,String url, String tag) {
        final DownloadRequest request = new DownloadRequest.Builder()
                .setTitle(name)
                .setUri(url)
                .setFolder(mDownloadDir)
                .build();
        if (downLoadControllerListener==null){//默认发送广播
            mDownloadManager.download(request, tag, new DownloadCallBack(name,url,this));
        }else{//自定义回到监听
            mDownloadManager.download(request, tag, downLoadControllerListener);
        }

    }

    private void pause(String tag) {
        mDownloadManager.pause(tag);
    }

    private void cancel(String tag) {
        mDownloadManager.cancel(tag);
    }

    private void pauseAll() {
        mDownloadManager.pauseAll();
    }

    private void cancelAll() {
        mDownloadManager.cancelAll();
    }

    public static class DownloadCallBack implements CallBack {
        private LocalBroadcastManager mLocalBroadcastManager;
        private String name;
        private String url;
        public DownloadCallBack(String name,String url, Context context) {
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
            this.name =name;
            this.url = url;
        }

        @Override
        public void onStarted() {
            L.i(TAG, "onStart()");
            sendBroadCast(ServiceDownStatus.STATUS_STARTED);
        }

        @Override
        public void onConnecting() {
            L.i(TAG, "onConnecting()");
            sendBroadCast(ServiceDownStatus.STATUS_CONNECTING);
        }

        @Override
        public void onConnected(long total, boolean isRangeSupport) {
            L.i(TAG, "onConnected()");
            sendBroadCast(ServiceDownStatus.STATUS_CONNECTED);
        }

        @Override
        public void onProgress(long finished, long total, int progress) {
            sendBroadCast(ServiceDownStatus.STATUS_PROGRESS);
        }

        @Override
        public void onCompleted() {
            L.i(TAG, "onCompleted()");
            sendBroadCast(ServiceDownStatus.STATUS_COMPLETE);
        }

        @Override
        public void onDownloadPaused() {
            L.i(TAG, "onDownloadPaused()");
            sendBroadCast(ServiceDownStatus.STATUS_PAUSED);
        }

        @Override
        public void onDownloadCanceled() {
            L.i(TAG, "onDownloadCanceled()");
            sendBroadCast(ServiceDownStatus.STATUS_CANCEL);
        }

        @Override
        public void onFailed(DownloadException e) {
            L.i(TAG, "onFailed()");
            e.printStackTrace();
            sendBroadCast(ServiceDownStatus.STATUS_FAILED);
        }



        private void sendBroadCast(ServiceDownStatus  status) {
            Intent intent = new Intent();
            intent.setAction(DownloadServiceControler.ACTION_DOWNLOAD_BROAD_CAST);
            intent.putExtra(EXTRA_STATUS, status);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = DownLoadController.getInstance(this);
        mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadManager.pauseAll();
    }


}
