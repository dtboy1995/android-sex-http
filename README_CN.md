# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
android async http 添加缓存功能二次封装

# 安装
- 在project的build.gradle中添加

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- 在module的build.gradle中添加

```gradle
compile 'com.github.dtboy1995:android-sex-http:0.10.2'
```

# 用法
```java
// 在app启动的时候初始化一次
Req.init(this); // Application context
Req.base("https://api.domain.com");
// 走起！
Req
  .build(this) // Activity this
  .url("/foo")
  .res(new Res<Foo>(){
    @Override
    public void ok(Header[] headers, Foo response) {
        // textView.setText(response.name)
    }

    @Override
    public void no(Header[] headers, String error) {
    }
  })
  .go();
```

# 配置
```java
Req.init(this); // 默认http是80https是443
Req.init(this, 3000); // 设置http
Req.init(this, 3000, 5000) // 设置https
Req.base("https://api.foo.com"); // 设置base url
Req.prefix("user_id"); // 用于区分不同用户的请求去缓存
Req.debug(true); // 如果设置为true响应会有日志
Req.hook(new IHTTPHook(){ // http生命周期钩子
  @Override
  public void disconnected(Context context) {
      // 没网的时候call
  }

  @Override
  public List<Header> headers() {
      // 添加一些通用的header
  }

  @Override
  public void pre(Context context) {
      // 请求前call，可以出个对话框
  }

  @Override
  public void post(Context context) {
      // 请求后call， 可以关闭对话框
  }

  @Override
  public void fail(Header[] headers, String response, Context context) {
      // 非正常响应call
  }
})
```

# translations
[英文](README.md)
