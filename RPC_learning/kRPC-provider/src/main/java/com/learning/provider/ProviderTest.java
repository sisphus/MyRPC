package com.learning.provider;

import com.learning.provider.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.version1.KRpcApplication;
import org.version1.Server.provider.ServiceProvider;
import org.version1.Server.server.RpcServer;
import org.version1.Server.server.impl.NettyRPCRPCServer;
import service.UserService;

/**
 * @ClassName ProviderExample
 * @Description 测试服务端
 * @Author Tong
 * @LastChangeDate 2024-12-05 0:34
 * @Version v5.0
 */
@Slf4j
public class ProviderTest {

    public static void main(String[] args) throws InterruptedException {
        KRpcApplication.initialize();
        String ip=KRpcApplication.getRpcConfig().getHost();
        int port=KRpcApplication.getRpcConfig().getPort();
        // 创建 UserService 实例
        UserService userService = new UserServiceImpl();
        ServiceProvider serviceProvider = new ServiceProvider(ip, port);
        // 发布服务接口到 ServiceProvider
        serviceProvider.provideServiceInterface(userService,true);  // 可以设置是否支持重试

        // 启动 RPC 服务器并监听端口
        RpcServer rpcServer = new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(port);  // 启动 Netty RPC 服务，监听 port 端口
        log.info("RPC 服务端启动，监听端口" + port);
    }

}
