# ![android-sex-http](static/icon.png)

# android-sex-http [![Build Status](https://travis-ci.org/dtboy1995/android-sex-http.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-http)
:airplane: android async http simple wrapper with cache policy

# install
```gradle
implementation 'org.ithot.android.transmit:http:0.3.2'
```

# usage
- ### permissions
```xml
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
- ### jsonserializer
```gradle
implementation 'org.ithot.android.serializer:gson:1.0.1'
```
- ### sample
```java
public class Dummy {
  public String id;
}
```
```java
// init once
Req.init(context, new JSON());
// send request
Req
  .build(context)
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
- ### config
```java
// default http 80 https 443
Req.init(context, serialier);
// set http port
Req.init(context, 3000, serialier);
// set https port
Req.init(context, 3000, 5000, serialier)
// set base url example for https://api.somedomain.com
Req.base("https://ithot.org");
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
});
```

- ### custom json serializer
```java
public class Serializer extends JSONSerializer {
    // you can use any serialization library such as Gson Fastjson etc example below
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

- ### download (break restoration)
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
