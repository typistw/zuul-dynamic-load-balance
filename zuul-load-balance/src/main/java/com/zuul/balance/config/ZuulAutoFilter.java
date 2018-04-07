package com.zuul.balance.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @author weijinsheng
 * @date 2018/4/7 20:26
 */
public class ZuulAutoFilter extends ZuulFilter{


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        // do something
//        RequestContext rc = RequestContext.getCurrentContext();
        return null;
    }
}
