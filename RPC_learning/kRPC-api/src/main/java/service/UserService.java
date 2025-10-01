package service;


import pojo.User;

public interface UserService {
    //根据ID查询用户
    User getUserByUserId(Integer id);

    //
    Integer insertUserId(User user);

}
