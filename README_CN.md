# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
:airplane: 带缓存策略的http请求库

# 安装
```gradle
implementation 'org.ithot.android.transmit:http:0.3.2'
```

# 用法
- ### 权限
```xml
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
- ### JSON序列化器
```gradle
implementation 'org.ithot.android.serializer:gson:1.0.1'
```
- ### 例子
```java
public class Dummy {
  public String id;
}
```
```java
// 初始化一次即可
Req.init(context, new JSON());
// 发送请求
Req.build(context)
   .url("https://ithot.org/dummy")
   .res(new Res<Dummy>(){
     @Override
     public void ok(Header[] headers, Dummy response) {
 
     }

     @Override
     public void no(Header[] headers, String error) {
     }
   })
   .go();
```
- ### 配置
```java
// 默认 http 80 https 443
Req.init(context, serialier);
// 设置 http 端口
Req.init(context, 3000, serialier);
// 设置 https 端口
Req.init(context, 3000, 5000, serialier)
// 设置 基 url
Req.base("https://your_domain");
// distinguish different users request
Req.prefix("user_id");
// 设置debug模式，可以打印响应
Req.debug(true);
// 请求生命周期钩子
Req.hook(new IHTTPHook(){
  // 没有网络连接
  @Override
  public void disconnected(Context context) {

  }
  // 通用请求头
  @Override
  public List<Header> headers() {

  }
  // 请求开始前
  @Override
  public void pre(Context context) {

  }
  // 请求结束后
  @Override
  public void post(Context context) {

  }
  // 请求异常
  @Override
  public void fail(Header[] headers, String response, Context context) {

  }
});
```

- ### 自定义JSON序列化器
```java
public class Serializer extends JSONSerializer {
    // 您可以使用任何序列化库，比如Gson或者FastJson等
    private Gson gson = new Gson();

    @Override
    public Object parse(String json, Type type) {
        return gson.fromJson(json, type);
    }

    @Override
    public String stringify(Object object) {
        return gson.toJson(object);
    }
}
Req.init(context, new Serializer());
```

- ### 下载 (断点恢复)
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
```java
File file = new File(Environment.getExternalStorageDirectory() + File.separator + "test.mp3");
Req.build(context)
   .base(false)
   .url("https://ithot.org/music")
   .res(new FileRes() {
        @Override
        public void done(File f) {
        }

        @Override
        public void undone() {
        }

        @Override
        public void progress(int rate) {
        }

    })
  .download(file);
```
