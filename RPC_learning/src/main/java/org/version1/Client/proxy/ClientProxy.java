package org.version1.Client.proxy;

import lombok.AllArgsConstructor;
import org.version1.Client.IOClient;
import org.version1.Client.rpcClient.RpcClient;
import org.version1.Client.rpcClient.impl.NettyRpcClient;
import org.version1.Client.rpcClient.impl.SimpleSocketRpcClient;
import org.version1.common.Message.RpcRequest;
import org.version1.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request
    //RPCClientProxy类中需要加入一个RPCClient类变量即可， 传入不同的client(simple,netty)
    // , 即可调用公共的接口sendRequest发送请求
    private RpcClient rpcClient;


    public ClientProxy(){
        rpcClient=new NettyRpcClient();
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
        RpcResponse response= rpcClient.sendRequest(request);
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
