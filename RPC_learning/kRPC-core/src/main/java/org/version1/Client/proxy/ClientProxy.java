package org.version1.Client.proxy;

import common.Message.RpcRequest;
import common.Message.RpcResponse;
import org.version1.Client.circuitBreaker.CircuitBreaker;
import org.version1.Client.circuitBreaker.CircuitBreakerProvider;
import org.version1.Client.retry.guavaRetry;
import org.version1.Client.rpcClient.RpcClient;
import org.version1.Client.rpcClient.impl.NettyRpcClient;
import org.version1.Client.serviceCenter.ServiceCenter;
import org.version1.Client.serviceCenter.ZKServiceCenter;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request
    //RPCClientProxy类中需要加入一个RPCClient类变量即可， 传入不同的client(simple,netty)
    // , 即可调用公共的接口sendRequest发送请求
    private RpcClient rpcClient;

    private ServiceCenter serviceCenter;

    //熔断器
    private CircuitBreakerProvider circuitBreakerProvider;
    public ClientProxy() throws InterruptedException{
        rpcClient=new NettyRpcClient();
        serviceCenter=new ZKServiceCenter();
        circuitBreakerProvider = new CircuitBreakerProvider();
    }


    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    //核心逻辑，用于封装请求并处理服务端响应。
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsType(method.getParameterTypes())
                .build();
        //与服务端进行通信，将请求发送出去，并接收 RpcResponse 响应。
        RpcResponse response;

        //熔断器
        CircuitBreaker circuitBreaker = circuitBreakerProvider.getCircuitBreaker(method.getName());
        //判断熔断器是否允许通过
        if (!circuitBreaker.allowRequest()) {
            //这里可以针对熔断进行特殊处理，返回特殊值
            return null;
        }
        //后续添加逻辑：为保持幂等性，只对白名单上的服务进行重试
        if (serviceCenter.checkRetry(request.getInterfaceName())){
            //调用retry框架进行重试操作
            response=new guavaRetry().sendServiceWithRetry(request,rpcClient);
        }else {
            //只调用一次
            response= rpcClient.sendRequest(request);
        }

        //记录response的状态，上报给熔断器
        if (response.getCode() ==200){
            circuitBreaker.recordSuccess();
        }
        //记录response的状态，上报给熔断器
        if (response.getCode()==500){
            circuitBreaker.recordFailure();
        }

        //获取服务端返回的结果，并返回给调用者。
        return response.getData();
        }

    //动态生成一个实现指定接口的代理对象。
    public <T>T getProxy(Class<T> clazz) {
        //使用 Proxy.newProxyInstance 方法动态创建一个代理对象，传入类加载器、需要代理的接口和调用处理程序。
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }

}
