package com.hqk.http.interceptor;


import com.hqk.http.okhttp.Chain2;
import com.hqk.http.okhttp.ChainManager;
import com.hqk.http.okhttp.Request2;
import com.hqk.http.okhttp.RequestBody2;
import com.hqk.http.okhttp.Response2;
import com.hqk.http.okhttp.SocketRequestServer;

import java.util.Map;

public class BridgeInterceptor implements Interceptor2 {
    @Override
    public Response2 doNext(Chain2 chain2){
        /**
         * Request封装
         */
        ChainManager chainManager = (ChainManager) chain2;
        Request2 request2 = chain2.getRequest();
        Map<String,String> mHeadList = request2.getmHeaderList();
        mHeadList.put("Host",new SocketRequestServer().getHost(chainManager.getRequest()));
        if("POST".equalsIgnoreCase(request2.getRequestMethod())){
            // 请求体   type lan
            /**
             * Content-Length: 48
             * Content-Type: application/x-www-form-urlencoded
             */
            mHeadList.put("Content-Length", request2.getRequestBody2().getBody().length()+"");
            mHeadList.put("Content-Type", RequestBody2.TYPE);
        }

        return chain2.getResponse(request2);
    }
}
