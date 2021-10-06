package com.hqk.http.okhttp;

public interface Chain2 {
    //责任链干的事情是封装Request，返回Response
    Request2 getRequest();
    Response2 getResponse(Request2 request2);
}
