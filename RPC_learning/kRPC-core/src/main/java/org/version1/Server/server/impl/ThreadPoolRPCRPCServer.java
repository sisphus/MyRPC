package org.version1.Server.server.impl;

import org.version1.Server.provider.ServiceProvider;
import org.version1.Server.server.RpcServer;
import org.version1.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRPCRPCServer implements RpcServer {
    private final ThreadPoolExecutor threadPool;//定义一个线程池对象 threadPool，用于管理和执行线程任务
    private ServiceProvider serviceProvider;
    //默认构造方法：创建一个线程池，核心线程数等于 CPU 核心数，
    // 最大线程数 1000，非核心线程空闲存活时间60秒，队列大小为 100。
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider){
        threadPool=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000,60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
        this.serviceProvider= serviceProvider;
    }
    //自定义构造方法：允许用户传入线程池参数，自定义线程池配置。
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider, int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue){
    threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        System.out.println("服务端启动了");
        try {
            //初始化 ServerSocket，监听端口。
            ServerSocket serverSocket=new ServerSocket();
            while (true){
                //接受连接：通过 accept() 阻塞等待客户端连接，返回 Socket 对象。
                Socket socket= serverSocket.accept();
                //使用线程池分发任务，每个客户端请求交给线程池管理。
                threadPool.execute(new WorkThread(socket,serviceProvider));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop() {

    }

}
