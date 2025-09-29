package org.version1.Server.ratelimit.provider;

import org.version1.Server.ratelimit.RateLimit;
import org.version1.Server.ratelimit.impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

public class RateLimitProvider {

    // 用于存储每个接口名称与对应的速率限制器实例之间的映射关系
    private Map<String, RateLimit> rateLimitMap=new HashMap<>();

    /**
     * * 根据接口名称获取对应的速率限制器实例。
     * * 如果该接口的速率限制器实例不存在，则会创建一个新的实例并返回。     *
     * * @param interfaceName 接口名称
     * * @return 对应接口的速率限制器实例
     */
    public RateLimit getRateLimit(String interfaceName){
        // 检查rateLimitMap中是否已经有该接口的速率限制器实例
        if(!rateLimitMap.containsKey(interfaceName)){
            // 如果没有，则创建一个新的速率限制器实例
            RateLimit rateLimit=new TokenBucketRateLimitImpl(100,10);
            // 将该接口的速率限制器实例添加到rateLimitMap中
            rateLimitMap.put(interfaceName,rateLimit);
            // 返回新创建的速率限制器
            return rateLimit;
        }
        // 如果rateLimitMap中已经有该接口的速率限制器实例，则直接返回
        return rateLimitMap.get(interfaceName);
    }
}
