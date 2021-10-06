package com.hqk.http.okhttp;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件下载工具类（单例模式）
 */

public class DownloadUtil2 {

    private static DownloadUtil2 downloadUtil;
    private final OkHttpClient2 okHttpClient;

    public static DownloadUtil2 get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil2();
        }
        return downloadUtil;
    }

    public DownloadUtil2() {
        okHttpClient = new OkHttpClient2.Builder().build();
    }


    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称，后面记得拼接后缀，否则手机没法识别文件类型
     * @param listener     下载监听
     */
    long lastTimes;
    public void download(final String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) {
        lastTimes = System.currentTimeMillis();
        Request2 request = new Request2.Builder()
                .url(url)
                .build();

        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback2() {
            @Override
            public void onFailure(Call2 call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call2 call, Response2 response) {
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                       // byteToFile(response.body().getBytes(),destFileDir,destFileName);

                        BufferedOutputStream bos = null;
                        FileOutputStream fos = null;
                        File file = null;
                        try {
                            File dir = new File(destFileDir);
                            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                                dir.mkdirs();
                            }
                            file = new File(destFileDir+File.separator+destFileName);
                            fos = new FileOutputStream(file);
                            bos = new BufferedOutputStream(fos);
                            bos.write(response.body().getBytes());
                            listener.onDownloadSuccess(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (bos != null) {
                                try {
                                    bos.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public interface OnDownloadListener{

        /**
         * 下载成功之后的文件
         */
        void onDownloadSuccess(File file);

        /**
         * 下载异常信息
         */

        void onDownloadFailed(Exception e);

    }
}