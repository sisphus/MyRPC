package org.version1.Client.netty.handler;

import common.Message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;


public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    //这是 SimpleChannelInboundHandler 的核心方法，用于读取服务端返回的数据。
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception{
        // 接收到response, 给channel设计别名，让sendRequest里读取response
        // 将服务端返回的 RpcResponse 绑定到当前 Channel 的属性中，以便后续逻辑能够通过 Channel 获取该响应数据
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(response);
        // 关闭当前 Channel（短连接模式）
        ctx.channel().close();
    }

    //用于捕获运行过程中出现的异常，进行处理并释放资源。
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
