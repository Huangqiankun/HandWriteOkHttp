package com.hqk.http.okhttp;


public class Response2 {

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseBody body() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }

    //1、code
    //2、响应体
    private int statusCode;
    private ResponseBody body;
}
