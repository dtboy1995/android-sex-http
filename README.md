# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
:airplane: android async http simple wrapper with cache policy

# install
```gradle
implementation 'org.ithot.android.transmit:http:0.2.10'
```

# usage
### json (to decouple from the serialization library)
```java
public class GJSON extends Req.JSON {
    // you can use any serialization library
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
```
### sample
```java
GJSON json = new GJSON();
// init once
Req.init(this, json);
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
Req.init(this, json);
// set http port
Req.init(this, 3000, json);
// set https port
Req.init(this, 3000, 5000, json)
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
