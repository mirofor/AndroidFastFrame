package com.fast.library.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 说明：带处理进度的请求体
 * @author xiaomi
 */
public class ProgressRequestBody extends RequestBody{
    //请求体
    private final RequestBody requestBody;
    //回调
    private final HttpTask httpTask;
    //包装完成
    private BufferedSink bufferedSink;
    //加载速度
    private long mPreviousTime;

    public ProgressRequestBody(RequestBody body,HttpTask httpTask){
        this.requestBody = body;
        this.httpTask = httpTask;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null){
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink){
        mPreviousTime = System.currentTimeMillis();
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;

                //回调
                if (httpTask!=null) {
                    //计算下载速度
                    long totalTime = (System.currentTimeMillis() - mPreviousTime)/1000;
                    if ( totalTime == 0 ) {
                        totalTime += 1;
                    }
                    long networkSpeed = bytesWritten / totalTime;
                    int progress = (int)(bytesWritten * 100 / contentLength);
                    boolean done = bytesWritten == contentLength;
                    httpTask.updateProgress(progress,networkSpeed,done?1:0);
                }
            }
        };
    }
}
