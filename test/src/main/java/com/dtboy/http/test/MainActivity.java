package com.dtboy.http.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.prajna.dtboy.http.IHTTPHook;
import com.prajna.dtboy.http.Method;
import com.prajna.dtboy.http.Pair;
import com.prajna.dtboy.http.Policy;
import com.prajna.dtboy.http.Req;
import com.prajna.dtboy.http.Res;
import com.prajna.dtboy.http.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    class Foo {
        String id;

        @Override
        public String toString() {
            return this.id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init
        Req.init(this);
        // Req.init(this,3000);
        // Req.init(this, 3000, 5000);
        Req.base("http://apistage.wenanle.com");
        Req.prefix("multi_users_system_id");
        Req.debug(true);
        Req.hook(new IHTTPHook() {
            @Override
            public void disconnected(Context context) {
                Utils.Logger.debug("network disconnected");
            }

            @Override
            public List<Header> headers() {
                List<Header> headers = new ArrayList<>();
                return headers;
            }

            @Override
            public void pre(Context context) {
                Utils.Logger.debug("Request start");
            }

            @Override
            public void post(Context context) {
                Utils.Logger.debug("Request end");
            }

            @Override
            public void fail(Header[] headers, String response, Context context) {
                Utils.Logger.debug(response + "");
            }
        });
        //  test cases

        simpleGET();
        //  stringGET();
        //  listGET();
        //  simplePOST();
        //  simplePUT();
        //  simpleDELETE();
    }

    class U {
        public String name;
    }

    void simplePUT() {
        U u = new U();
        u.name = "modified name";
        Req.build(this)
                .method(Method.PUT)
                .body(u)
                .base(false)
                .url("/doctors/58acec93a750150831fa830b/basic-profile")
                .res(new Res() {
                    @Override
                    public void ok(Header[] headers, Object response) {
                        Utils.Logger.debug("modify success");
                    }

                    @Override
                    public void no(Header[] headers, String error) {

                    }
                })
                .go();
    }

    void simpleDELETE() {
        Req.build(this)
                .base(false)
                .method(Method.DELETE)
                .url("http://apistage.wenanle.com/blood-pressure-records/58759c4ae2e8c2539e36b23e")
                .res(new Res() {
                    @Override
                    public void ok(Header[] headers, Object response) {
                        Utils.Logger.debug("delete success");
                    }

                    @Override
                    public void no(Header[] headers, String error) {
                    }
                })
                .go();
    }

    void simplePOST() {
        Req.build(this)
                .base(false)
                .method(Method.POST)
                .header("username", "uname")
                .header("password", "123456")
                .url("http://apistage.wenanle.com/login")
                .res(new Res<Foo>() {
                    @Override
                    public void ok(Header[] headers, Foo response) {
                        Utils.Logger.debug(response.toString());
                    }

                    @Override
                    public void no(Header[] headers, String error) {
                    }
                })
                .go();
    }

    void listGET() {
        Req.build().base(false).policy(Policy.NoCache).context(this).url("http://apistage.wenanle.com/banners")
                .res(new Res<List<Foo>>() {
                    @Override
                    public void ok(Header[] headers, List<Foo> response) {
                        Utils.Logger.debug(response.toString());
                    }

                    @Override
                    public void no(Header[] headers, String error) {
                    }

                }).go();
    }

    void stringGET() {
        Req.build().base(false).policy(Policy.NoCache).context(this).url("http://apistage.wenanle.com/doctors/58acec93a750150831fa830b/basic-profile")
                .res(new Res<String>() {
                    @Override
                    public void ok(Header[] headers, String response) {
                        Utils.Logger.debug(response.toString());
                    }

                    @Override
                    public void no(Header[] headers, String error) {
                    }

                }).go();
    }

    void simpleGET() {
        Req.build()
                .context(this)
                .url("/doctors/58acec93a750150831fa830b/basic-profile")
                .query(Pair.build().kvs("page", "1").kvs("size", 20).go())
                .res(new Res<Foo>() {
                    @Override
                    public void ok(Header[] headers, Foo response) {
                        Utils.Logger.debug(response.toString());
                    }

                    @Override
                    public void no(Header[] headers, String error) {
                    }

                }).go();
    }
}
