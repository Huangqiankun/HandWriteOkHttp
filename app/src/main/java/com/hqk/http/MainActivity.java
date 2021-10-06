package com.hqk.http;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.hqk.http.okhttp.Call2;
import com.hqk.http.okhttp.Callback2;
import com.hqk.http.okhttp.OkHttpClient2;
import com.hqk.http.okhttp.Request2;
import com.hqk.http.okhttp.Response2;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String content = "";
    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(this);
        setContentView(R.layout.activity_main);
    }


    public void testNet(View view) {
        String PATH = "https://hb.yxg12.cn/list.php?page=111&size=333";
        //String PATH = "http://jz.yxg12.cn/list.php?page=111&size=333";
//        Request request = new Request.Builder().url(PATH).build();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                content = response.body().toString();
//                mHandler.sendEmptyMessage(1);
//            }
//        });

        /*
        * 1、要干什么事情（网络请求，Request 封装之后转成 Response ）
        * 2、依葫芦画瓢，先创造身体，复制一份，再注入灵魂
        * 3、创造Request 对象，再造 Response 对象
        * 4、流程图：分发器、责任链、拦截器
        * 5、分发器：执行队列、等待队列、线程池、逻辑判断、线程结束
        * 6、拦截器：对应拦截器的职责干什么 （专一，只做自己的事）
        * 7、责任链模式：肯定有一个chain接口和其他实现类，遵循对类隐藏，对接口暴露
        * 8、辅助类完成
        *
        * */
        Request2 request2 = new Request2.Builder().url(PATH).build();
        OkHttpClient2 okHttpClient = new OkHttpClient2.Builder().build();
        Call2 call = okHttpClient.newCall(request2);
        call.enqueue(new Callback2() {
            @Override
            public void onFailure(Call2 call, IOException e) {

            }

            @Override
            public void onResponse(Call2 call, Response2 response) {
                content = response.body().toString();
                mHandler.sendEmptyMessage(1);
            }
        });

    }

    public void downloadFile(View view) {
    }

    public void showNetImage(View view) {
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_LONG).show();
            }
        }
    };

    //然后通过一个函数来申请
    public static void requestPermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}