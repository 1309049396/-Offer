package com.xiehang.user.remote;


import com.xiehang.client.param.Response;
import com.xiehang.user.bean.User;

import java.util.List;

public interface UserRemote {

    public Response saveUser(User user);

    public Response saveUsers(List<User> users);

}
