package org.version1.config;


import common.Serializer.mySerializer.Serializer;
import lombok.*;
import org.version1.Client.serviceCenter.balance.Impl.ConsistencyHashBalance;
import org.version1.Server.ServiceRegister.Impl.ZKServiceRegister;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class KRpcConfig {
    //名称
    private String name = "krpc";
    //端口
    private Integer port = 9999;
    //主机名
    private String host = "localhost";
    //版本号
    private String version = "1.0.0";
    //注册中心
    private String registry = new ZKServiceRegister().toString();
    //序列化器
    private String serializer = Serializer.getSerializerByCode(3).toString();
    //负载均衡
    private String loadBalance = new ConsistencyHashBalance().toString();

}
