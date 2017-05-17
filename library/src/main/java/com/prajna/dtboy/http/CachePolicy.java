package com.prajna.dtboy.http;

/**
 * 缓存策略枚举
 */
public enum CachePolicy {
    NoCache,    //不要缓存
    CacheOnly,  //只要缓存
    CacheAndRemote, //双重回调 先使用缓存后使用服务器
    IgnoreCache,    //忽略缓存 会缓存起来但是 不会使用缓存
    CacheOrRemote;  //如果有网就请求 没网就缓存
}
