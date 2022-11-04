package com.fast.library.http.download;

import android.os.AsyncTask;

import com.fast.library.http.HttpConfig;
import com.fast.library.http.OkHttpCallManager;
import com.fast.library.http.callback.DownloadCallBack;
import com.fast.library.utils.FileUtils;
import com.fast.library.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 说明：文件下载任务
 * @author xiaomi
 */
public class FileDownloadTask extends AsyncTask<Void,Long,Boolean>{

    private DownloadCallBack callBack;
    private String url;
    private File target;
    private OkHttpClient okHttpClient;
    //开始下载时间
    private long downStartTime;

    public FileDownloadTask(String url, File target, DownloadCallBack callBack) {
        this.url = url;
        this.target = target;
        this.callBack = callBack;
        this.okHttpClient = HttpConfig.get().getOkHttpClient();

        FileUtils.mkdirs(target.getParentFile());
        FileUtils.delete(target);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        downStartTime = System.currentTimeMillis();
        if (callBack != null){
            callBack.onStart();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //构造请求
        Request request = new Request.Builder().url(url).build();
        boolean success;
        try {
            Call call = okHttpClient.newCall(request);
            OkHttpCallManager.getInstance().addCall(url,call);
            Response response = call.execute();
            long total = response.body().contentLength();
            saveFile(response);
            success = (total == target.length());
        }catch (IOException e){
            if (HttpConfig.get().getDebug()){
                LogUtils.e(e);
            }
            success = false;
        }
        return success;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        if (callBack != null && values != null && values.length >=2){
            long sum = values[0];
            long total = values[1];

            float progress = (sum * 100.0f)/total;
            long totalTime = (System.currentTimeMillis() - downStartTime)/1000;
            if (totalTime == 0){
                totalTime += 1;
            }
            long networkSpeed = sum / totalTime;
            callBack.onProgress(progress,networkSpeed);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        OkHttpCallManager.getInstance().removeCall(url);
        if (aBoolean){
            if (callBack != null){
                callBack.onSuccess(target);
            }
        }else {
            if (callBack != null){
                callBack.onFailure();
            }
        }
    }

    public String saveFile(Response response) throws IOException{
        InputStream is = null;
        int len = 0;
        byte[] buf = new byte[1024*3];
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            long sum = 0;
            FileUtils.mkdirs(target.getParentFile());
            fos = new FileOutputStream(target);
            while ((len = is.read(buf)) != -1){
                sum += len;
                fos.write(buf,0,len);
                if (callBack != null){
                    publishProgress(sum,total);
                }
            }
            fos.flush();
            return target.getAbsolutePath();
        }finally {
            FileUtils.closeIO(is,fos);
        }
    }
}
