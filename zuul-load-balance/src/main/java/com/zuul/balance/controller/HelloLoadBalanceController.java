package com.zuul.balance.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weijinsheng
 * @date 2018/4/7 20:30
 */
@RestController
public class HelloLoadBalanceController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "static/loadbalance/hello", method = {RequestMethod.GET, RequestMethod.POST})
    public String sayHelloStaticLoadBalance(){
        String str = "静态负载均衡响应请求，端口：" + serverPort;
        System.out.println(str);
        return str;
    }

    @RequestMapping(value = "dynamic/loadbalance/hello", method = {RequestMethod.GET, RequestMethod.POST})
    public String sayHelloDynamicLoadBalance(){
        String str = "动态负载均衡响应请求，端口：" + serverPort;
        System.out.println(str);
        return str;
    }

    @RequestMapping(value = "hello", method = {RequestMethod.GET, RequestMethod.POST})
    public String sayHello(){
        String str = "服务响应请求，端口：" + serverPort;
        System.out.println(str);
        return str;
    }
}
