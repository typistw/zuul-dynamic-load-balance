package com.zuul.balance;

import com.zuul.balance.config.ZuulAutoFilter;
import com.zuul.balance.event.DynamicClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @author weijinsheng
 * @date 2018/4/7 20:27
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulBalanceApplication {

	@Bean
	public ZuulAutoFilter zuulAutoFilter(){
		return new ZuulAutoFilter();
	}

	public static void main(String[] args) {

		SpringApplication.run(ZuulBalanceApplication.class, args);

		DynamicClient.createRibbonClient();
	}
}
