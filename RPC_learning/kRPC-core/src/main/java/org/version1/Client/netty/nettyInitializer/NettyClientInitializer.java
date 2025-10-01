package org.version1.Client.netty.nettyInitializer;

import common.Serializer.myCode.MyDecoder;
import common.Serializer.myCode.MyEncoder;
import common.Serializer.mySerializer.JsonSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.version1.Client.netty.handler.NettyClientHandler;


public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //初始化，每个 SocketChannel 都有一个独立的管道（Pipeline），用于定义数据的处理流程。
        ChannelPipeline pipeline = ch.pipeline();

        //使用自定义的编/解码器
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));

        pipeline.addLast(new NettyClientHandler());
    }

}
