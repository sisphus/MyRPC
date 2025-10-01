package org.version1.Server.server.impl;

import lombok.AllArgsConstructor;
import org.version1.Server.provider.ServiceProvider;
import org.version1.Server.server.RpcServer;
import org.version1.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;//本地注册中心
    @Override
    public void start(int port) {
        try {
            //创建一个 ServerSocket 实例，用于在指定的 port 端口上监听客户端的连接请求
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("服务器启动了");
            //服务器持续接受客户端的连接请求
            while (true) {
                //如果没有连接，会堵塞在这里
                Socket socket = serverSocket.accept();
                //有连接，创建一个新的线程执行处理
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop() {
        //停止服务端
        // 可以在未来版本中优化服务端关闭的流程。
    }
}
