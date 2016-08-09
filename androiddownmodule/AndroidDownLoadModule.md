##Android下载模块
---------------
######概要
	android开发中我们需要使用到下载功能，无论是下载图片、文件还是自动更新。在下载中我们会遇到各种文件损坏、文件大小、SD卡管理等等问题，
	为了解决这些问题，我们需要些好多的逻辑判断，在github或者oschina上有好多的开源的下载功能，为什么还要写本模块呢？不是他们写的不好，
	笔者是处女座的，不想使用太多的第三方库文件，特别是有些项目中重复引用相同的包，给笔者造成好多修复性工作，为什么不能简简单单，
	不依赖或者尽可能少依赖其他jar包，让我们集成更简单容易，做成一个“砖”一样，简单无依赖！开开心心使用。
	
######支持功能和使用技术
	1. 文件下载
	2. 文件断点下载
	3. 文件多线程断点下载
	4. 数据库使用
	5. 文件完整性校验
	6. 下载完成清空数据库
	7. service使用--还在完善中...
	8. 通知栏使用
	9. 不支持多线程下载的-自动单线程下载
	10. SD卡管理--还在完善中
	11. 自动更新提示框
	12. 下载完成提示框
	
######使用指南
	兼容的版本：android4.0以上
	简单的通过配置文件完成各种基本配置
	开始使用步骤
		1.AndroidManifest.xml中添加权限
		   <uses-permission android:name="android.permission.INTERNET" />
           <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
           <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
		2. 添加androiddownloadmodule到app中
		3.  在Application中实例化
            private void initDownloader() {
                DownloadConfiguration configuration = new DownloadConfiguration();
                configuration.setMaxThreadNum(10);
                configuration.setThreadNum(3);
                DownloadManager.getInstance().init(getApplicationContext(), configuration);
            }
        4. 在需要使用的地方这么使用
            // first: build a DownloadRequest:
            final DownloadRequest request = new DownloadRequest.Builder()
                        .setTitle(appInfo.getName() + ".apk")
                        .setUri(appInfo.getUrl())
                        .setFolder(mDownloadDir)
                        .build();
            
            // download:
            // the tag here, you can simply use download uri as your tag;
            DownloadManager.getInstance().download(request, tag, new CallBack() {
                @Override
                public void onStarted() {
            
                }
            
                @Override
                public void onConnecting() {
            
                }
            
                @Override
                public void onConnected(long total, boolean isRangeSupport) {
            
                }
            
                @Override
                public void onProgress(long finished, long total, int progress) {
            
                }
            
                @Override
                public void onCompleted() {
            
                }
            
                @Override
                public void onDownloadPaused() {
            
                }
            
                @Override
                public void onDownloadCanceled() {
            
                }
            
                @Override
                public void onFailed(DownloadException e) {
            
                }
            });
            
            //pause
            DownloadManager.getInstance().pause(tag);
            
            //pause all
            DownloadManager.getInstance().pauseAll();
            
            //cancel
            DownloadManager.getInstance().cancel(tag);
            
            //cancel all
            DownloadManager.getInstance().cancelAll();
        5. 推荐使用DownLoadController类
           5.1. 封装了单例DownLoadController.getInstance(context);//推荐使用Application的Context
           5.2. 默认下载配置
            private void initDownloader(Context mContext) {
                   DownloadConfiguration configuration = new DownloadConfiguration();
                   configuration.setMaxThreadNum(10);
                   configuration.setThreadNum(3);
                   DownloadManager.getInstance().init(mContext, configuration);
               }
            自定义下载设置方法setDownloadConfig
            5.3. 封装了下载请求downloadRequest
            5.4. 封装了下载方法download
            5.5. 封装了pause pauseAll cancel cancelAll等方法
            5.6. 封装了下载回调DownLoadControllerListener类
        6. 文件完整性校验
            有些时候我们需要校验文件完整性,例如安装apk和打开pdf等等,我们需要知道文件是否下载完成,文件是否损坏,才有了本方法.
            通过服务器返回给的文件的md5串,和下载完成以后的文件的md5串比较,相同表示文件下载完成,没有损坏.
            Utils.fileVerifyByMd5方法比较,或者调用Utils.getMd5ByFile方法获取文件md5,自己处理比较
        7. 通知栏使用
            使用NotificationManager类管理通知栏,用户也可以根据自己需求定义通知栏,或者重写本包中NotificationManager部分方法.
        
        
		

######感谢
	感谢以下开源项目
	1. link:https://github.com/Aspsine/MultiThreadDownload
	2. link:https://github.com/AigeStudio/MultiThreadDownloader
	
	
######贡献
    如果你想贡献代码，你可以通过GitHub的分叉的仓库和发送pull请求。
	
######联系作者
	Github: https://github.com/zdl411437734
	邮箱：411437734@qq.com

######许可证
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
	
	
	
	

	