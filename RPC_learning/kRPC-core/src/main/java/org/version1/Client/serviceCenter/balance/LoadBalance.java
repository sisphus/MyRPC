package org.version1.Client.serviceCenter.balance;

import java.util.List;

public interface LoadBalance {
    //负责实现具体算法，返回分配的地址
    String balance(List<String> addressList);

    void addNode(String node) ;

    void delNode(String node);
}
