package com.prajna.dtboy.http;

/**
 */
public enum Policy {
    NoCache,
    CacheOnly,
    CacheAndRemote,
    IgnoreCache,
    CacheOrRemote;
}
