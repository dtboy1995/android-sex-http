package com.prajna.dtboy.http;

/**
 * get请求缓存策略
 */
public enum Policy {
    NoCache, // 不适用缓存 只使用请求返回的结果
    CacheOnly, // 如果有缓存只是用缓存 不再发送请求
    CacheAndRemote, // 使用缓存回调一次 发送请求响应再回调一次
    IgnoreCache, // 不回调缓存但是会更新缓存 发送请求回调
    CacheOrRemote; // 有网发送请求 没网回调缓存
}
