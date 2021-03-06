package org.ithot.android.transmit.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.ithot.android.serializerinterface.JSONSerializer;
import org.ithot.android.transmit.cache.Builder;
import org.ithot.android.transmit.cache.CacheSerializer;
import org.ithot.android.transmit.cache.DualCache;
import org.ithot.android.transmit.cache.StringSerializer;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * 对httpclient请求二次封装
 */
public class Req {

    // 是否开启调试模式
    static boolean DEBUG = false;
    // 请求的根域名
    private static String BASE_URL = "";
    // 请求的内容类型
    private static String CONTENT_TYPE = "application/json";
    // 请求的编码
    private static final String CHARSET = "UTF-8";
    // async http 实例
    static AsyncHttpClient client;
    // get请求缓存实例
    static DualCache<String> _cache;
    // http 生命周期回调函数
    static IHTTPHook ihttpHook;
    // 缓存key的前缀
    private static String CACHE_KEY_PREFIX = "";
    // 与序列化库解耦
    private static JSONSerializer _json;

    public static JSONSerializer json() {
        return _json;
    }

    /**
     * 懒加载 gson cache
     *
     * @param context
     */
    private static void lazy(Context context, JSONSerializer json) {
        _json = json;
        CacheSerializer<String> stringSerializer = new StringSerializer();
        _cache = new Builder<String>("android-sex-http", 1)
                .useSerializerInRam(500 * 1024, stringSerializer)
                .useSerializerInDisk(10 * 1024 * 1024, true, stringSerializer, context)
                .build();
    }

    /**
     * 设置是否开启调试模式
     *
     * @param debug
     */
    public static void debug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 设置http生命周期回调函数
     *
     * @param hook
     */
    public static void hook(IHTTPHook hook) {
        ihttpHook = hook;
    }

    /**
     * 设置根域名
     *
     * @param baseUrl 例：http://foo.com
     */
    public static void base(String baseUrl) {
        BASE_URL = baseUrl;
    }

    /**
     * 设置get请求缓存key的前缀
     *
     * @param prefix
     */
    public static void prefix(String prefix) {
        CACHE_KEY_PREFIX = prefix;
    }

    /**
     * 初始化 android sex http
     *
     * @param context
     */
    public static void init(Context context, JSONSerializer json) {
        client = new AsyncHttpClient(true, 80, 443);
        lazy(context, json);
    }

    /**
     * 初始化 android sex http
     *
     * @param context
     * @param http    http端口号
     */
    public static void init(Context context, int http, JSONSerializer json) {
        client = new AsyncHttpClient(true, http, 443);
        lazy(context, json);
    }

    /**
     * 初始化 android sex http
     *
     * @param context
     * @param http    http端口号
     * @param https   https端口号
     */
    public static void init(Context context, int http, int https, JSONSerializer json) {
        client = new AsyncHttpClient(true, http, https);
        lazy(context, json);
    }

    /**
     * 取消某个context下的所有请求 建议onDestroy()中调用
     *
     * @param context
     */
    public static void cancel(Context context) {
        client.cancelRequests(context, true);
    }

    /**
     * 取消全部请求 建议关闭应用的时候可以调用
     */
    public static void cancelAll() {
        client.cancelAllRequests(true);
    }

    // 请求url
    private String _url;
    // Activity的引用 为了取消请求和回调入UI线程
    Context _context;
    // 请求头
    private List<Header> _headers = new ArrayList<>();
    // 请求的内容类型 默认为json
    private String _contentType = CONTENT_TYPE;
    // get请求的缓存策略 默认为缓存远程策略
    Policy _cachePolicy = Policy.CacheAndRemote;
    // 响应
    Res _res;
    // 请求方法 默认为get
    Method _method = Method.GET;
    // 请求体
    private HttpEntity _body;
    // 内部用的处理类
    private HTTPHandler _handler;
    // 是否使用跟路由
    private boolean _base = true;
    // 真正的请求引用
    private RequestHandle request;
    // 该请求是否使用缓存前缀
    private boolean _prefix = true;

    /**
     * 调用此方法开启请求链
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

    /**
     * 调用此方法开启请求链
     *
     * @param context Activity引用
     * @return
     */
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

    /**
     * 调用此方法开启请求链
     *
     * @param context Activity引用
     * @param url     请求url
     * @return
     */
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
     * 是否使用根域名
     *
     * @param base
     * @return
     */
    public Req base(boolean base) {
        this._base = base;
        return this;
    }

    /**
     * 设置Activity 引用
     *
     * @param context
     * @return
     */
    public Req context(Context context) {
        this._context = context;
        return this;
    }

    /**
     * 设置query参数 ?page=1&size=10
     *
     * @param kvs 可以使用Pair.build().kvs().go()去构建query
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

    /**
     * 设置请求url
     *
     * @param url
     * @return
     */
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
     * 设置请求体
     *
     * @param body
     * @return
     */
    public Req body(HttpEntity body) {
        this._body = body;
        return this;
    }

    /**
     * 设置请求体
     *
     * @param body Java Bean
     * @return
     */
    public Req body(Object body) {
        this._body = new StringEntity(json().stringify(body), CHARSET);
        return this;
    }

