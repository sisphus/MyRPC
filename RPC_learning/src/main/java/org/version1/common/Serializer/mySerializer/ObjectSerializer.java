package org.version1.common.Serializer.mySerializer;

import java.io.*;

public class ObjectSerializer implements Serializer {
    //利用Java io 对象 -》字节数组
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes=null;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        try {
            //是一个对象输出流，用于将 Java 对象序列化为字节流，并将其连接到bos上
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            //把对象写入输出流中，触发序列化
            oos.writeObject(obj);
            //刷新 ObjectOutputStream，确保所有缓冲区中的数据都被写入到底层流中。
            oos.flush();
            //将bos其内部缓冲区中的数据转换为字节数组
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //字节数组 -》对象
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        //把字节数组包装成一个字节输入流
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            //使用 ObjectInputStream 包装一个 ByteArrayInputStream 对象
            ObjectInputStream ois = new ObjectInputStream(bis);
            //从 ois 中读取序列化的对象，并将其反序列化为 Java 对象
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    //0 代表Java 原生序列器
    @Override
    public int getType() {
        return 0;
    }
}
