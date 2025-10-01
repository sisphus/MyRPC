package org.version1.Client.rpcClient;


import common.Message.RpcRequest;
import common.Message.RpcResponse;

public interface RpcClient {
    //共性抽取出来，定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
