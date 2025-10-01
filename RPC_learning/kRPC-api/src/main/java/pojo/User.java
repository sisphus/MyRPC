package pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder               // 支持链式构建对象
@Data                  // 自动生成 Getter/Setter/toString/equals/hashCode
@NoArgsConstructor     // 自动生成无参构造器
@AllArgsConstructor    // 自动生成全参构造器
public class User implements Serializable {
    private Integer id;
    private String userName;
    private Boolean gender;
}
