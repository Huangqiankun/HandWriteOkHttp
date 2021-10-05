package com.hqk.http.okhttp;

public class OkHttpClient2 {


    public Call2 newCall(Request2 request2) {
        return new RealCall2();
    }

    public static class Builder {

        public Builder() {

        }


        public OkHttpClient2 build() {
            return new OkHttpClient2();
        }


    }
}
