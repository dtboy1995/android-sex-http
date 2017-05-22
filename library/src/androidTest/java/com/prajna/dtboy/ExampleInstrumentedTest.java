package com.prajna.dtboy;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.prajna.dtboy.http.CachePolicy;
import com.prajna.dtboy.http.Method;
import com.prajna.dtboy.http.Request;
import com.prajna.dtboy.http.Response;
import com.prajna.dtboy.http.HTTPUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import cz.msebera.android.httpclient.Header;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        HTTPUtil.initHttpCache(appContext);
//
//        assertEquals("com.prajna.dtboy.test", appContext.getPackageName());
        Request
                .build()
                .setUrl("www.baidu.com")
                .setCachePolicy(CachePolicy.CacheAndRemote)
                .setContext(appContext)
                .setMethod(Method.GET)
                .setResponse(new Response<String>() {
                    @Override
                    public void ok(Header[] headers, String response) {

                    }

                    @Override
                    public void no(String error) {

                    }
                })
                .done();
    }
}
