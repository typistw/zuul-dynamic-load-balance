package com.zuul.balance.event;

import com.netflix.client.ClientFactory;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.PingUrl;

/**
 *  动态生成 ribbon 客户端
 * @author weijinsheng
 * @date 2018/4/7 22:21
 */
public class DynamicClient {

    // 可配置文件、数据库读取
    private static final String CLIENT_ID = "dynamicClientId";
    private static final String LIST_SERVERS = "http://localhost:8081";

    public static void createRibbonClient(){

        /*
        * 设置：配置项 (ribbon 会读取此配置)
        *
        * 根据 archaius 动态配置的特性 ， 服务列表更新时只需重新赋值即可
        * */
        ConfigurationManager.getConfigInstance().setProperty(CLIENT_ID + ".ribbon.listOfServers", LIST_SERVERS);

        // 获取客户端， 若不存在则创建
        ClientFactory.getNamedClient(CLIENT_ID);

        // 负载均衡服务
        DynamicServerListLoadBalancer serverListLoadBalancer = (DynamicServerListLoadBalancer) ClientFactory.getNamedLoadBalancer(CLIENT_ID);
        /*
        * 根据需求定制化负载属性：
        *  轮询方式、 Rule、 Ping 等
        * */
//        serverListLoadBalancer.setPing(new PingUrl());

    }

}