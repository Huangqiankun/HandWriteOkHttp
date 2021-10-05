package com.hqk.http.okhttp;

import java.io.InputStream;

public class ResponseBody {

    private InputStream inputStream;
    private String bodyString;
    private long contentLength;
    private byte[] bytes;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String string() {
        return bodyString;
    }

    public ResponseBody setBodyString(String bodyString) {
        this.bodyString = bodyString;
        return this;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
