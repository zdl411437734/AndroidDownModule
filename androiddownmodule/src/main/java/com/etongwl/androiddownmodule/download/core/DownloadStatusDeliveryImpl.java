package com.etongwl.androiddownmodule.download.core;

import android.os.Handler;


import com.etongwl.androiddownmodule.download.CallBack;
import com.etongwl.androiddownmodule.download.DownloadException;
import com.etongwl.androiddownmodule.download.listener.DownloadStatus;
import com.etongwl.androiddownmodule.download.listener.DownloadStatusDelivery;

import java.util.concurrent.Executor;

/**
 *下载状态回调到Handler线程中
 */
public class DownloadStatusDeliveryImpl implements DownloadStatusDelivery {
    private Executor mDownloadStatusPoster;

    public DownloadStatusDeliveryImpl(final Handler handler) {
        mDownloadStatusPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void post(DownloadStatus status) {
        mDownloadStatusPoster.execute(new DownloadStatusDeliveryRunnable(status));
    }

    private static class DownloadStatusDeliveryRunnable implements Runnable {
        private final DownloadStatus mDownloadStatus;
        private final CallBack mCallBack;

        public DownloadStatusDeliveryRunnable(DownloadStatus downloadStatus) {
            this.mDownloadStatus = downloadStatus;
            this.mCallBack = mDownloadStatus.getCallBack();
        }

        @Override
        public void run() {
            switch (mDownloadStatus.getStatus()) {
                case DownloadStatus.STATUS_CONNECTING:
                    if (mCallBack!=null){
                        mCallBack.onConnecting();
                    }
                    break;
                case DownloadStatus.STATUS_CONNECTED:
                    if (mCallBack!=null){
                        mCallBack.onConnected(mDownloadStatus.getLength(), mDownloadStatus.isAcceptRanges());
                    }
                    break;
                case DownloadStatus.STATUS_PROGRESS:
                    if (mCallBack!=null){
                        mCallBack.onProgress(mDownloadStatus.getFinished(), mDownloadStatus.getLength(), mDownloadStatus.getPercent());
                    }
                    break;
                case DownloadStatus.STATUS_COMPLETED:
                    if (mCallBack!=null){
                        mCallBack.onCompleted();
                    }
                    break;
                case DownloadStatus.STATUS_PAUSED:
                    if (mCallBack!=null){
                        mCallBack.onDownloadPaused();
                    }
                    break;
                case DownloadStatus.STATUS_CANCELED:
                    if (mCallBack!=null){
                        mCallBack.onDownloadCanceled();
                    }
                    break;
                case DownloadStatus.STATUS_FAILED:
                    if (mCallBack!=null){
                        mCallBack.onFailed((DownloadException) mDownloadStatus.getException());
                    }
                    break;
            }
        }
    }
}
