package com.prajna.dtboy.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.prajna.dtboy.http.cache.Builder;
import com.prajna.dtboy.http.cache.CacheSerializer;
import com.prajna.dtboy.http.cache.DualCache;
import com.prajna.dtboy.http.cache.StringSerializer;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 */
public class Req {
    //
    public static boolean DEBUG = false;
    public static String BASE_URL;
    public static String CONTENT_TYPE = "application/json";
    public static final String CHARSET = "UTF-8";
    public static AsyncHttpClient client;
    public static Gson gson;
    public static DualCache<String> _cache;
    public static IHTTPHook ihttpHook;
    public static String CACHE_KEY_PREFIX = "";

    // field
    private static void lazy(Context context) {
        gson = new Gson();
        CacheSerializer<String> stringSerializer = new StringSerializer();
        _cache = new Builder<String>("android-sex-http", 1)
                .useSerializerInRam(500 * 1024, stringSerializer)
                .useSerializerInDisk(10 * 1024 * 1024, true, stringSerializer, context)
                .build();
    }

    public static void debug(boolean debug) {
        DEBUG = debug;
    }

    public static void hook(IHTTPHook hook) {
        ihttpHook = hook;
    }

    public static void base(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static void prefix(String prefix) {
        CACHE_KEY_PREFIX = prefix;
    }

    public static void init(Context context) {
        client = new AsyncHttpClient(true, 80, 443);
        lazy(context);
    }

    public static void init(Context context, int http) {
        client = new AsyncHttpClient(true, http, 443);
        lazy(context);
    }

    public static void init(Context context, int http, int https) {
        client = new AsyncHttpClient(true, http, https);
        lazy(context);
    }

    //
    public String _url;
    public Context _context;
    public List<Header> _headers = new ArrayList<>();
    public String _contentType = CONTENT_TYPE;
    public Policy _cachePolicy = Policy.CacheAndRemote;
    //
    public Res _res;
    public Method _method = Method.GET;
    public HttpEntity _body;
    public HTTPHandler _handler;
    public boolean _base = true;

    /**
     * build the request 开始请求链
     *
     * @return
     */
    public static Req build() {
        Req req = new Req();
        if (ihttpHook != null) {
            if (null != ihttpHook.headers())
                req._headers.addAll(ihttpHook.headers());
        }
        req._handler = new HTTPHandler(req);
        return req;
    }

    public static Req build(Context context) {
        Req req = new Req();
        req._context = context;
        if (ihttpHook != null) {
            if (null != ihttpHook.headers())
                req._headers.addAll(ihttpHook.headers());
        }
        req._handler = new HTTPHandler(req);
        return req;
    }

    public static Req build(Context context, String url) {
        Req req = new Req();
        req._context = context;
        req._url = url;
        if (ihttpHook != null) {
            if (null != ihttpHook.headers())
                req._headers.addAll(ihttpHook.headers());
        }
        req._handler = new HTTPHandler(req);
        return req;
    }

    /**
     * set use base url 设置是否使用base url
     *
     * @param base
     * @return
     */
    public Req base(boolean base) {
        this._base = base;
        return this;
    }

    /**
     * set context
     *
     * @param context
     * @return
     */
    public Req context(Context context) {
        this._context = context;
        return this;
    }

    /**
     * set request url 设置请求url
     *
     * @param kvs
     * @return
     */

    public Req query(Map<String, Object> kvs) {
        if (_url != null) {
            _url += "?";
            int i = 0;
            for (Map.Entry<String, Object> entry : kvs.entrySet()) {
                if (i == 0) {
                    _url += String.format("%s=%s", entry.getKey(), entry.getValue().toString());
                } else {
                    _url += String.format("&%s=%s", entry.getKey(), entry.getValue().toString());
                }
                i++;
            }
        }
        return this;
    }

    public Req url(String url) {
        if (_base) {
            if (BASE_URL == null) {
                this._url = url;
            } else {
                this._url = String.format("%s%s", BASE_URL, url);
            }
        } else {
            this._url = url;
        }
        return this;
    }

    /**
     * set request body 设置请求体
     *
     * @param body
     * @return
     */
    public Req body(HttpEntity body) {
        this._body = body;
        return this;
    }

    public Req body(Object body) {
        this._body = new StringEntity(gson.toJson(body), CHARSET);
        return this;
    }

    public Req body(Map<String, Object> map) {
        String json = gson.toJson(map);
        this._body = new StringEntity(json, CHARSET);
        return this;
    }

    /**
     * append request headers 追加请求头
     *
     * @param headers
     * @return
     */
    public Req headers(Map<String, String> headers) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            Header h = new BasicHeader(header.getKey(), header.getValue());
            this._headers.add(h);
        }
        return this;
    }

    /**
     * append request headers with clear 追加请求头前是否情况请求头
     *
     * @param headers
     * @param clear
     * @return
     */
    public Req headers(Map<String, String> headers, boolean clear) {
        if (clear) {
            this._headers.clear();
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            Header h = new BasicHeader(header.getKey(), header.getValue());
            this._headers.add(h);
        }
        return this;
    }

    /**
     * append request header 添加一个请求头
     *
     * @param key
     * @param value
     * @return
     */
    public Req header(String key, String value) {
        this._headers.add(new BasicHeader(key, value));
        return this;
    }

    public Req header(String key, String value, boolean clear) {
        this._headers.clear();
        this._headers.add(new BasicHeader(key, value));
        return this;
    }

    /**
     * set content type 设置请求内容的类型 默认 json
     *
     * @param type
     * @return
     */
    public Req type(String type) {
        this._contentType = type;
        return this;
    }

    /**
     * set request method
     *
     * @param method
     * @return
     */
    public Req method(Method method) {
        this._method = method;
        return this;
    }

    public Req handler(HTTPHandler handler) {
        this._handler = handler;
        return this;
    }

    public Req res(Res res) {
        this._res = res;
        return this;
    }

    public Req policy(Policy policy) {
        this._cachePolicy = policy;
        return this;
    }

    public String key() {
        return MD5(CACHE_KEY_PREFIX + _url);
    }

    Header[] _headers_() {
        Header[] results = new Header[_headers.size()];
        for (int i = 0; i < _headers.size(); i++) {
            results[i] = _headers.get(i);
        }
        return results;
    }

    public void go() {
        // call pre hook
        if (ihttpHook != null) {
            ihttpHook.pre(_context);
        }
        if (_method == Method.GET) {
            if (_cachePolicy == Policy.NoCache || _cachePolicy == Policy.IgnoreCache) {
                if (isNetworkConnected(_context)) {
                    client.get(_context, _url, _headers_(), null, _handler);
                } else {
                    _res.disconnected(_context);
                }
            } else if (_cachePolicy == Policy.CacheAndRemote) {
                String c = _cache.get(key());
                if (c != null) {
                    _res.cache(c);
                    if (isNetworkConnected(_context)) {
                        client.get(_context, _url, _headers_(), null, _handler);
                    } else {
                        _res.disconnected(_context);
                    }
                } else {
                    if (isNetworkConnected(_context)) {
                        client.get(_context, _url, _headers_(), null, _handler);
                    } else {
                        _res.disconnected(_context);
                    }
                }
            } else if (_cachePolicy == Policy.CacheOnly) {
                String c = _cache.get(key());
                if (c != null) {
                    _res.cache(c);
                    if (!isNetworkConnected(_context)) {
                        _res.disconnected(_context);
                    } else {
                        if (ihttpHook != null) {
                            ihttpHook.post(_context);
                        }
                    }
                } else {
                    if (!isNetworkConnected(_context)) {
                        _res.disconnected(_context);
                    } else {
                        client.get(_context, _url, _headers_(), null, _handler);
                    }
                }

            } else if (_cachePolicy == Policy.CacheOrRemote) {
                String cache = _cache.get(key());
                if (isNetworkConnected(_context)) {
                    client.get(_context, _url, _headers_(), null, _handler);
                } else {
                    _res.cache(cache);
                    _res.disconnected(_context);
                }
            }
        } else {
            if (!isNetworkConnected(_context)) {
                _res.disconnected(_context);
                return;
            }
            switch (_method) {
                case POST:
                    client.post(_context, _url, _headers_(), _body, _contentType, _handler);
                    break;
                case PUT:
                    client.put(_context, _url, _headers_(), _body, _contentType, _handler);
                    break;
                case DELETE:
                    client.delete(_context, _url, _headers_(), _handler);
                    break;
            }
        }
    }

    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
