package com.hqk.http.okhttp;

import java.io.IOException;

public interface Callback2 {


    public void onFailure(Call2 call, IOException e);


    public void onResponse(Call2 call, Response2 response);
}
