# android-sex-http
A sex http library, Simple and convenient, support many cache mechanism, simplify request most

# useful if you
- The GET request requires processing in different cases, and the request results are quickly converted to Object

# install
```java
//Add it in your root build.gradle at the end of repositories:
repositories {
		...
	  maven { url 'https://jitpack.io' }
}
//Add it in your module build.gradle
dependencies {
	  compile 'com.github.dtboy1995:float-compute-patch:-SNAPSHOT'
}
```

# usage
```java
// execute once
HTTPUtil.initHttpCache(context);
// sample
HTTPModel
    .build()
    .setUrl("http://apistage.wenanle.com/banners")
    .setCachePolicy(CachePolicy.NoCache)
    .setContext(this)
    .setMethod(HTTPMethod.GET)
    .setResult(new HTTPResult<List<Banner>>() {
        @Override
        public void ok(Header[] headers, List<Banner> response) {
            Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void no(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void cache(String response) {
            super.cache(response);
        }
    })
    .done();
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
- **HTTPResult&lt;T&gt;**
  - Data type that needs to be serialized
  - Can override cache to handle the cache by yourself
  - Can override disconneted to handle not network
