package com.taerhong.consistenthash;

import com.taerhong.consistenthash.client.CacheClient;
import com.taerhong.consistenthash.client.CacheCluster;
import com.taerhong.consistenthash.server.CacheConnector;
import org.junit.Before;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Test {

    @Before
    public void setup() {
        Set<String> serverIpSet = new HashSet<>();
        serverIpSet.add("192.168.110.40");
        serverIpSet.add("192.168.110.41");
        serverIpSet.add("192.168.110.42");
        serverIpSet.add("192.168.110.43");
        serverIpSet.add("192.168.110.44");
        serverIpSet.add("192.168.110.45");
        serverIpSet.add("192.168.110.46");
        serverIpSet.add("192.168.110.47");
        serverIpSet.add("192.168.110.48");
        serverIpSet.add("192.168.110.49");

        CacheCluster.getInstance().addNodes(serverIpSet);
        CacheConnector.getInstance().addCacheServers(serverIpSet);
    }

    @org.junit.Test
    public void standardDivision() {
        int numItems = 1000000;
        for (int i = 0; i < numItems; i++) {
//            CacheClient.getInstance().put(String.valueOf(i), String.valueOf(i));
            CacheClient.getInstance().put(UUID.randomUUID().toString(), String.valueOf(i));
        }
        CacheCluster.getInstance().standardDivision();
    }
}
