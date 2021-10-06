package com.hqk.http.interceptor;




import com.hqk.http.okhttp.CacheTemp;
import com.hqk.http.okhttp.Chain2;
import com.hqk.http.okhttp.HttpCodec;
import com.hqk.http.okhttp.Request2;
import com.hqk.http.okhttp.Response2;
import com.hqk.http.okhttp.ResponseBody;
import com.hqk.http.okhttp.SocketRequestServer;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;


public class CallServerInteceptor implements Interceptor2 {
    @Override
    public Response2 doNext(Chain2 chain2){

        Response2 response2 = new Response2();
        try {
            SocketRequestServer srs = new SocketRequestServer();

            Request2 request2 = chain2.getRequest();
            //http的创建socket直接new，而我们https的socket
//            Socket socket = new Socket(srs.getHost(request2), srs.getPort(request2));
            //https和http
            Socket socket = null;
            if(request2.getUrl().startsWith("https://")){
                //获得一个ssl上下文
                SSLContext sslContext = SSLContext.getInstance("TLS");
                //信任本机所有证书
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                //初始化证书
                trustManagerFactory.init((KeyStore) null);
                //信任证书设置
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                //证书管理器初始化
                sslContext.init(null, trustManagers, null);
                //由sslContext得到SSLSocket工厂
                SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                //创建socket
                socket = socketFactory.createSocket();
                socket.connect(new InetSocketAddress(srs.getHost(request2), srs.getPort(request2)));
            }else {
                socket = new Socket(srs.getHost(request2), srs.getPort(request2));
            }
            // todo 请求
            // output
            OutputStream os = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
            String requestAll = srs.getRequestHeaderAll(request2);
            // Log.d(TAG, "requestAll:" + requestAll);
            System.out.println("requestAll:" + requestAll);
            bufferedWriter.write(requestAll); // 给服务器发送请求 --- 请求头信息 所有的
            bufferedWriter.flush(); // 真正的写出去...
            // todo 响应
            //final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream is = socket.getInputStream();

            HttpCodec httpCodec = new HttpCodec();
            //读一行  响应行
            String responseLine = httpCodec.readLine(is);
            System.out.println("响应行：" + responseLine);

            //读响应头
            Map<String, String> headers = httpCodec.readHeaders(is);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            if (headers.containsKey("Location")) {//Location就代表这个请求是重定向的
                request2.setRedictUrl(headers.get("Location"));
            }
            //读响应体 ? 需要区分是以 Content-Length 还是以 Chunked分块编码
            if (headers.containsKey("Content-Length")) {
                int length = Integer.valueOf(headers.get("Content-Length"));
                byte[] bytes = httpCodec.readBytes(is, length);
                String content = new String(bytes);
                System.out.println("响应:" + content);
                if (request2.isCache()) {
                    CacheTemp.cacheMap.put(request2.getUrl(), content);
                }
                ResponseBody responseBody = new ResponseBody();
                //responseBody.setInputStream(is);
                responseBody.setContentLength(length);
                responseBody.setBytes(bytes);
                response2.setBody(responseBody);
                //response2.setBody(new ResponseBody().setBodyString(content.replaceAll("\\r\\n", "")));
                //mHandler.sendEmptyMessage(1);
            } else {
                //分块编码 chunk 分块返回数据，耗时返回的链接可以快速返回
                String response = httpCodec.readChunked(is);
                if (CacheTemp.isCache) {
                    CacheTemp.cacheMap.put(request2.getUrl(), response);
                }
                response2.setBody(new ResponseBody().setBodyString(response.replaceAll("\\r\\n", "")));
                System.out.println("响应:" + response);
            }
            is.close();
            socket.close();
            //response2 = chain2.getResponse(request2); // 执行下一个拦截器（任务节点）
        }catch (Exception e){
            e.printStackTrace();
        }

        // response2.setBody("流程走通....");
        return response2;
    }
}
