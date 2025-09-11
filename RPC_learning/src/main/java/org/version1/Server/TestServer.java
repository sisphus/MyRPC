package org.version1.Server;

import org.version1.Server.provider.ServiceProvider;
import org.version1.Server.server.RpcServer;
import org.version1.Server.server.impl.SimpleRPCRPCServer;
import org.version1.common.service.UserService;
import org.version1.common.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        //register service
        serviceProvider.provideServiceInterface(userService);
        //instantiate service
        RpcServer rpcServer = new SimpleRPCRPCServer(serviceProvider);

        rpcServer.start(9999);

    }
}
