package common.Serializer.myCode;

import common.Message.MessageType;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import common.Serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    //netty在写出数据时会调用这个方法，将Java对象编码成二进制数据
    // 参数ctx 是netty提供得上下文对象，代表管道上下文，包含通道和处理器相关信息。
    // 参数msg是要编码得消息对象
    // 参数out 是netty提供的字节缓冲区，编码后的字节数据写入其中
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //打印消息对象的类名，用于调试编码过程中消息的类型
        System.out.println(msg.getClass());
        //判断消息是否是RpcRequest或RpcResponse类型，
        // 根据类型写入类型标识
        if(msg instanceof RpcRequest){
            out.writeShort(MessageType.REQUEST.getCode());
        }
        else if(msg instanceof RpcResponse){
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        //写入当前序列化器的类型标识（short类型）
        out.writeShort(serializer.getType());
        //得到序列化数组
        byte[] serializeBytes = serializer.serialize(msg);
        //3.写入长度
        out.writeInt(serializeBytes.length);
        //4.写入序列化数组
        out.writeBytes(serializeBytes);
    }
}
