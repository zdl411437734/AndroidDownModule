package com.etongwl.androiddownmodule.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by 张东领 on 16/8/8.
 * 通知管理
 */
public class NotificationManager {

    private   android.app.NotificationManager mNotificationManager;
    HashMap<Integer, NotificationCompat.Builder> notificationHashMap = new HashMap<>();
    private Context mContext;

    public NotificationManager(Context mContext){
        this.mContext = mContext;
        mNotificationManager =
                (android.app.NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void finishedDownload(int notifyId) {
        notificationHashMap.get(notifyId).setContentText("Finished.");
        notificationHashMap.get(notifyId).setSmallIcon(android.R.drawable.stat_sys_download_done);

        mNotificationManager.notify(notifyId, notificationHashMap.get(notifyId).build());

    }

    public void updateNotification(String fileTitle, int progress, long totalBytes, int notifyId) {
        notificationHashMap.get(notifyId).setContentText(getBytesDownloaded(progress, totalBytes));
        notificationHashMap.get(notifyId).setContentTitle(progress + "% | " + fileTitle);
        notificationHashMap.get(notifyId).setProgress(100, progress, false);
        mNotificationManager.notify(notifyId, notificationHashMap.get(notifyId).build());
    }

    public void setNotification(String title, String content, int notifyId) {

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(mContext)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_sys_download);


        mNotificationManager.notify(
                notifyId,
                mNotifyBuilder.build());

        notificationHashMap.put(notifyId, mNotifyBuilder);
    }

    /**
     * 获取下时下载/总大小M 的字符串
     * @param progress
     * @param totalBytes
     * @return
     */
    public String getBytesDownloaded(int progress, long totalBytes) {
        //Greater than 1 MB
        long bytesCompleted = (progress * totalBytes) / 100;
        if (totalBytes >= 1000000) {
            return ("" + (String.format(Locale.ENGLISH, "%.1f",
                    (float) bytesCompleted / 1000000)) + "/" + (String.format(Locale.ENGLISH,
                    "%.1f", (float) totalBytes / 1000000)) + " MB");
        }
        if (totalBytes >= 1000) {
            return ("" + (String.format(Locale.ENGLISH, "%.1f",
                    (float) bytesCompleted / 1000)) + "/" + (String.format(Locale.ENGLISH,
                    "%.1f", (float) totalBytes / 1000)) + " Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
        }
    }
}
