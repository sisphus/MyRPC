package org.version1.Server.provider;

import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    //集合中存放服务的实例,接口的全限定名（String 类型）,接口对应的实现类实例（Object 类型）。
    private Map<String,Object> interfaceProvider;
    //为 interfaceProvider 字段分配一个新的 HashMap 实例
    public ServiceProvider(){
        this.interfaceProvider=new HashMap<>();
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

        }
    }
    //获取服务实例
    public Object getService(String interfaceName)
    {
        return interfaceProvider.get(interfaceName);
    }
}
