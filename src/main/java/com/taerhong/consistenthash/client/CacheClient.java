package com.taerhong.consistenthash.client;

import com.taerhong.consistenthash.server.CacheConnector;

/**
 * @author billhu
 * <p>
 * 供客户端使用的缓存操作代码
 */
public class CacheClient {

    private static final CacheClient instance = new CacheClient();

    private CacheClient() {
    }

    public static CacheClient getInstance() {
        return instance;
    }

    /**
     * 获取key对应缓存
     * @param key
     * @return
     */
    public String get(String key) {

        // 获取服务端IP
        String serverIp = CacheCluster.getInstance().getServerIpForKey(key);
        // 发送获取缓存请求
        return CacheConnector.getInstance().sendGetRequest(serverIp, key);
    }

    /**
     * 保存
     * @param key
     * @param value
     */
    public void put(String key, String value) {

        // 获取服务端IP
        String serverIp = CacheCluster.getInstance().getServerIpForKey(key);
        // 发送保存缓存请求
        CacheConnector.getInstance().sendPutRequest(serverIp, key, value);
    }

}
