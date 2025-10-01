package common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RpcResponse implements Serializable {
    //状态信息
    private int code;

    private String message;

    private Class<?> dataType;
    //具体数据
    private Object data;
    //构造成功信息
    public static RpcResponse success(Object data){
        return RpcResponse
                .builder()
                .code(200)
                .dataType(data != null ? data.getClass() : Object.class)
                .data(data).build();
    }
    //构造失败信息
    public static RpcResponse fail(){
        return RpcResponse.builder().code(500).message("服务器发生错误")
                .dataType(Object.class) // 兜底，避免客户端拿到 null
                .build();
    }

}
