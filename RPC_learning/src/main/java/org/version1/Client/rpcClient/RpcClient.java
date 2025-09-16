package org.version1.Client.rpcClient;

import org.version1.common.Message.RpcRequest;
import org.version1.common.Message.RpcResponse;

public interface RpcClient {
    //共性抽取出来，定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
