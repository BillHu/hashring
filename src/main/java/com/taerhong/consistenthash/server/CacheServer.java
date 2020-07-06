package com.taerhong.consistenthash.server;

import java.util.HashMap;
import java.util.Map;

/**
 * @author billhu
 *
 * 缓存服务器节点
 */
public class CacheServer {

    private Map<String, String> cacheMap = new HashMap<>();

    public String get(String key) {
        return cacheMap.get(key);
    }

    public void set(String key, String value) {
        cacheMap.put(key, value);
    }

    public int getCacheCount(){
        return cacheMap.size();
    }
}
