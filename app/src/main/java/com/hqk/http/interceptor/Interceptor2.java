package com.hqk.http.interceptor;

import com.hqk.http.okhttp.Chain2;
import com.hqk.http.okhttp.Response2;

public interface Interceptor2 {
    Response2 doNext(Chain2 chain2);
}

