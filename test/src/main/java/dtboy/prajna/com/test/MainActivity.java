package dtboy.prajna.com.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.prajna.dtboy.http.Body;
import com.prajna.dtboy.http.HTTPUtil;
import com.prajna.dtboy.http.IGlobalRequestHandler;
import com.prajna.dtboy.http.IGlobalResponseHandler;
import com.prajna.dtboy.http.Method;
import com.prajna.dtboy.http.Request;
import com.prajna.dtboy.http.Response;
import com.prajna.dtboy.http.ResponseRaw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        request();
    }

    void init() {
        // init the global cache
        HTTPUtil.initHttpCache(this);
        HTTPUtil.BASE_URL = "http://domain";
        HTTPUtil.globalResponseHandler = new IGlobalResponseHandler() {
            @Override
            public void disconnected(Context context) {
                Toast.makeText(context, "no networking!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(String response, Context context) {
                Toast.makeText(context, "error happened!", Toast.LENGTH_SHORT).show();
            }
        };
        HTTPUtil.globalRequestHandler = new IGlobalRequestHandler() {
            @Override
            public List<Header> addHeaders() {
                List<Header> headers = new ArrayList<>();
                headers.add(new BasicHeader("version", "1.0.2"));
                // add ...
                return headers;
            }
        };
    }

    void request() {
        Request
                .build()
                .setMethod(Method.GET)
                .setContext(this)
                .setUrl("/banners")
                .setResponse(new Response<List<Banner>>() {

                    @Override
                    public void ok(Header[] headers, List<Banner> response) {
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void no(String error) {

                    }
                })
                .done();

        Request
                .build()
                .setMethod(Method.POST)
                .addHeader("token", "foo_token")
                .setUrl("/foo")
                .setBody(Body.build().addKvs("username", "foo").addKvs("age", 25).done())
                .setResponse(new Response<Banner>() {

                    @Override
                    public void ok(Header[] headers, Banner response) {

                    }

                    @Override
                    public void no(String error) {

                    }
                })
                .done();
    }
}
