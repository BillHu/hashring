package com.taerhong.consistenthash.client;

import com.taerhong.consistenthash.client.util.Util;
import com.taerhong.consistenthash.server.CacheConnector;

import java.util.*;

/**
 * @author billhu
 * <p>
 * 客户端使用的缓存服务器集群选择
 */
public class CacheCluster {

    public static final int NUM_VIRTUAL_NODES = 200;

    private static final CacheCluster instance = new CacheCluster();

    private CacheCluster() {
    }

    /**
     * 服务端节点
     */
    private Set<String> nodes = new HashSet<>();

    /**
     * 虚拟节点，key：0 -> 2^32-1，value：服务端节点真实ip
     */
    private SortedMap<Long, String> virtualNodes = new TreeMap<>();

    public static CacheCluster getInstance() {
        return instance;
    }

    /**
     * 计算所有服务器节点存储数量的标准差
     *
     * @return 标准差
     */
    public double standardDivision() {
        Map<String, Integer> result = CacheConnector.getInstance().getServerCacheCounts();

        // 总数
        double totalCacheItemsCount = .0f;
        // 平均值
        double average = .0f;
        // 方差
        double variance = .0f;
        // 标准差
        double sd = .0f;

        for (String serverIp : result.keySet()) {
            totalCacheItemsCount += result.get(serverIp);
            System.out.println("ip = " + serverIp + ", caches = " + result.get(serverIp));
        }
        average = totalCacheItemsCount / result.size();
        System.out.println("平均值 = " + average);

        for (String serverIp : result.keySet()) {
            double cacheCount = result.get(serverIp);
            variance += Math.pow(cacheCount - average, 2);
        }
        variance = variance / result.size();
        System.out.println("方差 = " + variance);

        sd = Math.sqrt(variance);
        return sd;
    }

    /**
     * 批量添加节点
     *
     * @param nodes
     */
    public void addNodes(Set<String> nodes) {
        this.nodes.addAll(nodes);
        for (String nodeIp : nodes) {
            this.generateVirtualNodesFor(nodeIp);
        }
    }

    /**
     * 添加节点
     *
     * @param nodeIp
     */
    public void addNode(String nodeIp) {
        this.nodes.add(nodeIp);
        this.generateVirtualNodesFor(nodeIp);
    }

    /**
     * 删除节点
     *
     * @param nodeIp
     */
    public void removeNode(String nodeIp) {
        this.nodes.remove(nodeIp);
        this.removeVirtualNodesFor(nodeIp);
    }

    /**
     * 根据缓存key判断key所在服务器IP
     *
     * @param key
     * @return
     */
    public String getServerIpForKey(String key) {

        Long keyHash = Util.hashCodeFor(key);
        // 如果大于最大的虚拟节点key，则返回第一个节点的IP
        if (keyHash > virtualNodes.lastKey()) {
            return virtualNodes.get(virtualNodes.firstKey());
        }

        // 返回大于key的下一个缓存节点
        SortedMap<Long, String> subMap = virtualNodes.tailMap(keyHash);
        Long nodeKey = subMap.firstKey();
        String nodeIp = subMap.get(nodeKey);
        return nodeIp;
    }

    /**
     * 根据服务器IP生成相应虚拟节点
     *
     * @param nodeIp
     */
    private void generateVirtualNodesFor(String nodeIp) {
        this.virtualNodes.put(Util.hashCodeFor(nodeIp), nodeIp);
        for (int i = 0; i < NUM_VIRTUAL_NODES; i++) {
            String nodeName = nodeIp + "_" + i;
            Long key = Util.hashCodeFor(nodeName);
            this.virtualNodes.put(key, nodeIp);
        }
    }

    /**
     * 删除虚拟节点
     *
     * @param nodeIp
     */
    private void removeVirtualNodesFor(String nodeIp) {
        this.virtualNodes.remove(Util.hashCodeFor(nodeIp));
        for (int i = 0; i < NUM_VIRTUAL_NODES; i++) {
            String nodeName = nodeIp + "_" + i;
            Long key = Util.hashCodeFor(nodeName);
            this.virtualNodes.remove(key);
        }
    }

}
