package com.xiehang.user.remote;

import com.xiehang.user.model.User;

import java.util.List;

public interface UserRemote {

    public Object saveUser(User user);

    public Object saveUsers(List<User> users);

}
