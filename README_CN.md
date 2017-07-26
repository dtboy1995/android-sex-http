# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
一个性感的网络请求库，简单方便，支持多缓存机制，最简化请求

## Translations
[英文](README.md)

# 什么情况下使用
- GET请求需要在不同的情况下进行处理，在页面中没有网络的情况有网络的情况，数据如何获得，并且将请求结果快速转换为对象，以上情况可以方便的使用该库
- 您的服务器基于JSON响应并遵循REST规范
- 使用该库不会被fiddler抓包

# 安装
```java
// 在你project级别的build.gradle 中添加
allprojects {
  repositories {
    jcenter() // 此处不用变
    // 加个这
    maven { url 'https://jitpack.io' }
  }
}
// 在你的module级别的build.gradle 中添加
dependencies {
  // 你的其他依赖...
  compile 'com.github.dtboy1995:android-sex-http:0.1.2'
}
// 如果gradle的sync出现错误 在module的build.gradle 中添加这些
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

# 全局配置
```java
// 执行一次就行了 在Application类或者MainActivity中
// 缓存使用了二级缓存需要用到context对象所以需要初始化一下缓存
HTTPUtil.initHttpCache(context);
// 比如你要请求 http://domain/foo 那么配置了baseurl 之后的所有请求 setUrl 直接写/foo
HTTPUtil.setBaseUrl("http://domain");
// GET 请求缓存的key, 如果是单用户系统那么不用配置，如果是多用户系统很多请求可能重复，所以需要一个key来区分
HTTPUtil.setCacheKey('user_id');
// 设置http端口和https端口
HTTPUtil.setHttpPort(8080); // 默认 80
HTTPUtil.setHttpsPort(8888); // 默认 443
// 全局响应处理器 就是在每个请求处理完成时 如果配置了这个接口，那么每个请求都会执行这个接口的响应函数
HTTPUtil.setGlobalResponseHandler(new IGlobalResponseHandler() {
    @Override
    public void disconnected(Context context) {
        Toast.makeText(context, "no networking!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(Header[] headers, String response, Context context) {
        Toast.makeText(context, "error happened!", Toast.LENGTH_SHORT).show();
    }
});
// 全局请求处理器 在每个请求发送前 如果配置了这个接口 那么每个请求发送前都会执行这个接口的函数
HTTPUtil.setGlobalRequestHandler(new IGlobalRequestHandler() {
    @Override
    public List<Header> addHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("version", "1.0.2"));
        // add ...
        return headers;
    }
});
```

# 用法
```java
// GET请求示例
Request
    .build()
    .setUrl("/foo")
    .setCachePolicy(CachePolicy.NoCache) // 默认是CacheAndRemote
    .setContext(this)
    .setMethod(Method.GET) // 默认请求是GET
    .setResponse(new Response<T>() {
        @Override
        public void ok(Header[] headers, T response) {
            // 你的处理代码
        }

        @Override
        public void no(Header[] headers, String error) {

        }

        // 如果你需要直接操作缓存 那么重写这个方法
        @Override
        public void cache(String response) {
            // 如果调用super.cache() 会调用到ok()上
            super.cache(response);
            // 注释掉 super.cache(response)
            // 自己处理缓存的数据
        }
    })
    .done();
// POST请求示例
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
        public void no(Header[] headers, String error) {

        }
    })
    .done();
// put和delete方法类似
// 如果你不需要帮你把结果转化为对应的类那么 按照下面的方法编写
Request
  .build()
  .setIsRawResponse(true)
  .setResponseRaw(new ResponseRaw() {
            @Override
            public void ok(Header[] headers, String response) {

            }

            @Override
            public void no(Header[] headers, String error) {

            }
        })
  .done();
// 文件下载
// 别忘了添加权限 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
FileRequest
  .build()
  .setUrl("http://foo.com/download/foo.png")
  .setResponse(new FileResponse() {
      @Override
      public void ok() {
          // 成功
      }

      @Override
      public void fail(Throwable throwable) {
         // 失败
      }

      @Override
      public void progress(int percent) {
          // 进度
      }
  })
  .download(new File("你要把文件下载到哪")); // 比如 Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"图片.png"
// 文件上传
// 别忘了权限 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
File uploadFile = new File("existed_file"); // file to upload
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
```

# GET缓存策略
- **NoCache**   
  - 直接发送请求忽略缓存只回调一次
- **CacheOnly**
  - 如果缓存存在那么只回调缓存如果不存在缓存起来响应结果，回调一次
- **CacheAndRemote**
  - 缓存回调一次直接请求的结果回调一次
- **IgnoreCache**
  - 直接请求回调一次并且该请求的缓存
- **CacheOrRemote**
  - 如果网络连接直接请求回调一次不缓存数据，如果没有网络连接直接回调缓存

# 响应
- **Response&lt;T&gt;**
  - T 是你要转化为响应的类型
  - 可以重写cache()自己处理缓存
  - 可以重写disconneted()来自己处理没有网络时的情况比如弹框提示
- **ResponseRaw**
  - 直接返回json字符串

# 请求
- **setMethod()** 默认是 GET
  - Method.GET
  - Method.POST
  - Method.PUT
  - Method.DELETE
- **setBody(Map<String, Object> body)** 设置请求的body
  - 你可以使用辅助类 Body.bulid().addKvs(key,value).done()
- **setHeaders(Map<String, String> headers)**
- **addHeader(String key, String value)**
- **setUrl(String url)**
- **setCachePolicy(CachePolicy policy)** 默认是 CacheAndRemote
- **setIsUseBaseUrl(bool isUseBaseUrl)** 默认是 true
- **setIsRawResponse(bool isRawResponse)** 默认是 false
  - 如果你想使用 setResponseRaw() 那么你需要调用下 setIsRawResponse(true)

# 网络状态广播接收者
```java
// 既然APP用到了网络那么或多或少需要这么一个广播接收者。经过我测试系统可能在网络状况改变时发送两次广播，以下是我给的解决方案
BroadcastReceiver netStateReceiver = new BroadcastReceiver() {
        long splitTime;

        @Override
        public void onReceive(Context context, Intent intent) {
            if ((System.currentTimeMillis() - splitTime) > 500) {
                if (HTTPUtil.isNetworkConnected(context)) {
                  // 这里编写网络连接时的代码 比如修改一些UI的显示 参考qq的第一个tab  
                }else{
                  // 这里编写网络断开时的代码 比如修改一些UI的显示 参考qq的第一个tab  
                }
                splitTime = System.currentTimeMillis();
            }
        }
    };
// 注册该接收者
registerReceiver(netStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
/* 注意权限
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
别忘了 unregisterReceiver(netStateReceiver);
*/
```