    /**
     * 设置请求体
     *
     * @param map 可以使用Pair.build().kvs().go()去构建body
     * @return
     */
    public Req body(Map<String, Object> map) {
        String json = json().stringify(map);
        this._body = new StringEntity(json, CHARSET);
        return this;
    }

    /**
     * 追加若干请求头
     *
     * @param headers 可以使用Pair.build().kvs().go()去构建body
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
     * 追加若干请求头
     *
     * @param headers 可以使用Pair.build().kvs().go()去构建body
     * @param clear   设置true 追加前清空所有请求头
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
     * 追加一个请求头
     *
     * @param key
     * @param value
     * @return
     */
    public Req header(String key, String value) {
        this._headers.add(new BasicHeader(key, value));
        return this;
    }

    /**
     * 追加一个请求头
     *
     * @param key
     * @param value
     * @param clear 设置true 追加前清空所有请求头
     * @return
     */
    public Req header(String key, String value, boolean clear) {
        this._headers.clear();
        this._headers.add(new BasicHeader(key, value));
        return this;
    }

    /**
     * 设置请求内容的类型 默认 json
     *
     * @param type application/json?
     * @return
     */
    public Req type(String type) {
        this._contentType = type;
        return this;
    }

    /**
     * 设置请求方法
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

    /**
     * 设置响应 可以设置泛型类型
     *
     * @param res
     * @return
     */
    public Req res(Res res) {
        this._res = res;
        return this;
    }

    /**
     * 取消这个请求
     */
    public void cancel() {
        if (this.request != null) {
            request.cancel(true);
        }
    }

    /**
     * 设置get请求缓存策略
     *
     * @param policy
     * @return
     */
    public Req policy(Policy policy) {
        this._cachePolicy = policy;
        return this;
    }

    /**
     * 缓存时是否使用缓存前缀
     *
     * @param prefix
     * @return
     */
    public Req prefix(boolean prefix) {
        this._prefix = prefix;
        return this;
    }

    /**
     * 得到缓存需要的key
     *
     * @return
     */
    public String key() {
        if (this._prefix)
            return md5(CACHE_KEY_PREFIX + _url);
        else
            return md5(_url);
    }

    Header[] _headers_() {
        Header[] results = new Header[_headers.size()];
        for (int i = 0; i < _headers.size(); i++) {
            results[i] = _headers.get(i);
        }
        return results;
    }

    /**
     * 发送请求
     */
    public void go() {
        // call pre hook
        if (ihttpHook != null) {
            ihttpHook.pre(_context);
        }
        if (_method == Method.GET) {
            if (_cachePolicy == Policy.NoCache || _cachePolicy == Policy.IgnoreCache) {
                if (isNetworkConnected(_context)) {
                    request = client.get(_context, _url, _headers_(), null, _handler);
                } else {
                    _res.disconnected(_context);
                }
            } else if (_cachePolicy == Policy.CacheAndRemote) {
                String c = _cache.get(key());
                if (c != null) {
                    _res.cache(c);
                    if (isNetworkConnected(_context)) {
                        request = client.get(_context, _url, _headers_(), null, _handler);
                    } else {
                        _res.disconnected(_context);
                    }
                } else {
                    if (isNetworkConnected(_context)) {
                        request = client.get(_context, _url, _headers_(), null, _handler);
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
                        request = client.get(_context, _url, _headers_(), null, _handler);
                    }
                }

            } else if (_cachePolicy == Policy.CacheOrRemote) {
                String cache = _cache.get(key());
                if (isNetworkConnected(_context)) {
                    request = client.get(_context, _url, _headers_(), null, _handler);
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
                    request = client.post(_context, _url, _headers_(), _body, _contentType, _handler);
                    break;
                case PUT:
                    request = client.put(_context, _url, _headers_(), _body, _contentType, _handler);
                    break;
                case DELETE:
                    request = client.delete(_context, _url, _headers_(), _handler);
                    break;
            }
        }
    }

    public static String md5(String s) {
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

    private static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // new feature

    private RequestParams _params;
    private FileRes _fileRes;

    private Req params(RequestParams _params_) {
        this._params = _params_;
        return this;
    }

    public Req res(FileRes fileRes) {
        _fileRes = fileRes;
        return this;
    }

    public void download(File file) {
        download(file, false);
    }

    public void download(File file, boolean ugly) {

        if (!isNetworkConnected(_context)) {
            _fileRes.disconnected(_context);
            return;
        }

        FileAsyncHttpResponseHandler handler;
        if (ugly) {
            handler = new FileAsyncHttpResponseHandler(file) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    _fileRes.undone();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    _fileRes.done(file);
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    _fileRes.progress(Utils.progress(bytesWritten, totalSize));
                }
            };
        } else {
            handler = new RangeFileAsyncHttpResponseHandler(file) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    _fileRes.undone();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    _fileRes.done(file);
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    _fileRes.progress(Utils.progress(bytesWritten, totalSize));
                }
            };
        }

        client.get(_context, _url, _headers_(), _params, handler);
    }

}
