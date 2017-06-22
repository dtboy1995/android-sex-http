package dtboy.prajna.com.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.prajna.dtboy.http.FileRequest;
import com.prajna.dtboy.http.FileResponse;
import com.prajna.dtboy.http.HTTPUtil;
import com.prajna.dtboy.http.Request;
import com.prajna.dtboy.http.Response;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        test();
    }

    void test() {

        Request
                .build()
                .setUrl("")
                .setResponse(new Response() {
                    @Override
                    public void ok(Header[] headers, Object response) {

                    }

                    @Override
                    public void no(Header[] headers, String error) {

                    }
                })
                .done();

        File uploadFile = new File("existed_file");
        RequestParams params = new RequestParams();
        try {
            params.put("foo_key", uploadFile, "content_type");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileRequest
                .build()
                .setUrl("http://upload.com")
                .setParams(params)
                .setResponse(new FileResponse() {
                    @Override
                    public void ok() {

                    }

                    @Override
                    public void fail(Throwable throwable) {

                    }

                    @Override
                    public void progress(int percent) {

                    }
                })
                .upload();

        FileRequest
                .build()
                .setUrl("https://wenan-public.oss-cn-beijing.aliyuncs.com/vean-website/home/doctor_app.jpg")
                .setResponse(new FileResponse() {
                    @Override
                    public void ok() {
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(Throwable throwable) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void progress(int percent) {
                        Log.e("P", percent + "");
                    }
                })
                .download(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "foo_download" + File.separator + "1.png"));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netStateReceiver);
    }

    BroadcastReceiver netStateReceiver = new BroadcastReceiver() {
        long splitTime;

        @Override
        public void onReceive(Context context, Intent intent) {
            if ((System.currentTimeMillis() - splitTime) > 500) {
                if (HTTPUtil.isNetworkConnected(context)) {
                    Toast.makeText(context, "有网了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "没网了....", Toast.LENGTH_SHORT).show();
                }
                splitTime = System.currentTimeMillis();
            }

        }
    };
}
