package org.version1.Client;

import org.version1.Client.proxy.ClientProxy;
import pojo.User;
import service.UserService;

public class TestClient {
    public static void main(String[] args) throws InterruptedException{
        ClientProxy clientProxy=new ClientProxy();
        UserService proxy=clientProxy.getProxy(UserService.class);
        for(int i = 0; i < 120; i++) {
            Integer i1 = i;
            if (i%30==0) {
                Thread.sleep(10000);
            }
            new Thread(()->{
                try{
                    User user = proxy.getUserByUserId(i1);
                    if (user != null) {
                        System.out.println("从服务端得到的user=" + user);
                    } else {
                        System.out.println("从服务端得到的user=null (可能被限流或者反序列化失败)");
                    }
                   // System.out.println("从服务端得到的user="+user.toString());

                    Integer id = proxy.insertUserId(User.builder().id(i1).userName("User" + i1.toString()).gender(true).build());
                    System.out.println("向服务端插入user的id"+id);
                } catch (NullPointerException e){
                    System.out.println("user为空");
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
