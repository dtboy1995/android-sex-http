# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
android async http simple wrapper with cache policy

# install
- add to your project gradle file

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- add to your module gradle file

```gradle
compile 'com.github.dtboy1995:android-sex-http:0.1.2'
```

# usage
```java
// init once
Req.init(this);
// just go
Req
  .build(MainActivity.this)
  .url("/foos")
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

# config
```java
Req.init(this); // default http 80 https 443
Req.init(this, 3000); // set http port
Req.init(this, 3000, 5000) // set https port
Req.base("https://api.foo.com"); // set base url
Req.prefix("user_id"); // distinguish different users request
Req.debug(true); // if true log response default false
Req.hook(new IHTTPHook(){ // http lifecycle hooks
  @Override
  public void disconnected(Context context) {
      // no network call this hook
  }

  @Override
  public List<Header> headers() {
      // you can set common headers
  }

  @Override
  public void pre(Context context) {
      // before request call this hook, you can display a dialog
  }

  @Override
  public void post(Context context) {
      // request done call this hook, you can dismiss a dialog
  }

  @Override
  public void fail(Header[] headers, String response, Context context) {
      // not 200 response call this hook
  }
})
```

# translations
[中文](README_CN.md)
