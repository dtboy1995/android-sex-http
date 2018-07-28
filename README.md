# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
:airplane: android async http simple wrapper with cache policy

# install
- add to your project gradle file

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- add to your module gradle file

```gradle
implementation 'com.github.dtboy1995:android-sex-http:0.10.2'
```

# usage
```java
// init once
Req.init(this);
// send request
Req
  .build(this)
  .url("https://your_domain/some_url")
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

# config
```java
// default http 80 https 443
Req.init(this); 
// set http port
Req.init(this, 3000); 
// set https port
Req.init(this, 3000, 5000) 
// set base url example for https://api.somedomain.com
Req.base("https://your_domain");
// distinguish different users request
Req.prefix("user_id"); 
// if true log response default false
Req.debug(true); 
// http lifecycle hooks
Req.hook(new IHTTPHook(){
  // no network call this hook
  @Override
  public void disconnected(Context context) {
      
  }
  // you can set common headers
  @Override
  public List<Header> headers() {
      
  }
  // before request call this hook, you can display a dialog
  @Override
  public void pre(Context context) {
      
  }
  // request done call this hook, you can dismiss a dialog
  @Override
  public void post(Context context) {
      
  }
  // abnormal response call this hook
  @Override
  public void fail(Header[] headers, String response, Context context) {
      
  }
})
```
