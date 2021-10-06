package com.hqk.http.okhttp;

import com.hqk.http.interceptor.Interceptor2;

import java.util.List;

public class ChainManager implements Chain2 {
    //链节点
    //一种是指定下一任领导，index
    private int index;//表示当前执行的链节点的角标
    private Call2 call;//表示整个责任链给谁用的
    private Request2 request2;//每个节点都是在组装Request2
    private List<Interceptor2> interceptors;//我的节点必须要实现Interceptor2，才是责任链里面的一员

    public Call2 getCall(){
        return call;
    }

    public ChainManager(int index, Call2 call, Request2 request2, List<Interceptor2> interceptors) {
        this.index = index;
        this.call = call;
        this.request2 = request2;
        this.interceptors = interceptors;
    }

    @Override
    public Request2 getRequest() {
        return request2;
    }

    @Override
    public Response2 getResponse(Request2 request2) {
        //就是讲Request进行封装，想办法返回Response
        if(interceptors==null || interceptors.isEmpty()){
            return new Response2();
        }
        if(index>=interceptors.size()){
            //如果index超过了size，就直接返回，不会走下一个节点了
            return new Response2();
        }
        Interceptor2 interceptor2 = interceptors.get(index);
        ChainManager manager = new ChainManager(index+1,call,request2,interceptors);
        Response2 response2 = interceptor2.doNext(manager);
        //责任链写完了
        return response2;
    }
}

