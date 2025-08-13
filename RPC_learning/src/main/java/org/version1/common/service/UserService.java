package org.version1.common.service;

import org.version1.common.pojo.User;

public interface UserService {
    //根据ID查询用户
    User GetUserByUserId(Integer id);

    //
    Integer insertUserId(User user);

}
