package com.taerhong.consistenthash.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author billhu
 * <p>
 * 模拟客户端连接服务端，发送请求
 */
public class CacheConnector {

    Map<String, CacheServer> cacheServers = new HashMap<>();

    private static final CacheConnector instance = new CacheConnector();

    private CacheConnector() {
    }

    public static CacheConnector getInstance() {
        return instance;
    }

    public String sendGetRequest(String ip, String key) {
        CacheServer cacheServer = cacheServers.get(ip);
        return cacheServer.get(key);
    }

    public void sendPutRequest(String ip, String key, String value) {
        CacheServer cacheServer = cacheServers.get(ip);
        cacheServer.set(key, value);
    }

    public void addCacheServer(String ip) {
        cacheServers.put(ip, new CacheServer());
    }

    public void removeCacheServer(String ip) {
        cacheServers.remove(ip);
    }

    public void addCacheServers(Set<String> ips) {
        for (String ip : ips) {
            cacheServers.put(ip, new CacheServer());
        }
    }

    public Map<String, Integer> getServerCacheCounts() {

        Map<String, Integer> resultMap = new HashMap<>(10);
        for (String ip : cacheServers.keySet()) {
            resultMap.put(ip, cacheServers.get(ip).getCacheCount());
        }
        return resultMap;
    }
}
