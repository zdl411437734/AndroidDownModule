package com.etongwl.androiddownmodule;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 工具类
 * Created by jason on 16/8/8.
 */
public class Utils {
    private static final String DOWNLOAD_DIR = "download";

    /**
     * MD5加密
     * @param s
     * @return
     */
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取下载文件文件夹
     * @param context
     * @return
     */
    public static final File getDownloadDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), DOWNLOAD_DIR);
        }
        return new File(context.getCacheDir(), DOWNLOAD_DIR);
    }

    /**
     * 获取文件前缀(-文件名称)
     * @param fileName
     * @return
     */
    public static final String getPrefix(@NonNull String fileName) {
        if (fileName.contains(".")){
            return fileName.substring(0, fileName.lastIndexOf("."));
        }else{
            return fileName;
        }

    }

    /**
     * 获取文件后缀(文件后缀扩展名称)
     * @param fileName
     * @return
     */
    public static final String getSuffix(@NonNull String fileName) {
        if (fileName.contains(".")){
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }else{
            return "";
        }

    }

    //数字格式化,保留2位小数
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 获取文件完成文件M/总文件M字符串
     * @param finished
     * @param total
     * @return
     */
    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

    /**
     * 安装APK文件
     * @param context 上下文环境
     * @param file 安装文件
     */
    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载APK文件
     * @param context 上下文环境
     * @param packageName 包名
     */
    public static void unInstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取APK文件包名
     * @param context 上下文环境
     * @param apkFile 文件
     * @return
     */
    public static String getApkFilePackage(Context context, File apkFile) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
        if (info != null){
            return info.applicationInfo.packageName;
        }
        return null;
    }

    /**
     * 是否安装了app
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (!isEmpty(packages)) {
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数组是否为空
     * @param list
     * @return true是空 false不为空
     */
    public static final boolean isEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }

    public static final File getDefaultDownloadDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), DOWNLOAD_DIR);
        }
        return new File(context.getCacheDir(), DOWNLOAD_DIR);
    }

    /**
     * SD卡是否可以用
     * @return
     */
    public static boolean isSDMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 关闭
     * @param closeable
     * @throws IOException
     */
    public static final void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            synchronized (Utils.class) {
                closeable.close();
            }
        }
    }

    /**
     * 删除文件
     * @param save_dir_path
     */
    public static void deleteFile(String save_dir_path) {
        File file = new File(save_dir_path);
        if (file.exists()){
            file.delete();
        }
    }


    /**
     * 文件完整性校验
     * 文件MD5校验
     * @param md5 文件的md5
     * @param filePath 需要校验的文件全路径
     * @return
     */
    public static boolean fileVerifyByMd5(String md5,String filePath){
        boolean result = false;
        try {
            File file = new File(filePath);
            if (md5!=null&&file.exists()){
                String fileMd5 = getMd5ByFile(file);
                if (fileMd5!=null&&md5.equals(fileMd5)){
                        result = true;
                }
            }else{//文件不存在
                result = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 文件完整性校验
     * 文件MD5校验
     * @param md5 文件的md5
     * @param file 需要校验的文件
     * @return
     */
    public static boolean fileVerifyByMd5(String md5,File file){
        boolean result = false;
        try {
            if (md5!=null){
                String fileMd5 = getMd5ByFile(file);
                if (fileMd5!=null&&md5.equals(fileMd5)){
                        result = true;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 获取文件的MD5
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
