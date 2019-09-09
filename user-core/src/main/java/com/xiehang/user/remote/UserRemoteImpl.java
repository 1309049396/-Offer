package com.xiehang.user.remote;

import com.xiehang.user.model.User;
import com.xiehang.annotation.Remote;
import com.xiehang.user.service.UserService;
import com.xiehang.util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote {
    @Resource
    private UserService userService;

    @Override
    public Object saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    @Override
    public Object saveUsers(List<User> users) {
        userService.saveList(users);
        return ResponseUtil.createSuccessResult(users);
    }
}
