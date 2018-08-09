package org.ithot.android.transmit.http;

/**
 * 请求方法
 */
public enum Method {
    GET, // get 该方法下不能设置body
    POST,// post
    PUT, // put
    DELETE // delete 该方法下不能设置body
}
