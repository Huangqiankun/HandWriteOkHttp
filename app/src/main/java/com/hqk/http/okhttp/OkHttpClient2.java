package com.hqk.http.okhttp;

import com.hqk.http.interceptor.Interceptor2;

import java.util.ArrayList;
import java.util.List;

public class OkHttpClient2 {
    //1、分发器
    //2、重试次数
    //3、自定义的拦截器数组
    //重试次数
    int recount;
    //分发器初始化
    Dispatcher2 dispatcher;
    List<Interceptor2> myInterceptors=new ArrayList<>();

    public OkHttpClient2(Builder builder) {
        this.recount = builder.recount;
        this.dispatcher = builder.dispatcher;
        this.myInterceptors = builder.myInterceptors;
    }
    public Dispatcher2 dispatcher(){
        return dispatcher;
    }

    public int getRecount() {
        return recount;
    }

    public Call2 newCall(Request2 request){
        return new RealCall2(this,request);
    }


    public static class Builder{
        List<Interceptor2> myInterceptors=new ArrayList<>();
        int recount = 3; // 重试次数
        //分发器初始化
        Dispatcher2 dispatcher;

        /**
         * 构造函数
         */
        public  Builder(){
            dispatcher = new Dispatcher2();
        }

        public Builder addInterceptor(Interceptor2 interceptor2){
            myInterceptors.add(interceptor2);
            return this;
        }
        public void setRecount(int recount) {
            this.recount = recount;
        }


        public OkHttpClient2 build(){
            return new OkHttpClient2(this);
        }
    }


    public List<Interceptor2> getMyInterceptors() {
        return myInterceptors;
    }
}

