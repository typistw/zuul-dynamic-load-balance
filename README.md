# zuul-dynamic-load-balance
客户端负载均衡：客户端会有一个服务器地址列表，在发送请求前通过负载均衡算法选择一个服务器，然后进行访问。
##  说明
ribbon客户端需配合zuul使用。
```
#  zuul 配置
zuul:
  routes:
    custom:  # route key， 随意，不重复即可
      path: /target/page/** # 路由表达式
      serviceId: customId # 客户端id
      stripPrefix: false   # 路由时保留 表达式前缀

# ribbon
ribbon:
  eureka:
    enabled: false #不使用注册中心
```
ribbon生成服务列表时，只会保留服务器地址+端口的信息，会忽略掉后面的路径，故zuul配置```stripPrefix=true```保留前缀以便路由到正确的地址。

## 1.配置文件方式
### 1.1 ribbon客户端配置
```
# ribbon 客户端，名字与zuul里serverId保持一致
customId:
  ribbon:
    # 服务端列表
    listOfServers: http://localhost:8081,http://localhost:8081
```
### 1.2 点评
优点：通过简单的配置，无需多余的代码即可生成客户端，适合于客户端服务列表稳定，且负载均衡不需特别定制的情况。  

缺点：客户端负载使用ribbon默认的实现方式，不能满足特定的场景。
## 2. 注解方式
### 2.1 注解 RibbonClient
spring cloud 允许我们通过注解配置来定制化客户端，需去掉配置文件中关于ribbon客户端的配置。
```
@Configuration
@RibbonClient(name = "customId", configuration = CustomConfiguration.class)
public class TestConfiguration {
}
```
一个名为 customId 的客户端将被生成，此配置不能被**主运用程序上下文**扫描到，需放到单独的包中(笔者猜测配置多个客户端的情况下可能造成配置数据共享问题)。
```
class CustomConfiguration{

	@Bean
	public IRule ribbonRule() {
		return new BestAvailableRule();
	}

	@Bean
	public IPing ribbonPing() {
		return new PingUrl();
	}

	  @Bean
    public ServerList<Server> ribbonServerList(){
        //配置服务列表
        Server[] serveArray = new Server[3];
        serveArray[0] = (new Server("localhost", 9999));

        ServerList<Server> serverServerList = new StaticServerList<>(serveArray);

        return serverServerList;
    }

	@Bean
	public ServerListSubsetFilter serverListFilter() {
		ServerListSubsetFilter filter = new ServerListSubsetFilter();
		return filter;
	}
}
```
### 2.2 点评
优点：通过少许的代码，解决了负载均衡中特殊需求的定制化问题。  

缺点：解决不了动态改变客户端服务列表难题。
## 3 动态配置方式
### 3.1 archaius
ribbon在负载均衡中选择服务需要读取客户端的服务列表信息，可通过clientId获取负载均衡器并改变相关的配置信息，但ribbon的刷新机制默认是从配置中读取数据，服务列表中的数据会被快速的替换掉。若我们实现按需改变```listOfServers```的配置，即可达到动态刷新客户端服务列表的目的。  

archius 的作用即为动态改变配置。
```
 public static void createRibbonClient(){
        //设置：配置项 (ribbon 会读取此配置)
        //根据 archaius 动态配置的特性 ， 服务列表更新时只需重新赋值即可
        ConfigurationManager.getConfigInstance().setProperty(CLIENT_ID + ".ribbon.listOfServers", LIST_SERVERS);

        // 获取客户端， 若不存在则创建
        ClientFactory.getNamedClient(CLIENT_ID);

        // 负载均衡服务
        DynamicServerListLoadBalancer serverListLoadBalancer = (DynamicServerListLoadBalancer) ClientFactory.getNamedLoadBalancer(CLIENT_ID);
        
        //根据需求定制化负载属性：
        //轮询方式、 Rule、 Ping 等
		//serverListLoadBalancer.setPing(new PingUrl());

    }
```

## 参考资源
[Client Side Load Balancer: Ribbon](http://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.0.0.M8/single/spring-cloud-netflix.html#spring-cloud-ribbon)  
[Spring Cloud源码分析（二）Ribbon ](http://blog.didispace.com/springcloud-sourcecode-ribbon/)  
[Archaius](https://github.com/Netflix/archaius/wiki)
