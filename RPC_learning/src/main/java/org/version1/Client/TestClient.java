package org.version1.Client;

import org.version1.Client.proxy.ClientProxy;
import org.version1.common.pojo.User;
import org.version1.common.service.UserService;

public class TestClient {
    public static void main(String[] args) throws InterruptedException{
        //创建 ClientProxy 对象：初始化 ClientProxy 对象，连接到指定的服务器地址和端口。
        ClientProxy clientProxy=new ClientProxy();
        //获得UserService的代理对象
        //内部改为通过动态服务发现机制（如 Zookeeper）获取服务端地址
        UserService proxy = clientProxy.getProxy(UserService.class);
        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());
        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
