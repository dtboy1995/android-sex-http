package dtboy.prajna.com.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.prajna.dtboy.http.CachePolicy;
import com.prajna.dtboy.http.HTTPMethod;
import com.prajna.dtboy.http.HTTPModel;
import com.prajna.dtboy.http.HTTPResult;
import com.prajna.dtboy.http.HTTPUtil;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HTTPUtil.initHttpCache(this);
        String s = "[{},{}]";
//        String s1 = HTTPUtil.gson.fromJson(s, String.class);
//        Log.e("sadf", s1);
        request();
    }

    void request() {
        HTTPModel
                .build()
                .setUrl("http://apistage.wenanle.com/banners")
                .setCachePolicy(CachePolicy.NoCache)
                .setContext(this)
                .setMethod(HTTPMethod.GET)
                .setResult(new HTTPResult<List<Banner>>() {
                    @Override
                    public void ok(Header[] headers, List<Banner> response) {
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void no(String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                })
                .done();
    }
}
