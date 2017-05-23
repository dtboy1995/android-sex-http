# ![android-sex-http](static/logo1.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
A sex http library, Simple and convenient, support many cache mechanism, simplify request most

## Translations
[中文](README_CN.md)

# useful if you
- The GET request requires processing in different cases, and the request results are quickly converted to Object
- Your server is based on JSON and follows the REST specification

# install
```java
//Add it in your root build.gradle at the end of repositories:
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
//Add it in your module build.gradle
dependencies {
  compile 'com.github.dtboy1995:android-sex-http:0.0.2'
}
// if compile has errors
android {
  packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }
}
```

# global setting
```java
// execute once
// init cache core
HTTPUtil.initHttpCache(context);
// init the baseurl then Request().setUrl('/foo') -> baseurl + '/foo'
HTTPUtil.BASE_URL = "http://domain";
// set the get request cache key unique identification request
HTTPUtil.setCacheKey('user_id');
// set http or https port
HTTPUtil.setHttpPort(8080); // default 80
HTTPUtil.setHttpsPort(8888); // default 443
// init global response-posted include disconnected()-> handle all disconnected of requests fail()-> handle all fail of requests
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
// init global pre-request for example addHeaders() -> set header for all request
HTTPUtil.globalRequestHandler = new IGlobalRequestHandler() {
    @Override
    public List<Header> addHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("version", "1.0.2"));
        // add ...
        return headers;
    }
};
```

# usage
```java
// get request sample
HTTPModel
    .build()
    .setUrl("/foo")
    .setCachePolicy(CachePolicy.NoCache) // default CacheAndRemote
    .setContext(this)
    .setMethod(HTTPMethod.GET) // default GET
    .setResult(new Response<T>() {
        @Override
        public void ok(Header[] headers, T response) {
            // your code
        }

        @Override
        public void no(String error) {

        }

        // if need can override
        @Override
        public void cache(String response) {
            // this will call ok()
            super.cache(response);
            // you can comment out super.cache(response)
            // to deal with the response
        }
    })
    .done();
// post request sample
Request
    .build()
    .setMethod(Method.POST)
    .addHeader("token","foo_token")
    .setUrl("/foo")
    .setBody(Body.build().addKvs("username","foo").addKvs("age", 25).done())
    .setResponse(new Response<T>(){

        @Override
        public void ok(Header[] headers, T response) {

        }

        @Override
        public void no(String error) {

        }
    })
    .done();
// put delete similar
// if you want to handle raw json string yourself
Request
  .build()
  .setIsRawResponse(true)
  .setResponseRaw(new ResponseRaw() {
            @Override
            public void ok(Header[] headers, String response) {

            }

            @Override
            public void no(String error) {

            }
        })
  .done();
  // ...
```

# cache policy
- **NoCache**   
  - Get remote server data directly
- **CacheOnly**
  - If the cache exists, only cache is taken
- **CacheAndRemote**
  - Take the cache once and fetch the data from the remote server
- **IgnoreCache**
  - Update cache and fetch remote server data
- **CacheOrRemote**
  - If the network is unblocked, remote server data is obtained and cached data is obtained without a network connection

# response
- **Response&lt;T&gt;**
  - T is data type that needs to be serialized
  - Can override cache() to handle the cache by yourself
  - Can override disconneted() to deal with no network
- **ResponseRaw**
  - Returns the JSON string directly

# request
- **setMethod()** default GET
  - Method.GET
  - Method.POST
  - Method.PUT
  - Method.DELETE
- **setBody(Map<String, Object> body)** set reqeust body
  - you can use Body.bulid().addKvs(key,value).done()
- **setHeaders(Map<String, String> headers)**
- **addHeader(String key, String value)**
- **setUrl(String url)**
- **setCachePolicy(CachePolicy policy)** default CacheAndRemote
- **setIsUseBaseUrl(bool isUseBaseUrl)** default true
- **setIsRawResponse(bool isRawResponse)** default false
  - if you want to use setResponseRaw() then you show setIsRawResponse(true)

# network state receiver
```java
//Since APP uses the network, more or less such a broadcast receiver is needed. After my test, the system may send two broadcasts when the network condition changes. Here is my solution
BroadcastReceiver netStateReceiver = new BroadcastReceiver() {
        long splitTime;

        @Override
        public void onReceive(Context context, Intent intent) {
            if ((System.currentTimeMillis() - splitTime) > 500) {
                if (HTTPUtil.isNetworkConnected(context)) {
                  // The code used to write network connections here
                }else{
                  // Write code for network disconnection here
                }
                splitTime = System.currentTimeMillis();
            }
        }
    };
// register receiver
registerReceiver(netStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
/* Attention authority
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
don't forget unregisterReceiver(netStateReceiver);
*/
```
