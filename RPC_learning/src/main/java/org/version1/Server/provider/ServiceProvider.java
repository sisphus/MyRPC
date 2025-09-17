package org.version1.Server.provider;

import org.version1.Server.ServiceRegister.Impl.ZKServiceRegister;
import org.version1.Server.ServiceRegister.ServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    //集合中存放服务的实例,接口的全限定名（String 类型）,接口对应的实现类实例（Object 类型）。
    private Map<String,Object> interfaceProvider;

    private int port;
    private String host;
    //注册服务类
    private ServiceRegister serviceRegister;


    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
    }

    //本地注册服务
    public void provideServiceInterface(Object service){
        //接收一个服务实例（service）
        //获取服务对象的完整类名
        String serviceName=service.getClass().getName();
        //获取服务对象实现的所有接口
        Class<?>[] interfaceName=service.getClass().getInterfaces();

        for (Class<?> clazz:interfaceName){
            //遍历 service 实现的所有接口。
            //将接口的全限定名和对应的服务的实例添加到 map 中。
            interfaceProvider.put(clazz.getName(),service);

            //在注册中心注册服务
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port));

        }
    }
    //获取服务实例
    public Object getService(String interfaceName)
    {
        return interfaceProvider.get(interfaceName);
    }
}
