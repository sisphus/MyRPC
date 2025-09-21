package org.version1.common.Serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.version1.common.Message.MessageType;
import org.version1.common.Serializer.mySerializer.Serializer;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    //它负责传入的字节流解码为业务对象，并将解码后的对象添加到out中，供下一个handler处理
    // ctx是Netty的ChannelHandlerContext对象，提供对管道、通道和事件的访问
    // in是ByteBuf对象，接收到的字节流，它是netty的缓冲区，可以理解为字节数组
    // out是List对象，用于存储解码后的对象
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //1.读取消息类型
        short messageType = in.readShort();
        //判断是否是请求或响应消息
        if(messageType != MessageType.REQUEST.getCode() &&
                messageType != MessageType.RESPONSE.getCode()){
            System.out.println("暂不支持此种数据");
            return;
        }
        //2.读取序列化的方式&类型
        short serializerType = in.readShort();
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if(serializer == null)
            throw new RuntimeException("不存在对应的序列化器");
        //3.读 4字节 int → 序列化字节数组的长度。
        int length = in.readInt();
        //4.读取序列化数组
        byte[] bytes=new byte[length];
        in.readBytes(bytes);
        Object deserialize= serializer.deserialize(bytes, messageType);
        out.add(deserialize);
    }
}
