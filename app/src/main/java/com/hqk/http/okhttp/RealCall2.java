package com.hqk.http.okhttp;

import com.hqk.http.interceptor.BridgeInterceptor;
import com.hqk.http.interceptor.CacheInterceptor;
import com.hqk.http.interceptor.CallServerInteceptor;
import com.hqk.http.interceptor.Interceptor2;
import com.hqk.http.interceptor.RetryAndFollowInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RealCall2 implements Call2{

    private OkHttpClient2 okHttpClient2;
    private Request2 request2;
    //包工头执行流程
    public OkHttpClient2 getOkHttpClient2(){
        return okHttpClient2;
    }

    public RealCall2(OkHttpClient2 okHttpClient2, Request2 request2) {
        this.okHttpClient2 = okHttpClient2;
        this.request2 = request2;
    }

    @Override
    public void enqueue(Callback2 callback2) {
        //准备要干事情的地方
        //分发出去
        okHttpClient2.dispatcher().enqueue(new AsyncCall2(callback2));
    }

    class AsyncCall2 implements Runnable{

        private Callback2 callback2;
        public AsyncCall2(Callback2 callback) {
            this.callback2 = callback;
        }

        Request2 getRequest(){
            return RealCall2.this.request2;
        }
        @Override
        public void run() {

            //这里才是真正开始干活的地方，就要吊起责任链
            //callback2返回结果，要么成功要么失败
            try {
                //1、得到责任链
                Response2 response2 = getResponseChain();
                callback2.onResponse(RealCall2.this, response2);
            }catch (Exception e){
                callback2.onFailure(RealCall2.this, new IOException("OKHTTP getResponseWithInterceptorChain 错误... e:" + e.toString()));
            }finally {
                okHttpClient2.dispatcher().finished(this);
            }

        }

        private Response2 getResponseChain() {
            //2、在链里面添加元素，即拦截器
            List<Interceptor2> interceptor2s = new ArrayList<>();
            interceptor2s.add(new BridgeInterceptor());
            interceptor2s.add(new RetryAndFollowInterceptor());
            interceptor2s.add(new CacheInterceptor());
            //这里添加自己的拦截器
            interceptor2s.addAll(okHttpClient2.getMyInterceptors());
            interceptor2s.add(new CallServerInteceptor());
            ChainManager chainManager = new ChainManager(0,RealCall2.this,request2,interceptor2s);

            return chainManager.getResponse(request2);
        }
    }
}
