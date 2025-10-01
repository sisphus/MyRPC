package org.version1.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

public class CircuitBreakerProvider {
    // 用于存储每个接口名称与对应的熔断器实例之间的映射关系
    private Map<String,CircuitBreaker> circuitBreakerMap=new HashMap<>();

    //获取熔断器
    public synchronized CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;
        //检查circuitBreakerMap中是否已经有该接口的熔断器实例
        if(circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker=circuitBreakerMap.get(serviceName);
        }else {

            System.out.println("serviceName="+serviceName+"创建一个新的熔断器");
            //如果没有，则创建一个新的熔断器实例并返回
            circuitBreaker=new CircuitBreaker(1,0.5,10000);
            //将该接口的熔断器实例添加到circuitBreakerMap中
            circuitBreakerMap.put(serviceName,circuitBreaker);
        }
        //返回熔断器
        return circuitBreaker;
    }
}
