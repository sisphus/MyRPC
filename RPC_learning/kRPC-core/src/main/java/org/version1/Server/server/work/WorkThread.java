package org.version1.Server.server.work;

import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.AllArgsConstructor;
import org.version1.Server.provider.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            //将响应数据（即服务端返回的 RpcResponse）通过网络连接发送给客户端。
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //从客户端的网络连接中接收数据，读取序列化的对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //读取客户端传过来的request
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            //反射调用服务方法获取返回值
            RpcResponse rpcResponse = getResponse(rpcRequest);
            //向客户端写入response
            oos.writeObject(rpcResponse);
            oos.flush();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //处理客户端请求，根据请求内容调用对应服务的方法，并返回执行结果
    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();
        //得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method = null;
        try {
            //获得方法对象
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
            //通过反射调用方法
            Object invoke = method.invoke(service, rpcRequest.getParams());
            //封装响应对象并返回
            return RpcResponse.success(invoke);
            //找不到请求的方法、方法无法访问、方法执行过程中抛出异常
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }

    }

}
